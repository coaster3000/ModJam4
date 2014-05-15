package com.gmail.ckrier3000.secureitmod.forge;

import javax.annotation.PreDestroy;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gmail.ckrier3000.secureitmod.forge.items.KeyItem;
import com.gmail.ckrier3000.secureitmod.forge.items.LockAndKeyItem;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = " cSecureItMod",
	version = "1.7.2-1.0",
	name = "Secure It Mod")
public class SecureItMod {
	
	@Instance("cSecureItMod")
	public static SecureItMod instance;
	
	public static Item lockAndKeyItem, keyItem;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		lockAndKeyItem = new LockAndKeyItem().setTextureName("secureitmod:lockAndKey");
		keyItem = new KeyItem().setTextureName("secureitmod:lock");
		
		GameRegistry.registerItem(lockAndKeyItem, lockAndKeyItem.getUnlocalizedName());
		GameRegistry.registerItem(keyItem, keyItem.getUnlocalizedName());
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
