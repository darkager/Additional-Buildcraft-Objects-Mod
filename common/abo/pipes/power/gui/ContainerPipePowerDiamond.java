/** 
 * Copyright (C) 2011-2013 Flow86
 * 
 * AdditionalBuildcraftObjects is open-source.
 *
 * It is distributed under the terms of my Open Source License. 
 * It grants rights to read, modify, compile or run the code. 
 * It does *NOT* grant the right to redistribute this software or its 
 * modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package abo.pipes.power.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import abo.pipes.power.PipeLogicPowerDistribution;
import abo.pipes.power.PipePowerDistribution;
import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.transport.TileGenericPipe;

public class ContainerPipePowerDiamond extends BuildCraftContainer {

	public final PipePowerDistribution pipe;
	private final boolean[] connectionMatrix = new boolean[6];

	public ContainerPipePowerDiamond(InventoryPlayer inventory, TileGenericPipe tile) {
		super(0);

		pipe = (PipePowerDistribution) tile.pipe;
		PipeLogicPowerDistribution logic = (PipeLogicPowerDistribution) pipe.logic;

		for (int i = 0; i < 6; ++i)
			connectionMatrix[i] = logic.connectionMatrix[i];
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		PipeLogicPowerDistribution logic = (PipeLogicPowerDistribution) pipe.logic;

		// send update to all connected crafters
		for (Object crafter : crafters) {
			for (int i = 0; i < 6; ++i) {
				if (connectionMatrix[i] != logic.connectionMatrix[i]) {
					// System.out.println("detectAndSendChanges: " + i + " to " + logic.connectionMatrix[i]);
					((ICrafting) crafter).sendProgressBarUpdate(this, i, (logic.connectionMatrix[i] ? 1 : 0));
					connectionMatrix[i] = logic.connectionMatrix[i];
				}
			}
		}
	}

	@Override
	public void updateProgressBar(int id, int value) {
		PipeLogicPowerDistribution logic = (PipeLogicPowerDistribution) pipe.logic;

		// System.out.println("updateProgress: " + id + " to " + (value == 1));
		logic.update(id, (value == 1));
		connectionMatrix[id] = logic.connectionMatrix[id];
	}

}
