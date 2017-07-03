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

import java.io.IOException;

import javax.annotation.Nonnull;

import io.lethinh.matterexpansion.backend.helpers.IBlobsWrapper;
import io.lethinh.matterexpansion.backend.helpers.INBTWrapper;
import io.lethinh.matterexpansion.init.PacketHandler;
import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import io.lethinh.matterexpansion.network.packet.PacketSyncCraftingInventory;
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
 * saved NBT and the packet blobs to retrieve the ItemStacks in blobs.
 *
 * @author Le Thinh
 */
public class PerpetualInventoryCrafting implements IInventory, IBlobsWrapper, INBTWrapper {

	private final GenericTile tile;
	public NonNullList<ItemStack> stacksList; // No final modifier, for reading
												// and writing changes to packet
												// in the network.
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
		this.stacksList = NonNullList.withSize(width * height, ItemStack.EMPTY);
		this.eventHandler = null; // Defines it later.
		this.width = width;
		this.height = height;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.getName());
	}

	@Override
	public String getName() {
		return this.toString();
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

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index >= this.getSizeInventory())
			return ItemStack.EMPTY;

		return this.stacksList.get(index);
	}

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

		// Send the packet to server. Because if the player is using this
		// container and changes something that the server didn't know, but the
		// player knew what has changed, because the player is interacting with
		// the container. So I have to sync this inventory with the server.
		PacketHandler.sendToServer(new PacketSyncCraftingInventory());
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
		this.stacksList.clear();
	}

	/* IBlobsWrapper */
	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		this.stacksList = packet.readItemStacks();
		this.width = packet.readInt();
		this.height = packet.readInt();
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		packet.writeItemStacks(this.stacksList);
		packet.writeInt(this.width);
		packet.writeInt(this.height);
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
