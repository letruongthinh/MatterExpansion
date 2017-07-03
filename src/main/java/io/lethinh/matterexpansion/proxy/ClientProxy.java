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

package io.lethinh.matterexpansion.proxy;

import java.io.File;

import io.lethinh.matterexpansion.Config;
import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.helpers.TooltipHandler;
import io.lethinh.matterexpansion.init.GuiHandler;
import io.lethinh.matterexpansion.init.ModBlocks;
import io.lethinh.matterexpansion.init.ModCrafting;
import io.lethinh.matterexpansion.init.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 *
 * @author Le Thinh
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(File configFile) {
		super.preInit(configFile);
		Config.preInit(configFile);
		ModItems.preInit();
		ModBlocks.preInit();
	}

	@Override
	public void init() {
		super.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(MatterExpansion.instance, new GuiHandler());
	}

	@Override
	public void postInit() {
		super.postInit();
		ModCrafting.postInit();

		// EVENT HANDLER
		MinecraftForge.EVENT_BUS.register(new TooltipHandler());
	}

}
