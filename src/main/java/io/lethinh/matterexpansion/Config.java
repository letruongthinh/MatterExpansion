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

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Le Thinh
 */
public class Config {

	public static boolean enableAnimalConverter = true;
	public static boolean enableSpectrumPickaxe = true;
	public static boolean enableFireWand = true;

	public static int freezerEnergyUse = 2000;
	public static int fireWandEnergyUseBase = 1000;
	public static int fireWandExplosionPower = 10;
	public static int animalConverterEnergyUse = 2000;

	public static void preInit(File file) {
		final Configuration config = new Configuration(file);

		try {
			config.load();

			// General
			enableAnimalConverter = config.getBoolean("Enable Animal Converter", "General", true,
					"Enable or disable the animal converter.");
			enableSpectrumPickaxe = config.getBoolean("Enable Spectrum Pickaxe", "General", true,
					"Enable or disable the spectrum pickaxe.");
			enableFireWand = config.getBoolean("Enable Fire Wand", "General", true, "Enable or disable the fire wand.");

			freezerEnergyUse = config.getInt("Freezer Energy Required", "General", 2000, 0, Integer.MAX_VALUE,
					"Modify the blockFreezer process energy required per work. Can't be negative.");
			fireWandEnergyUseBase = config.getInt("Fire Wand Energy Required", "General", 10, 0, Integer.MAX_VALUE,
					"Modify the fire wand energy required base (will be multiplied by the explosion power) Default energy required: 10000. Can't be negative.");
			fireWandExplosionPower = config.getInt("Fire Wand Explosion Power", "General", 10, 0, Integer.MAX_VALUE,
					"Modify the explosion power generated from the fire wand. Can't be negative.");
			animalConverterEnergyUse = config.getInt("Animal Converter Energy Required", "General", 2000, 0,
					Integer.MAX_VALUE,
					"Modify the energy required for the animal converting process. Can't be negative.");

		} catch (final Exception e) {
			MatterExpansion.LOGGER.fatal("Failed to load the config file!");
		} finally {
			config.save();
		}
	}

}
