package com.gmail.ckrier3000.secureitmod.forge;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Logger;

import com.gmail.ckrier3000.secureitmod.forge.items.*;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

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

	@Instance(value = "cSecureItMod")
	public static SecureItMod instance;
	public static boolean isServer;

	public static Item lockAndKeyItem, keyItem;

	public static int maxGenRetries = 50;

	public static final String WORLDINFO_LOCKS = "SILocks";
	public static final String WORLDINFO_USEDLOCKS = "SIUsedLockIDS";
	public static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockID";
	public static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";

	private Map<Integer, NBTTagList> usedLockLists;
	private Map<Integer, NBTTagCompound> lockDataLists;

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
			return toStringList(usedLockLists.get(demID));
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
			if (isLocked(world, x, y, z))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onChestAccess(PlayerInteractEvent event) {
		final int x = event.x, y = event.y, z = event.z;
		World world = event.entity.worldObj;

		EntityPlayer player = event.entityPlayer;
		Block b = world.getBlock(x, y, z);
		if (b instanceof BlockChest)
			if (isLocked(world, x, y, z)) {
				if (player.getCurrentEquippedItem() == null || !player.getCurrentEquippedItem().getItem().equals(keyItem)) {
					event.setCanceled(true);
					MessageUtil.sendMessage(player, "Chest is locked!");
				} else if (player.getCurrentEquippedItem().getItem().equals(keyItem)) {
					if (player.getCurrentEquippedItem().stackTagCompound != null && player.getCurrentEquippedItem().stackTagCompound.hasKey(COMPOUND_TAG_ID_CHEST_LOCK_ID))
						if (isKey(world, x, y, z, player.getCurrentEquippedItem().stackTagCompound.getString(LockAndKeyItem.COMPOUND_TAG_KEY_ID))) {
							if (event.action.equals(event.action.LEFT_CLICK_BLOCK)) {
								MessageUtil.sendMessage(player, "Unlocked chest");
								unlock(world, x, y, z);
								return;
							}
							else {
								MessageUtil.sendMessage(player, "Other");
								return;
							}
						} 
					MessageUtil.sendMessage(player, "Wrong key...");
					event.setCanceled(true);
						
					
				}
					
			}
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
					.getCompoundTag(WORLDINFO_LOCKS));
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		World w = event.world;

		int id = w.provider.dimensionId;

		if (usedLockLists.containsKey(id))
			w.getWorldInfo().getNBTTagCompound()
					.setTag(WORLDINFO_LOCKS, usedLockLists.get(id));
		if (lockDataLists.containsKey(id)) {
			w.getWorldInfo().getNBTTagCompound()
					.setTag(WORLDINFO_LOCKS, lockDataLists.get(id));
		}
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
		lockDataLists = new HashMap<Integer, NBTTagCompound>();

		lockAndKeyItem = new LockAndKeyItem()
				.setTextureName("secureitmod:lockAndKey");
		keyItem = new KeyItem().setTextureName("secureitmod:key");

		GameRegistry.registerItem(lockAndKeyItem,
				lockAndKeyItem.getUnlocalizedName());
		GameRegistry.registerItem(keyItem, keyItem.getUnlocalizedName());
	}

	private List<NBTTagCompound> toCompoundList(NBTTagList list) {
		List<NBTTagCompound> ret = new ArrayList<NBTTagCompound>();
		for (int i = 0; i < list.tagCount(); i++)
			ret.add(list.getCompoundTagAt(i));

		return ret;
	}

	private List<String> toStringList(NBTTagList list) {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < list.tagCount(); i++)
			ret.add(list.getStringTagAt(i));

		return ret;
	}

	private NBTTagCompound getLocks(World world) {
		return getLocks(world.provider.dimensionId);
	}

	private NBTTagCompound getLocks(int dim) {
		if (lockDataLists.containsKey(dim))
			return lockDataLists.get(dim);
		else {
			NBTTagCompound t = new NBTTagCompound();
			lockDataLists.put(dim, t);
			return t;
		}
	}

	private NBTTagList toTagList(List<String> list) {
		NBTTagList ret = new NBTTagList();

		for (String item : list)
			ret.appendTag(new NBTTagString(item));
		return ret;
	}

	public String lock(World world, int x, int y, int z, UUID owner) {
		return lock(world, x, y, z, owner.toString());
	}

	public String lock(World world, int x, int y, int z, String owner) {
		String key = getNewLockID(world);
		if (key == null)
			return null;

		String id = getLocString(x, y, z);
		NBTTagCompound t = new NBTTagCompound();
		t.setString(COMPOUND_TAG_ID_CHEST_LOCK_ID, key);
		t.setString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER, owner);
		NBTTagCompound a = getLocks(world);
		a.setTag(id, t);

		setLocks(world, a);
		getLocks(world).setTag(id, t);

		return key;
	}

	private void setLocks(World world, NBTTagCompound a) {
		setLocks(world.provider.dimensionId, a);
	}

	private void setLocks(int dimensionId, NBTTagCompound a) {
		lockDataLists.put(dimensionId, a);
	}

	private String getLocString(int x, int y, int z) {
		return new StringBuilder().append(x).append(',').append(y).append(',')
				.append(z).toString();
	}
	
	public boolean isKey(World world, int x, int y, int z, String key) {
		if (!getLocks(world).hasKey(getLocString(x, y, z)))
			return true;
		if (getLocks(world).getCompoundTag(getLocString(x, y, z)).hasKey(COMPOUND_TAG_ID_CHEST_LOCK_ID))
			return getLocks(world).getCompoundTag(getLocString(x, y, z)).getString(COMPOUND_TAG_ID_CHEST_LOCK_ID).equals(key);
		else return false;
	}

	public boolean isLocked(World world, int x, int y, int z) {
		String id = getLocString(x, y, z);
		System.out.println(getLocks(world).hasKey(id));
		return getLocks(world).hasKey(id);
	}

	public void unlock(World world, int x, int y, int z) {
		// TODO Auto-generated method stub
	}
}
