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

import org.apache.commons.lang3.Validate;

import io.lethinh.matterexpansion.MatterExpansion;
import io.lethinh.matterexpansion.network.GenericPacket;
import io.lethinh.matterexpansion.network.packet.PacketSyncCraftingInventory;
import io.lethinh.matterexpansion.network.packet.PacketTileUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 *
 * @author Le Thinh
 */
public class PacketHandler {

	public static final String channelName = MatterExpansion.ModID + "Channel";
	private static SimpleNetworkWrapper networkWrapper;
	private static GenericMessageHandler handler;
	private static int id = 0;

	public static void init() {
		PacketHandler.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		PacketHandler.handler = new GenericMessageHandler();

		PacketHandler.registerServerPacketOnly(PacketTileUpdate.class);
		PacketHandler.registerServerPacketOnly(PacketSyncCraftingInventory.class);
	}

	/* NETWORK */
	public static void sendToAll(GenericPacket packet) {
		Validate.notNull(packet);
		networkWrapper.sendToAll(packet);
	}

	public static void sendToAllAround(GenericPacket packet, NetworkRegistry.TargetPoint point) {
		Validate.notNull(packet);
		Validate.notNull(point);
		networkWrapper.sendToAllAround(packet, point);
	}

	public static void sendToServer(GenericPacket packet) {
		Validate.notNull(packet);
		networkWrapper.sendToServer(packet);
	}

	public static void sendToAllPlayers(WorldServer world, BlockPos pos, GenericPacket packet) {
		Validate.notNull(world);
		Validate.notNull(pos);
		Validate.notNull(packet);

		final Chunk chunk = world.getChunkFromBlockCoords(pos);
		for (final EntityPlayer player : world.playerEntities) {
			// only send to relevant players
			if (!(player instanceof EntityPlayerMP)) {
				continue;
			}

			final EntityPlayerMP playerMP = (EntityPlayerMP) player;
			if (world.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition)) {
				sendToPlayer(packet, playerMP);
			}
		}
	}

	public static void sendToPlayer(GenericPacket packet, EntityPlayerMP playerMP) {
		Validate.notNull(packet);
		Validate.notNull(playerMP);
		networkWrapper.sendTo(packet, playerMP);
	}

	public static void registerPacket(Class<? extends GenericPacket> packetClazz) {
		Validate.notNull(packetClazz);
		registerClientPacketOnly(packetClazz);
		registerServerPacketOnly(packetClazz);
	}

	public static void registerClientPacketOnly(Class<? extends GenericPacket> packetClazz) {
		Validate.notNull(packetClazz);
		registerMessagePacket(packetClazz, Side.CLIENT);
	}

	public static void registerServerPacketOnly(Class<? extends GenericPacket> packetClazz) {
		Validate.notNull(packetClazz);
		registerMessagePacket(packetClazz, Side.SERVER);
	}

	private static void registerMessagePacket(Class<? extends GenericPacket> packetClazz, Side side) {
		Validate.notNull(packetClazz);
		networkWrapper.registerMessage(handler, packetClazz, id++, side);
	}

	public static class GenericMessageHandler implements IMessageHandler<GenericPacket, IMessage> {
		@Override
		public IMessage onMessage(GenericPacket packet, MessageContext ctx) {
			Validate.notNull(packet);
			Validate.notNull(ctx);

			if (ctx.side == Side.CLIENT)
				return packet.handleClient(ctx.getClientHandler());
			else
				return packet.handleServer(ctx.getServerHandler());
		}
	}

}
