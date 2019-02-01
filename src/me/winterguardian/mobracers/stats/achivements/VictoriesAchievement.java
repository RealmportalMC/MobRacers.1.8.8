package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;

import org.bukkit.entity.Player;

public class VictoriesAchievement extends PointsAchievement
{
	private CourseAchievement next;
	private int victories;
	
	public VictoriesAchievement(int victories, int reward, String name, CourseAchievement next)
	{
		super(name, reward);
		this.victories = victories;
		this.next = next;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!MobRacersPlugin.getGame().getConfig().enableStats())
			return true;
		
		return CourseStats.get(p).getTotalVictories() >= victories;
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_VICTORIES_DESC.toString().replace("#", "" + this.victories);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getTotalVictories() + " / " + victories + ")";
	}
	
	@Override
	public CourseAchievement getNext()
	{
		return this.next;
	}
}
