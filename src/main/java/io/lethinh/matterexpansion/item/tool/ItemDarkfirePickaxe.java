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

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.model.bakery.CCBakeryModel;
import codechicken.lib.model.bakery.IBakeryProvider;
import codechicken.lib.model.bakery.ModelBakery;
import codechicken.lib.model.bakery.generation.IItemBakery;
import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.helpers.IItemModelRegister;
import io.lethinh.matterexpansion.backend.helpers.TooltipHandler;
import io.lethinh.matterexpansion.backend.model.ModelToolItem;
import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import io.lethinh.matterexpansion.backend.utils.ItemNBTUtils;
import io.lethinh.matterexpansion.backend.utils.StringUtils;
import io.lethinh.matterexpansion.backend.utils.ToolUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class ItemDarkfirePickaxe extends ItemPickaxe implements IItemModelRegister, IBakeryProvider {

	public static Item.ToolMaterial DARKFIRE_PICKAXE = EnumHelper.addToolMaterial("itemDarkfirePickaxe", 64, -1, 100.0f,
			12, 1000); // Unbreakble. Yay

	private final String NBT_MODE = MatterExpansion.ModID + "_mode";

	public ItemDarkfirePickaxe() {
		super(DARKFIRE_PICKAXE);
		this.setRegistryName(MatterExpansion.ModID, "itemDarkfirePickaxe");
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setCreativeTab(MatterExpansion.tab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && player.isSneaking()) {
			final ItemStack heldItem = player.getHeldItem(hand);

			this.toggleMode(heldItem);
			TooltipHandler
					.setTooltip(I18n.format(StringUtils.prefixTooltip("itemDarkfirePickaxe", this.getMode(heldItem))));
		}

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos,
			EntityPlayer player) {
		final RayTraceResult raycast = ToolUtils.raytraceFromEntity(player.world, player, true, 10);

		if (!player.world.isRemote && raycast != null) {
			this.breakOtherBlock(player, stack, pos, pos, raycast.sideHit);
		}

		return false;
	}

	/**
	 * Copied from Botania by Vazkii. Have edited and modifed. Under the Botania
	 * license: http://botaniamod.net/license.php
	 */
	private void breakOtherBlock(EntityPlayer player, ItemStack stack, BlockPos pos, BlockPos originPos,
			EnumFacing side) {
		final World world = player.world;
		final Material mat = world.getBlockState(pos).getMaterial();
		if (!ToolUtils.materialsPick.contains(mat))
			return;

		if (world.isAirBlock(pos))
			return;

		final boolean doX = side.getFrontOffsetX() == 0;
		final boolean doY = side.getFrontOffsetY() == 0;
		final boolean doZ = side.getFrontOffsetZ() == 0;

		switch (this.getMode(stack)) {
		case 0:
			break;
		case 1:
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
			boolean silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,
					player.getHeldItemMainhand()) > 0;
			int range = 3;
			int rangeY = Math.max(1, range);

			BlockPos beginDiff = new BlockPos(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
			BlockPos endDiff = new BlockPos(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

			ToolUtils.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null,
					ToolUtils.materialsPick, silk, fortune);
			break;
		case 2:
			fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
			silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;

			range = 5;
			rangeY = Math.max(1, range);

			beginDiff = new BlockPos(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
			endDiff = new BlockPos(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

			ToolUtils.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null,
					ToolUtils.materialsPick, silk, fortune);
			break;
		case 3:
			fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
			silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;

			range = 7;
			rangeY = Math.max(1, range);

			beginDiff = new BlockPos(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
			endDiff = new BlockPos(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

			ToolUtils.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null,
					ToolUtils.materialsPick, silk, fortune);
			break;
		case 4:
			fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
			silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;

			range = 9;
			rangeY = Math.max(1, range);

			beginDiff = new BlockPos(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
			endDiff = new BlockPos(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

			ToolUtils.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null,
					ToolUtils.materialsPick, silk, fortune);
			break;
		case 5:
			fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
			silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;

			range = 11;
			rangeY = Math.max(1, range);

			beginDiff = new BlockPos(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
			endDiff = new BlockPos(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

			ToolUtils.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null,
					ToolUtils.materialsPick, silk, fortune);
			break;
		case 6:
			fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand());
			silk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0;

			range = 10;

			final int xo = -side.getFrontOffsetX();
			final int yo = -side.getFrontOffsetY();
			final int zo = -side.getFrontOffsetZ();

			beginDiff = new BlockPos(xo >= 0 ? 0 : -range, yo >= 0 ? 0 : -range, zo >= 0 ? 0 : -range);
			endDiff = new BlockPos(xo > 0 ? range : 1, yo > 0 ? range : 1, zo > 0 ? range : 1);

			ToolUtils.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, null,
					ToolUtils.materialsPick, silk, fortune);
			break;
		}
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

	/* MODE */
	/**
	 * @param stack
	 *            The stack that has mode
	 *
	 * @return The current mode of the stack
	 */
	private int getMode(ItemStack stack) {
		return ItemNBTUtils.getInteger(stack, this.NBT_MODE);
	}

	/**
	 * @param stack
	 *            The stack that uses to get the range of the pickaxe
	 *
	 * @return The current range of the pickaxe
	 */
	private int getModeRange(ItemStack stack) {
		switch (this.getMode(stack)) {
		case 0:
			return 1;
		case 1:
			return 3;
		case 2:
			return 5;
		case 3:
			return 7;
		case 4:
			return 9;
		case 5:
			return 11;
		case 6:
			return 10;
		default:
			return 0;
		}
	}

	/**
	 * @param stack
	 *            The stack that can toggle the mode
	 */
	private void toggleMode(ItemStack stack) {
		ItemNBTUtils.setInteger(stack, this.NBT_MODE, this.getMode(stack) < 6 ? this.getMode(stack) + 1 : 0);
	}

	/* EVENT */
	@SubscribeEvent
	public void onPlayerDig(PlayerInteractEvent.LeftClickBlock event) {
		final World world = event.getWorld();
		final EntityPlayer player = event.getEntityPlayer();
		final BlockPos pos = event.getPos();
		final IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();
		final ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);

		if (world.isRemote || event.getFace() == null || heldItem.isEmpty() || player.capabilities.isCreativeMode)
			return;

		// Why is not integer zero? Because Java count the value that compares
		// with value (less or greater), it will take that values use to
		// compare. So if any block doesn't have hardness. Like 0.0F. It will
		// cause to do the wrong thing in the event.
		if (state.getBlockHardness(world, pos) <= -1.0F && heldItem.isItemEqual(new ItemStack(this))) {
			block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), heldItem); // More
																								// efficient
																								// than
																								// world.setBlockToAir.
			world.destroyBlock(pos, false); // If it is set two true, it can
											// cause two block drops problem.
											// The problem is when the block
											// breaking is meta, it will drop
											// the default block and the current
											// meta block.
			InventoryUtils.dropItemStack(world, pos, block.getPickBlock(state,
					ToolUtils.raytraceFromEntity(world, player, true, this.getModeRange(heldItem)), world, pos,
					player));
		}
	}

}
