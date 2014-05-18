package com.gmail.ckrier3000.secureitmod.forge.listeners;

import org.apache.logging.log4j.Logger;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

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
	
	public InteractListener() {
		instance = SecureItMod.instance;
	}
	

	// OLD CODE
//	@SubscribeEvent
//	public void onChestAccess(PlayerInteractEvent event) {
//		final int x = event.x, y = event.y, z = event.z;
//		World world = event.entity.worldObj;
//		EntityPlayer player = event.entityPlayer;
//		Block b = world.getBlock(x, y, z);
//		if (b instanceof BlockChest)
//			if (isLocked(world, x, y, z)) {
//				if (player.getCurrentEquippedItem() == null || !player.getCurrentEquippedItem().getItem().getClass().equals(keyItem.getClass()) && !player.getCurrentEquippedItem().getItem().getClass().equals(forceUnlockItem.getClass()) && !player.getCurrentEquippedItem().getItem().getClass().equals(lockAndKeyItem.getClass())) {
//					event.setCanceled(true);
//					
//					MessageUtil.sendMessage(player, "Chest is locked!");
//				} else
//					return;
//
//			}
//	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		//Setup some accessors.
		final int x = event.x, y = event.y, z = event.z;
		final Action action = event.action;
		World world = event.entityPlayer.worldObj;
		Block block = world.getBlock(x, y, z);
		Logger log = instance.getLogger();
		//Our needed specifics.
		final boolean isServer = !world.isRemote;
		final boolean isChest = block instanceof BlockChest;
		
		if (!isServer) //Kill none server.
			return;
		
		EntityPlayer player = event.entityPlayer;
		log.info(player instanceof EntityPlayerMP);
		log.info(player instanceof EntityPlayerSP);
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
