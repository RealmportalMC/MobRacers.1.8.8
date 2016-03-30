package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class MellohiAchievement extends CourseAchievement
{

	private int victories;
	
	public MellohiAchievement(int victories)
	{
		this.victories = victories;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return CourseStats.get(p).getVehicleVictories(VehicleType.UNDEAD_HORSE.name()) >= victories;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_DISCMELLOHI_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}
	
	@Override
	public String getName()
	{
		return "§2§l" + CourseMessage.ACHIEVEMENT_DISCMELLOHI_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_DISCMELLOHI_DESC.toString().replace("#", "" + this.victories);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getVehicleVictories(VehicleType.UNDEAD_HORSE.name()) + " / " + victories + ")";
	}

}
