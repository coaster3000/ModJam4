package com.gmail.ckrier3000.secureitmod.forge.listeners;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

public abstract class BaseListener {
	
	public final void register() {
		if (onRegister())
			return;
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public final void unregister() {
		if (onUnregister())
			return;
		
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	
	/**
	 * Called before registering the listener.
	 * 
	 * @return true if canceling register.
	 */
	public abstract boolean onRegister();
	
	/**
	 * Called when before listener is unregistered.
	 * 
	 * @return true if cancel unregister.
	 */
	public abstract boolean onUnregister();
}
