package com.gmail.ckrier3000.secureitmod.forge.tileentity;

import java.util.UUID;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.util.Constants.NBT;

public class ProtectedTileEntityChest extends TileEntityChest {
	
	static final String COMPOUND_TAG_ID_CHEST_LOCK = "SILock";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockID";
	
	private String lockID;
	private UUID owner;
	
	public ProtectedTileEntityChest(TileEntityChest chest) {
		super();
		this.worldObj = chest.getWorldObj();
		blockType = chest.blockType;
		blockMetadata = chest.getBlockMetadata();
		for (int i = 0; i < chest.getSizeInventory(); i++)
			setInventorySlotContents(i, chest.getStackInSlot(i));
		
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		
		Item i;
		if (player.getCurrentEquippedItem() == null)
			i = null;
		else
			i = player.getCurrentEquippedItem().getItem();
		
		
		if (i == null || !i.equals(SecureItMod.keyItem)) {
			return false;
		}
		
		
		return true;
	}
	
	public void setLock(String lock) {
		this.lockID = lock;
	}
	
	public void setOwner(UUID id) {
		this.owner = id;
	}
	
	public void setAll(String lock, UUID id) {
		setLock(lock);
		setOwner(id);
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

	public void setAll(UUID uniqueID) {
		setAll(SecureItMod.instance.getNewLockID(worldObj), uniqueID);
	}
}
