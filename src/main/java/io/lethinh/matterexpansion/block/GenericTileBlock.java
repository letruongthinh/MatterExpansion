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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.Validate;

import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import io.lethinh.matterexpansion.backend.utils.ItemNBTUtils;
import io.lethinh.matterexpansion.init.GuiHandler;
import io.lethinh.matterexpansion.tile.GenericPowerTile;
import io.lethinh.matterexpansion.tile.GenericTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericTileBlock<TE extends GenericTile> extends GenericBlock {

	protected final TE tile;

	public GenericTileBlock(String name, Material material, TE tile) {
		super(name, material);
		Validate.notNull(tile);

		this.tile = tile;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			@Nonnull ItemStack stack) {
		assert this.tile != null;

		this.tile.readFromNBT(ItemNBTUtils.getCompoundTag(stack, "TileData"));

		if (this.tile instanceof GenericPowerTile) {
			((GenericPowerTile) this.tile).setEnergyStored(ItemNBTUtils.getInteger(stack, "Energy"));
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY,
			float hitZ) {
		if (this.tile.isInvalid())
			return false;

		if (world.isRemote)
			return true;
		else {
			player.openGui(MatterExpansion.instance, GuiHandler.GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		InventoryUtils.dropItemStack(world, pos, InventoryUtils.createItemStackFromTile(this.tile));
		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		super.onBlockExploded(world, pos, explosion);
		InventoryUtils.dropItemStack(world, pos, InventoryUtils.createItemStackFromTile(this.tile));
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos,
			EntityLiving.SpawnPlacementType type) {
		return false;
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return InventoryUtils.createItemStackFromTile(this.tile);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(world, pos, state, fortune);
		final ArrayList<ItemStack> retStacks = new ArrayList<>();
		retStacks.add(InventoryUtils.createItemStackFromTile(this.tile));
		return retStacks;
	}

	/* TILE */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TE createTileEntity(World world, IBlockState state) {
		return this.createNewTileEntity(world, state.getBlock().getStateId(state));
	}

	@Nullable
	protected TE createNewTileEntity(World world, int meta) {
		return this.tile;
	}

	public abstract Class<TE> getTileClass();

}
