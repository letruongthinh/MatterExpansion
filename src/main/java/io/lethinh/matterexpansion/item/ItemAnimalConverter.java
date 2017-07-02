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

import java.util.Random;

import org.apache.commons.lang3.Validate;

import io.lethinh.matterexpansion.Config;
import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

/**
 *
 * @author Le Thinh
 */
public class ItemAnimalConverter extends GenericEnergizedItem {

	public ItemAnimalConverter() {
		super("item_animal_converter", 1000000);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target,
			EnumHand hand) {
		Validate.isTrue(!target.world.isRemote || target instanceof EntityAnimal || !stack.isEmpty());

		final ResourceLocation animalName = EntityList.getKey(target);
		final Random random = new Random();

		if (this.getEnergyStored(stack) >= Config.animalConverterEnergyUse && stack.isItemEqual(new ItemStack(this))) {
			if (EntityList.ENTITY_EGGS.containsKey(animalName)) {
				final EntityAnimal animal = (EntityAnimal) target;
				final ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);

				final NBTTagCompound compound = new NBTTagCompound();
				final NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setString("id", animalName.getResourcePath());
				compound.setTag("EntityTag", tagCompound);
				spawnEgg.setTagCompound(compound);

				animal.world.playSound(player, animal.getPosition(), SoundEvents.ENTITY_BOBBER_THROW,
						SoundCategory.NEUTRAL, 0.3F, random.nextFloat() * 0.1F + 0.9F);
				animal.world.removeEntity(animal);
				InventoryUtils.dropItemStack(animal.world, animal.getPosition(), spawnEgg);

				if (!player.capabilities.isCreativeMode) {
					this.extractEnergy(stack, Config.animalConverterEnergyUse, false);
				}
			}
		}

		return true;
	}

}
