package com.gmail.ckrier3000.secureitmod.forge;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
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

import com.gmail.ckrier3000.secureitmod.forge.common.CommonProxy;
import com.gmail.ckrier3000.secureitmod.forge.items.*;
import com.gmail.ckrier3000.secureitmod.forge.listeners.DebugToolListener;
import com.gmail.ckrier3000.secureitmod.forge.listeners.InteractListener;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "secureitmod", version = "1.7.2-1.0", name = "Secure It Mod")
public class SecureItMod {

	public static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockID";
	public static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";

	@Instance(value = "secureitmod")
	public static SecureItMod instance;

	public static boolean isServer;

	public static Item lockAndKeyItem, keyItem, forceUnlockItem;

	public static int maxGenRetries = 50;
	
	@SidedProxy(serverSide = "com.gmail.ckrier3000.secureitmod.forge.common.CommonProxy", clientSide = "com.gmail.ckrier3000.secureitmod.forge.common.ClientProxy")
	public static CommonProxy proxy;
	
	public static final String WORLDINFO_LOCKS = "SILocks";
	public static final String WORLDINFO_USEDLOCKS = "SIUsedLockIDS";

	private Map<Integer, NBTTagCompound> lockDataLists;
	private Logger log;

	private File modConfigurationDirectory, suggestedConfig;
	private Map<Integer, Integer> usedLockLists;
	private DebugToolListener debugListener;
	private InteractListener interactListener;
	
	public Integer getLastID(int did) {
		if (!usedLockLists.containsKey(did)) {
			usedLockLists.put(did, 0);
			return 0;
		}
		return usedLockLists.get(did);
	}

	public Integer getLastID(World world) {
		return getLastID(world.provider.dimensionId);
	}
	
	public Integer getLockID(World world, int x, int y, int z) {
		if (isLocked(world, x, y, z))
			return getLocks(world).getInteger(COMPOUND_TAG_ID_CHEST_LOCK_ID);
		else
			return null;
	}

	private NBTTagCompound getLocks(int dim) {
		if (lockDataLists.containsKey(dim)) {
			return lockDataLists.get(dim);
		} else {
			NBTTagCompound t = new NBTTagCompound();
			lockDataLists.put(dim, t);
			return t;
		}
	}

	private NBTTagCompound getLocks(World world) {
		return getLocks(world.provider.dimensionId);
	}

	private String getLocString(int x, int y, int z) {
		return new StringBuilder().append(x).append(',').append(y).append(',').append(z).toString();
	}

	public Logger getLogger() {
		return log;
	}

	public int getNewLockID(World world) {
		final int did = world.provider.dimensionId;

		int ret = getLastID(world) + 1;
		usedLockLists.put(did, ret);
		return ret;

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		debugListener = new DebugToolListener();
		interactListener = new InteractListener();
		
		
	}

	public boolean isKey(World world, int x, int y, int z, Integer key) {
		if (!isLocked(world, x, y, z))
			return true;
		
		if (key == null)
			return false; 

		if (getLocks(world).getCompoundTag(getLocString(x, y, z)).hasKey(COMPOUND_TAG_ID_CHEST_LOCK_ID))
			return getLocks(world).getCompoundTag(getLocString(x, y, z)).getInteger(COMPOUND_TAG_ID_CHEST_LOCK_ID) == key;
		else
			return true;
	}

	public boolean isLocked(World world, int x, int y, int z) {
		String id = getLocString(x, y, z);
		return getLocks(world).hasKey(id);
	}

	public Integer lock(World world, int x, int y, int z, String owner) {
		Integer key = getNewLockID(world);

		String id = getLocString(x, y, z);
		NBTTagCompound t = new NBTTagCompound();
		
		t.setInteger(COMPOUND_TAG_ID_CHEST_LOCK_ID, key);
		if (owner != null)
			t.setString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER, owner);
		else
			t.setString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER, "None");
		
		NBTTagCompound a = getLocks(world);
		a.setTag(id, t);

		setLocks(world, a);
		getLocks(world).setTag(id, t);

		return key;
	}

	public Integer lock(World world, int x, int y, int z, UUID owner) {
		return lock(world, x, y, z, (owner!= null) ? owner.toString() : null);
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


	@EventHandler
	public void onComplete(FMLLoadCompleteEvent event) {
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		World w = event.world;
		int id = w.provider.dimensionId;

		// XXX: Not really loading for some dumb reason...
		if (w.getWorldInfo().getNBTTagCompound().hasKey(WORLDINFO_USEDLOCKS, NBT.TAG_INT))
			usedLockLists.put(id, w.getWorldInfo().getNBTTagCompound().getInteger(WORLDINFO_USEDLOCKS));
		if (w.getWorldInfo().getNBTTagCompound().hasKey(WORLDINFO_LOCKS, NBT.TAG_LIST))
			lockDataLists.put(id, w.getWorldInfo().getNBTTagCompound().getCompoundTag(WORLDINFO_LOCKS));
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		World w = event.world;

		int id = w.provider.dimensionId;

		// XXX: Not really saving for some dumb reason.
		if (usedLockLists.containsKey(id))
			w.getWorldInfo().getNBTTagCompound().setInteger(WORLDINFO_USEDLOCKS, usedLockLists.get(id));
		if (lockDataLists.containsKey(id)) {
			w.getWorldInfo().getNBTTagCompound().setTag(WORLDINFO_LOCKS, lockDataLists.get(id));
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		lockAndKeyItem = new LockAndKeyItem().setTextureName("secureitmod:lockAndKey");
		keyItem = new KeyItem().setTextureName("secureitmod:key");
		forceUnlockItem = new ForceUnlockToolItem().setTextureName("secureitmod:forceUnlock");

		GameRegistry.registerItem(lockAndKeyItem, lockAndKeyItem.getUnlocalizedName());
		GameRegistry.registerItem(keyItem, keyItem.getUnlocalizedName());
		GameRegistry.registerItem(forceUnlockItem, forceUnlockItem.getUnlocalizedName());
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		isServer = event.getSide().isServer();
		modConfigurationDirectory = event.getModConfigurationDirectory();
		log = event.getModLog();
		
		suggestedConfig = event.getSuggestedConfigurationFile();
		usedLockLists = new HashMap<Integer, Integer>();
		lockDataLists = new HashMap<Integer, NBTTagCompound>();

	}

	private void setLocks(int dimensionId, NBTTagCompound a) {
		lockDataLists.put(dimensionId, a);
	}

	private void setLocks(World world, NBTTagCompound a) {
		setLocks(world.provider.dimensionId, a);
	}

	private NBTTagList toTagList(List<String> list) {
		NBTTagList ret = new NBTTagList();

		for (String item : list)
			ret.appendTag(new NBTTagString(item));
		return ret;
	}

	public void unlock(World world, int x, int y, int z) {
		if (!isLocked(world, x, y, z))
			return;
		
		getLocks(world).removeTag(getLocString(x, y, z));
	}
}
