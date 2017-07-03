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
public class WidgetFilledTank extends GenericWidget {

	private static final ResourceLocation texFluidEmpty = StringUtils
			.prefixResourceLocation("textures/gui/widgets/widget_energy_empty.png");
	private static final ResourceLocation texFluidFull = StringUtils
			.prefixResourceLocation("textures/gui/widgets/widget_energy_ful.png");

	private int fluidAmount, capacity = 0;

	public WidgetFilledTank(int x, int y, int fluidAmount, int capacity) {
		super(x, y, 18, 54);
		this.fluidAmount = fluidAmount;
		this.capacity = capacity;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderBackground(GenericGui gui, int mouseX, int mouseY) {
		gui.mc.getTextureManager().bindTexture(texFluidEmpty);
		gui.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight());

		gui.mc.getTextureManager().bindTexture(texFluidFull);
		final int pixels = 24;
		int scale = 0;

		if (this.fluidAmount > 0 && this.capacity > 0) {
			scale = pixels * this.fluidAmount / this.capacity;
		}

		gui.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), scale);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ArrayList<String> tooltip) {
		if (tooltip != null) {
			tooltip.add(StringUtils.prefixNumber(this.fluidAmount, this.capacity));
		}
	}

	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		super.loadBlobsTickets(packet);
		this.fluidAmount = packet.readInt();
		this.capacity = packet.readInt();
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		super.saveBlobsTickets(packet);
		packet.writeInt(this.fluidAmount);
		packet.writeInt(this.capacity);
	}

}
