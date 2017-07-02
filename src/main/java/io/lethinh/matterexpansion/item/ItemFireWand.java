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

import org.apache.commons.lang3.Validate;

import io.lethinh.matterexpansion.Config;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 *
 * @author Le Thinh
 */
public class ItemFireWand extends GenericEnergizedItem {

	public ItemFireWand() {
		super("item_fire_wand", 1000000);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		player.setActiveHand(hand);
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		Validate.isTrue(!world.isRemote);

		if (stack.isItemEqual(new ItemStack(this))
				&& this.getEnergyStored(stack) >= Config.fireWandEnergyUseBase * Config.fireWandExplosionPower) {
			final EntityPlayer player = (EntityPlayer) entityLiving;
			final Vec3d playerLook = player.getForward();
			final EntityLargeFireball fireball = new EntityLargeFireball(world, player, 0, 0, 0);

			fireball.explosionPower = Config.fireWandExplosionPower;
			fireball.setPosition(player.posX + playerLook.xCoord * 5, player.posY + playerLook.yCoord * 5,
					player.posZ + playerLook.zCoord * 5);
			fireball.accelerationX = playerLook.xCoord * 0.1;
			fireball.accelerationY = playerLook.yCoord * 0.1;
			fireball.accelerationZ = playerLook.zCoord * 0.1;
			world.spawnEntity(fireball);

			if (!player.capabilities.isCreativeMode) {
				this.extractEnergy(stack, Config.fireWandEnergyUseBase * Config.fireWandExplosionPower, false);
			}
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

}
