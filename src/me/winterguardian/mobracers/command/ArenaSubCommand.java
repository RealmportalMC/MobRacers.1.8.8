package me.winterguardian.mobracers.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.util.ActionBarUtil;
import me.winterguardian.core.util.RecordUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.arena.Breakline;
import me.winterguardian.mobracers.arena.RoadType;
import me.winterguardian.mobracers.state.game.ItemBox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ArenaSubCommand extends SubCommand
{
	private static List<String> argList = Arrays.asList("help", 
			"create", "delete", "info", 
			"setregion", "weather", "setlaps", "setlimit", "setregendelay", "setauthor", 
			"addspawn", "delspawn", "resetspawns", "showspawns", 
			"additem", "delitem", "resetitems", "showitems", 
			"addspectator", "delspectator", "resetspectators", "showspectators", 
			"addjukebox", "deljukebox", "resetjukeboxes", "playjukeboxes", 
			"addline", "delline", "resetlines", "showlines", 
			"adddeathzone", "deldeathzone", "resetdeathzones", "testdeathzones", "seticon");
	
	public ArenaSubCommand()
	{
		super("arena", Arrays.asList("arene", "arène", "mapconfig"), MobRacersPlugin.STAFF, CourseMessage.COMMAND_INVALID_PERMISSION.toString(), "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers arena help");
	}

	@Override
	public boolean onSubCommand(final CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
			args = new String[]{"help"};
		
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("aide"))
		{
			int page;
			try
			{
				page = Integer.parseInt(args[1]);	
			}
			catch(Exception e)
			{
				page = 1;
			}
			
			if(page < 1 || page > 9)
				page = 1;
			
			CourseMessage.COMMAND_ARENA_HELP_MENU.say(sender, "<page>", "" + page, "<max>", "" + 9);
			
			switch(page)
			{
			case 1:
				CourseMessage.COMMAND_ARENA_HELP_LIST_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LIST_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_CREATE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_CREATE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DELETE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DELETE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_INFO_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_INFO_COMMAND.say(sender);
				break;
				
			case 2:
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_ADD_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_ADD_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_REMOVE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_REMOVE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_RESET_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_RESET_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_SHOW_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPAWN_SHOW_COMMAND.say(sender);
				break;
				
			case 3:
				CourseMessage.COMMAND_ARENA_HELP_ITEM_ADD_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_ADD_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_REMOVE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_REMOVE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_RESET_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_RESET_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_SHOW_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEM_SHOW_COMMAND.say(sender);
				break;
				
			case 4:
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_ADD_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_ADD_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_REMOVE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_REMOVE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_RESET_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_RESET_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_SHOW_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPECTATOR_SHOW_COMMAND.say(sender);
				break;
				
			case 5:
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_ADD_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_ADD_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_REMOVE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_REMOVE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_RESET_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_RESET_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_PLAY_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_JUKEBOX_PLAY_COMMAND.say(sender);
				break;
				
			case 6:
				CourseMessage.COMMAND_ARENA_HELP_LINE_ADD_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_ADD_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_REMOVE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_REMOVE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_RESET_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_RESET_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_TELEPORT_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_TELEPORT_COMMAND.say(sender);
				break;
				
			case 7:
				CourseMessage.COMMAND_ARENA_HELP_LINE_TEST_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LINE_TEST_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_REGION_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_REGION_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_WEATHER_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_WEATHER_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_AUTHOR_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_AUTHOR_COMMAND.say(sender);
				break;
				
			case 8:
				CourseMessage.COMMAND_ARENA_HELP_SPEEDMODIFIER_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_SPEEDMODIFIER_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LAPS_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_LAPS_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_TIMELIMIT_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_TIMELIMIT_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEMREGENDELAY_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_ITEMREGENDELAY_COMMAND.say(sender);
				break;
				
			case 9:
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_ADD_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_ADD_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_REMOVE_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_REMOVE_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_RESET_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_RESET_COMMAND.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_TEST_INFO.say(sender);
				CourseMessage.COMMAND_ARENA_HELP_DEATHZONE_TEST_COMMAND.say(sender);
			
				CourseMessage.COMMAND_ARENA_HELP_NONEXT.say(sender);
				return true;
			}
			sender.sendMessage(CourseMessage.COMMAND_ARENA_HELP_NEXT.toString().replace("<next>", "" + (page + 1)));
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("liste"))
		{
			if(Arena.getArenaList().size() > 0)
			{
				CourseMessage.COMMAND_ARENA_LIST_LIST.say(sender);
				for(Arena arena : Arena.getArenaList())
					sender.sendMessage("  §f>" + (arena.isReady() ? "§a" : "§c") + arena.getName());
			}
			else
				CourseMessage.COMMAND_ARENA_LIST_NOARENA.say(sender);
				return true;
		}
		
		if(args.length < 2)
		{
			CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		final Arena arena = new Arena(args[1]);
		
		if(args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("nouveau") || args[0].equalsIgnoreCase("nouvelle") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("créer"))
		{
			if(args.length < 2)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			
			if(arena.exists())
			{
				CourseMessage.COMMAND_ARENA_NEW_ALREADYCREATED.say(sender);
				return false;
			}
			
			arena.setAuthor(sender.getName());
			arena.save();
			CourseMessage.COMMAND_ARENA_NEW_DONE.say(sender);
			return true;
		}
		
		if(!arena.exists())
		{
			CourseMessage.COMMAND_ARENA_INVALIDARENA.say(sender);
			return false;
			
		}
		
		arena.load();
		
		if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("infos"))
		{
			CourseMessage.COMMAND_ARENA_INFO_HEAD.say(sender, "<arena>", (arena.isReady() ? "§a" : "§c") + arena.getName());
			CourseMessage.COMMAND_ARENA_INFO_BREAKLINES.say(sender, "#", "" + arena.getLines().size());
			CourseMessage.COMMAND_ARENA_INFO_WEATHER.say(sender, "<time>", "" + arena.getTime(), "<time-locked>", arena.isTimeLocked() ? "§atrue" : "§cfalse", "<raining>", arena.isRaining() ? "§atrue" : "§cfalse");
			CourseMessage.COMMAND_ARENA_INFO_LAPSANDTIMELIMIT.say(sender, "<time>", "" + arena.getTime(), "<laps>", "" + arena.getLaps(), "<time-limit>", "" + arena.getRaceTimeLimit());
			CourseMessage.COMMAND_ARENA_INFO_SPAWNSANDITEMS.say(sender, "<spawns>", "" + arena.getSpawnPoints().size(), "<items>", "" + arena.getItems().size());
			CourseMessage.COMMAND_ARENA_INFO_REGION.say(sender, "<region>", arena.isRegionReady() ? "§atrue" : "§cfalse");
			CourseMessage.COMMAND_ARENA_INFO_JUKEBOXES.say(sender, "#", "" + arena.getJukeboxes().size());
			CourseMessage.COMMAND_ARENA_INFO_SPECTATORS.say(sender, "#", "" + arena.getSpectatorLocations().size());
			CourseMessage.COMMAND_ARENA_INFO_ITEMREGENDELAY.say(sender, "<delay>", "" + arena.getItemRegenDelay());
			CourseMessage.COMMAND_ARENA_INFO_DEATHZONES.say(sender, "#", "" + arena.getDeathZones().size());
			CourseMessage.COMMAND_ARENA_INFO_ROADSPEED_NORMAL.say(sender);
			
			if(arena.getRoadSpeedModifier().size() != 0)
			{
				for(RoadType road : arena.getRoadSpeedModifier().keySet())
					sender.sendMessage("  §e" + road.toString() + " §f-> " + (arena.getRoadSpeedModifier().get(road) > 0 ? "§a+" : "§c") + arena.getRoadSpeedModifier().get(road));
			}
			else
				CourseMessage.COMMAND_ARENA_INFO_ROADSPEED_EMPTY.say(sender);
			
			if(arena.isReady())
				CourseMessage.COMMAND_ARENA_INFO_READY.say(sender);
			else
			{	CourseMessage.COMMAND_ARENA_INFO_NOTREADY.say(sender);
				
				if(arena.getSpawnPoints().size() == 0)
					CourseMessage.COMMAND_ARENA_INFO_NOTREADY_NOSPAWNS.say(sender);
				
				if(arena.getSpectatorLocations().size() == 0)
					CourseMessage.COMMAND_ARENA_INFO_NOTREADY_NOSPECTATORS.say(sender);
				
				if(arena.getLines().size() < 2)
					CourseMessage.COMMAND_ARENA_INFO_NOTREADY_NOLINES.say(sender);
				
				if(arena.getRegion() == null)
					CourseMessage.COMMAND_ARENA_INFO_NOTREADY_NOREGION.say(sender);
				else if(!arena.isRegionReady())
					CourseMessage.COMMAND_ARENA_INFO_NOTREADY_BADREGION.say(sender);
			}
			
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("supprimer"))
		{
			if(arena.delete())
				CourseMessage.COMMAND_ARENA_DELETE_DONE.say(sender);
			else
				CourseMessage.COMMAND_ARENA_DELETE_FAILED.say(sender);
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setregion") || args[0].equalsIgnoreCase("région"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			SerializableRegion region = Core.getWand().getRegion((Player) sender);
			
			if(region != null)
			{
				
				arena.setRegion(region);
				arena.save();
				CourseMessage.COMMAND_ARENA_REGIONSET.say(sender);
				CourseMessage.REGIONWARNING.say(sender);
			}
			else
			{
				CourseMessage.COMMAND_INVALID_SELECTION.say(sender);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("weather") || args[0].equalsIgnoreCase("météo") || args[0].equalsIgnoreCase("meteo"))
		{
			
			if(args.length < 5)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			
			try
			{
				arena.setTime(Integer.parseInt(args[2]));
				arena.setTimeLocked(Boolean.parseBoolean(args[3]));
				arena.setRaining(Boolean.parseBoolean(args[4]));
				arena.save();
				CourseMessage.COMMAND_ARENA_WEATHER_DONE.say(sender);
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setlaps") || args[0].equalsIgnoreCase("tours"))
		{
			if(args.length < 3)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			
			try
			{
				arena.setLaps(Integer.parseInt(args[2]));
				arena.save();
				CourseMessage.COMMAND_ARENA_RACESETTINGS_DONE.say(sender);
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setauthor") || args[0].equalsIgnoreCase("auteur"))
		{
			if(args.length < 3)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			
			try
			{
				String author = args[2];
				
				for(int i = 3; i < args.length; i++)
					author += " " + args[i];
				
				arena.setAuthor(author);
				arena.save();
				CourseMessage.COMMAND_ARENA_AUTHOR_DONE.say(sender);
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setlimit") || args[0].equalsIgnoreCase("tempsmax"))
		{
			if(args.length < 3)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			
			try
			{
				arena.setRaceTimeLimit(Integer.parseInt(args[2]));
				arena.save();
				CourseMessage.COMMAND_ARENA_RACESETTINGS_DONE.say(sender);
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("setitemregendelay") || args[0].equalsIgnoreCase("délaiitems") || args[0].equalsIgnoreCase("setregendelay"))
		{
			if(args.length < 3)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			
			try
			{
				arena.setItemRegenDelay(Integer.parseInt(args[2]));
				arena.save();
				CourseMessage.COMMAND_ARENA_RACESETTINGS_DONE.say(sender);
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("addline") || args[0].equalsIgnoreCase("ajoutligne"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			if(args.length == 3)
			{
				if(Core.getWand().getPos1((Player) sender) == null || Core.getWand().getPos2((Player) sender) == null)
				{
					CourseMessage.COMMAND_INVALID_SELECTION.say(sender);
					return false;
				}
				
				try
				{
					arena.getLines().add(Integer.parseInt(args[2]), new Breakline((Player) sender));
				}
				catch(Exception e)
				{
					CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
					return false;
				}
			}
			else
				arena.getLines().add(new Breakline((Player) sender));
			
			arena.save();
			CourseMessage.COMMAND_ARENA_BREAKLINE_ADD.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("removeline") || args[0].equalsIgnoreCase("retraitligne") || args[0].equalsIgnoreCase("delline"))
		{
			if(args.length == 3)
			{
				try
				{
					arena.getLines().remove(Integer.parseInt(args[2]));
					arena.save();
					CourseMessage.COMMAND_ARENA_BREAKLINE_REMOVE.say(sender);
					return true;
				}
				catch(Exception e){ }
			}
			CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("testlines") || args[0].equalsIgnoreCase("testlignes"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			if(arena.getLines().size() == 0)
				CourseMessage.COMMAND_ARENA_RIDE_NOBREAKLINE.say(sender);
			else
				for(int i = 0; i < arena.getLines().size(); i++)
				{
					final int j = i;
					Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
					{
						@Override
						public void run()
						{
							if(((Player) sender).isOnline())
							{
								((Player) sender).teleport(arena.getLines().get(j).getCenter().setDirection(((Player) sender).getLocation().getDirection()));
								ActionBarUtil.sendActionMessage((Player) sender, "§eLine #" + j);
							}
						}
					}, i * 20);
				}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("tpline") || args[0].equalsIgnoreCase("lignetp"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			if(args.length == 3)
			{
				try
				{
					((Player) sender).teleport(arena.getLines().get(Integer.parseInt(args[2])).getCenter());
					CourseMessage.COMMAND_ARENA_BREAKLINE_TELEPORT.say(sender);
				}
				catch(Exception e){ }
			}
			CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("resetlines") || args[0].equalsIgnoreCase("réinitlignes"))
		{
			arena.getLines().clear();
			arena.save();
			CourseMessage.COMMAND_ARENA_BREAKLINE_RESET.say(sender);
			return true;
		}
		
		
		
		if(args[0].equalsIgnoreCase("addspawn") || args[0].equalsIgnoreCase("ajoutdépart"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			arena.getSpawnPoints().add(new SerializableLocation(((Player) sender).getLocation()));
			arena.save();
			CourseMessage.COMMAND_ARENA_ADDSPAWN.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("removespawn") || args[0].equalsIgnoreCase("retraitdépart") || args[0].equalsIgnoreCase("delspawn"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			SerializableLocation toRemove = null;
			for(SerializableLocation loc : arena.getSpawnPoints())
				if(loc.getLocation().distance(((Player) sender).getLocation()) < 1.5f)
					toRemove = loc;
			
			if(toRemove != null)
			{
				arena.getSpawnPoints().remove(toRemove);
				arena.save();
				CourseMessage.COMMAND_ARENA_REMOVESPAWN.say(sender);
			}
			else
				CourseMessage.COMMAND_ARENA_REMOVESPAWN_FAIL.say(sender);

			return true;
		}
		
		if(args[0].equalsIgnoreCase("resetspawns") || args[0].equalsIgnoreCase("réinitdéparts"))
		{
			arena.getSpawnPoints().clear();
			arena.save();
			CourseMessage.COMMAND_ARENA_RESETSPAWNS.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("showspawns") || args[0].equalsIgnoreCase("voirdéparts"))
		{
			for(SerializableLocation loc : arena.getSpawnPoints())
				loc.getWorld().spawnEntity(loc.getLocation(), EntityType.ARMOR_STAND);
					
			CourseMessage.COMMAND_ARENA_SHOWSPAWNS.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("addjukebox") || args[0].equalsIgnoreCase("ajoutjukebox"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			arena.getJukeboxes().add(new SerializableLocation(((Player) sender).getLocation()));
			arena.save();
			CourseMessage.COMMAND_ARENA_ADDJUKEBOX.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("removejukebox") || args[0].equalsIgnoreCase("retraitjukebox") || args[0].equalsIgnoreCase("deljukebox"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			SerializableLocation toRemove = null;
			for(SerializableLocation loc : arena.getJukeboxes())
				if(loc.getLocation().distance(((Player) sender).getLocation()) < 1.5f)
					toRemove = loc;
			
			if(toRemove != null)
			{
				arena.getJukeboxes().remove(toRemove);
				arena.save();
				CourseMessage.COMMAND_ARENA_REMOVEJUKEBOX.say(sender);
			}
			else
				CourseMessage.COMMAND_ARENA_REMOVEJUKEBOX_FAIL.say(sender);

			return true;
		}
		
		if(args[0].equalsIgnoreCase("resetjukeboxes") || args[0].equalsIgnoreCase("réinitjukeboxes"))
		{
			arena.getJukeboxes().clear();
			arena.save();
			CourseMessage.COMMAND_ARENA_RESETJUKEBOXES.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("playjukeboxes") || args[0].equalsIgnoreCase("jouerjukeboxes"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			for(SerializableLocation loc : arena.getJukeboxes())
				RecordUtil.playRecord((Player) sender, loc.getLocation(), Material.GREEN_RECORD);
					
			CourseMessage.COMMAND_ARENA_PLAYJUKEBOXES.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("addspectator") || args[0].equalsIgnoreCase("ajoutspectateur"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			arena.getSpectatorLocations().add(new SerializableLocation(((Player) sender).getLocation()));
			arena.save();
			CourseMessage.COMMAND_ARENA_ADDSPECTATOR.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("removespectator") || args[0].equalsIgnoreCase("retraitspectateur") || args[0].equalsIgnoreCase("delspectator"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			SerializableLocation toRemove = null;
			for(SerializableLocation loc : arena.getSpectatorLocations())
				if(loc.getLocation().distance(((Player) sender).getLocation()) < 1.5f)
					toRemove = loc;
			
			if(toRemove != null)
			{
				arena.getSpectatorLocations().remove(toRemove);
				arena.save();
				CourseMessage.COMMAND_ARENA_REMOVESPECTATOR.say(sender);
			}
			else
				CourseMessage.COMMAND_ARENA_REMOVESPECTATOR_FAIL.say(sender);

			return true;
		}
		
		if(args[0].equalsIgnoreCase("resetspectators") || args[0].equalsIgnoreCase("réinitspectateurs"))
		{
			arena.getSpectatorLocations().clear();
			arena.save();
			CourseMessage.COMMAND_ARENA_RESETSPECTATORS.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("showspectators") || args[0].equalsIgnoreCase("voirspectateurs"))
		{
			for(SerializableLocation loc : arena.getSpectatorLocations())
				loc.getWorld().spawnEntity(loc.getLocation(), EntityType.ARMOR_STAND);
					
			CourseMessage.COMMAND_ARENA_SHOWSPECTATORS.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("seticon") || args[0].equalsIgnoreCase("icon"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}

			Player player = (Player)sender;

			if(player.getItemInHand() == null)
			{
				CourseMessage.COMMAND_ARENA_ICON_NOITEM.say(sender);
				return false;
			}

			arena.setIcon(player.getItemInHand());
			arena.save();
			CourseMessage.COMMAND_ARENA_ICON_SET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("additem") || args[0].equalsIgnoreCase("ajoutitem"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			arena.getItems().add(new SerializableLocation(((Player) sender).getLocation()));
			arena.save();
			CourseMessage.COMMAND_ARENA_ADDITEM.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("removeitem") || args[0].equalsIgnoreCase("retraititem") || args[0].equalsIgnoreCase("delitem"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			SerializableLocation toRemove = null;
			for(SerializableLocation loc : arena.getItems())
				if(loc.getLocation().distance(((Player) sender).getLocation()) < 1.5f)
					toRemove = loc;
			
			if(toRemove != null)
			{
				arena.getItems().remove(toRemove);
				arena.save();
				CourseMessage.COMMAND_ARENA_REMOVEITEM.say(sender);
			}
			else
				CourseMessage.COMMAND_ARENA_REMOVEITEM_FAIL.say(sender);

			return true;
		}
		
		if(args[0].equalsIgnoreCase("resetitems") || args[0].equalsIgnoreCase("réinititems"))
		{
			arena.getItems().clear();
			arena.save();
			CourseMessage.COMMAND_ARENA_RESETITEMS.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("showitems") || args[0].equalsIgnoreCase("voiritems"))
		{
			for(SerializableLocation loc : arena.getItems())
				new ItemBox(loc.getLocation(), 0).spawn();
					
			CourseMessage.COMMAND_ARENA_SHOWITEMS.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("adddeathzone") || args[0].equalsIgnoreCase("ajoutzonemorte"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			SerializableRegion region = Core.getWand().getRegion((Player) sender);
			
			if(region != null)
			{
				arena.getDeathZones().add(region);
				arena.save();
				CourseMessage.COMMAND_ARENA_ADDDEATHZONE.say(sender);
				CourseMessage.REGIONWARNING.say(sender);
			}
			else
			{
				CourseMessage.COMMAND_INVALID_SELECTION.say(sender);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("removedeathzone") || args[0].equalsIgnoreCase("retraitzonemorte") || args[0].equalsIgnoreCase("deldeathzone"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			int count = 0;
			for(int i = 0; i < arena.getDeathZones().size(); i++)
				if(arena.getDeathZones().get(i).contains(((Player)sender).getLocation()))
				{
					arena.getDeathZones().remove(i--);
					count++;
				}
			arena.save();
			CourseMessage.COMMAND_ARENA_REMOVEDEATHZONE.say(sender, "#", "" + count);
		}
		
		if(args[0].equalsIgnoreCase("resetdeathzones") || args[0].equalsIgnoreCase("réinitzonesmortes"))
		{
			arena.getDeathZones().clear();
			arena.save();
			CourseMessage.COMMAND_ARENA_RESETDEATHZONES.say(sender);
			CourseMessage.REGIONWARNING.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("testdeathzones") || args[0].equalsIgnoreCase("testzonesmortes"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
			
			int count = 0;
			for(SerializableRegion region : arena.getDeathZones())
				if(region.contains(((Player)sender).getLocation()))
					count++;
			
			CourseMessage.COMMAND_ARENA_TESTDEATHZONES.say(sender, "#", "" + count);
		}
		
		if(args[0].equalsIgnoreCase("setspeed") || args[0].equalsIgnoreCase("vitesse"))
		{
			if(args.length < 4)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
			try
			{
				arena.getRoadSpeedModifier().put(new RoadType(args[2]), Double.parseDouble(args[3]));
				arena.save();
				CourseMessage.COMMAND_ARENA_SETSPEEDMODIFIER.say(sender);
				return true;
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
				return false;
			}
		}
		
		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 1)
			return TextUtil.getStringsThatStartWith(args[0], argList);
		
		if(args.length != 2)
			return null;
		
		if(args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("nouveau") || args[0].equalsIgnoreCase("nouvelle") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("créer"))
			return Arrays.asList("YourArena", "AwesomeRace", "RainbowRoad");
		
		List<String> string = new ArrayList<String>();
		
		for(Arena arena : Arena.getArenaList())
			string.add(arena.getName());
		
		return TextUtil.getStringsThatStartWith(args[1], string);
	}

}
