package com.gmail.ckrier3000.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class LockAndKeyItem extends Item {
	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
	}
}
