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

package io.lethinh.matterexpansion.backend.helpers;

import java.io.IOException;

import javax.annotation.Nullable;

import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 *
 * @author Le Thinh
 */
public class EligibleTank extends FluidTank implements IBlobsWrapper {

	public EligibleTank(TileEntity tile, int capacity) {
		super(capacity);
		this.tile = tile;
	}

	public EligibleTank(TileEntity tile, @Nullable FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
		this.tile = tile;
	}

	public EligibleTank(TileEntity tile, Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
		this.tile = tile;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return fluid != null && FluidRegistry.isUniversalBucketEnabled() || super.canDrainFluidType(fluid);
	}

	@Override
	protected void onContentsChanged() {
		final IBlockState state = this.tile.getWorld().getBlockState(this.tile.getPos());

		this.tile.markDirty();
		this.tile.getWorld().notifyBlockUpdate(this.tile.getPos(), state, state, 8);

	}

	/* FLUID */
	public boolean isFull() {
		return this.getFluidAmount() == this.getCapacity();
	}

	public boolean isEmpty() {
		return this.getFluid() == null || this.getFluidAmount() == 0;
	}

	/* PACKET */
	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		this.fluid = packet.readFluidStack();
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		packet.writeFluidStack(this.fluid);
	}

}
