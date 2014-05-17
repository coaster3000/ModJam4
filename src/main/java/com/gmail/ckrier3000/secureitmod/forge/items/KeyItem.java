package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.List;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class KeyItem extends Item {
	public KeyItem() {
		setUnlocalizedName("key");
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean canItemEditBlocks() {
		return false;
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
