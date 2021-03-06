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

package abo.pipes.items;

import net.minecraftforge.common.ForgeDirection;
import abo.ABO;
import abo.PipeIconProvider;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.pipes.PipeItemsWood;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This pipe will always prefer to insert it's objects into another pipe over
 * one that is not a pipe.
 * 
 * @author Scott Chamberlain (Leftler) ported to BC > 2.2 by Flow86
 */
public class PipeItemsExtraction extends PipeItemsWood implements IPowerReceptor {
	private final int baseTexture = PipeIconProvider.PipeItemsExtraction;
	private final int sideTexture = PipeIconProvider.PipeItemsExtractionSide;

	public PipeItemsExtraction(int itemID) {
		super(itemID, new PipeTransportItemsExtraction());

		((PipeTransportItems) transport).allowBouncing = true;

		// THIS IS DAMN UGLY - but I have no other chance to change logic
		// afterwards :/
		try {
			ReflectionHelper.setPrivateValue(Pipe.class, this, new PipeLogicExtraction(), "logic");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider() {
		return ABO.instance.pipeIconProvider;
	}

	@Override
	public int getIconIndex(ForgeDirection direction) {
		if (direction == ForgeDirection.UNKNOWN)
			return baseTexture;
		else {
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

			if (metadata == direction.ordinal())
				return sideTexture;

			return baseTexture;
		}
	}
}
