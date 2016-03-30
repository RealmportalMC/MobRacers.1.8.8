package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;

public class DonkeyAchievement extends CourseAchievement
{
	private int passings;
	
	public DonkeyAchievement(int passings)
	{
		this.passings = passings;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return this.getPassings(p) >= this.passings;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_DONKEY_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}

	@Override
	public String getName()
	{
		return "§e§l" + CourseMessage.ACHIEVEMENT_DONKEY_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_DONKEY_DESC.toString().replace("#", "" + this.passings);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + this.getPassings(p) + " / " + this.passings + ")";
	}

	private int getPassings(Player p)
	{
		CourseStats stats = CourseStats.get(p);
		return stats.getPassings(VehicleType.HORSE.name()) + stats.getPassings(VehicleType.UNDEAD_HORSE.name()) + stats.getPassings(VehicleType.SKELETON_HORSE.name());
	}
}
