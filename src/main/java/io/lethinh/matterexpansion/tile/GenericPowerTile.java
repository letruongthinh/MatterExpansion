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

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericPowerTile extends GenericTile implements IEnergyReceiver {

	public final EnergyStorage energy = new EnergyStorage(this.getCapacity(), this.getCapacity(), 0);

	public GenericPowerTile(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.energy.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		this.energy.writeToNBT(compound);
		return super.writeToNBT(compound);
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.energy.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return this.energy.receiveEnergy(maxReceive, simulate);
	}

	/* ENERGY */
	public int getEnergyStored() {
		return this.energy.getEnergyStored();
	}

	public abstract int getCapacity();

	public void setEnergyStored(int energy) {
		this.energy.setEnergyStored(energy);
	}

	public int extractEnergy(int energy) {
		return this.energy.extractEnergy(energy, false);
	}

	/* CONTAINER */
	public int getScaledEnergy(int pixels) {
		return (int) Math.floor(this.getEnergyStored() * pixels / this.getCapacity());
	}

}
