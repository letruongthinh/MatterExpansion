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

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.model.bakery.CCBakeryModel;
import codechicken.lib.model.bakery.IBakeryProvider;
import codechicken.lib.model.bakery.ModelBakery;
import codechicken.lib.model.bakery.generation.IItemBakery;
import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.helpers.IItemModelRegister;
import io.lethinh.matterexpansion.backend.model.ModelGenericItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericItem extends Item implements IItemModelRegister, IBakeryProvider {

	public GenericItem(String name) {
		this.setRegistryName(MatterExpansion.ModID, name);
		this.setUnlocalizedName(this.getRegistryName().getResourcePath());
		this.setCreativeTab(MatterExpansion.tab);
		this.setContainerItem(this);
		this.setNoRepair();
	}

	/* IItemModelRegister */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemModel() {
		final ModelResourceLocation location = new ModelResourceLocation(this.getRegistryName(), "inventory");

		ModelLoader.setCustomModelResourceLocation(this, 0, location);
		ModelLoader.setCustomMeshDefinition(this, stack -> location);
		ModelRegistryHelper.register(location, new CCBakeryModel(""));
		ModelBakery.registerItemKeyGenerator(this, ModelBakery.getKeyGenerator(this));
	}

	/* IBakeryProvider */
	@SideOnly(Side.CLIENT)
	@Override
	public IItemBakery getBakery() {
		return ModelGenericItem.INSTANCE;
	}

}
