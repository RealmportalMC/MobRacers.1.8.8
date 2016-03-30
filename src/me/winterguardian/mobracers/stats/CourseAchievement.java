package me.winterguardian.mobracers.stats;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.stats.achivements.BlockAchievement;
import me.winterguardian.mobracers.stats.achivements.CaveSpiderAchievement;
import me.winterguardian.mobracers.stats.achivements.DonkeyAchievement;
import me.winterguardian.mobracers.stats.achivements.ElderGuardianAchievement;
import me.winterguardian.mobracers.stats.achivements.GamesplayedAchievement;
import me.winterguardian.mobracers.stats.achivements.MagmaCubeAchievement;
import me.winterguardian.mobracers.stats.achivements.MellohiAchievement;
import me.winterguardian.mobracers.stats.achivements.MushroomCowAchievement;
import me.winterguardian.mobracers.stats.achivements.SilverfishAchievement;
import me.winterguardian.mobracers.stats.achivements.StalAchievement;
import me.winterguardian.mobracers.stats.achivements.SuperSheepAchievement;
import me.winterguardian.mobracers.stats.achivements.VictoriesAchievement;
import me.winterguardian.mobracers.stats.achivements.WaitAchievement;

import org.bukkit.entity.Player;

public abstract class CourseAchievement
{
	public static final CourseAchievement VICTORIES10000 = new VictoriesAchievement(10000, 100000, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_7.toString(), null);
	public static final CourseAchievement VICTORIES1000 = new VictoriesAchievement(1000, 50000, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_6.toString(), VICTORIES10000);
	public static final CourseAchievement VICTORIES500 = new VictoriesAchievement(500, 25000, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_5.toString(), VICTORIES1000);
	public static final CourseAchievement VICTORIES200 = new VictoriesAchievement(200, 10000, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_4.toString(), VICTORIES500);
	public static final CourseAchievement VICTORIES75 = new VictoriesAchievement(75, 5000, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_3.toString(), VICTORIES200);
	public static final CourseAchievement VICTORIES30 = new VictoriesAchievement(30, 2500, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_2.toString(), VICTORIES75);
	public static final CourseAchievement VICTORIES10 = new VictoriesAchievement(10, 1000, "§c§l" + CourseMessage.ACHIEVEMENT_VICTORIES_NAME_1.toString(), VICTORIES30);
	
	public static final CourseAchievement GAMESPLAYED50000 = new GamesplayedAchievement(50000, 25000, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_7.toString(), null);
	public static final CourseAchievement GAMESPLAYED10000 = new GamesplayedAchievement(10000, 10000, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_6.toString(), GAMESPLAYED50000);
	public static final CourseAchievement GAMESPLAYED1000 = new GamesplayedAchievement(1000, 5000, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_5.toString(), GAMESPLAYED10000);
	public static final CourseAchievement GAMESPLAYED500 = new GamesplayedAchievement(500, 2000, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_4.toString(), GAMESPLAYED1000);
	public static final CourseAchievement GAMESPLAYED100 = new GamesplayedAchievement(100, 1000, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_3.toString(), GAMESPLAYED500);
	public static final CourseAchievement GAMESPLAYED25 = new GamesplayedAchievement(25, 500, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_2.toString(), GAMESPLAYED100);
	public static final CourseAchievement GAMESPLAYED1 = new GamesplayedAchievement(1, 50, "§e§l" + CourseMessage.ACHIEVEMENT_GAMESPLAYED_NAME_1.toString(), GAMESPLAYED25);
	
	public static final CourseAchievement COW_PLAY = new MushroomCowAchievement(80);
	public static final CourseAchievement HORSE_PASSINGS = new DonkeyAchievement(500);
	public static final CourseAchievement SHEEP_BESTTIME = new SuperSheepAchievement(3);
	public static final CourseAchievement SILVERFISH_ITEMS = new SilverfishAchievement(2000);
	public static final CourseAchievement SLIME_BESTTIME = new MagmaCubeAchievement(2);
	public static final CourseAchievement SPIDER_WIN = new CaveSpiderAchievement(25);
	
	public static final CourseAchievement GUARDIAN_WIN = new ElderGuardianAchievement(40);
	public static final CourseAchievement BLOCK_BUILD = new BlockAchievement();
	
	public static final CourseAchievement UNDEADHORSE_WIN = new MellohiAchievement(15);
	public static final CourseAchievement PIG_PASSINGS = new StalAchievement(200);
	public static final CourseAchievement WOLF_PLAY = new WaitAchievement(30);
	
	
	public abstract boolean isComplete(Player p);
	public abstract void onComplete(Player p);
	public abstract String getName();
	public abstract String getDescription();
	public abstract String getProgression(Player p);
	
	public CourseAchievement getNext()
	{
		return null;
	}


	
	public static CourseAchievement[] values()
	{
		CourseAchievement[] achievements = new CourseAchievement[CourseAchievement.class.getFields().length];
		
		for(int i = 0; i < CourseAchievement.class.getFields().length; i++)
			try
			{
				achievements[i] = (CourseAchievement) CourseAchievement.class.getFields()[i].get(null);
			}
			catch (Exception e)
			{
				achievements[i] = null;
			}
		return achievements;
	}
	
	public static List<CourseAchievement> getTodo(Player p)
	{
		List<CourseAchievement> achievements = new ArrayList<>();
		
		for(CourseAchievement achiev : CourseAchievement.values())
			if(!achiev.isComplete(p))
				achievements.add(achiev);
		
		for(CourseAchievement achiev : CourseAchievement.values())
			if(!achiev.isComplete(p))
				achievements.remove(achiev.getNext());
				
		return achievements;
	}
	
	public static int getDone(Player p)
	{
		int i = 0;
		for(CourseAchievement achiev : CourseAchievement.values())
			if(achiev.isComplete(p))
				i++;
		return i;
	}
	
	public static int getRemaining(Player p)
	{
		int i = 0;
		for(CourseAchievement achiev : CourseAchievement.values())
			if(!achiev.isComplete(p))
				i++;
		return i;
	}
}
