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

import io.lethinh.matterexpansion.backend.helpers.IItemModelRegister;
import io.lethinh.matterexpansion.item.ItemAnimalConverter;
import io.lethinh.matterexpansion.item.ItemFireWand;
import io.lethinh.matterexpansion.item.ItemMetal;
import io.lethinh.matterexpansion.item.tool.ItemDarkfirePickaxe;
import io.lethinh.matterexpansion.item.tool.ItemDarkfireSword;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 *
 * @author Le Thinh
 */
public class ModItems {

	public static final Item.ToolMaterial DARKFIRE = EnumHelper.addToolMaterial("itemDarkfirePickaxe", 64, -1, 64.0f,
			12, 1000);

	public static ItemAnimalConverter itemAnimalConverter;
	public static ItemFireWand itemFireWand;
	public static ItemDarkfirePickaxe itemDarkfirePickaxe;
	public static ItemDarkfireSword itemDarkfireSword;
	public static ItemMetal itemMetal;

	/* METAL */
	public static ItemStack itemIngotNeon;
	public static ItemStack itemIngotMega;
	public static ItemStack itemIngotDarkfire;

	public static ItemStack itemNuggetNeon;
	public static ItemStack itemNuggetMega;
	public static ItemStack itemNuggetDarkfire;

	public static void preInit() {
		itemAnimalConverter = registerItem(new ItemAnimalConverter());
		itemFireWand = registerItem(new ItemFireWand());
		itemDarkfirePickaxe = registerItem(new ItemDarkfirePickaxe());
		itemDarkfireSword = registerItem(new ItemDarkfireSword());
		itemMetal = registerItem(new ItemMetal());

		initMetaItemStacks();
	}

	private static void initMetaItemStacks() {
		// METAL
		itemIngotNeon = itemMetal.addOreDictItemStack("ingotNeon", 0);
		itemIngotMega = itemMetal.addOreDictItemStack("ingotMega", 1);
		itemIngotDarkfire = itemMetal.addOreDictItemStack("ingotDarkfire", 2);

		itemNuggetNeon = itemMetal.addOreDictItemStack("nuggetNeon", 3);
		itemNuggetMega = itemMetal.addOreDictItemStack("nuggetMega", 4);
		itemNuggetDarkfire = itemMetal.addOreDictItemStack("nuggetDarkfire", 5);
	}

	/* REGISTRY */
	private static <T extends Item> T registerItem(T item) {
		GameRegistry.register(item);

		if (item instanceof IItemModelRegister) {
			((IItemModelRegister) item).registerItemModel();
		}

		return item;
	}

}
