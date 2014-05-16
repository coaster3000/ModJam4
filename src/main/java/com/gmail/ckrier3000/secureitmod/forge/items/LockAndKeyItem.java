package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;

import org.apache.commons.lang3.RandomStringUtils;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

public class LockAndKeyItem extends Item {
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
				
				
				NBTTagCompound chestTag = new NBTTagCompound();
				teChest.writeToNBT(chestTag);
				
				NBTTagCompound lockTag = new NBTTagCompound();
				
				if (chestTag.hasKey(COMPOUND_TAG_ID_CHEST_LOCK)) {
					MessageUtil.sendMessage(player, "Already locked...");
				}
				
				
//				NBTTagCompound worldTags = world.getWorldInfo().getNBTTagCompound();
//				
//				NBTTagCompound lockInfo = new NBTTagCompound();
//				NBTTagList worldLockInfo = null;
//				
//				
//				teChest.writeToNBT(lockInfo);
//								
//				if (lockInfo.hasKey(COMPOUND_TAG_ID_CHEST_LOCK)) {
//					MessageUtil.message(player, "Lock already exists");
//					return true;
//				}
//				
//				if (worldTags.hasKey(SecureItMod.WORLDINFO_TAG_USED_IDS, NBT.TAG_LIST))
//					worldLockInfo = worldTags.getTagList((SecureItMod.WORLDINFO_TAG_USED_IDS, NBT.TAG_STRING);
//				else
//					worldLockInfo = new NBTTagList();
//				
//				int retries = 50;
//				List<String> t = new ArrayList<String>();
//				
//				for (int i = 0; i > worldLockInfo.tagCount(); i++)
//					t.add(worldLockInfo.getStringTagAt(i));
//				
//				String lockId = null;
//				while (retries > 0) {
//					retries++;
//					String n = RandomStringUtils.randomAlphanumeric(32);
//					System.out.println(n);
//					if (t.contains(n)) 
//						continue;
//					lockId = n;
//					break;
//				}
//				
//				if (lockId == null) {
//					MessageUtil.message(player, "Failed to lock.");
//					return true;
//				}
//				
//				NBTTagCompound lockTag = new NBTTagCompound();
//				
//				lockTag.setString(COMPOUND_TAG_ID_CHEST_LOCK_ID, lockId);
//				lockTag.setString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER, player.getUniqueID().toString());
//				
//				lockInfo.setTag(COMPOUND_TAG_ID_CHEST_LOCK, lockTag);
//				
//				teChest.readFromNBT(lockInfo);
//				
//				teChest.writeToNBT(lockInfo);
//				if (!lockInfo.hasKey(COMPOUND_TAG_ID_CHEST_LOCK)) {
//					MessageUtil.message(player, "Failed to write lock.");
//					return true;
//				}
//				MessageUtil.message(player, "Locked");
			}
			return true; // Prevent's use from what I tested.
		}
		return false;
	}

}
