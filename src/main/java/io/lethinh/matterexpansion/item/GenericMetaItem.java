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

package io.lethinh.matterexpansion.item;

import java.util.HashMap;

import javax.annotation.Nonnull;

import io.lethinh.matterexpansion.MatterExpansion;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericMetaItem extends GenericItem {

	protected final HashMap<Integer, String> names = new HashMap<>();

	public GenericMetaItem(String name) {
		super(name);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, NonNullList<ItemStack> subItems) {
		this.names.entrySet().forEach(entry -> {
			subItems.add(new ItemStack(item, 1, entry.getKey()));
		});
	}

	@Override
	public String getUnlocalizedName(@Nonnull ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + this.names.get(stack.getMetadata());
	}

	/* IItemModelRegister */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemModel() {
		this.names.entrySet().forEach(entry -> {
			ModelLoader.setCustomModelResourceLocation(this, entry.getKey(), new ModelResourceLocation(
					this.getRegistryName(), "type=" + this.names.get(entry.getValue())));
		});
	}

	/* META */
	/**
	 * This method will add an ItemStack to the meta ItemStacks list.
	 *
	 * @param name
	 *            The name of the ItemStack in the meta ItemStacks list need to
	 *            add.
	 * @param meta
	 *            The meta of the ItemStack in the meta items list.
	 *
	 * @return The ItemStack to be added in the meta items list.
	 */
	@Nonnull
	public ItemStack addItemStack(String name, int meta) {
		if (this.names.containsKey(meta)) {
			MatterExpansion.LOGGER.error("The meta ItemStack's name has already registered or something went wrong!");
		}

		this.names.put(meta, name);
		return new ItemStack(this, 1, meta);
	}

	/**
	 * Adds an ItemStack to the meta ItemStacks list and registers it in the
	 * OreDictionary list.
	 *
	 * @param name
	 *            The name of the ItemStack and the Ore Dictionary stack in the
	 *            meta ItemStacks list need to add and register in the Ore
	 *            Dictionary list.
	 * @param meta
	 *            The meta of the ItemStack in the meta ItemStacks list.
	 *
	 * @return The ItemStack to be added in the meta ItemStacks list.
	 */
	@Nonnull
	public ItemStack addOreDictItemStack(String name, int meta) {
		return this.addOreDictItemStack(name, meta, name);
	}

	/**
	 * Adds an ItemStack to the meta ItemStacks list and registers it in the
	 * OreDictionary list.
	 *
	 * @param name
	 *            The name of the ItemStack in the meta ItemStacks list need to
	 *            add.
	 * @param meta
	 *            The meta of the ItemStack in the meta ItemStacks list.
	 *
	 * @param oreName
	 *            The name of the Ore Dictionary ItemStack need to register in
	 *            the Ore Dictionary list.
	 *
	 * @return The ItemStack to be added in the meta ItemStacks list.
	 */
	@Nonnull
	public ItemStack addOreDictItemStack(String name, int meta, String oreName) {
		final ItemStack stack = this.addItemStack(name, meta);
		assert !stack.isEmpty();
		OreDictionary.registerOre(oreName, stack);
		return stack;
	}

}
