package com.gmail.ckrier3000.secureitmod.forge.items;

import com.gmail.ckrier3000.secureitmod.forge.SecureItMod;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class LockAndKeyItem extends Item {
	public LockAndKeyItem() {
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("lockAndKey");
		setMaxStackSize(64);
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
		
		player.swingItem();
		return stack;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack,
			EntityPlayer player, World world, int x, int y,
			int z, int side, float hitX, float hitY, float hitZ) {
		
		player.addChatMessage(new ChatComponentText("onItemUse"));
		return true;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		
		player.addChatMessage(new ChatComponentText("onItemUseFirst"));
		
		return ;
	}
	
//	public void onItemUseEvent(PlayerInteractEvent event) {
//		EntityPlayer player = event.entityPlayer;
//		World world = player.worldObj;
//		
//		ItemStack cur = player.getCurrentEquippedItem();
//		final int x = event.x, y = event.y, z = event.z;
//		if (cur != null && cur.getItem().equals(this) && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
//			Block block = world.getBlock(x,y,z);
//			if (block instanceof BlockChest) {
//				BlockChest chest = (BlockChest) block;
//				event.useBlock = Result.DENY;
//				int slot = player.inventory.currentItem;
//				int count = cur.stackSize;
//				
//				if (player.inventory.consumeInventoryItem(this)) {
//					
////				world.setBlock(x,y,z, BLOCK)
//				}
//			}
//		}
//	}
}
