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

import io.lethinh.matterexpansion.backend.utils.StringUtils;
import io.lethinh.matterexpansion.gui.widgets.WidgetEnergy;
import io.lethinh.matterexpansion.tile.TileFreezer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
@SideOnly(Side.CLIENT)
public class GuiFreezer extends GenericGui {

	public GuiFreezer(TileFreezer tile, InventoryPlayer inventoryPlayer) {
		super(new ContainerFreezer(tile, inventoryPlayer));

		// Widget Energy
		this.addWidget(new WidgetEnergy(150, 10, tile.energy));
	}

	/* TEXTURE */
	@SideOnly(Side.CLIENT)
	@Override
	protected ResourceLocation getGuiTexture() {
		return StringUtils.prefixResourceLocation("textures/gui/freezer.png");
	}

}
