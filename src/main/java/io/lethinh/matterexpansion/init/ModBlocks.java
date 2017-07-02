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

import io.lethinh.matterexpansion.block.BlockFreezer;
import io.lethinh.matterexpansion.block.BlockMetal;
import io.lethinh.matterexpansion.block.BlockSolderingStation;
import io.lethinh.matterexpansion.block.GenericItemBlock;
import io.lethinh.matterexpansion.block.ItemBlockMetal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * @author Le Thinh
 */
public class ModBlocks {

	public static BlockFreezer blockFreezer;
	public static BlockMetal blockMetal;
	public static BlockSolderingStation blockSolderingStation;

	/* METAL */
	public static ItemStack blockNeon;
	public static ItemStack blockMega;
	public static ItemStack blockDarkfire;

	public static void preInit() {
		blockFreezer = new BlockFreezer();
		GameRegistry.register(new GenericItemBlock(blockFreezer));

		blockMetal = new BlockMetal();
		GameRegistry.register(new ItemBlockMetal(blockMetal));

		blockSolderingStation = new BlockSolderingStation();
		GameRegistry.register(new GenericItemBlock(blockSolderingStation));

		initMetaItemStacks();
	}

	private static void initMetaItemStacks() {
		// METAL
		blockNeon = new ItemStack(blockMetal, 1, BlockMetal.MetalType.NEON.getMeta());
		blockMega = new ItemStack(blockMetal, 1, BlockMetal.MetalType.MEGA.getMeta());
		blockDarkfire = new ItemStack(blockMetal, 1, BlockMetal.MetalType.DARKFIRE.getMeta());
	}

}
