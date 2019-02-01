package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;

import org.bukkit.entity.Player;

public class SilverfishAchievement extends CourseAchievement
{
	private int items;
	
	public SilverfishAchievement(int items)
	{
		this.items = items;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!MobRacersPlugin.getGame().getConfig().enableStats())
			return true;
		
		return CourseStats.get(p).getTotalCollectedItems() >= items;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_SILVERFISH_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}

	@Override
	public String getName()
	{
		return "§3§l" + CourseMessage.ACHIEVEMENT_SILVERFISH_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_SILVERFISH_DESC.toString().replace("#", "" + this.items);
	}
	
	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getTotalCollectedItems() + " / " + this.items + ")";
	}

}
