package com.gmail.ckrier3000.secureitmod.forge.listeners;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class InteractListener extends BaseListener {
	
	private final SecureItMod instance;
	
	public InteractListener() {
		instance = SecureItMod.instance;
	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.entityPlayer.worldObj;
		
		
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
