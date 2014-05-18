package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import org.apache.commons.lang3.RandomStringUtils;

import com.gmail.ckrier3000.secureitmod.forge.InteractData;
import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LockAndKeyItem extends Item implements InteractProxy {
	static final String COMPOUND_TAG_ID_CHEST_LOCK = "SILock";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_ID = "lockID";
	static final String COMPOUND_TAG_ID_CHEST_LOCK_OWNER = "owner";

	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
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

	public void interactProxy(InteractData data) {
		
		if (!data.isServer)
			return;
		
		ItemStack stack = data.player.getCurrentEquippedItem();
		if (data.player.isSneaking())
			if (data.block instanceof BlockChest) {
				if (SecureItMod.instance.isLocked(data.world, data.x, data.y, data.z))
					MessageUtil.sendMessage(data.player, LanguageRegistry.instance().getStringLocalization("mod.secureit.message.alreadLocked"));
				else {
					int	lock = SecureItMod.instance.lock(data.world, data.x, data.y, data.z, data.player.getUniqueID());
					
					ItemStack key = new ItemStack(SecureItMod.keyItem, 1);
					
					key.stackTagCompound = new NBTTagCompound();
					
					key.stackTagCompound.setInteger(KeyItem.COMPOUND_TAG_KEY_ID, lock);
					key.stackTagCompound.setString(KeyItem.COMPOUND_TAG_KEY_CREATOR, data.player.getDisplayName());
					
					if (!data.player.inventory.addItemStackToInventory(key.copy()))
						data.player.entityDropItem(key, 1);
					
					stack.stackSize--;
					data.player.inventory.setInventorySlotContents(data.player.inventory.currentItem, stack);
					
					data.player.inventory.markDirty();
					data.player.inventoryContainer.detectAndSendChanges();
					String msg = LanguageRegistry.instance().getStringLocalization("mod.secureit.message.locked");
					if (!MathHelper.stringNullOrLengthZero(msg))
						MessageUtil.sendMessage(data.player, msg);
				}
				data.cancelEvent = true;
				return;
			}
		data.cancelEvent =  SecureItMod.instance.isLocked(data.world, data.x, data.y, data.z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add("Locks a chest on shift click");
	}
	
//	@Override
//	public boolean onItemUse(
//			ItemStack stack, // Non interactive blocks.
//			EntityPlayer player, World world, int x, int y, int z, int side,
//			float hitX, float hitY, float hitZ) {

//		return true;
//	}
//
//	@Override
//	public boolean onItemUseFirst(ItemStack stack,
//			EntityPlayer player, // Interactive blocks.
//			World world, int x, int y, int z, int side, float hitX, float hitY,
//			float hitZ) {
//		
//	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		return true;
	}
	
	
	
}
