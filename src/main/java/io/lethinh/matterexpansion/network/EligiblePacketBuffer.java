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
import java.util.stream.IntStream;

import org.apache.commons.lang3.Validate;

import com.google.common.base.Charsets;

import io.lethinh.matterexpansion.MatterExpansion;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 *
 * @author Le Thinh
 */
public class EligiblePacketBuffer extends PacketBuffer {

	private static final ByteBuf buf = Unpooled.buffer();
	protected byte bytearr[] = new byte[80];
	protected char chararr[] = new char[80];

	public EligiblePacketBuffer() {
		super(buf);
	}

	public EnumFacing readSide() {
		return EnumFacing.values()[this.readUnsignedByte()];
	}

	public void writeSide(final EnumFacing e) {
		this.writeByte(e.ordinal());
	}

	public EligiblePacketBuffer readPacket() {
		final int len = this.readInt();
		this.readBytes(this, len);
		return this;
	}

	public void writePacket(EligiblePacketBuffer packet) {
		this.writeVarInt(packet.readableBytes());
		this.writeBytes(packet);
	}

	public NonNullList<ItemStack> readItemStacks() throws IOException {
		final int stacksSize = this.readInt();

		if (stacksSize <= -1)
			return null;

		final NonNullList<ItemStack> stacks = NonNullList.withSize(stacksSize, ItemStack.EMPTY);

		IntStream.range(0, stacksSize).forEach(i -> {
			try {
				stacks.add(this.readItemStack());
			} catch (final IOException e) {
				MatterExpansion.LOGGER
						.error("Failed to read a List of ItemStacks! ItemStacks List: " + stacks.toString());
			}
		});

		return stacks;
	}

	public void writeItemStacks(NonNullList<ItemStack> stacks) {
		this.writeInt(stacks.size());
		stacks.stream().filter(stack -> !stack.isEmpty()).forEach(stack -> this.writeItemStack(stack));
	}

	public FluidStack readFluidStack() {
		final int amount = this.readVarInt();

		if (amount > 0) {
			final String fluidName = this.readUTF8String();
			final Fluid fluid = FluidRegistry.getFluid(fluidName);

			if (fluid == null)
				return null;

			return new FluidStack(fluid, amount);
		}

		return null;
	}

	public void writeFluidStack(FluidStack fluid) {
		if (fluid == null) {
			this.writeInt(0);
		} else {
			this.writeVarInt(fluid.amount);
			this.writeUTF8String(fluid.getFluid().getName());
		}
	}

	/**
	 * [VanillaCopy] Copied from {@link ByteBufUtils}.
	 */
	public int varIntByteCount(int toCount) {
		return (toCount & 0xF0000000) == 0 ? 4
				: (toCount & 0xFFE00000) == 0 ? 3
						: (toCount & 0xFFFFC000) == 0 ? 2 : (toCount & 0xFFFFFF80) == 0 ? 1 : 5;
	}

	public String readUTF8String() {
		final int len = this.readVarInt();
		final String str = this.toString(this.readerIndex(), len, Charsets.UTF_8);
		this.readerIndex(this.readerIndex() + len);
		return str;
	}

	public void writeUTF8String(String str) {
		final byte[] utf8Bytes = str.getBytes(Charsets.UTF_8);
		Validate.isTrue(this.varIntByteCount(utf8Bytes.length) < 3, "The string is too long for this encoding.",
				new Object[0]);
		this.writeVarInt(utf8Bytes.length);
		this.writeBytes(utf8Bytes);
	}

}
