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

import java.io.IOException;

import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.helpers.IBlobsWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 *
 * @author Le Thinh
 */
public abstract class GenericPacket implements IMessage, IBlobsWrapper {

	// Client-side only. Just in case that the packet is client but the world is
	// server (isRemote).
	protected final Minecraft mc = Minecraft.getMinecraft();
	protected final EligiblePacketBuffer packetBuffer = new EligiblePacketBuffer();

	public GenericPacket() {

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			this.loadBlobsTickets(this.packetBuffer);
		} catch (final IOException e) {
			MatterExpansion.LOGGER.fatal("Changes of the packet received from the network wasn't valid!", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			this.saveBlobsTickets(this.packetBuffer);
		} catch (final IOException e) {
			MatterExpansion.LOGGER.fatal("Changes of the packet sent to the network wasn't valid!", e);
		}
	}

	public IMessage handleClient(NetHandlerPlayClient client) {
		Minecraft.getMinecraft().addScheduledTask(() -> this.handleClientDelegate(client));
		return null;
	}

	public IMessage handleServer(NetHandlerPlayServer server) {
		Minecraft.getMinecraft().addScheduledTask(() -> this.handleServerDelegate(server));
		return null;
	}

	protected abstract void handleClientDelegate(NetHandlerPlayClient client);

	protected abstract void handleServerDelegate(NetHandlerPlayServer server);

}
