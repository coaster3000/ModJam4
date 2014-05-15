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
		return stack;
	}
	
	@SubscribeEvent
	public void onItemUseEvent(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		World world = player.worldObj;
		
		if (player.getCurrentEquippedItem().getItem().equals(this) && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			Block block = world.getBlock(event.x, event.y, event.z);
			if (block instanceof BlockChest) {
				BlockChest chest = (BlockChest) block;
				event.useBlock = Result.DENY;
				int slot = player.inventory.currentItem;
				int count = player.getCurrentEquippedItem().stackSize;
				
				if (count > 1) {
					player.getCurrentEquippedItem().stackSize--;
					slot = player.inventory.getFirstEmptyStack();
				}
				
				player.inventory.setInventorySlotContents(slot, new ItemStack(SecureItMod.keyItem));
			}
		}
	}
}
