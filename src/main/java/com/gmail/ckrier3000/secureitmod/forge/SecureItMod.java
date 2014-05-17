package com.gmail.ckrier3000.secureitmod.forge;

import ibxm.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;

import com.gmail.ckrier3000.secureitmod.forge.items.KeyItem;
import com.gmail.ckrier3000.secureitmod.forge.items.LockAndKeyItem;
import com.gmail.ckrier3000.secureitmod.forge.tileentity.ProtectedTileEntityChest;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "cSecureItMod", version = "1.7.2-1.0", name = "Secure It Mod")
public class SecureItMod {

	public static final String WORLDINFO_TAG_USED_IDS = "SIUsedIds";
	
	@Instance(value = "cSecureItMod")
	public static SecureItMod instance;

	public static Item lockAndKeyItem, keyItem;
	public static int maxGenRetries = 50;
	
	private File modConfigurationDirectory, suggestedConfig;
	private Logger log;
	
	private Map<Integer, NBTTagList> usedLockLists;
	
	public static boolean isServer;
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		World w = event.world;
		int id = w.provider.dimensionId;
		
		if (w.getWorldInfo().getNBTTagCompound().hasKey(WORLDINFO_TAG_USED_IDS, NBT.TAG_LIST)) {
			usedLockLists.put(id, w.getWorldInfo().getNBTTagCompound().getTagList(WORLDINFO_TAG_USED_IDS, NBT.TAG_STRING));
		}
	}
	
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		World w = event.world;
		
		int id = w.provider.dimensionId;
		
		if (usedLockLists.containsKey(id))
			w.getWorldInfo().getNBTTagCompound().setTag(WORLDINFO_TAG_USED_IDS, usedLockLists.get(id));
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		isServer = event.getSide().isServer();
		modConfigurationDirectory = event.getModConfigurationDirectory();
		log = event.getModLog();
		suggestedConfig = event.getSuggestedConfigurationFile();
		usedLockLists  = new HashMap<Integer, NBTTagList>();
		
		lockAndKeyItem = new LockAndKeyItem()
				.setTextureName("secureitmod:lockAndKey");
		keyItem = new KeyItem().setTextureName("secureitmod:key");
		
		GameRegistry.registerItem(lockAndKeyItem,
				lockAndKeyItem.getUnlocalizedName());
		GameRegistry.registerItem(keyItem, keyItem.getUnlocalizedName());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
	
	@EventHandler
	public void complete(FMLLoadCompleteEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public List<String> getUsedIDList(World world) {
		return getUsedIDList(world.provider.dimensionId);
	}
	
	public List<String> getUsedIDList(int demID) {
		if (usedLockLists.containsKey(demID)) 
			return toList(usedLockLists.get(demID));
		else
			return Collections.emptyList();
	}
	
	private NBTTagList toTagList(List<String> list) {
		NBTTagList ret = new NBTTagList();
		
		for (String item : list)
			ret.appendTag(new NBTTagString(item));
		return ret;
	}
	
	private List<String> toList(NBTTagList list) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < list.tagCount(); i++)
			ret.add(list.getStringTagAt(i));
		
		return ret;
	}

	public Logger getLogger() {
		return log;
	}

	public String getNewLockID(World world) {
		List<String> ids = getUsedIDList(world);
		String id = null;
		for (int i = 0; i < SecureItMod.maxGenRetries; i++) {
			String t = RandomStringUtils.randomAlphabetic(32);
			if (!ids.contains(t)) {
				id = t;
				break;
			}
		}
		
		if (id == null) 
			getLogger().error("Failed to generate a id for lock.");
		return id;
	}
	
	@SubscribeEvent
	public void onChestAccess(PlayerInteractEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.entity.worldObj;
		
		EntityPlayer player = event.entityPlayer;
		
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof ProtectedTileEntityChest)
		{
			ProtectedTileEntityChest cte = (ProtectedTileEntityChest) te;
			if (!cte.isUseableByPlayer(player)) {
				MessageUtil.sendMessage(player, "Chest is locked. Must use a key!");
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.world;
		Block block = event.block;
		
		if (block instanceof BlockChest)
		{
			BlockChest chest = (BlockChest) block;
			
			TileEntity te = world.getTileEntity(x, y, z);
			
			if (te instanceof TileEntityChest)
			{
				MessageUtil.sendMessage(event.getPlayer(), "RanC");
			
				if (((TileEntityChest)te) instanceof ProtectedTileEntityChest)
				{
					MessageUtil.sendMessage(event.getPlayer(), "RanC");
					ProtectedTileEntityChest cte = (ProtectedTileEntityChest) te;
					if (!cte.isUseableByPlayer(event.getPlayer())) {
						event.setCanceled(true);
						event.setResult(Result.DENY);
					}
				}
			}
		}
	}
}
