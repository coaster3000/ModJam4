package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.gmail.ckrier3000.secureitmod.forge.InteractData;
import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

public class KeyItem extends Item implements InteractProxy {
	public static final String COMPOUND_TAG_KEY_CREATOR = "maker";
	public static final String COMPOUND_TAG_KEY_ID = "keyID";
	
	public KeyItem() {
		setUnlocalizedName("key");
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean canItemEditBlocks() {
		return false;
	}
	
	
	public static Integer getKey(ItemStack stack) {
		if (stack == null)
			return null;
		
		if (!stack.getItem().getClass().equals(KeyItem.class))
			return null;
		
		if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(COMPOUND_TAG_KEY_ID))
			return stack.stackTagCompound.getInteger(COMPOUND_TAG_KEY_ID);
		return null;
	}
	
	public void interactProxy(InteractData data) {
		
	}
	
//	@Override
//	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//		ItemStack lockKey = new ItemStack(SecureItMod.lockAndKeyItem ,1);
//		
//		if (SecureItMod.instance.isLocked(world, x, y, z)) {
//			if (SecureItMod.instance.isKey(world, x, y, z, getKey(stack))) {
//				if (player.isSneaking()) {
//					SecureItMod.instance.unlock(world, x, y, z);
//					
//					stack.stackSize--;
//					player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy());
//					
//					if (!player.inventory.addItemStackToInventory(lockKey.copy()))
//						player.dropItem(SecureItMod.lockAndKeyItem, 1);
//					
//					MessageUtil.sendMessage(player, "Unlocked chest.");
//					player.inventory.markDirty();
//				}
//				return false;
//			} else {
//				MessageUtil.sendMessage(player, "Wrong key");
//			}
//		}
//		
//		return true;
//	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z,
			EntityPlayer player) {
		return true;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return super.getItemStackDisplayName(par1ItemStack) + (getKey(par1ItemStack) != null?" ("+getKey(par1ItemStack) + ")":"");
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab,
			List rets) {
		ItemStack i = new ItemStack(item, 1);
		i.stackTagCompound = new NBTTagCompound();
		rets.add(i);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}
}
