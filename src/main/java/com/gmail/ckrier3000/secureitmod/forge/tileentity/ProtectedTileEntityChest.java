package com.gmail.ckrier3000.secureitmod.forge.tileentity;

import java.util.UUID;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.Constants.NBT;

public class ProtectedTileEntityChest extends TileEntityChest {
	
	static final String COMPOUND_TAG_ID_CHEST_LOCK = "SILock";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockId";
	
	
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
		xCoord = chest.xCoord;
		yCoord = chest.yCoord;
		zCoord = chest.zCoord;
		for (int i = 0; i < chest.getSizeInventory(); i++)
			setInventorySlotContents(i, chest.getStackInSlot(i));
	}

	@Override
	public void readFromNBT(NBTTagCompound arg0) {
		if (arg0.hasKey(COMPOUND_TAG_ID_CHEST_LOCK, NBT.TAG_COMPOUND)) {
			lockID = arg0.getCompoundTag(COMPOUND_TAG_ID_CHEST_LOCK).getString(COMPOUND_TAG_ID_CHEST_LOCK_ID);
			owner = UUID.fromString(arg0.getCompoundTag(COMPOUND_TAG_ID_CHEST_LOCK).getString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER));
		}
		super.readFromNBT(arg0);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound arg0) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(COMPOUND_TAG_ID_CHEST_LOCK_ID, lockID);
		tag.setString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER, owner.toString());
		
		arg0.setTag(COMPOUND_TAG_ID_CHEST_LOCK, tag);
		super.writeToNBT(arg0);
	}
}
