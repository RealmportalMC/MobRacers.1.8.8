package me.winterguardian.mobracers.music;

import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class VipMusic extends CourseMusic
{
	private Permission permission;
	private String permName;

	public VipMusic(String permName, CourseRecord record)
	{
		super(record);
		this.permName = permName;
	}

	@Override
	public boolean isAvailable(Player p)
	{
		return p.hasPermission(getPermission());
	}
	
	protected Permission getPermission()
	{
		if(permission != null)
			return permission;
		
		permission = new Permission("MobRacers.vip." + permName, "Access to " + permName + " disc in MobRacers", PermissionDefault.OP);
		if(Bukkit.getPluginManager().getPermission(permission.getName()) == null)
			Bukkit.getPluginManager().addPermission(permission);
		
		permission.addParent(MobRacersPlugin.VIP, true);
		
		return permission;
	}
}
