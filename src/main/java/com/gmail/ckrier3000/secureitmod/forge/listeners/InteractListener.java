package com.gmail.ckrier3000.secureitmod.forge.listeners;

import java.util.UUID;

import org.apache.logging.log4j.Logger;

import com.gmail.ckrier3000.secureitmod.forge.InteractData;
import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.forge.items.ForceUnlockToolItem;
import com.gmail.ckrier3000.secureitmod.forge.items.InteractProxy;
import com.gmail.ckrier3000.secureitmod.forge.items.KeyItem;
import com.gmail.ckrier3000.secureitmod.forge.items.LockAndKeyItem;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import cpw.mods.fml.common.eventhandler.Event.Result;
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
		final boolean isServer = !world.isRemote;
		
		//Our needed specifics.
		InteractData data = new InteractData(event);
		if (!(block instanceof BlockChest))
			return;
			
		if (isLocked(world, x, y, z)) {
			if ((player.getCurrentEquippedItem() == null || !isAnyMatch(player.getCurrentEquippedItem().getItem().getClass()))) { 
				event.setCanceled(true);
				event.useBlock = Result.DENY;
				
				MessageUtil.sendMessage(player, "Chest is locked!");
			} else if (player.getCurrentEquippedItem() == null) {
				event.setCanceled(true);
				event.useBlock = Result.DENY;
				
				MessageUtil.sendMessage(player, "Chest is locked!");
			} else {
				if (player.getCurrentEquippedItem().getItem() instanceof InteractProxy) {
					((InteractProxy)player.getCurrentEquippedItem().getItem()).interactProxy(data);
					
					event.setCanceled(data.cancelEvent);
					event.useBlock = data.useBlock;
					event.useItem = data.useItem;
					
				} else event.setCanceled(isLocked(world, x, y, z));
			}
		} else if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof InteractProxy) {
			((InteractProxy)player.getCurrentEquippedItem().getItem()).interactProxy(data);
			
			event.setCanceled(data.cancelEvent);
			event.useBlock = data.useBlock;
			event.useItem = data.useItem;
		}
	}
	
	private boolean hasInterface(Class c, Class i) {
		for (Class ci : c.getInterfaces())
			if (ci.equals(i))
				return true;
		return false;
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
