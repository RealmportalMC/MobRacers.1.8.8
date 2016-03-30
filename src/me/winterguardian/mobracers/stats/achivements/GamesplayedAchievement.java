package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;

import org.bukkit.entity.Player;

public class GamesplayedAchievement extends PointsAchievement
{
	private CourseAchievement next;
	private int games;
	
	public GamesplayedAchievement(int games, int reward, String name, CourseAchievement next)
	{
		super(name, reward);
		this.games = games;
		this.next = next;
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		return CourseStats.get(p).getTotalGamesPlayed() >= games;
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_GAMESPLAYED_DESC.toString().replace("#", "" + this.games);
	}

	@Override
	public String getProgression(Player p)
	{
		return "(" + CourseStats.get(p).getTotalGamesPlayed() + " / " + games + ")";
	}
	
	@Override
	public CourseAchievement getNext()
	{
		return this.next;
	}
}