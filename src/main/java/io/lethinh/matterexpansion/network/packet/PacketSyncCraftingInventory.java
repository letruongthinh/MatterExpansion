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

package io.lethinh.matterexpansion.network.packet;

import java.io.IOException;

import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import io.lethinh.matterexpansion.network.GenericPacketClientToServer;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;

/**
 *
 * @author Le Thinh
 */
public class PacketSyncCraftingInventory extends GenericPacketClientToServer {

	public PacketSyncCraftingInventory() {
	}

	@Override
	protected void handleServerDelegate(NetHandlerPlayServer server) {
		final Container container = server.playerEntity.openContainer;

		if (container != null) {
			container.onCraftMatrixChanged(null);
		}
	}

	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
	}

}
