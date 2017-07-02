/**
 * This class was created by <Vazkii>. It's distributed as part of the
 * ThaumicTinkerer Mod.
 *
 * ThaumicTinkerer is Open Source and distributed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * ThaumicTinkerer is a Derivative Work on Thaumcraft 4. Thaumcraft 4 (c) Azanor
 * 2012 (http://www.minecraftforum.net/topic/1585216-)
 *
 * File Created @ [Dec 29, 2013, 11:38:29 PM (GMT)]
 */
package io.lethinh.matterexpansion.backend.helpers;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TooltipHandler {

	private static String currentTooltip;
	private static int tooltipDisplayTicks;

	public static void setTooltip(String tooltip) {
		if (!tooltip.equals(currentTooltip)) {
			currentTooltip = tooltip;
			tooltipDisplayTicks = 100;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void drawTooltip(RenderGameOverlayEvent.Post event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && tooltipDisplayTicks > 0
				&& !currentTooltip.isEmpty()) {
			final Minecraft mc = Minecraft.getMinecraft();
			final ScaledResolution scaledResolution = new ScaledResolution(mc);
			final int width = scaledResolution.getScaledWidth();
			final int height = scaledResolution.getScaledHeight();
			final FontRenderer fontRenderer = mc.fontRendererObj;

			final int tooltipX = (width - fontRenderer.getStringWidth(currentTooltip)) / 2;
			final int tooltipY = height - 68;

			int opacity = (int) (this.tooltipDisplayTicks * 256.0F / 10.0F);

			final int elapsedTicks = 500;
			final int color = Color
					.getHSBColor((float) Math.cos(elapsedTicks + mc.world.rand.nextInt() / 250D), 0.6F, 1F)
					.getRGB();

			if (opacity >= 160) {
				opacity = 160;
			}

			if (opacity > 0) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				fontRenderer.drawStringWithShadow(currentTooltip, tooltipX, tooltipY, color + (opacity << 24));

				GlStateManager.disableBlend();
				GlStateManager.popMatrix();

				if (tooltipDisplayTicks > 0) {
					--tooltipDisplayTicks;
				}
			}

			if (tooltipDisplayTicks > 0) {
				--tooltipDisplayTicks;
			}
		}
	}

}
