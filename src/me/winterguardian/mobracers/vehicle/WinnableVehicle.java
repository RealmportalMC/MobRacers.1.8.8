package me.winterguardian.mobracers.vehicle;

import org.bukkit.entity.Player;

import me.winterguardian.mobracers.stats.CourseAchievement;

public abstract class WinnableVehicle extends Vehicle
{
	public abstract CourseAchievement getWinAchievement();
	
	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p);
	}
}
