package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class LockAndKeyItem extends Item {
	static final String WORLDINFO_TAG_USED_IDS = "SIUsedIds";
	static final String COMPOUND_TAG_ID_CHEST_LOCK = "SILock";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "owner";

	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return 0;
	}

	@Override
	public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {

		player.swingItem();
		return stack;
	}

	@Override
	public boolean onItemUse(
			ItemStack stack, // Non interactive blocks.
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {
			
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack,
			EntityPlayer player, // Interactive blocks.
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {

		if (world.getBlock(x, y, z) instanceof BlockChest) {
			BlockChest chest = (BlockChest) world.getBlock(x, y, z);
			TileEntity te = world.getTileEntity(x, y, z);
			
			
			if (te instanceof TileEntityChest) {
				TileEntityChest teChest = (TileEntityChest) te;

				NBTTagCompound worldTags = world.getWorldInfo().getNBTTagCompound();
				
				NBTTagCompound lockInfo = null;
				NBTTagList worldLockInfo = null;
				
				if (worldTags.hasKey(WORLDINFO_TAG_USED_IDS, NBT.TAG_LIST))
					worldLockInfo = worldTags.getTagList(WORLDINFO_TAG_USED_IDS, NBT.TAG_STRING);
				else
					worldLockInfo = new NBTTagList();
				
				worldTags.setTag(WORLDINFO_TAG_USED_IDS, worldLockInfo);
//				NBTTagCompound lockInfo = new NBTTagCompound();
//				NBTTagCompound worldTags = world.getWorldInfo().getNBTTagCompound();
//				
//				NBTTagList list = null;
//				if (worldTags.hasKey(LockAndKeyItem.LOCKS_TAG_ID, NBT.TAG_LIST))
//					list = worldTags.getTagList(LockAndKeyItem.LOCKS_TAG_ID, NBT.TAG_STRING);
//				else
//					list = new NBTTagList();
//				
//				worldTags.setTag(LockAndKeyItem.LOCKS_TAG_ID, list);
//				
//				int m = list.tagCount();
//				int retries = 50;
//				String key = null;
//				
//				while (retries > 0) {
//					retries++;
//					String n = RandomStringUtils.randomAlphanumeric(32);
//					
//					for (int i = 0; i < m; i++) {
//						if (list.getStringTagAt(i).equals(n)) 
//							continue;
//					}
//					
//					key = n;
//					break;
//				}
//				
//				//TODO: Add conditions for if a id already exists. Next thing to do for tomorrow.
//				
//				list.appendTag(new NBTTagString(key));
//				lockInfo.setString(LockAndKeyItem.LOCKS_TAG_ID, key);
//				
//				teChest.writeToNBT(lockInfo);
			}
			return true; // Prevent's use from what I tested.
		}
		return false;
	}

}
