package me.winterguardian.core.util;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AchievementUtil
{
	private AchievementUtil() {}
	
	/**
	 * Removes all achievements for a player.
	 * 
	 * @param p
	 */
	public static void clear(Player p)
	{
		for(Achievement achiev : Achievement.values())
			p.removeAchievement(achiev);
	}
	
	/**
	 * Give all achievements to a player.
	 * 
	 * @param p
	 */
	public static void giveAll(Player p)
	{
		for(Achievement achiev : Achievement.values())
			p.awardAchievement(achiev);
	}
	
	/**
	 * Gives a player an achievement so he have the animation but remove it just after.
	 * <br>
	 * 
	 * @param p
	 */
	public static void flashShow(final Player p, final Achievement achiev, Plugin plugin)
	{
		p.removeAchievement(achiev);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				p.awardAchievement(achiev);
			}
		}, 1);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				p.removeAchievement(achiev);
			}
		}, 2);
	}
	
	/**
	 * Give a player an achievement and let him see the animation event if he already had the achievement.
	 * 
	 * @param p
	 */
	public static void give(final Player p, final Achievement achiev, Plugin plugin)
	{
		p.removeAchievement(achiev);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				p.awardAchievement(achiev);
			}
		}, 1);
	}
}
