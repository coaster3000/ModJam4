package com.gmail.ckrier3000.secureitmod.items;

import com.gmail.ckrier3000.secureitmod.SecureItMod;

import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LockAndKeyItem extends Item {
	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {
		
		if (stack.getItem().getUnlocalizedName().equals(SecureItMod.lockAndKeyItem.getUnlocalizedName())) {
			player.swingItem();
			player.attackEntityFrom(DamageSource.magic, 1);
			
		}
		return stack;
	}
	
}
