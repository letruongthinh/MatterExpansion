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

package io.lethinh.matterexpansion.backend.model;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import codechicken.lib.model.ItemQuadBakery;
import codechicken.lib.model.PerspectiveAwareModelProperties;
import codechicken.lib.model.bakery.generation.IItemBakery;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class ModelGenericItem implements IItemBakery {

	public static final ModelGenericItem INSTANCE = new ModelGenericItem();

	/* IItemBakery */
	@SideOnly(Side.CLIENT)
	@Override
	public List<BakedQuad> bakeItemQuads(EnumFacing side, ItemStack stack) {
		final ArrayList<BakedQuad> quads = new ArrayList<>();

		if (side == null) {
			quads.addAll(ItemQuadBakery.bakeItem(ImmutableList.of()));
		}

		return quads;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public PerspectiveAwareModelProperties getModelProperties(ItemStack stack) {
		return PerspectiveAwareModelProperties.DEFAULT_ITEM;
	}

}
