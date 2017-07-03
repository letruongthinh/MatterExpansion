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

package io.lethinh.matterexpansion.tile.inventory;

import javax.annotation.Nonnull;

import io.lethinh.matterexpansion.backend.helpers.INBTWrapper;
import io.lethinh.matterexpansion.tile.GenericTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * This class is created in case if the inventory is cleared, it still has the
 * saved NBT to retrieve the ItemStacks. There are no blobs here. There are only
 * blobs for width and height of the craft matrix. It can cause unstable
 * ItemStacks list size and can even cause crash because ItemStacks list size is
 * too long.
 *
 * @author Le Thinh
 */
public class PerpetualInventoryCrafting implements IInventory, INBTWrapper {

	private final GenericTile tile;
	public NonNullList<ItemStack> stacksList; // No final modifier, for reading
												// and writing changes to NBT.
	public int width;
	public int height;
	public Container eventHandler; // This is public because when you create a
									// new instance of this in the tile, you
									// will have to create a new instance of the
									// container. Defines it null in that
									// instance and then use
									// GenericContainer#drawCraftingInventory,
									// it will assigned the container to
									// the eventHandler. It will also draw
									// the crafting inventory and update it.

	public PerpetualInventoryCrafting(GenericTile tile, int width, int height) {
		this.tile = tile;
		this.width = width;
		this.height = height;
		this.stacksList = NonNullList.withSize(width * height, ItemStack.EMPTY);
		this.eventHandler = null; // Defines it later.
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public String getName() {
		return "perpetual_craft_matrix";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.stacksList.size();
	}

	@Override
	public boolean isEmpty() {
		return this.stacksList.stream().allMatch(stack -> !stack.isEmpty());
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		if (index >= this.getSizeInventory())
			return ItemStack.EMPTY;

		return this.stacksList.get(index);
	}

	@Nonnull
	public ItemStack getStackInRowAndColumn(int row, int column) {
		if (row < 0 || column >= this.width || column < 0 || column > this.height)
			return ItemStack.EMPTY;

		return this.getStackInSlot(row + column * this.width);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		this.eventHandler.onCraftMatrixChanged(this);
		return ItemStackHelper.getAndSplit(this.stacksList, index, count);
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		this.eventHandler.onCraftMatrixChanged(this);
		return ItemStackHelper.getAndRemove(this.stacksList, index);
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
		this.eventHandler.onCraftMatrixChanged(this);
		this.stacksList.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		this.eventHandler.onCraftMatrixChanged(this);
		this.tile.markDirty();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int data) {
		return 0;
	}

	@Override
	public void setField(int data, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// this.stacksList.clear(); Cannot clear because it is perpetual.
	}

	/* INBTWrapper */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		ItemStackHelper.loadAllItems(compound, this.stacksList);
		this.width = compound.getInteger("Width");
		this.height = compound.getInteger("Height");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		ItemStackHelper.saveAllItems(compound, this.stacksList);
		compound.setInteger("Width", this.width);
		compound.setInteger("Height", this.height);
		return compound;
	}

}
