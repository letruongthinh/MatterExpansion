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

package io.lethinh.matterexpansion.init;

import javax.annotation.Nullable;

import io.lethinh.matterexpansion.backend.helpers.IGuiTile;
import io.lethinh.matterexpansion.tile.GenericTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 *
 * @author Le Thinh
 */
public class GuiHandler implements IGuiHandler {

	public static final int GUI_ID = 0;

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			final GenericTile tile = (GenericTile) world.getTileEntity(new BlockPos(x, y, z));

			if (tile instanceof IGuiTile)
				return ((IGuiTile) tile).getServerGuiElement(player, GUI_ID, x, y, z);
		default:
			return null;
		}
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case 0:
			final GenericTile tile = (GenericTile) world.getTileEntity(new BlockPos(x, y, z));

			if (tile instanceof IGuiTile)
				return ((IGuiTile) tile).getClientGuiElement(player, GUI_ID, x, y, z);
		default:
			return null;
		}
	}

}
