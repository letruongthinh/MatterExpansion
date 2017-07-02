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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 *
 * @author Le Thinh
 */
public class SolderRecipe {

	public final FluidStack fluid;
	public final SolderShapedRecipe recipe;

	public SolderRecipe(FluidStack fluid, Block output, Object... recipe) {
		this(fluid, new ItemStack(output), recipe);
	}

	public SolderRecipe(FluidStack fluid, Item output, Object... recipe) {
		this(fluid, new ItemStack(output), recipe);
	}

	public SolderRecipe(FluidStack fluid, @Nonnull ItemStack output, Object... ingredients) {
		this(fluid, new SolderShapedRecipe(output, ingredients));
	}

	public SolderRecipe(FluidStack fluid, SolderShapedRecipe recipe) {
		this.fluid = fluid;
		this.recipe = recipe;
	}

}
