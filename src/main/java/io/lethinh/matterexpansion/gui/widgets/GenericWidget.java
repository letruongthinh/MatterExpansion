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

import java.awt.Rectangle;
import java.util.ArrayList;

import io.lethinh.matterexpansion.gui.GenericGui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
@SideOnly(Side.CLIENT)
public class GenericWidget {

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public GenericWidget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets the X position of the widget.
	 *
	 * @return the X position of the widget.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Gets the Y position of the widget.
	 *
	 * @return the Y position of the widget.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Gets the width of the widget.
	 *
	 * @return the width of the widget.
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Gets the height of the widget.
	 *
	 * @return the height of the widget.
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Renders the background of the widget through
	 * {@code GenericGui#drawGuiContainerBackgroundLayer}.
	 *
	 * @param gui
	 *            The gui contains all the widgets {@link GenericGui}.
	 * @param mouseX
	 *            The X position of the mouse.
	 * @param mouseY
	 *            The Y position of the mouse.
	 */
	@SideOnly(Side.CLIENT)
	public void renderBackground(GenericGui gui, int mouseX, int mouseY) {
	}

	/**
	 * Renders the foreground of the widget through
	 * {@link GenericGui#drawGuiContainerForegroundLayer}.
	 *
	 * @param gui
	 *            The gui contains all the widgets {@link GenericGui}.
	 * @param mouseX
	 *            The X position of the mouse.
	 * @param mouseY
	 *            The Y position of the mouse.
	 */

	@SideOnly(Side.CLIENT)
	public void renderForeground(GenericGui gui, int mouseX, int mouseY) {
	}

	/**
	 * Shows the widget's information when it is pointing into the widget area
	 * using {@code GenericWidget#isMouseInWidget} and adds the tooltip.
	 *
	 * @param tooltip
	 *            The tooltip of the widget to be added.
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ArrayList<String> tooltip) {
	}

	/**
	 * Checks if the mouse is pointing into the widget area using
	 * {@link java.awt.Rectangle} and at the specified location.
	 *
	 * @param mouseX
	 *            The X position of the mouse.
	 * @param mouseY
	 *            The Y position of the mouse.
	 *
	 * @return true if if the mouse is pointing into the widget area
	 *         {@link java.awt.Rectangle} specified by (X, Y) and is entirely
	 *         enclosed inside this widget area {@link java.awt.Rectangle}
	 *         otherwise false.
	 */
	public boolean isMouseInWidget(int mouseX, int mouseY) {
		return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight()).contains(mouseX, mouseY);
	}

}
