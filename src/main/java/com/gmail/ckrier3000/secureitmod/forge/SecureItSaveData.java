package com.gmail.ckrier3000.secureitmod.forge;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class SecureItSaveData extends WorldSavedData {
	public static final String NAME = "SecureItData";
	
	private Map<Integer, Integer> usedLockLists;
	private Map<Integer, NBTTagCompound> lockDataLists;
	
	private int id;
	
	public SecureItSaveData(String par1Str) {
		super(par1Str);
		
		try {
			id = Integer.parseInt(par1Str.split(":")[1]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid ID");
		} catch (ArrayIndexOutOfBoundsException a) {
			throw new IllegalArgumentException("Wrong name format need one : in name followed by dimension ID");
		}
		lockDataLists = SecureItMod.instance.lockDataLists;
		usedLockLists = SecureItMod.instance.usedLockLists;
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		if (data.hasKey(SecureItMod.WORLDINFO_USEDLOCKS, NBT.TAG_INT))
			usedLockLists.put(id, data.getInteger(SecureItMod.WORLDINFO_USEDLOCKS));
		if (data.hasKey(SecureItMod.WORLDINFO_LOCKS, NBT.TAG_COMPOUND))
			lockDataLists.put(id, data.getCompoundTag(SecureItMod.WORLDINFO_LOCKS));
	}
	

	@Override
	public void writeToNBT(NBTTagCompound data) {
		if (usedLockLists.containsKey(id))
			data.setInteger(SecureItMod.WORLDINFO_USEDLOCKS, usedLockLists.get(id));
		
		if (lockDataLists.containsKey(id))
			data.setTag(SecureItMod.WORLDINFO_LOCKS, lockDataLists.get(id));
	}

}
