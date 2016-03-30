package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class SuperSheepAchievement extends CourseAchievement
{
	int bestRecords;
	
	public SuperSheepAchievement(int bestRecords)
	{
		this.bestRecords = bestRecords;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		if(CourseStats.get(p).isAchievementComplete("achievement.super_sheep"))
			return true;
		
		int bestRecords = 0;
		for(Arena arena : Arena.getArenaList())
		{
			String vehicle = CourseStats.get(p).getBestVehicle(arena.getName());
			if(vehicle != null && vehicle.equals(VehicleType.SHEEP.name()))
				bestRecords++;
		}
		
		return bestRecords >= this.bestRecords;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseStats.get(p).setAchievementCompleted("achievement.super_sheep", true);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_SUPERSHEEP_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}

	@Override
	public String getName()
	{
		return "§f§l" + CourseMessage.ACHIEVEMENT_SUPERSHEEP_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_SUPERSHEEP_DESC.toString().replace("#", "" + this.bestRecords);
	}

	@Override
	public String getProgression(Player p)
	{
		int bestRecords = 0;
		for(Arena arena : Arena.getArenaList())
		{
			String vehicle = CourseStats.get(p).getBestVehicle(arena.getName());
			if(vehicle != null && vehicle.equals(VehicleType.SHEEP.name()))
				bestRecords++;
		}
		
		return "(" + bestRecords + " / " + this.bestRecords + ")";
	}

}
