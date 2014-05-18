package com.gmail.ckrier3000.secureitmod.forge;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

/**
 * This class is explicitly used to proxy some data to item methods.
 * <p>Do not use unless its a perfect match for ya.
 * @author ChrisKrier
 *
 */
public class InteractData {
	public final int x, y, z;
	public final EntityPlayer player;
	public final World world;
	public final boolean isServer, isServerPlayer;
	public final Block block;
	public final Action action;
	public Result useItem, useBlock;
	public boolean cancelEvent;
	
	public InteractData(PlayerInteractEvent event) {
		x = event.x;
		y = event.y;
		z = event.z;
		
		player = event.entityPlayer;
		world = player.worldObj;
		
		action = event.action;
		
		isServer = !world.isRemote;
		isServerPlayer = player instanceof EntityPlayerMP;
		
		block = world.getBlock(x, y, z);
		
		useItem = event.useItem;
		useBlock = event.useBlock;
		
		cancelEvent = event.isCanceled();
	}
}
