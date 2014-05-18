package com.gmail.ckrier3000.secureitmod.forge.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.message.Message;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class DebugToolListener extends BaseListener {

	private SecureItMod instance; //Easy access
	private Logger log;
	
	public DebugToolListener() {
		instance = SecureItMod.instance;
		log = instance.getLogger();
		
	}
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.entity.worldObj;
		final boolean isServer = !world.isRemote;
		final Action action = event.action;
		
		log.debug(isServer);
		log.debug(action);
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
