package com.gmail.ckrier3000.secureitmod.forge.items;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class LockAndKeyItem extends Item {
	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
		
		
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
	
	@EventHandler
	public void onItemUseEvent(PlayerInteractEvent event) {
		if (event.entityPlayer.getCurrentEquippedItem().getItem().equals(SecureItMod.lockAndKeyItem)) {
			
		}
	}
}
