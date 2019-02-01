package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class WaitAchievement extends CourseAchievement
{

	private int games;
	
	public WaitAchievement(int games)
	{
		this.games = games;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!MobRacersPlugin.getGame().getConfig().enableStats())
			return true;
		
		return CourseStats.get(p).getVehicleGamesPlayed(VehicleType.WOLF.name()) >= games;
	}
	
	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_DISCWAIT_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}
	
	@Override
	public String getName()
	{
		return "§9§l" + CourseMessage.ACHIEVEMENT_DISCWAIT_NAME.toString();
	}
	
	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_DISCWAIT_DESC.toString().replace("#", "" + this.games);
	}
	
	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getVehicleGamesPlayed(VehicleType.WOLF.name()) + " / " + games + ")";
	}

}
