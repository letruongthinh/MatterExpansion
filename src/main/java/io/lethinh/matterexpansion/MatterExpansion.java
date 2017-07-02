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

package io.lethinh.matterexpansion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.lethinh.matterexpansion.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Le Thinh
 */
@Mod(modid = MatterExpansion.ModID, name = MatterExpansion.NAME, version = MatterExpansion.VERSION)
public class MatterExpansion {

	// For some reasons.
	static {
		FluidRegistry.enableUniversalBucket();
	}

	public static final String ModID = "matterexpansion";
	public static final String NAME = "Matter Expansion";
	public static final String VERSION = "0.1 - Alpha";

	public static final Logger LOGGER = LogManager.getLogger(MatterExpansion.NAME);
	public static final CreativeTabs tab = new CreativeTabs(MatterExpansion.ModID) {
		@SideOnly(Side.CLIENT)
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.END_CRYSTAL);
		}
	};

	@SidedProxy(serverSide = "io.lethinh.matterexpansion.proxy.CommonProxy", clientSide = "io.lethinh.matterexpansion.proxy.ClientProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static MatterExpansion instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MatterExpansion.instance = this;
		proxy.preInit(event.getSuggestedConfigurationFile());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}

}
