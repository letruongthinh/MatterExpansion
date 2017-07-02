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

package io.lethinh.matterexpansion.block;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class BlockMetal extends GenericBlock {

	public static final PropertyEnum<MetalType> VARIANT = PropertyEnum.create("type", MetalType.class);

	public BlockMetal() {
		super("block_metal", Material.IRON);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		subItems.addAll(Stream.of(MetalType.values()).map(type -> new ItemStack(item, 1, type.getMeta()))
				.collect(Collectors.toList()));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, MetalType.byMetadata(meta));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, BlockPos originPos, BlockPos beaconPos) {
		return true;
	}

	/* IItemModelRegister */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerItemModel() {
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this),
				stack -> new ModelResourceLocation(this.getRegistryName(),
						"type=" + MetalType.byMetadata(stack.getMetadata()).getName()));
	}

	public enum MetalType implements IStringSerializable {
		NEON(0, "neon"),
		MEGA(1, "mega"),
		DARKFIRE(2, "darkfire");

		private static final MetalType[] META_LOOKUP = Stream.of(values()).toArray(MetalType[]::new);

		private final int meta;
		private final String name;

		MetalType(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public int getMeta() {
			return this.meta;
		}

		public static MetalType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

	}

}
