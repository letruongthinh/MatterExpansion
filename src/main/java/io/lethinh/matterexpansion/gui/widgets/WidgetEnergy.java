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

package io.lethinh.matterexpansion.gui.widgets;

import java.io.IOException;
import java.util.ArrayList;

import cofh.api.energy.EnergyStorage;
import io.lethinh.matterexpansion.backend.utils.StringUtils;
import io.lethinh.matterexpansion.gui.GenericGui;
import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
@SideOnly(Side.CLIENT)
public class WidgetEnergy extends GenericWidget {

	private static final ResourceLocation texEnergyEmpty = StringUtils
			.prefixResourceLocation("textures/gui/widgets/widget_energy_empty.png");
	private static final ResourceLocation texEnergyFull = StringUtils
			.prefixResourceLocation("textures/gui/widgets/widget_energy_ful.png");

	private final EnergyStorage energy;

	public WidgetEnergy(int x, int y, EnergyStorage energy) {
		super(x, y, 18, 54);
		this.energy = energy;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderBackground(GenericGui gui, int mouseX, int mouseY) {
		gui.mc.getTextureManager().bindTexture(texEnergyEmpty);
		gui.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight());

		gui.mc.getTextureManager().bindTexture(texEnergyFull);
		final int pixels = 24;
		int scale = 0;

		if (this.energy.getEnergyStored() > 0 && this.energy.getMaxEnergyStored() > 0) {
			scale = pixels * this.energy.getEnergyStored() / this.energy.getMaxEnergyStored();
		}

		gui.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), scale);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ArrayList<String> tooltip) {
		if (tooltip != null) {
			tooltip.add(StringUtils.prefixRFEnergy(this.energy.getEnergyStored(), this.energy.getMaxEnergyStored()));
		}
	}

	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		super.loadBlobsTickets(packet);
		this.energy.setEnergyStored(packet.readInt());
		this.energy.setCapacity(packet.readInt());
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		super.saveBlobsTickets(packet);
		packet.writeInt(this.energy.getEnergyStored());
		packet.writeInt(this.energy.getMaxEnergyStored());
	}

}