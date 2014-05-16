package com.gmail.ckrier3000.secureitmod.forge.tileentity;

import java.util.UUID;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;

public class ProtectedTileEntityChest extends TileEntityChest {
	private String lockID;
	private UUID owner;
	
	
	public ProtectedTileEntityChest(TileEntityChest chest) {
		worldObj = chest.getWorldObj();
		blockType = chest.blockType;
		blockMetadata = chest.getBlockMetadata();
		lidAngle = chest.lidAngle;
		prevLidAngle = chest.prevLidAngle;
		adjacentChestChecked = chest.adjacentChestChecked;
		adjacentChestXNeg = chest.adjacentChestXNeg;
		adjacentChestXPos = chest.adjacentChestXPos;
		adjacentChestZNeg = chest.adjacentChestZNeg;
		adjacentChestZPos = chest.adjacentChestZPos;
		tileEntityInvalid = chest.isInvalid();
	}

	@Override
	public void readFromNBT(NBTTagCompound arg0) {
		super.readFromNBT(arg0);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound arg0) {
		super.writeToNBT(arg0);
	}
}
