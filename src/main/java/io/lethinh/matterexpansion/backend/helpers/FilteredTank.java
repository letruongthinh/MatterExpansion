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

import java.util.HashSet;

import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

/**
 *
 * @author Le Thinh
 */
public class FilteredTank extends EligibleTank {

	protected HashSet<Fluid> fluidsFilter = new HashSet<>();

	public FilteredTank(@Nullable TileEntity tile, int capacity) {
		super(tile, capacity);
	}

	public FilteredTank(@Nullable TileEntity tile, @Nullable FluidStack fluidStack, int capacity) {
		super(tile, fluidStack, capacity);
	}

	public FilteredTank(@Nullable TileEntity tile, Fluid fluid, int amount, int capacity) {
		super(tile, fluid, amount, capacity);
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid) {
		return this.filterFluidFillAndDrain(fluid);
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluid) {
		return this.filterFluidFillAndDrain(fluid);
	}

	/* FILTER */
	protected boolean filterFluidFillAndDrain(FluidStack fluid) {
		Validate.notNull(fluid);
		return this.fluidsFilter.contains(fluid.getFluid());
	}

	public FilteredTank setFluidFilter(Fluid... fluidsFilter) {
		// Clear the fluids filter before setting the new one.
		this.fluidsFilter.clear();

		// Convert fluidsFilter array to HashSet for adding new elements.
		this.fluidsFilter = new HashSet<>(Arrays.asList(fluidsFilter));

		this.fluidsFilter.stream().filter(fluid -> fluid != null).forEach(fluid -> this.fluidsFilter.add(fluid));
		return this;
	}

}
