package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SecureItItem extends Item {

	static enum Type {
		keyAndLock, key, unlocker;
	}
	
	public SecureItItem() {
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("SecureItItem");
	}
	
	public Type getType(ItemStack stack) {
		return Type.values()[stack.getItemDamage()%Type.values().length-1];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + getType(stack).name();
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List ret) {
		for (Type t: Type.values()) {
			
		}
			

			
	}
}
