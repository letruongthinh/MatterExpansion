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

import io.lethinh.matterexpansion.backend.helpers.IGuiTile;
import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import io.lethinh.matterexpansion.crafting.FreezerRecipe;
import io.lethinh.matterexpansion.gui.ContainerFreezer;
import io.lethinh.matterexpansion.gui.GenericContainer;
import io.lethinh.matterexpansion.gui.GuiFreezer;
import io.lethinh.matterexpansion.init.ModCrafting;
import io.lethinh.matterexpansion.init.PacketHandler;
import io.lethinh.matterexpansion.network.packet.PacketTileUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class TileFreezer extends GenericMachineTile implements IGuiTile {

	public int maxTime = 200;
	public final int SLOT_INPUT = 0;
	public final int SLOT_OUTPUT = 1;

	public TileFreezer() {
		super(2, "blockFreezer");
	}

	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
		if (index == this.SLOT_INPUT)
			return this.getStackInSlot(this.SLOT_INPUT).isItemEqual(this.getCurrentRecipe().input);

		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.UP)
			return new int[] { 0 };

		return new int[] { 1 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing side) {
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing side) {
		if (index == this.SLOT_OUTPUT && side != EnumFacing.UP)
			return true;

		return false;
	}

	/* ENERGY */
	@Override
	public int getCapacity() {
		return 500000;
	}

	/* IGuiTile */
	@Override
	public Object getServerGuiElement(EntityPlayer player, int ID, int x, int y, int z) {
		return new ContainerFreezer(this, player.inventory);
	}

	@Override
	public Object getClientGuiElement(EntityPlayer player, int ID, int x, int y, int z) {
		return new GuiFreezer(this, player.inventory);
	}

	/* WORK */
	@Override
	protected boolean canWork() {
		final FreezerRecipe recipe = this.getCurrentRecipe();
		return this.getEnergyStored() >= 2000 && this.progress >= this.maxTime;
	}

	@Override
	protected void doClientWork() {
		this.progress++;
		final FreezerRecipe currentRecipe = this.getCurrentRecipe();
		final ItemStack output = InventoryUtils.copyItemStack(currentRecipe.output);

		if (this.getStackInSlot(this.SLOT_OUTPUT).isEmpty()) {
			this.setInventorySlotContents(this.SLOT_OUTPUT, output);
		} else {
			output.grow(1);
		}

		this.markDirty();
		this.extractEnergy(2000);
		this.progress = 0;
	}

	@Override
	protected void stopWorking() {
		this.progress = 0;
	}

	/* RECIPE */
	private FreezerRecipe getCurrentRecipe() {
		final ItemStack input = this.getStackInSlot(this.SLOT_INPUT);

		if (!input.isEmpty()) {
			ModCrafting.FREEZER_RECIPES.stream().filter(recipe -> input.isItemEqual(recipe.input)).findFirst()
					.orElse(null);
		}

		return null;
	}

	/* CONTAINER */
	@SideOnly(Side.CLIENT)
	@Override
	public void getGuiNetworkData(int data, int value) {
		this.progress = value;
	}

	@Override
	public void sendGuiNetworkData(GenericContainer container, IContainerListener listener) {
		listener.sendProgressBarUpdate(container, 0, this.progress);
		PacketHandler.sendToPlayer(new PacketTileUpdate(this.pos), (EntityPlayerMP) listener);
	}

}
