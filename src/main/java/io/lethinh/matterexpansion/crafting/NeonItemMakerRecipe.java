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

/**
 *
 * @author Le Thinh
 */
public class NeonItemMakerRecipe {

	public final ItemStack input_1;
	public final ItemStack input_2;
	public final ItemStack output;

	public NeonItemMakerRecipe(@Nonnull ItemStack input_1, @Nonnull ItemStack input_2, ItemStack output) {
		this.input_1 = input_1;
		this.input_2 = input_2;
		this.output = output;
	}

}
