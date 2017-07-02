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

import java.io.IOException;
import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import io.lethinh.matterexpansion.backend.helpers.IBlobsWrapper;
import io.lethinh.matterexpansion.backend.utils.ItemNBTUtils;
import io.lethinh.matterexpansion.backend.utils.StringUtils;
import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public class GenericEnergizedItem extends GenericItem implements IEnergyContainerItem, IBlobsWrapper {

	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public GenericEnergizedItem(String name, int capacity) {
		this(name, capacity, capacity, capacity);
	}

	public GenericEnergizedItem(String name, int capacity, int maxTransfer) {
		this(name, capacity, maxTransfer, maxTransfer);
	}

	public GenericEnergizedItem(String name, int capacity, int maxReceive, int maxExtract) {
		super(name);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;

		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		return stack.getItemDamage() != Short.MAX_VALUE;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(StringUtils.prefixRFEnergy(this.getEnergyStored(stack), this.getMaxEnergyStored(stack)));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		final ItemStack stackEmpty = new ItemStack(item);
		// this.setEnergy(stackEmpty, 0); We don't add this because the default
		// value of no energy container's item is always 0.
		subItems.add(stackEmpty);

		final ItemStack stackFull = new ItemStack(item);
		this.setEnergy(stackFull, this.getMaxEnergyStored(stackFull));
		subItems.add(stackFull);
	}

	// This makes the tooltip not show the durability bar when the energy is
	// full.
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !(this.getEnergyStored(stack) == this.getMaxEnergyStored(stack));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ((double) this.getEnergyStored(stack) / (double) this.getMaxEnergyStored(stack));
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		int curEnergy = this.getEnergyStored(container);
		final int energyReceived = Math.min(this.getMaxEnergyStored(container) - curEnergy,
				Math.min(this.getMaxReceive(container), maxReceive));

		if (!simulate) {
			curEnergy += energyReceived;
			ItemNBTUtils.setInteger(container, "Energy", curEnergy);
		}

		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		int curEnergy = this.getEnergyStored(container);
		final int energyExtracted = Math.min(curEnergy, Math.min(this.getMaxExtract(container), maxExtract));

		if (!simulate) {
			curEnergy -= energyExtracted;
			ItemNBTUtils.setInteger(container, "Energy", curEnergy);
		}

		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		return ItemNBTUtils.getInteger(container, "Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return this.capacity;
	}

	/* ENERGY */
	/**
	 * @author KingLemming
	 *
	 *         There are some methods and fields that I have modified and edited
	 *         so that it can write any changed energy value to NBT.
	 */
	public int getMaxReceive(ItemStack container) {
		return this.maxReceive;
	}

	public int getMaxExtract(ItemStack container) {
		return this.maxExtract;
	}

	public void setEnergy(ItemStack container, int energy) {
		if (energy > this.getMaxEnergyStored(container)) {
			energy = this.getMaxEnergyStored(container);
		} else if (energy < 0) {
			energy = 0;
		}

		ItemNBTUtils.setInteger(container, "Energy", energy);
	}

	/**
	 * This function is included to allow the containing tile to directly and
	 * efficiently modify the energy contained in the EnergyStorage. Do not rely
	 * on this externally, as not all IEnergyHandlers are guaranteed to have it.
	 *
	 * @param modifyEnergy
	 */
	public void modifyEnergyStored(ItemStack container, int modifyEnergy) {
		int curEnergy = this.getEnergyStored(container);
		curEnergy += modifyEnergy;

		if (this.energy > this.capacity) {
			this.energy = this.capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}

		ItemNBTUtils.setInteger(container, "Energy", curEnergy);
	}

	/* PACKET */
	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		this.energy = packet.readInt();
		this.capacity = packet.readInt();
		this.maxExtract = packet.readInt();
		this.maxReceive = packet.readInt();
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		packet.writeInt(this.energy);
		packet.writeInt(this.capacity);
		packet.writeInt(this.maxExtract);
		packet.writeInt(this.maxReceive);
	}

}
