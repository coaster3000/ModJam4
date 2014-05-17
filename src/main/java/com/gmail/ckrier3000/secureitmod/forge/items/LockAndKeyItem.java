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
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockID";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";
	private static final String COMPOUND_TAG_KEY_CREATOR = "maker";
	public static final String COMPOUND_TAG_KEY_ID = "keyID";

	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private boolean useFlag = false;

	@Override
	public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
		return 0;
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab,
			List rets) {
		ItemStack i = new ItemStack(item, 1);
		i.stackTagCompound = new NBTTagCompound();
		rets.add(i);
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
			if (SecureItMod.instance.isLocked(world, x, y, z))
				MessageUtil.sendMessage(player, "Already locked!");
			else {
				if (player.inventory.consumeInventoryItem(this)) {
					String lock = SecureItMod.instance.lock(world, x, y, z, player.getUniqueID());
					if (lock != null) {
						MessageUtil.sendMessage(player, "Locked Chest!");
						ItemStack key = new ItemStack(SecureItMod.keyItem);
						
						key.stackTagCompound = new NBTTagCompound();
						
						key.stackTagCompound.setString(COMPOUND_TAG_KEY_ID, lock);
						key.stackTagCompound.setString(COMPOUND_TAG_KEY_CREATOR, player.getDisplayName());
						
						int empty = player.inventory.getFirstEmptyStack();
						if (empty < 0) {
							player.entityDropItem(key, 1);
						} else {
							player.inventory.addItemStackToInventory(key);
						}
					} else {
						MessageUtil.sendMessage(player, "Failed to lock chest!");
						if (player.inventory.hasItem(this)) {
							for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
								if (player.inventory.getStackInSlot(i).stackSize < player.inventory.getStackInSlot(i).getMaxStackSize()) {
									player.inventory.getStackInSlot(i).stackSize++;
									return true;
								}
							}
							player.dropItem(this, 1);
						}
					}
				} else {
					MessageUtil.sendMessage(player, "Failed consume key and lock");
				}
			}
				
			
			return true; // Prevent's use from what I tested.
		}
		return false;
	}

}
