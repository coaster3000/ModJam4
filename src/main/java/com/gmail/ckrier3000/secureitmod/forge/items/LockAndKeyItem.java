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
import com.gmail.ckrier3000.secureitmod.forge.tileentity.ProtectedTileEntityChest;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

public class LockAndKeyItem extends Item {
	static final String COMPOUND_TAG_ID_CHEST_LOCK = "SILock";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockId";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";

	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
		return 0;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return 0;
	}

	// Purely a helper function for less typing for now. Maybe will keep it.
	private SecureItMod instance() {
		return SecureItMod.instance;
	}

	@Override
	public boolean isDamageable() {
		return false;
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

			if (te instanceof TileEntityChest
					&& !(te instanceof ProtectedTileEntityChest)) {
				TileEntityChest teChest = (TileEntityChest) te;
				try {
					ProtectedTileEntityChest en = new ProtectedTileEntityChest(
							teChest);
					world.setTileEntity(teChest.xCoord, teChest.yCoord,
							teChest.zCoord, en);
					world.mapStorage.saveAllData();
					try {
						en.setAll(player.getUniqueID());
					} catch (Exception e) {
						instance().getLogger().error("Failed to lock chest!");
						e.printStackTrace();
						world.setTileEntity(teChest.xCoord, teChest.yCoord,
								teChest.zCoord, teChest); // revert.
						return true;
					}
					instance().getLogger().debug("Locked!");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// NBTTagCompound chestTag = new NBTTagCompound();
				// teChest.writeToNBT(chestTag);
				//
				// NBTTagCompound lockTag = new NBTTagCompound();
				//
				// if (chestTag.hasKey(COMPOUND_TAG_ID_CHEST_LOCK)) {
				// MessageUtil.sendMessage(player, "Already locked.");
				// return true;
				// }
				//
				//
				//
				// lockTag.setString(COMPOUND_TAG_ID_CHEST_LOCK_ID, id);
				// lockTag.setString(COMPOUND_TAG_ID_CHEST_LOCK_OWNER,
				// player.getUniqueID().toString());
				//
				// chestTag.setTag(COMPOUND_TAG_ID_CHEST_LOCK, lockTag);
				//
				// te = world.getTileEntity(x, y, z);
				// if (te instanceof TileEntityChest)
				// teChest = (TileEntityChest) te;
				// else
				// instance().getLogger().error("Lost chest tile entity!");
				//
				// teChest.readFromNBT(chestTag);
				// chestTag = new NBTTagCompound();
				// teChest.writeToNBT(chestTag);
				//
				// if (chestTag.hasKey(COMPOUND_TAG_ID_CHEST_LOCK)) {
				// if (true) //TODO: Replace with a debug config option.
				// instance().getLogger().info("Lock made successfully");
				// } else if (true) //TODO: Replace with a debug config option.
				// instance().getLogger().warn("Lock made failed.");
				//
				// return true;

			} else {
				MessageUtil.sendMessage(player, "Already locked");
			}
			return true; // Prevent's use from what I tested.
		}
		return false;
	}

}
