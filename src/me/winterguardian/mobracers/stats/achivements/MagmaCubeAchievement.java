package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class MagmaCubeAchievement extends CourseAchievement
{
	int bestRecords;
	
	public MagmaCubeAchievement(int bestRecords)
	{
		this.bestRecords = bestRecords;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return CourseStats.get(p).isAchievementComplete("achievement.magma_cube") || getBestRecords(p) >= this.bestRecords;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseStats.get(p).setAchievementCompleted("achievement.magma_cube", true);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_MAGMACUBE_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}

	@Override
	public String getName()
	{
		return "§a§l" + CourseMessage.ACHIEVEMENT_MAGMACUBE_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_MAGMACUBE_DESC.toString().replace("#", "" + this.bestRecords);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + getBestRecords(p) + " / " + this.bestRecords + ")";
	}

	private int getBestRecords(Player p)
	{
		int bestRecords = 0;
		for(Arena arena : Arena.getArenaList())
		{
			String vehicle = CourseStats.get(p).getBestVehicle(arena.getName());
			if(vehicle != null && vehicle.equals(VehicleType.SLIME.name()))
				bestRecords++;
		}
		return bestRecords;
	}
}
