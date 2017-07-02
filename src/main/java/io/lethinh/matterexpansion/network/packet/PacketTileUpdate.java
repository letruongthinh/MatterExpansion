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

import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.backend.helpers.IBlobsWrapper;
import io.lethinh.matterexpansion.network.EligiblePacketBuffer;
import io.lethinh.matterexpansion.network.GenericPacketClientToServer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 *
 * @author Le Thinh
 */
public class PacketTileUpdate extends GenericPacketClientToServer {

	private BlockPos pos;
	private NBTTagCompound tag;

	public PacketTileUpdate() {
	}

	public PacketTileUpdate(BlockPos pos, NBTTagCompound tag) {
		this.pos = pos;
		this.tag = tag;
	}

	@Override
	protected void handleServerDelegate(NetHandlerPlayServer server) {
		final TileEntity tile = server.playerEntity.world.getTileEntity(this.pos);
		Validate.notNull(tile);

		if (tile instanceof IBlobsWrapper) {
			try {
				((IBlobsWrapper) tile).loadBlobsTickets(this.packetBuffer);
			} catch (final IOException e) {
				MatterExpansion.LOGGER.error(
						"Blobs of the tile received from the packet wasn't valid ... Position: " + this.pos.toString(),
						e);
			}
		}
	}

	@Override
	public void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		this.pos = packet.readBlockPos();
		this.tag = packet.readCompoundTag();
	}

	@Override
	public void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException {
		packet.writeBlockPos(this.pos);
		packet.writeCompoundTag(this.tag);
	}

}
