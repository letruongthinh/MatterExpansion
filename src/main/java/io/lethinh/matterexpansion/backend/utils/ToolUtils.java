/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 13, 2014, 7:13:04 PM (GMT)]
 */
package io.lethinh.matterexpansion.backend.utils;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ToolUtils {

	public static final List<Material> materialsPick = Arrays.asList(Material.ROCK, Material.IRON, Material.ICE,
			Material.GLASS, Material.PISTON, Material.ANVIL);
	public static final List<Material> materialsShovel = Arrays.asList(Material.GRASS, Material.GROUND, Material.SAND,
			Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY);
	public static final List<Material> materialsAxe = Arrays.asList(Material.CORAL, Material.LEAVES, Material.PLANTS,
			Material.WOOD, Material.GOURD);

	public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, BlockPos centerPos,
			Vec3i startDelta, Vec3i endDelta, Block filterBlock, List<Material> materialsListing,
			boolean silk, int fortune) {
		for (BlockPos iterPos : BlockPos.getAllInBox(centerPos.add(startDelta), centerPos.add(endDelta))) {
			if (iterPos.equals(centerPos)) // skip original block space to avoid
											// crash, vanilla code in the tool
											// class will handle it
				continue;
			removeBlockWithDrops(player, stack, world, iterPos, centerPos, filterBlock, materialsListing, silk,
					fortune);
		}
	}

	public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos,
			BlockPos bPos,
			Block filterBlock, List<Material> materialsListing,
			boolean silk, int fortune) {
		if (!world.isBlockLoaded(pos))
			return;

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (filterBlock != null && block != filterBlock)
			return;

		Material mat = world.getBlockState(pos).getMaterial();
		if (!world.isRemote && !block.isAir(state, world, pos)
				&& state.getPlayerRelativeBlockHardness(player, world, pos) > 0) {
			if (!block.canHarvestBlock(player.world, pos, player) || !materialsListing.contains(mat)) {
				return;
			}

			int exp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(),
					(EntityPlayerMP) player, pos);
			if (exp == -1)
				return;

			boolean spawnedDrops = false;

			if (!player.capabilities.isCreativeMode) {
				TileEntity tile = world.getTileEntity(pos);
				IBlockState localState = world.getBlockState(pos);

				if (block.removedByPlayer(state, world, pos, player, true)) {
					block.onBlockDestroyedByPlayer(world, pos, state);
				}
			} else
				world.setBlockToAir(pos);

			if (spawnedDrops)
				block.dropXpOnBlockBreak(world, pos, exp);
		}
	}

	// [VanillaCopy] of Item.rayTrace, edits noted
	public static RayTraceResult raytraceFromEntity(World worldIn, Entity playerIn, boolean useLiquids, double range) {
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.posX;
		double d1 = playerIn.posY + playerIn.getEyeHeight();
		double d2 = playerIn.posZ;
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = range; // Botania - use custom range param, don't limit to
							// reach distance
		/*
		 * if (playerIn instanceof net.minecraft.entity.player.EntityPlayerMP) {
		 * d3 = ((net.minecraft.entity.player.EntityPlayerMP)playerIn).
		 * interactionManager.getBlockReachDistance(); }
		 */
		Vec3d vec3d1 = vec3d.addVector(f6 * d3, f5 * d3, f7 * d3);
		return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

}
