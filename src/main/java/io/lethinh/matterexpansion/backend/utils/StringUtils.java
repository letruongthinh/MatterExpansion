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

package io.lethinh.matterexpansion.backend.utils;

import java.text.NumberFormat;

import javax.annotation.Nonnegative;

import io.lethinh.matterexpansion.MatterExpansion;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author Le Thinh
 */
public class StringUtils {

	public static ResourceLocation prefixResourceLocation(String location) {
		return new ResourceLocation(MatterExpansion.ModID, location);
	}

	public static String prefixRFEnergy(@Nonnegative double energy, @Nonnegative double capacity) {
		return prefixNumber(energy, capacity) + " RF";
	}

	public static String prefixNumber(@Nonnegative double par1, @Nonnegative double par2) {
		try {
			final NumberFormat format = NumberFormat.getInstance();
			return format.format(par1) + "/" + format.format(par2);
		} catch (final NumberFormatException e) {
			MatterExpansion.LOGGER.error("NumberFormat has encountered an error!");
		}

		return "";
	}

	public static String prefixTooltip(String itemName, Object... tooltip) {
		return "tooltip." + MatterExpansion.ModID + "." + itemName + "." + tooltip;
	}

}
