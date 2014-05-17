package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SecureItItem extends Item {
	private static String[] subNames = new String[] { "LockAndKey", "Key", "Unlocker"};
	
	public SecureItItem() {
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("SecureItItem");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + subNames[stack.getItemDamage()%3];
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List ret) {
		for (int i = 0; i < 3; i++) {
			ItemStack stack = new ItemStack(item, 1, i);
			
			ret.add(stack);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List ret, boolean something) {
		super.addInformation(stack, player, ret, something);
	}
}
