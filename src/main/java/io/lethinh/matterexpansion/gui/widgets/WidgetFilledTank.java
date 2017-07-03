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

import java.util.ArrayList;

import io.lethinh.matterexpansion.backend.utils.StringUtils;
import io.lethinh.matterexpansion.gui.GenericGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
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

	private final IFluidTank tank;

	public WidgetFilledTank(int x, int y, IFluidTank tank) {
		super(x, y, 18, 54);
		this.tank = tank;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderBackground(GenericGui gui, int mouseX, int mouseY) {
		gui.mc.getTextureManager().bindTexture(texFluidEmpty);
		gui.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight());

		gui.mc.getTextureManager().bindTexture(texFluidFull);
		final int pixels = 24;
		int scale = 0;

		if (this.tank.getFluidAmount() > 0 && this.tank.getCapacity() > 0) {
			scale = pixels * this.tank.getFluidAmount() / this.tank.getCapacity();
		}

		gui.drawTexturedModalRect(this.getX(), this.getY(), 0, 0, this.getWidth(), scale);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ArrayList<String> tooltip) {
		if (tooltip != null) {
			tooltip.add(StringUtils.prefixNumber(this.tank.getFluidAmount(), this.tank.getCapacity()));
		}
	}

}
