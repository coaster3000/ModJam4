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
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import org.apache.commons.lang3.RandomStringUtils;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

public class LockAndKeyItem extends Item {
	static final String COMPOUND_TAG_ID_CHEST_LOCK = "SILock";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockID";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";

	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private boolean useFlag = false;

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
	public boolean canItemEditBlocks() {
		return false;
	}

	@Override
	public boolean onItemUse(
			ItemStack stack, // Non interactive blocks.
			EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) {

		return true;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack,
			EntityPlayer player, // Interactive blocks.
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		
		if (world.isRemote)
			return true;
		
		if (player.isSneaking())
			if (world.getBlock(x, y, z) instanceof BlockChest) {
				if (SecureItMod.instance.isLocked(world, x, y, z))
					MessageUtil.sendMessage(player, "Cannot lock already locked chest!");
				else {
					int lock = SecureItMod.instance.lock(world, x, y, z, player.getUniqueID());
					ItemStack key = new ItemStack(SecureItMod.keyItem);
					
					key.stackTagCompound = new NBTTagCompound();
					
					key.stackTagCompound.setInteger(KeyItem.COMPOUND_TAG_KEY_ID, lock);
					key.stackTagCompound.setString(KeyItem.COMPOUND_TAG_KEY_CREATOR, player.getDisplayName());
				
					if (!player.inventory.addItemStackToInventory(key.copy()))
						player.entityDropItem(key, 1);
					
					stack.stackSize--;
					player.inventory.setItemStack(stack.copy());
				}
				player.inventory.markDirty();
				return true; // Prevent's use from what I tested.
			}
		return SecureItMod.instance.isLocked(world, x, y, z);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		return true;
	}
	
	
	
}
