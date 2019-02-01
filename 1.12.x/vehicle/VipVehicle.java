package me.winterguardian.mobracers.vehicle;

import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class VipVehicle extends Vehicle
{
	private Permission permission;
	
	@Override
	public boolean canChoose(Player p)
	{
		return p.hasPermission(getPermission());
	}
	
	protected Permission getPermission()
	{
		if(permission != null)
			return permission;
		
		permission = new Permission("MobRacers.vip." + getType().name().toLowerCase().replace("_", "-"), "Access to " + getName() + " vehicle in MobRacers", PermissionDefault.OP);
		if(Bukkit.getPluginManager().getPermission(permission.getName()) == null)
			Bukkit.getPluginManager().addPermission(permission);
		
		permission.addParent(MobRacersPlugin.VIP, true);
		
		return permission;
	}
}
