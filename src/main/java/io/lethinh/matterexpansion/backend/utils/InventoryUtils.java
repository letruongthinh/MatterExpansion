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

import java.util.Random;

import javax.annotation.Nonnull;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Le Thinh
 */
public class InventoryUtils {

	/* DROP */
	/**
	 * [VanillaCopy]
	 * {@link net.minecraft.inventory.InventoryHelper#spawnItemStack} Make it
	 * easier using BlockPos instead of x, y, z.
	 *
	 * @param world
	 *            The current world
	 * @param pos
	 *            The ItemStack position
	 * @param stack
	 *            The ItemStack will be dropped
	 */
	public static void dropItemStack(World world, BlockPos pos, @Nonnull ItemStack stack) {
		if (!world.isRemote) {
			final Random random = new Random();
			final float f = random.nextFloat() * 0.8F + 0.1F;
			final float f1 = random.nextFloat() * 0.8F + 0.1F;
			final float f2 = random.nextFloat() * 0.8F + 0.1F;

			while (!stack.isEmpty()) {
				final int i = random.nextInt(21) + 10;

				final EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2,
						stack.splitStack(i));

				entityitem.motionX = random.nextGaussian() * 0.05000000074505806D;
				entityitem.motionY = random.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
				entityitem.motionZ = random.nextGaussian() * 0.05000000074505806D;
				world.spawnEntity(entityitem);
			}
		}
	}

	/* STACK */
	@Nonnull
	public static ItemStack createItemStackFromTile(TileEntity tile) {
		final ItemStack retStack = new ItemStack(tile.getBlockType(), 1, tile.getBlockMetadata());

		ItemNBTUtils.initStackTag(retStack);
		ItemNBTUtils.setCompoundTag(retStack, "TileData", tile.writeToNBT(new NBTTagCompound()));

		if (tile instanceof IEnergyReceiver) {
			ItemNBTUtils.setInteger(retStack, "Energy", ((IEnergyReceiver) tile).getEnergyStored(null));
		}

		return !retStack.isEmpty() ? retStack : ItemStack.EMPTY;
	}

	@Nonnull
	public static ItemStack copyItemStack(@Nonnull ItemStack stack) {
		final ItemStack retStack = stack.copy();
		return !retStack.isEmpty() ? retStack : ItemStack.EMPTY;
	}

	@Nonnull
	public static ItemStack copyItemStack(@Nonnull ItemStack stack, int stackSize) {
		final ItemStack retStack = copyItemStack(stack);
		retStack.setCount(stackSize);
		return !retStack.isEmpty() ? retStack : ItemStack.EMPTY;
	}

}
