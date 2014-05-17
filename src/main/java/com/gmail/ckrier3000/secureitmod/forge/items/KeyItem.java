package com.gmail.ckrier3000.secureitmod.forge.items;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class KeyItem extends Item {
	public KeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("key");
		setMaxStackSize(1);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
		return 0;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return 0;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {

		return true;
	}
}
