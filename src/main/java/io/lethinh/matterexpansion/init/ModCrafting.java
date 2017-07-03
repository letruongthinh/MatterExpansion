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

package io.lethinh.matterexpansion.init;

import java.util.HashSet;

import javax.annotation.Nonnull;

import io.lethinh.matterexpansion.backend.utils.RecipeUtils;
import io.lethinh.matterexpansion.crafting.FreezerRecipe;
import io.lethinh.matterexpansion.crafting.NeonItemMakerRecipe;
import io.lethinh.matterexpansion.crafting.SolderMeltingRecipe;
import io.lethinh.matterexpansion.crafting.SolderRecipe;
import io.lethinh.matterexpansion.tile.inventory.PerpetualInventoryCrafting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Le Thinh
 */
public class ModCrafting {

	// The elements cannot be duplicate. HashSet is more preferable than
	// ArrayList in this case.
	public static final HashSet<FreezerRecipe> FREEZER_RECIPES = new HashSet<>();
	public static final HashSet<NeonItemMakerRecipe> NEON_ITEM_MAKER_RECIPES = new HashSet<>();
	public static final HashSet<SolderRecipe> SOLDER_RECIPES = new HashSet<>();
	public static final HashSet<SolderMeltingRecipe> SOLDER_MELTING_RECIPES = new HashSet<>();

	public static void postInit() {
		// CRAFTING
		RecipeUtils.addMetalRecipe("Neon", ModItems.itemNuggetNeon, ModItems.itemIngotNeon, ModBlocks.blockNeon);
		RecipeUtils.addMetalRecipe("Mega", ModItems.itemNuggetMega, ModItems.itemIngotMega, ModBlocks.blockMega);
		RecipeUtils.addMetalRecipe("Darkfire", ModItems.itemNuggetDarkfire, ModItems.itemIngotDarkfire,
				ModBlocks.blockDarkfire);

		// FREEZER
		addFreezerRecipe(new ItemStack(Items.WATER_BUCKET), new ItemStack(Blocks.ICE));

		// SOLDER
		addSolderMeltingRecipe(new ItemStack(Items.LAVA_BUCKET),
				FluidRegistry.getFluidStack(Blocks.LAVA.getUnlocalizedName(), Fluid.BUCKET_VOLUME),
				Fluid.BUCKET_VOLUME);

		addSolderRecipe(FluidRegistry.getFluidStack("Lava", Fluid.BUCKET_VOLUME), ModItems.itemIngotDarkfire,
				"ABA",
				"BCB",
				"ABA",
				'A', ModItems.itemNuggetNeon,
				'B', Items.ENDER_EYE,
				'C', ModItems.itemIngotMega);
	}

	/* FREEZER */
	public static void addFreezerRecipe(ItemStack input, ItemStack output) {
		FREEZER_RECIPES.add(new FreezerRecipe(input, output));
	}

	/* NEON ITEM MAKER */
	public static void addNeonItemMakerRecipe(ItemStack input_1, ItemStack input_2, ItemStack output) {
		NEON_ITEM_MAKER_RECIPES.add(new NeonItemMakerRecipe(input_1, input_2, output));
	}

	/* SOLDER */
	public static void addSolderRecipe(FluidStack fluid, ItemStack result, Object... recipe) {
		SOLDER_RECIPES.add(new SolderRecipe(fluid, result, recipe));
	}

	public static SolderRecipe findMatchingSolderRecipe(PerpetualInventoryCrafting craftMatrix) {
		return SOLDER_RECIPES.stream().filter(recipe -> recipe.recipe.matches(craftMatrix)).findFirst()
				.orElse(null);
	}

	public static void addSolderMeltingRecipe(@Nonnull ItemStack input, FluidStack result, int fluidPerMelt) {
		SOLDER_MELTING_RECIPES.add(new SolderMeltingRecipe(input, result, fluidPerMelt));
	}

	public static SolderMeltingRecipe findMatchingSolderMeltingRecipe(@Nonnull ItemStack input) {
		return SOLDER_MELTING_RECIPES.stream().filter(recipe -> OreDictionary.itemMatches(recipe.input, input, true))
				.findFirst().orElse(null);
	}

}
