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

package io.lethinh.matterexpansion.item.tool;

import com.google.common.collect.Multimap;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.model.bakery.CCBakeryModel;
import codechicken.lib.model.bakery.IBakeryProvider;
import codechicken.lib.model.bakery.ModelBakery;
import codechicken.lib.model.bakery.generation.IItemBakery;
import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.helpers.IItemModelRegister;
import io.lethinh.matterexpansion.backend.model.ModelToolItem;
import io.lethinh.matterexpansion.backend.utils.AttributeUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class ItemDarkfireSword extends ItemSword implements IItemModelRegister, IBakeryProvider {

	public static Item.ToolMaterial DARKFIRE_SWORD = EnumHelper.addToolMaterial("itemDarkfireSword", 64, -1, 100.0f,
			1000.0F, 1000); // Unbreakble. Yay

	public ItemDarkfireSword() {
		super(DARKFIRE_SWORD);
		this.setRegistryName(MatterExpansion.ModID, "darkfire_sword");
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setCreativeTab(MatterExpansion.tab);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public boolean hitEntity(ItemStack heldItem, EntityLivingBase target, EntityLivingBase player) {
		if (player.world.isRemote)
			return true;

		final EntityPlayer realPlayer = (EntityPlayer) player;

		if (!realPlayer.capabilities.isCreativeMode && target instanceof EntityPlayer
				&& heldItem.isItemEqual(new ItemStack(this))) {
			// Attack the entity
			target.attackEntityFrom(
					new EntityDamageSource("darkfire_sword", realPlayer).setDamageBypassesArmor().setFireDamage()
							.setMagicDamage()
							.setProjectile(),
					1000.0F);
			target.addVelocity(5.0D, 5.0D, 5.0D); // Adding knockback
			realPlayer.attackTargetEntityWithCurrentItem(target); // What does
																	// this do?
																	// May be
																	// does some
																	// special
																	// attacks?
			target.heal(-2.0F);
			realPlayer.heal(4.0F); // YEAH. Soul stealer
		}

		target.setDead();
		target.onDeath(new EntityDamageSource("darkfire_sword", realPlayer));
		target.heal(-2.0F);
		realPlayer.heal(2.0F); // YEAH. Soul stealer. Again
		return true;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		final Multimap<String, AttributeModifier> modifierMap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityEquipmentSlot.MAINHAND) {
			AttributeUtils.replaceAttribute(modifierMap, SharedMonsterAttributes.ATTACK_SPEED,
					ATTACK_SPEED_MODIFIER, 6.0D); // Super fast sword. 10
													// attack speed. Offset by
													// 4.0D
		}

		return modifierMap;
	}

	/* IBakeryProvider */
	@SideOnly(Side.CLIENT)
	@Override
	public IItemBakery getBakery() {
		return ModelToolItem.INSTANCE;
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

}
