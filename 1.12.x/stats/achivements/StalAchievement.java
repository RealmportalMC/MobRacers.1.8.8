package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class StalAchievement extends CourseAchievement
{

	private int passings;
	
	public StalAchievement(int passings)
	{
		this.passings = passings;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!MobRacersPlugin.getGame().getConfig().enableStats())
			return true;
		
		return this.getPassings(p) >= this.passings;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_DISCSTAL_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}

	@Override
	public String getName()
	{
		return "§d§l" + CourseMessage.ACHIEVEMENT_DISCSTAL_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_DISCSTAL_DESC.toString().replace("#", "" + this.passings);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + this.getPassings(p) + " / " + this.passings + ")";
	}

	private int getPassings(Player p)
	{
		return CourseStats.get(p).getPassings(VehicleType.PIG.name());
	}
}
