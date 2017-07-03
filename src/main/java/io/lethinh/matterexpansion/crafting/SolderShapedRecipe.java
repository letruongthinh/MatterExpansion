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

import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import io.lethinh.matterexpansion.tile.inventory.PerpetualInventoryCrafting;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 *
 * @author Le Thinh
 */
public class SolderShapedRecipe extends ShapedOreRecipe {

	public SolderShapedRecipe(Block output, Object... recipe) {
		super(new ItemStack(output), recipe);
	}

	public SolderShapedRecipe(Item output, Object... recipe) {
		super(new ItemStack(output), recipe);
	}

	public SolderShapedRecipe(@Nonnull ItemStack output, Object... recipe) {
		super(output, recipe);
	}

	/* RECIPE */
	public boolean matches(PerpetualInventoryCrafting craftMatrix) {
		return IntStream.range(0, this.getWidth())
				.anyMatch(row -> IntStream.range(0, this.getHeight()).anyMatch(column -> {
					if (this.checkMatch(craftMatrix, row, column, false))
						return true;

					if (this.mirrored && this.checkMatch(craftMatrix, row, column, true))
						return true;

					return false;
				}));
	}

	protected boolean checkMatch(PerpetualInventoryCrafting craftMatrix, int x, int y, boolean mirror) {
		return IntStream.range(0, this.getWidth())
				.anyMatch(row -> IntStream.range(0, this.getHeight()).anyMatch(column -> {
					final int xCorner = row - x;
					final int yCorner = column - y;
					Object target = null;

					if (xCorner >= 0 && yCorner >= 0 && xCorner < this.getWidth() && yCorner < this.getHeight()) {
						if (mirror) {
							target = this.input[this.getWidth() - xCorner - 1 + yCorner * this.getWidth()];
						} else {
							target = this.input[xCorner + yCorner * this.getWidth()];
						}
					}

					final ItemStack slot = craftMatrix.getStackInRowAndColumn(row, column);

					if (target instanceof ItemStack) {
						if (!OreDictionary.itemMatches((ItemStack) target, slot, false))
							return false;
					} else if (target instanceof List) {
						boolean matched = false;

						for (final Object recipe : this.input) {
							if (!matched) {
								matched = OreDictionary.itemMatches((ItemStack) recipe, slot, false);
							}
						}

						if (!matched)
							return false;
					} else if (!slot.isEmpty())
						return false;

					return true;
				}));
	}

}
