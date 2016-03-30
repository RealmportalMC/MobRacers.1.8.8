package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;

import org.bukkit.entity.Player;

public abstract class PointsAchievement extends CourseAchievement
{
	private String name;
	private int reward;
	
	public PointsAchievement(String name, int reward)
	{
		this.name = name;
		this.reward = reward;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");

		CourseStats.get(p).addGamePoints(this.reward);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}

	@Override
	public String getName()
	{
		return this.name;
	}

}
