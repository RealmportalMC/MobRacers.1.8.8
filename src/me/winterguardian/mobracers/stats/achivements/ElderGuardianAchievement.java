package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.core.Core;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class ElderGuardianAchievement extends CourseAchievement
{
private int victories;
	
	public ElderGuardianAchievement(int victories)
	{
		this.victories = victories;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return CourseStats.get(p).getVehicleVictories(VehicleType.GUARDIAN.name()) >= victories;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_ELDERGUARDIAN_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}
	
	@Override
	public String getName()
	{
		return "§b§l" + CourseMessage.ACHIEVEMENT_ELDERGUARDIAN_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_ELDERGUARDIAN_DESC.toString().replace("#", "" + this.victories);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getVehicleVictories(VehicleType.GUARDIAN.name()) + " / " + victories + ")";
	}
}
