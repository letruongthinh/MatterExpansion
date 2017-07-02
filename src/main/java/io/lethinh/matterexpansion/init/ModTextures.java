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

import io.lethinh.matterexpansion.backend.utils.StringUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author Le Thinh
 *
 *         We don't implement TextureUtils.IIconRegister on our item/block, but
 *         instead of that, we create a separate event handler that can register
 *         CCL Icons. Easy to manage.
 */
public class ModTextures {

	/* ITEM */
	public static TextureAtlasSprite ANIMAL_CONVERTER;
	public static TextureAtlasSprite FIRE_WAND;
	public static TextureAtlasSprite DARKFIRE_PICKAXE;
	public static TextureAtlasSprite DARKFIRE_SWORD;

	@SubscribeEvent
	public void registerCCLIcons(TextureStitchEvent.Pre event) {
		final TextureMap textureMap = event.getMap();

		// ITEM
		ANIMAL_CONVERTER = textureMap
				.registerSprite(StringUtils.prefixResourceLocation("items/itemAnimalConverter.png"));
		FIRE_WAND = textureMap.registerSprite(StringUtils.prefixResourceLocation("items/itemFireWand.png"));
		DARKFIRE_PICKAXE = textureMap
				.registerSprite(StringUtils.prefixResourceLocation("items/itemDarkfirePickaxe.png"));
		DARKFIRE_SWORD = textureMap.registerSprite(StringUtils.prefixResourceLocation("items/darkfire_sword.png"));
	}

}
