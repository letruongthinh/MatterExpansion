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

import io.lethinh.matterexpansion.gui.slot.SlotOutput;
import io.lethinh.matterexpansion.tile.TileSolderingStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class ContainerSolderingStation extends GenericContainer<TileSolderingStation> {

	public ContainerSolderingStation(TileSolderingStation tile, InventoryPlayer inventoryPlayer) {
		super(tile, inventoryPlayer);

		// Crafting Inventory
		this.drawCraftingInventory(tile.craftMatrix, 8, 84, this);

		// Melting Input
		this.addSlotToContainer(new Slot(tile, tile.SLOT_MELT, 26, 21));

		// Output
		this.addSlotToContainer(new SlotOutput(tile, tile.SLOT_OUTPUT, 139, 53));

		// Player Inventory
		this.drawPlayerInventory(inventoryPlayer, 8, 97);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventory) {
		super.onCraftMatrixChanged(inventory);
		this.tile.markDirty();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUsableByPlayer(player);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tile);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		this.listeners.forEach(listener -> this.tile.sendGuiNetworkData(this, listener));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int data, int value) {
		this.tile.getGuiNetworkData(data, value);
	}

}
