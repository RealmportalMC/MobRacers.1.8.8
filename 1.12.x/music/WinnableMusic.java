package me.winterguardian.mobracers.music;

import me.winterguardian.mobracers.stats.CourseAchievement;

import org.bukkit.entity.Player;

public class WinnableMusic extends CourseMusic
{
	private CourseAchievement achievement;
	
	public WinnableMusic(CourseRecord record, CourseAchievement achievement)
	{
		super(record);
		this.achievement = achievement;
	}

	@Override
	public boolean isAvailable(Player p)
	{
		return this.achievement.isComplete(p);
	}

}
