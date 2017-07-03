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

import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;

import io.lethinh.matterexpansion.backend.helpers.FilteredTank;
import io.lethinh.matterexpansion.backend.helpers.IGuiTile;
import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import io.lethinh.matterexpansion.crafting.SolderMeltingRecipe;
import io.lethinh.matterexpansion.crafting.SolderRecipe;
import io.lethinh.matterexpansion.gui.ContainerSolderingStation;
import io.lethinh.matterexpansion.gui.GenericContainer;
import io.lethinh.matterexpansion.gui.GuiSolderingStation;
import io.lethinh.matterexpansion.init.ModCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class TileSolderingStation extends GenericMachineCraftingTile implements IGuiTile {

	public int meltingPoint = -1;
	public final FilteredTank tank = new FilteredTank(this, 10 * Fluid.BUCKET_VOLUME)
			.setFluidFilter(FluidRegistry.LAVA);
	public final int SLOT_MELT = 0;
	public final int SLOT_OUTPUT = 1;
	public final int fluidRequired = Fluid.BUCKET_VOLUME / 2;
	public final int maxTime = 300;

	public TileSolderingStation() {
		super(2, "soldering_station", 3, 3);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.tank.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		this.tank.writeToNBT(compound);
		return super.writeToNBT(compound);
	}

	@Override
	public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
		if (index == this.SLOT_MELT)
			return this.getCurrentMeltingRecipe().input.isItemEqual(stack);
		else if (index == this.SLOT_OUTPUT)
			return this.getStackInSlot(this.SLOT_OUTPUT).isItemEqual(stack);
		else if (index >= 2)
			return IntStream.range(0, this.craftMatrix.getSizeInventory())
					.filter(i -> !this.craftMatrix.getStackInSlot(i).isEmpty()
							&& this.getCurrentRecipe().recipe.matches(this.craftMatrix))
					.anyMatch(i -> this.getStackInSlot(i).isItemEqual(stack));

		return false;
	}

	@Override
	public int getField(int data) {
		return this.progress;
	}

	@Override
	public void setField(int data, int value) {
		this.progress = value;
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	/* WORK */
	@Override
	protected boolean canWork() {
		boolean matchesRecipe = false;
		boolean matchesFluid = false;
		final SolderRecipe recipe = ModCrafting.findMatchingRecipe(this.craftMatrix);
		final SolderMeltingRecipe meltingRecipe = ModCrafting
				.findMatchingMeltingRecipe(this.getStackInSlot(this.SLOT_MELT));

		IntStream.range(0, this.craftMatrix.getSizeInventory())
				.filter(i -> !this.craftMatrix.getStackInSlot(i).isEmpty())
				.forEach(i -> this.craftMatrix.stacksList.set(i, this.craftMatrix.getStackInSlot(i)));

		matchesRecipe = recipe.recipe.matches(this.craftMatrix);
		final FluidStack toDrain = recipe.fluid;
		final FluidStack drained = this.tank.drainInternal(toDrain, false);
		matchesFluid = drained != null && drained.isFluidStackIdentical(toDrain);

		return matchesRecipe && matchesFluid;
	}

	@Override
	protected void doClientWork() {
		final SolderRecipe recipe = this.getCurrentRecipe();
		final SolderMeltingRecipe meltingRecipe = this.getCurrentMeltingRecipe();
		final ItemStack output = InventoryUtils.copyItemStack(recipe.recipe.getRecipeOutput());

		// I only apply the timer the soldering process, not the melting
		// process.
		if (this.progress >= this.maxTime && this.getEnergyStored() >= 4000
				&& this.tank.getFluidAmount() >= this.fluidRequired) {
			IntStream.range(0, this.craftMatrix.getSizeInventory()).forEach(i -> {
				// The input fluid of the recipe.
				final FluidStack toDrain = recipe.fluid;

				// The fluid currently contains in the tank.
				final FluidStack drained = this.tank.drainInternal(toDrain, false);

				if (!this.getStackInSlot(i).isEmpty()) {
					// If the fluid currently contains in the tank is matched
					// with the fluid input of the recipe.
					if (drained != null && drained.isFluidStackIdentical(toDrain)) {
						this.decrStackSize(i, 1);
						this.setInventorySlotContents(this.SLOT_OUTPUT, output);
						this.tank.drain(this.fluidRequired, true);
						this.craftMatrix.markDirty();
						this.extractEnergy(4000);
						this.progress = 0;
					}
					// If the fluid has already contained in the tank and it can
					// work.
				} else if (drained != null && drained.isFluidStackIdentical(toDrain)) {
					this.decrStackSize(i, 1);
					this.setInventorySlotContents(this.SLOT_OUTPUT, output);
					this.tank.drain(this.fluidRequired, true);
					this.craftMatrix.markDirty();
					this.extractEnergy(4000);
					this.progress = 0;
				}
			});
		}

		if (this.getStackInSlot(this.SLOT_MELT).isItemEqual(meltingRecipe.input)
				&& this.tank.getFluidAmount() < this.tank.getCapacity() && this.getEnergyStored() >= 500) {
			final FluidStack fluidOutput = meltingRecipe.output;

			if (this.tank.fillInternal(fluidOutput, false) == fluidOutput.amount) {
				this.decrStackSize(this.SLOT_MELT, 1);
				this.tank.fillInternal(fluidOutput, true);
				this.meltingPoint += fluidOutput.amount;
			} else {
				this.meltingPoint = 0;
			}

			this.markDirty();
			this.extractEnergy(500);
		}
	}

	@Override
	protected void doServerWork() {
		super.doServerWork();
		this.progress++;
	}

	@Override
	protected void stopWorking() {
		this.progress = 0;
	}

	/* ENERGY */
	@Override
	public int getCapacity() {
		return 500000;
	}

	private SolderRecipe getCurrentRecipe() {
		final FluidStack fluid = this.tank.getFluid();
		final SolderRecipe recipe = ModCrafting.findMatchingRecipe(this.craftMatrix);
		Validate.notNull(fluid);
		Validate.notNull(recipe);

		if (!fluid.isFluidStackIdentical(recipe.fluid))
			return null;

		return recipe;
	}

	private SolderMeltingRecipe getCurrentMeltingRecipe() {
		final FluidStack fluid = this.tank.getFluid();
		final SolderMeltingRecipe recipe = ModCrafting
				.findMatchingMeltingRecipe(this.getStackInSlot(this.SLOT_MELT));
		Validate.notNull(fluid);
		Validate.notNull(recipe);

		if (!fluid.isFluidStackIdentical(recipe.output))
			return null;

		return recipe;
	}

	/* CAPABILITY */
	@Override
	public IFluidHandler getFluidHandler(EnumFacing facing) {
		return this.tank;
	}

	/* IGuiTile */
	@Override
	public Object getServerGuiElement(EntityPlayer player, int ID, int x, int y, int z) {
		return new ContainerSolderingStation(this, player.inventory);
	}

	@Override
	public Object getClientGuiElement(EntityPlayer player, int ID, int x, int y, int z) {
		return new GuiSolderingStation(this, player.inventory);
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
	}

}
