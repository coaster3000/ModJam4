package com.gmail.ckrier3000.secureitmod.forge.items;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class KeyItem extends Item {
	public KeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("key");
		setMaxStackSize(1); 
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
}
