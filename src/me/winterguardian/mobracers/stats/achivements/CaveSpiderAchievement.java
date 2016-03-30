package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.core.Core;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class CaveSpiderAchievement extends CourseAchievement
{
	private int victories;
	
	public CaveSpiderAchievement(int victories)
	{
		this.victories = victories;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return CourseStats.get(p).getVehicleVictories(VehicleType.SPIDER.name()) >= victories;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_CAVESPIDER_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}
	
	@Override
	public String getName()
	{
		return "§5§l" + CourseMessage.ACHIEVEMENT_CAVESPIDER_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_CAVESPIDER_DESC.toString().replace("#", "" + this.victories);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getVehicleVictories(VehicleType.SPIDER.name()) + " / " + victories + ")";
	}
}
