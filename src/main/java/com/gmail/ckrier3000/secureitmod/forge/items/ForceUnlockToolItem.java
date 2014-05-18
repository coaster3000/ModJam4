package com.gmail.ckrier3000.secureitmod.forge.items;

import com.gmail.ckrier3000.secureitmod.forge.InteractData;
import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;
import com.gmail.ckrier3000.secureitmod.util.MessageUtil;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ForceUnlockToolItem extends Item implements InteractProxy {
	public ForceUnlockToolItem() {
		setMaxStackSize(1);
		setUnlocalizedName("SIUnlocker");
		setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (SecureItMod.instance.isLocked(world, x, y, z)) {
			SecureItMod.instance.unlock(world, x, y, z);
			MessageUtil.sendMessage(player, "Unlocked!");
		}

		return false;
	}
	
	@Override
	public boolean canItemEditBlocks() {
		return false;
	}

	@Override
	public void interactProxy(InteractData data) {
		if (data.isServer)
			if (SecureItMod.instance.isLocked(data.world, data.x, data.y, data.z))
				SecureItMod.instance.unlock(data.world, data.x, data.y, data.z);
	}
	
	
}
