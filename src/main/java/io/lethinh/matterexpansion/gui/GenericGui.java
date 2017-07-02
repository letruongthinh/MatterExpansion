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

package io.lethinh.matterexpansion.gui;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.Validate;

import io.lethinh.matterexpansion.gui.widgets.GenericWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
@SideOnly(Side.CLIENT)
public abstract class GenericGui extends GuiContainer {

	protected final HashSet<GenericWidget> widgets = new HashSet<>();

	public GenericGui(GenericContainer<?> container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.getGuiTexture());

		final int width = (this.width - this.xSize) / 2;
		final int height = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(width, height, 0, 0, this.xSize, this.ySize);

		// WIDGET
		this.widgets.forEach(widget -> widget.renderBackground(this, mouseX, mouseY));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// WIDGET
		final ArrayList<String> tooltip = new ArrayList<>();

		this.widgets.forEach(widget -> {
			widget.renderForeground(this, mouseX, mouseY);

			if (widget.isMouseInWidget(mouseX, mouseY)) {
				widget.addInformation(tooltip);
			}
		});

		Validate.notNull(tooltip);
		this.drawHoveringText(tooltip, mouseX, mouseY, this.fontRendererObj);
	}

	@Override
	public void setWorldAndResolution(Minecraft minecraft, int mouseX, int mouseY) {
		super.setWorldAndResolution(minecraft, mouseX, mouseY);
		this.clearWidgets();
	}

	/* WIDGET */
	protected void addWidget(GenericWidget widget) {
		Validate.notNull(widget);
		this.widgets.add(widget);
	}

	protected void removeWidget(GenericWidget widget) {
		Validate.notNull(widget);
		this.widgets.remove(widget);
	}

	protected void clearWidgets() {
		this.widgets.clear();
	}

	protected boolean hasWidget(GenericWidget widget) {
		Validate.notNull(widget);
		return this.widgets.contains(widget);
	}

	/* TEXTURE */
	@SideOnly(Side.CLIENT)
	protected abstract ResourceLocation getGuiTexture();

}
