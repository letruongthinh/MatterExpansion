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

package io.lethinh.matterexpansion.backend.helpers;

import io.lethinh.matterexpansion.init.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * By implementing it this interface, it allows getting the container/gui of the
 * tile and register the container/gui through {@link GuiHandler}. Very useful
 * for iteration.
 *
 * @author Le Thinh
 */
public interface IGuiTile {

	Object getServerGuiElement(EntityPlayer player, int ID, int x, int y, int z);

	Object getClientGuiElement(EntityPlayer player, int ID, int x, int y, int z);

}
