package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class MushroomCowAchievement extends CourseAchievement
{
	private int games;
	
	public MushroomCowAchievement(int games)
	{
		this.games = games;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!MobRacersPlugin.getGame().getConfig().enableStats())
			return true;
		
		return CourseStats.get(p).getVehicleGamesPlayed(VehicleType.COW.name()) >= games;
	}
	
	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_MUSHROOMCOW_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}
	
	@Override
	public String getName()
	{
		return "§6§l" + CourseMessage.ACHIEVEMENT_MUSHROOMCOW_NAME.toString();
	}
	
	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_MUSHROOMCOW_DESC.toString().replace("#", "" + this.games);
	}
	
	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getVehicleGamesPlayed(VehicleType.COW.name()) + " / " + games + ")";
	}
}
