package com.gmail.ckrier3000.secureitmod.forge;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.logging.log4j.Logger;

import com.gmail.ckrier3000.secureitmod.forge.items.KeyItem;
import com.gmail.ckrier3000.secureitmod.forge.items.LockAndKeyItem;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
}
