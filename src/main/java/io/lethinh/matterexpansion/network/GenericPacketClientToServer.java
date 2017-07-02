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

package io.lethinh.matterexpansion.network;

import net.minecraft.client.network.NetHandlerPlayClient;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericPacketClientToServer extends GenericPacket {

	public GenericPacketClientToServer() {

	}

	@Override
	protected void handleClientDelegate(NetHandlerPlayClient client) {
		throw new UnsupportedOperationException("Wrong side! Server-side only!");
	}

}
