package com.gmail.ckrier3000.secureitmod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class MessageUtil {
	public static void message(EntityPlayer player, String message) {
		player.addChatMessage(new ChatComponentText(message));
	}
}
