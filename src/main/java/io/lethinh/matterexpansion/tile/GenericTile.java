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

package io.lethinh.matterexpansion.tile;

import java.io.IOException;

import javax.annotation.Nullable;

import io.lethinh.matterexpansion.backend.helpers.IBlobsWrapper;
import io.lethinh.matterexpansion.gui.GenericContainer;
import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericTile extends TileEntity implements IBlobsWrapper {

	protected final String name;

	public GenericTile(String name) {
		this.name = name;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		final NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		PacketHandler.sendToServer(new PacketTileUpdate(this.pos, compound));
		return new SPacketUpdateTileEntity(this.pos, -1, compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		final NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		return compound;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState,
			IBlockState newState) {
		return !oldState.getBlock().isAssociatedBlock(newState.getBlock());
	}

	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(this.name);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			final IFluidHandler handlerFluid = this.getFluidHandler(facing);

			if (handlerFluid != null)
				return (T) handlerFluid;
		} else if (capability == CapabilityEnergy.ENERGY) {
			final IEnergyStorage handlerEnergy = this.getEnergyHandler(facing);

			if (handlerEnergy != null)
				return (T) handlerEnergy;
		}

		return super.getCapability(capability, facing);
	}

	/* CAPABILITY */
	@Nullable
	public IFluidHandler getFluidHandler(EnumFacing facing) {
		return null;
	}

	@Nullable
	public IEnergyStorage getEnergyHandler(EnumFacing facing) {
		return null;
	}

	/* IBlobsWrapper */
	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		packet.writeBlockPos(this.pos);
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		this.pos = packet.readBlockPos();
	}

	/* CONTAINER */
	@SideOnly(Side.CLIENT)
	public abstract void getGuiNetworkData(int data, int value);

	public abstract void sendGuiNetworkData(GenericContainer container, IContainerListener listener);

}
