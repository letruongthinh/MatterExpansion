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

package io.lethinh.matterexpansion.tile;

import io.lethinh.matterexpansion.backend.utils.InventoryUtils;
import io.lethinh.matterexpansion.crafting.NeonItemMakerRecipe;
import io.lethinh.matterexpansion.init.ModCrafting;
import net.minecraft.item.ItemStack;

/**
 *
 * @author Le Thinh
 */
public class TileNeonItemMaker extends GenericMachineTile {

	public int maxTime = 100;
	public final int SLOT_INPUT_1 = 0;
	public final int SLOT_INPUT_2 = 1;
	public final int SLOT_OUTPUT = 2;

	public TileNeonItemMaker() {
		super(3);
	}

	@Override
	protected boolean canWork() {
		final NeonItemMakerRecipe recipe = this.getCurrentRecipe();
		return this.getEnergyStored() >= 2500 && this.progress >= this.maxTime
				&& !this.getStackInSlot(this.SLOT_INPUT_1).isEmpty()
				&& this.getStackInSlot(this.SLOT_INPUT_1).isItemEqual(recipe.input_1)
				&& !this.getStackInSlot(this.SLOT_INPUT_2).isEmpty()
				&& this.getStackInSlot(this.SLOT_INPUT_2).isItemEqual(recipe.input_2)
				&& (this.getStackInSlot(this.SLOT_OUTPUT).isEmpty()
						|| recipe.output.isItemEqual(this.getStackInSlot(this.SLOT_OUTPUT)));
	}

	@Override
	protected void doClientWork() {
		final NeonItemMakerRecipe recipe = this.getCurrentRecipe();
		final ItemStack output = InventoryUtils.copyItemStack(recipe.output);

		if (this.getStackInSlot(this.SLOT_OUTPUT).isEmpty()) {
			this.setInventorySlotContents(this.SLOT_OUTPUT, output);
		} else {
			output.grow(1);
		}

		this.extractEnergy(2000);
		this.progress = 0;
	}

	@Override
	protected void doServerWork() {
		super.doServerWork();
		this.progress++;
	}

	@Override
	protected void stopWorking() {
		this.progress = 0;
	}

	@Override
	public int getEnergyCapacity() {
		return 50000;
	}

	/* RECIPE */
	private NeonItemMakerRecipe getCurrentRecipe() {
		for (final NeonItemMakerRecipe recipe : ModCrafting.NEON_ITEM_MAKER_RECIPES) {
			if (!this.getStackInSlot(this.SLOT_INPUT_1).isEmpty()
					&& this.getStackInSlot(this.SLOT_INPUT_1).isItemEqual(recipe.input_1)
					&& !this.getStackInSlot(this.SLOT_INPUT_2).isEmpty()
					&& this.getStackInSlot(this.SLOT_INPUT_2).isItemEqual(recipe.input_2))
				return recipe;
		}

		return null;
	}

}
