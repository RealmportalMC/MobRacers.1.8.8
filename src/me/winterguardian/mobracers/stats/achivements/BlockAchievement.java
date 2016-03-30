package me.winterguardian.mobracers.stats.achivements;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;

import org.bukkit.entity.Player;

public class BlockAchievement extends CourseAchievement
{
	public BlockAchievement()
	{
		
	}
	
	@Override
	public boolean isComplete(Player p)
	{
		if(!((MobRacersConfig)MobRacersPlugin.getGame().getConfig()).enableStats())
			return true;
		
		if(CourseStats.get(p).isAchievementComplete("achievement.block"))
			return true; 
		
		for(Arena arena : Arena.getArenaList())
			if(arena.getAuthor().equalsIgnoreCase(p.getName()) && CourseStats.get(p).getArenaGamesPlayed(arena.getName()) >= 1)
				return true;
		
		return false;
	}

	@Override
	public void onComplete(Player p)
	{
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
		CourseStats.get(p).setAchievementCompleted("achievement.block", true);
		CourseMessage.ACHIEVEMENT_COMPLETE.say(p);
		p.sendMessage(getName() + " §f(" + getDescription() + ")");
		CourseMessage.ACHIEVEMENT_BLOCK_UNLOCK.say(p);
		CourseMessage.SEPARATOR_ACHIEVEMENT.say(p);
	}
	
	@Override
	public String getName()
	{
		return "§4§l" + CourseMessage.ACHIEVEMENT_BLOCK_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.ACHIEVEMENT_BLOCK_DESC.toString();
	}

	@Override
	public String getProgression(Player p)
	{
		return "";
	}

}
