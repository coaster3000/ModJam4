package com.gmail.ckrier3000.secureitmod.forge.listeners;

import java.util.UUID;

import org.apache.logging.log4j.Logger;

import com.gmail.ckrier3000.secureitmod.forge.InteractData;
import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.forge.items.ForceUnlockToolItem;
import com.gmail.ckrier3000.secureitmod.forge.items.KeyItem;
import com.gmail.ckrier3000.secureitmod.forge.items.LockAndKeyItem;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class InteractListener extends BaseListener {
	
	private final SecureItMod instance; //Easy Access
	private Logger log;
	
	public InteractListener() {
		instance = SecureItMod.instance;
		log = instance.getLogger();
	}
	

	// OLD CODE
//	@SubscribeEvent
//	public void onChestAccess(PlayerInteractEvent event) {
//	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		//Setup some accessors.
		final int x = event.x, y = event.y, z = event.z;
//		final Action action = event.action;
		World world = event.entityPlayer.worldObj;
		Block block = world.getBlock(x, y, z);
		
		EntityPlayer player = event.entityPlayer;
		//Our needed specifics.
		final boolean isServer = !world.isRemote;
		final boolean isChest = block instanceof BlockChest;
		final boolean isServerPlayer = player instanceof EntityPlayerMP;
		
		InteractData data = new InteractData(event);
		
		if (!isServer || !isChest || !isServerPlayer) //skips
			return;
		
		if (isLocked(world, x, y, z)) {
			Class<?> c = player.getCurrentEquippedItem().getItem().getClass();
			if (player.getCurrentEquippedItem() == null || !isAnyMatch(c)) {
				event.setCanceled(true);
				
				MessageUtil.sendMessage(player, "Chest is locked!");
			} 
			if (c.equals(SecureItMod.keyItem))
				((KeyItem)SecureItMod.keyItem).interactProxy(data);
		}
	
	}
	
	private boolean isAnyMatch(Class<?> c) {
		return c.equals(KeyItem.class) || c.equals(ForceUnlockToolItem.class) || c.equals(LockAndKeyItem.class);
	}

	private Integer getLastID(int did) {
		return instance.getLastID(did);
	}


	private Integer getLastID(World world) {
		return instance.getLastID(world);
	}


	private Integer getLockID(World world, int x, int y, int z) {
		return instance.getLockID(world, x, y, z);
	}


	private boolean isKey(World world, int x, int y, int z, Integer key) {
		return instance.isKey(world, x, y, z, key);
	}


	private boolean isLocked(World world, int x, int y, int z) {
		return instance.isLocked(world, x, y, z);
	}


	private Integer lock(World world, int x, int y, int z, String owner) {
		return instance.lock(world, x, y, z, owner);
	}


	private Integer lock(World world, int x, int y, int z, UUID owner) {
		return instance.lock(world, x, y, z, owner);
	}


	private void unlock(World world, int x, int y, int z) {
		instance.unlock(world, x, y, z);
	}


	@Override
	public boolean onRegister() {
		return false;
	}

	@Override
	public boolean onUnregister() {
		return false;
	}
}
