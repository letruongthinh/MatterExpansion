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

import io.lethinh.matterexpansion.MatterExpansion;

/**
 *
 * @author Le Thinh
 */
public abstract class CommonProxy {

	public void preInit(File configFile) {
		MatterExpansion.LOGGER.info("Pre-Initializing mod... Hello word!");
	}

	public void init() {
		MatterExpansion.LOGGER.info("Initializing mod");
	}

	public void postInit() {
		MatterExpansion.LOGGER.info("Post-Initializing mod");
	}

}
