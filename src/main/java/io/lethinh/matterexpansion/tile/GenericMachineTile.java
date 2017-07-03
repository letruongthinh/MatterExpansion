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

package io.lethinh.matterexpansion.tile;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericMachineTile extends GenericPowerTile
		implements ISidedInventory, ITickable {

	protected NonNullList<ItemStack> stacks;
	protected long ticks;
	protected boolean isActive;
	public int progress;

	public GenericMachineTile(int size, String name) {
		super(name);
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
		this.ticks = 0;
		this.isActive = false;
		this.progress = -1;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		ItemStackHelper.loadAllItems(compound, this.stacks);
		this.ticks = compound.getLong("Ticks");
		this.progress = compound.getInteger("Progress");
		this.isActive = compound.getBoolean("IsActive");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		ItemStackHelper.saveAllItems(compound, this.stacks);
		compound.setLong("Ticks", this.ticks);
		compound.setInteger("Progress", this.progress);
		compound.setBoolean("IsActive", this.isActive);
		return super.writeToNBT(compound);
	}

	/* IInventory */
	@Override
	public void clear() {
		this.stacks.clear();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.stacks, index, count);
	}

	@Override
	public int getField(int data) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public int getSizeInventory() {
		return this.stacks.size();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.stacks.get(index);
	}

	@Override
	public boolean isEmpty() {
		return this.stacks.stream().anyMatch(itemStack -> !itemStack.isEmpty());
	}

	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
		return false;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D
				&& !this.isInvalid() && this.world.getTileEntity(this.pos) == this;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.stacks, index);
	}

	@Override
	public void setField(int data, int value) {
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
		this.stacks.set(index, stack);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	/* ISidedInventory */
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int index, @Nonnull ItemStack stack, EnumFacing side) {
		return !stack.isEmpty();
	}

	@Override
	public boolean canExtractItem(int index, @Nonnull ItemStack stack, EnumFacing side) {
		return stack.isEmpty();
	}

	/* ITickable */
	@Override
	public void update() {
		if (this.world.isRemote) {
			this.doServerWork();
		}

		if (this.canWork()) {
			if (!this.isActive)
				return;

			this.isActive = true;
			this.doClientWork();
		} else if (this.isActive) {
			this.isActive = false;
			this.stopWorking();
		}
	}

	/* WORK */
	/**
	 * Called when the tile wants to check if it can work.
	 *
	 * @return true if the tile can work otherwise false.
	 */
	protected abstract boolean canWork();

	/**
	 * Called when the tile works in server-side.
	 */
	protected void doServerWork() {
		this.ticks++;
	}

	/**
	 * Called when the tile works in client-side. And checked with the
	 * {@link GenericMachineTile#canWork} if it can work.
	 */
	protected abstract void doClientWork();

	/**
	 * Called when the tile stops working. And do something when it stopped
	 * (reset the timer, set the block state, ...)
	 */
	protected abstract void stopWorking();
}
