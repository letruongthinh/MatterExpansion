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

package io.lethinh.matterexpansion.crafting;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 *
 * @author Le Thinh
 */
public class SolderMeltingRecipe {

	public final ItemStack input;
	public final FluidStack output;
	public final int fluidPerMelt;

	public SolderMeltingRecipe(@Nonnull ItemStack input, FluidStack output, int fluidPerMelt) {
		this.input = input;
		this.output = output;
		this.fluidPerMelt = fluidPerMelt;
	}

}
