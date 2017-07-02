/**
 * Copyright (C) 2017 Le Thinh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.lethinh.matterexpansion.gui;

import javax.annotation.Nonnull;

import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import io.lethinh.matterexpansion.tile.GenericTile;
import io.lethinh.matterexpansion.tile.inventory.PerpetualInventoryCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericContainer<TE extends GenericTile> extends Container {

	protected final TE tile;
	protected int playerInventoryStart;

	public GenericContainer(TE tile, InventoryPlayer inventoryPlayer) {
		this.tile = tile;
		this.playerInventoryStart = -1;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		if (!this.canInteractWith(player))
			return ItemStack.EMPTY;

		final Slot slot = this.inventorySlots.get(index);
		ItemStack itemStack = ItemStack.EMPTY;

		if (slot != null && slot.getHasStack() && this.playerInventoryStart > 0) {
			final ItemStack itemStack1 = ItemStack.EMPTY;
			itemStack = InventoryUtils.copyItemStack(itemStack1);
			assert !itemStack1.isEmpty();

			if (index < this.playerInventoryStart) {
				if (!this.mergeItemStack(itemStack1, this.playerInventoryStart, this.inventorySlots.size(), true))
					return ItemStack.EMPTY;

				slot.onSlotChange(itemStack1, itemStack);
			} else if (!this.mergeItemStack(itemStack1, 0, this.playerInventoryStart, false))
				return null;

			if (itemStack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemStack1.getCount() == itemStack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, itemStack1);
		}

		return itemStack;
	}

	/* INVENTORY */
	/**
	 * Draws the inventory of the player.
	 *
	 * @param inventoryPlayer
	 *            The current inventory of the player.
	 * @param x
	 *            The X (integer) of the inventory player's location. Default
	 *            value: 8
	 * @param y
	 *            The Y (integer) of the inventory player's location. Default
	 *            value: 84
	 */
	protected void drawPlayerInventory(InventoryPlayer inventoryPlayer, int x, int y) {
		this.playerInventoryStart = this.inventorySlots.size();

		// Player Inventory
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlotToContainer(new Slot(inventoryPlayer, column + row * 9 + 9, x + column * 18, y + row * 18));
			}
		}

		// Player Hotbar
		for (int column = 0; column < 9; ++column) {
			this.addSlotToContainer(new Slot(inventoryPlayer, column, x + column * 18, y + 58));
		}
	}

	/**
	 * Draws and updates the craft matrix through
	 * {@link Container#onCraftMatrixChanged}.
	 *
	 * @param craftMatrix
	 *            The craft matrix needs to add.
	 * @param x
	 *            The X (integer) of the inventory player's location. Default
	 *            value: 30
	 * @param y
	 *            The Y (integer) of the inventory player's location. Default
	 *            value: 17
	 */
	protected void drawCraftingInventory(PerpetualInventoryCrafting craftMatrix, int x, int y, Container container) {
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 3; ++column) {
				this.addSlotToContainer(
						new Slot(craftMatrix, column + row * 3, x + column * 18, y + row * 18));
			}
		}

		craftMatrix.eventHandler = container; // Now, it is time to assign the
												// value.
		container.onCraftMatrixChanged(craftMatrix); // Updates the craft
														// matrix.
	}

}
