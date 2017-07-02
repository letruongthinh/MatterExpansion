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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 *
 * @author Le Thinh
 */
public class ItemNBTUtils {

	/* GETTERS */
	public static NBTTagCompound getTagMap(@Nonnull ItemStack stack) {
		initStackTag(stack);

		return stack.getTagCompound();
	}

	public static boolean hasKey(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).hasKey(key);
	}

	public static void removeTag(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		getTagMap(stack).removeTag(key);
	}

	public static int getInteger(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).getInteger(key);
	}

	public static boolean getBoolean(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).getBoolean(key);
	}

	public static double getDouble(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).getDouble(key);
	}

	public static String getString(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).getString(key);
	}

	public static NBTTagCompound getCompoundTag(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).getCompoundTag(key);
	}

	public static NBTTagList getTagList(@Nonnull ItemStack stack, String key) {
		initStackTag(stack);

		return getTagMap(stack).getTagList(key, NBT.TAG_COMPOUND);
	}

	/* SETTERS */
	public static void setInteger(@Nonnull ItemStack stack, String key, int i) {
		initStackTag(stack);

		getTagMap(stack).setInteger(key, i);
	}

	public static void setBoolean(@Nonnull ItemStack stack, String key, boolean b) {
		initStackTag(stack);

		getTagMap(stack).setBoolean(key, b);
	}

	public static void setDouble(@Nonnull ItemStack stack, String key, double d) {
		initStackTag(stack);

		getTagMap(stack).setDouble(key, d);
	}

	public static void setString(@Nonnull ItemStack stack, String key, String s) {
		initStackTag(stack);

		getTagMap(stack).setString(key, s);
	}

	public static void setCompoundTag(@Nonnull ItemStack stack, String key, NBTTagCompound tag) {
		initStackTag(stack);

		getTagMap(stack).setTag(key, tag);
	}

	public static void setTagList(@Nonnull ItemStack stack, String key, NBTTagList tag) {
		initStackTag(stack);

		getTagMap(stack).setTag(key, tag);
	}

	public static void initStackTag(@Nonnull ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		} else {
			stack.setTagCompound(stack.getTagCompound());
		}
	}

}
