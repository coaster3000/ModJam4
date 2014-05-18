package com.gmail.ckrier3000.secureitmod.forge.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class SecureItItem extends Item {
	private static String[] subNames;
	private static String[] subIcons;
	
	static {
		subNames = new String[] { "LockAndKey", "Key", "Unlocker"};
		subIcons = new String[subNames.length];
		
		for (int i = 0; i < subIcons.length; i++) subIcons[i] = "_"+subNames[i].toLowerCase();
	}
	
	@SideOnly(Side.CLIENT)
	private IIcon[] iconArray;
	
	public SecureItItem() {
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabTools);
		setUnlocalizedName("SecureItItem");
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + subNames[MathHelper.clamp_int(stack.getItemDamage(), 0, 3)];
	}
	
	@Override
	public boolean getShareTag() {
		return true;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List ret) {
		for (int i = 0; i < 3; i++) {
			ItemStack stack = new ItemStack(item, 1, i);
			
			ret.add(stack);
		}
	}
	
	@Override
	public boolean canItemEditBlocks() {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
		this.iconArray = new IIcon[subIcons.length];
		for (int i = 0; i < this.iconArray.length; i++) this.iconArray[i] = par1IconRegister.registerIcon(this.getIconString() + subIcons[i]);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List ret, boolean something) {
		super.addInformation(stack, player, ret, something);
	}
}
