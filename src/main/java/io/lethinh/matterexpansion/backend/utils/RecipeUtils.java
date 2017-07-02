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

package io.lethinh.matterexpansion.backend.utils;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 *
 * @author Le Thinh
 */
public class RecipeUtils {

	/* CRAFTING */
	public static void addShapedOreRecipe(Block result, Object... recipe) {
		GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
	}

	public static void addShapedOreRecipe(Item result, Object... recipe) {
		GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
	}

	public static void addShapedOreRecipe(@Nonnull ItemStack result, Object... recipe) {
		GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
	}

	public static void addShapelessOreRecipe(Block result, Object... recipe) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
	}

	public static void addShapelessOreRecipe(Item result, Object... recipe) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
	}

	public static void addShapelessOreRecipe(@Nonnull ItemStack result, Object... recipe) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
	}

	/* AUTO */
	public static void addMetalRecipe(String oreDict, @Nonnull ItemStack nugget, @Nonnull ItemStack ingot,
			@Nonnull ItemStack block) {
		// Compress
		addShapedOreRecipe(ingot, "###", "###", "###", '#', oreDict);
		addShapedOreRecipe(block, "###", "###", "###", '#', oreDict);

		// Uncompress
		addShapelessOreRecipe(InventoryUtils.copyItemStack(nugget, 9), oreDict);
		addShapelessOreRecipe(InventoryUtils.copyItemStack(ingot, 9), oreDict);
	}

}
