package me.winterguardian.mobracers.pluginsupport;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultSupport
{
	private Economy economy;
	
	public VaultSupport()
	{
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if(rsp == null)
			economy = null;
		
		economy = rsp.getProvider();
	}
	
	public boolean isEnabled()
	{
		return economy != null;
	}
	
	public Economy getEconomy()
	{
		return economy;
	}
}
