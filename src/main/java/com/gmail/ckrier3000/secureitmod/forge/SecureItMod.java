package com.gmail.ckrier3000.secureitmod.forge;

import ibxm.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Vec3;
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
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;
import com.google.common.collect.MapMaker;

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

	@Instance(value = "cSecureItMod")
	public static SecureItMod instance;
	public static boolean isServer;

	public static Item lockAndKeyItem, keyItem;

	public static int maxGenRetries = 50;
	
	
	public static final String WORLDINFO_LOCKS = "SILocks";
	public static final String WORLDINFO_USEDLOCKS = "SIUsedLockIDS";
	
	private Map<Integer, NBTTagList> lockDataLists, usedLockLists;

	private Logger log;
	private File modConfigurationDirectory, suggestedConfig;


	public Logger getLogger() {
		return log;
	}

	public String getNewLockID(World world) {
		final int did = world.provider.dimensionId;

		List<String> ids = getUsedIDList(world);
		String id = null;
		for (int i = 0; i < SecureItMod.maxGenRetries; i++) {
			String t = RandomStringUtils.randomAlphabetic(32);
			if (!ids.contains(t)) {
				id = t;
				break;
			}
		}
		if (id == null) {
			getLogger().error("Failed to generate a id for lock.");
			return id;
		}
		ids.add(id);

		if (usedLockLists.containsKey(did))
			usedLockLists.put(did, toTagList(ids));

		return id;
	}

	public List<String> getUsedIDList(int demID) {
		if (usedLockLists.containsKey(demID))
			return toList(usedLockLists.get(demID));
		else
			return new ArrayList<String>();
	}

	public List<String> getUsedIDList(World world) {
		return getUsedIDList(world.provider.dimensionId);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.world;
		Block block = event.block;

		if (block instanceof BlockChest) {
			BlockChest chest = (BlockChest) block;

		}
	}

	@SubscribeEvent
	public void onChestAccess(PlayerInteractEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.entity.worldObj;

		EntityPlayer player = event.entityPlayer;

		TileEntity te = world.getTileEntity(x, y, z);
	}
	
	@EventHandler
	public void onComplete(FMLLoadCompleteEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		World w = event.world;
		int id = w.provider.dimensionId;

		if (w.getWorldInfo().getNBTTagCompound()
				.hasKey(WORLDINFO_USEDLOCKS, NBT.TAG_LIST))
			usedLockLists.put(id, w.getWorldInfo().getNBTTagCompound()
					.getTagList(WORLDINFO_USEDLOCKS, NBT.TAG_STRING));
		if (w.getWorldInfo().getNBTTagCompound()
				.hasKey(WORLDINFO_LOCKS, NBT.TAG_LIST))
			lockDataLists.put(id, w.getWorldInfo().getNBTTagCompound()
					.getTagList(WORLDINFO_LOCKS, NBT.TAG_COMPOUND));
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		World w = event.world;

		int id = w.provider.dimensionId;

		if (usedLockLists.containsKey(id))
			w.getWorldInfo().getNBTTagCompound()
					.setTag(WORLDINFO_LOCKS, usedLockLists.get(id));
		if (lockDataLists.containsKey(id))
			w.getWorldInfo().getNBTTagCompound()
					.setTag(WORLDINFO_LOCKS, lockDataLists.get(id));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		isServer = event.getSide().isServer();
		modConfigurationDirectory = event.getModConfigurationDirectory();
		log = event.getModLog();
		suggestedConfig = event.getSuggestedConfigurationFile();
		usedLockLists = new HashMap<Integer, NBTTagList>();

		lockAndKeyItem = new LockAndKeyItem()
				.setTextureName("secureitmod:lockAndKey");
		keyItem = new KeyItem().setTextureName("secureitmod:key");

		GameRegistry.registerItem(lockAndKeyItem,
				lockAndKeyItem.getUnlocalizedName());
		GameRegistry.registerItem(keyItem, keyItem.getUnlocalizedName());
	}

	private List<String> toList(NBTTagList list) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < list.tagCount(); i++)
			ret.add(list.getStringTagAt(i));

		return ret;
	}

	private NBTTagList toTagList(List<String> list) {
		NBTTagList ret = new NBTTagList();

		for (String item : list)
			ret.appendTag(new NBTTagString(item));
		return ret;
	}

	public String lock(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLocked(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}
}
