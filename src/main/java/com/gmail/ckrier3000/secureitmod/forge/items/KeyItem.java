package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
		if (!data.isServer || !data.isServerPlayer)
			return;
		
		ItemStack stack = data.player.getCurrentEquippedItem();
		
		ItemStack lockKey = new ItemStack(SecureItMod.lockAndKeyItem ,1);
		
		EntityPlayerMP playerS = ((EntityPlayerMP)data.player);
		
		if (SecureItMod.instance.isLocked(data.world, data.x, data.y, data.z)) {
			if (SecureItMod.instance.isKey(data.world, data.x, data.y, data.z, getKey(stack))) {
				if (data.player.isSneaking()) {
					SecureItMod.instance.unlock(data.world, data.x, data.y, data.z);
					
					stack.stackSize--;
					data.player.inventory.setInventorySlotContents(data.player.inventory.currentItem, stack.copy());
					
					if (!data.player.inventory.addItemStackToInventory(lockKey.copy()))
						data.player.dropItem(SecureItMod.lockAndKeyItem, 1);
					
					MessageUtil.sendMessage(data.player, "Unlocked chest.");
					data.player.inventory.markDirty();
					playerS.sendContainerAndContentsToPlayer(playerS.inventoryContainer, playerS.inventoryContainer.inventoryItemStacks);
				}
				return;
			} else {
				MessageUtil.sendMessage(data.player, "Wrong key");
			}
		}
		
	}
	
//	@Override
//	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
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
