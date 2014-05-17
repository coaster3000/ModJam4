package com.gmail.ckrier3000.secureitmod.forge.listeners;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class InteractListener extends BaseListener {
	
	private final SecureItMod instance; //Easy Access
	
	public InteractListener() {
		instance = SecureItMod.instance;
	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		//Setup some accessors.
		final int x = event.x, y = event.y, z = event.z;
		final Action action = event.action;
		World world = event.entityPlayer.worldObj;
		Block block = world.getBlock(x, y, z);
		
		//Our needed specifics.
		final boolean isChest = block instanceof BlockChest;
		
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
