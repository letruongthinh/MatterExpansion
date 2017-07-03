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

import java.util.Collection;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;

/**
 *
 * @author Le Thinh
 */
public class AttributeUtils {

	public static void replaceAttribute(Multimap<String, AttributeModifier> modifierMultimap,
			IAttribute attribute, UUID uuid, double newModifierValue) {
		final Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());

		for (final AttributeModifier modifier : modifiers) {
			if (modifier.getID().equals(uuid)) {
				modifiers.remove(modifier);
				modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), newModifierValue,
						modifier.getOperation()));
			}
		}
	}

}
