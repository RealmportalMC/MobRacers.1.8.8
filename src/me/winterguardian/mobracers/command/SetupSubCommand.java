package me.winterguardian.mobracers.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.entity.EntityUtil;
import me.winterguardian.core.util.MathUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.MobRacersSetup;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;

public class SetupSubCommand extends SubCommand
{
	private MobRacersGame game;
	
	public SetupSubCommand(MobRacersGame game)
	{
		super("setup", Arrays.asList("settings", "option", "options", "réglages", "reglages", "paramètres", "parametres", "config", "configuration"), MobRacersPlugin.ADMIN, CourseMessage.COMMAND_INVALID_PERMISSION.toString(), "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers setup");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length == 0)
		{
			CourseMessage.COMMAND_CONFIG_MENU.say(sender);
			
			String lobbyStatus = CourseMessage.COMMAND_CONFIG_MENU_UNDEFINED.toString();
			
			if(game.getSetup().getLobby() != null)
				lobbyStatus = CourseMessage.COMMAND_CONFIG_MENU_LOCATION.toString().
				replace("<world>", game.getSetup().getLobby().getWorld().getName()).
				replace("<x>", "" + MathUtil.round(game.getSetup().getLobby().getX(), 1)).
				replace("<y>", "" + MathUtil.round(game.getSetup().getLobby().getY(), 1)).
				replace("<z>", "" + MathUtil.round(game.getSetup().getLobby().getZ(), 1));
			
			sender.sendMessage(CourseMessage.COMMAND_CONFIG_MENU_LOBBY.toString().replace("<status>", lobbyStatus));
			
			String exitStatus = CourseMessage.COMMAND_CONFIG_MENU_UNDEFINED.toString();
			
			if(game.getSetup().getExit() != null)
				exitStatus = CourseMessage.COMMAND_CONFIG_MENU_LOCATION.toString(
				"<world>", game.getSetup().getLobby().getWorld().getName(),
				"<x>", "" + MathUtil.round(game.getSetup().getExit().getX(), 1),
				"<y>", "" + MathUtil.round(game.getSetup().getExit().getY(), 1),
				"<z>", "" + MathUtil.round(game.getSetup().getExit().getZ(), 1));
			
			CourseMessage.COMMAND_CONFIG_MENU_EXIT.say(sender, "<status>", exitStatus);
			
			String regionStatus = CourseMessage.COMMAND_CONFIG_MENU_UNDEFINED.toString();
			
			if(game.getSetup().getRegion() != null)
				regionStatus = CourseMessage.COMMAND_CONFIG_MENU_DEFINED.toString();
			
			sender.sendMessage(CourseMessage.COMMAND_CONFIG_MENU_REGION.toString().replace("<status>", regionStatus));
	
			sender.sendMessage(CourseMessage.COMMAND_CONFIG_MENU_VEHICLES.toString("<status>", "§f(" + ((MobRacersSetup) game.getSetup()).getVehicleLocations().size() + " / " + VehicleType.values().length + ")"));
			CourseMessage.COMMAND_CONFIG_MENU_WARNING.say(sender);
			if(game.isReady())
				CourseMessage.COMMAND_CONFIG_MENU_READY.say(sender);
			else if(game.isLobbyReady())
				CourseMessage.COMMAND_CONFIG_MENU_ALMOSTREADY.say(sender);
			else
			{
				sender.sendMessage("");
				CourseMessage.COMMAND_CONFIG_MENU_NOTREADY.say(sender);
				
				if(game.getSetup().getRegion() == null)
					CourseMessage.COMMAND_CONFIG_MENU_NOTREADY_NOREGION.say(sender);
				
				if(game.getSetup().getLobby() == null)
					CourseMessage.COMMAND_CONFIG_MENU_NOTREADY_NOLOBBY.say(sender);
				
				if(game.getSetup().getExit() == null && !game.getConfig().isAutoJoin() && !((MobRacersConfig) game.getConfig()).keepPlayerStates())
					CourseMessage.COMMAND_CONFIG_MENU_NOTREADY_NOEXIT.say(sender);
				
				if(((MobRacersSetup) game.getSetup()).getVehicleLocations().size() == 0)
					CourseMessage.COMMAND_CONFIG_MENU_NOTREADY_NOVEHICLES.say(sender);
				
				if(((MobRacersSetup) game.getSetup()).isBadRegion())
					CourseMessage.COMMAND_CONFIG_MENU_NOTREADY_BADREGION.say(sender);
			}
			sender.sendMessage("");
			CourseMessage.COMMAND_CONFIG_MENU_HELP.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("help"))
		{
			CourseMessage.COMMAND_CONFIG_HELP1.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP2.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP3.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP4.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP5.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP6.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP7.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP8.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP9.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP10.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP11.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP12.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP13.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP14.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP15.say(sender);
			CourseMessage.COMMAND_CONFIG_HELP16.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("permission")
				|| args[0].equalsIgnoreCase("perm")
				|| args[0].equalsIgnoreCase("permissions")
				|| args[0].equalsIgnoreCase("perms"))
		{
			CourseMessage.COMMAND_CONFIG_PERMISSION_PLAY.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_VOTE.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_VIP_FULL.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_VIP_VEHICLE.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_VIP_MUSIC.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_STAFF.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_CMDBYPASS.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_AUTOJOINBYPASS.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_ADMIN.say(sender);
			CourseMessage.COMMAND_CONFIG_PERMISSION_ALLUNLOCKED.say(sender);

			return true;
		}
		
		if(args[0].equalsIgnoreCase("setlobby"))
		{
			if(sender instanceof Player)
			{
				game.getSetup().setLobby(((Player) sender).getLocation());
				CourseMessage.COMMAND_CONFIG_LOBBYSET.say(sender);
				return true;
			}
			else
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
		}
		
		if(args[0].equalsIgnoreCase("setexit"))
		{
			if(sender instanceof Player)
			{
				game.getSetup().setExit(((Player) sender).getLocation());
				CourseMessage.COMMAND_CONFIG_EXITSET.say(sender);
				return true;
			}
			else
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
		}
		
		if(args[0].equalsIgnoreCase("setregion"))
		{
			if(sender instanceof Player)
			{
				SerializableRegion region = Core.getWand().getRegion((Player) sender);
				
				if(region == null)
				{
					CourseMessage.COMMAND_INVALID_SELECTION.say(sender);
					return true;
				}
				
				game.getSetup().setRegion(region);
				CourseMessage.COMMAND_CONFIG_REGIONSET.say(sender);
				CourseMessage.REGIONWARNING.say(sender);
				return true;
			}
			else
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
		}
		
		if(args[0].equalsIgnoreCase("vehiclelist"))
		{
			CourseMessage.COMMAND_CONFIG_VEHICLELIST.say(sender);
			for(VehicleType type : VehicleType.values())
				sender.sendMessage("  " + (((MobRacersSetup) game.getSetup()).getVehicleLocations().containsKey(type) ? "§a" : "§c") + type.name().toLowerCase().replaceAll("_", "-"));
			return true;
		}
		
		if(args.length < 1)
		{
			CourseMessage.COMMAND_INVALID_ARGUMENT.say(sender);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("setvehicle"))
		{
			if(sender instanceof Player)
			{
				
				VehicleType type;
				try
				{	
					type = VehicleType.valueOf(args[1].toUpperCase().replaceAll("-", "_"));
				}
				catch(Exception e)
				{	
					CourseMessage.COMMAND_CONFIG_INVALIDVEHICLE.say(sender);
					return false;
				}
				((MobRacersSetup) game.getSetup()).getVehicleLocations().put(type, new SerializableLocation(((Player) sender).getLocation()));
				CourseMessage.COMMAND_CONFIG_VEHICLESET.say(sender);
				return true;
				
			}
			else
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
		}
		
		if(args[0].equalsIgnoreCase("resetvehicles"))
		{
			((MobRacersSetup) game.getSetup()).getVehicleLocations().clear();
			CourseMessage.COMMAND_CONFIG_VEHICLESET.say(sender);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("testvehicles"))
		{
			for(VehicleType type : ((MobRacersSetup) MobRacersPlugin.getGame().getSetup()).getVehicleLocations().keySet())
			{
				Vehicle vehicle = type.createNewVehicle();
				vehicle.spawn(((MobRacersSetup) game.getSetup()).getVehicleLocations().get(type).getLocation(), null);
				Entity entity = vehicle.getEntity();
				if(entity == null)
					continue;
				EntityUtil.setNoAI(entity, true);
				EntityUtil.setYawPitch(game.getPlayers(), entity, ((MobRacersSetup) game.getSetup()).getVehicleLocations().get(type).getLocation().getYaw(), ((MobRacersSetup) game.getSetup()).getVehicleLocations().get(type).getLocation().getPitch());
				
				
				if(entity.getCustomName() == null)
				{
					entity.setCustomName(vehicle.getName());
					entity.setCustomNameVisible(true);
				}
				
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("killvehicles"))
		{
			for(Entity entity : game.getSetup().getRegion().getEntities())
			{
				if(entity instanceof Player || entity instanceof Hanging)
					continue;
				
				entity.remove();
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("tovehicle") || args[0].equalsIgnoreCase("tpvehicle"))
		{
			if(sender instanceof Player)
			{	VehicleType type = VehicleType.valueOf(args[1].toUpperCase().replaceAll("-", "_"));
				if(type == null)
				{	CourseMessage.COMMAND_CONFIG_INVALIDVEHICLE.say(sender);
					return false;
				}
				SerializableLocation serialLoc = ((MobRacersSetup) game.getSetup()).getVehicleLocations().get(type);
				
				if(serialLoc == null)
				{	CourseMessage.COMMAND_CONFIG_INVALIDVEHICLE.say(sender);
					return false;
				}
				
				((Player) sender).teleport(serialLoc.getLocation());
				return true;
			}
			else
			{	CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return false;
			}
		}

		if(args[0].equalsIgnoreCase("removevehicle"))
		{
			VehicleType type;
			try
			{
				type = VehicleType.valueOf(args[1].toUpperCase().replaceAll("-", "_"));
			}
			catch(Exception e)
			{
				CourseMessage.COMMAND_CONFIG_INVALIDVEHICLE.say(sender);
				return false;
			}
			((MobRacersSetup) game.getSetup()).getVehicleLocations().remove(type);
			CourseMessage.COMMAND_CONFIG_VEHICLEREMOVED.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setleaveitem"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}

			Player player = (Player)sender;

			if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
			{
				CourseMessage.COMMAND_CONFIG_NOITEM.say(sender);
				return true;
			}

			((MobRacersSetup)game.getSetup()).setLeaveItem(player.getItemInHand());
			CourseMessage.COMMAND_CONFIG_ITEMSET.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setarenaitem"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}

			Player player = (Player)sender;

			if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
			{
				CourseMessage.COMMAND_CONFIG_NOITEM.say(sender);
				return true;
			}

			((MobRacersSetup)game.getSetup()).setArenaItem(player.getItemInHand());
			CourseMessage.COMMAND_CONFIG_ITEMSET.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setvehicleitem"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}

			Player player = (Player)sender;

			if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
			{
				CourseMessage.COMMAND_CONFIG_NOITEM.say(sender);
				return true;
			}

			((MobRacersSetup)game.getSetup()).setVehicleItem(player.getItemInHand());
			CourseMessage.COMMAND_CONFIG_ITEMSET.say(sender);
			return true;
		}

		if(args[0].equalsIgnoreCase("setspectatoritem"))
		{
			if(!(sender instanceof Player))
			{
				CourseMessage.COMMAND_INVALID_SENDER.say(sender);
				return true;
			}

			Player player = (Player)sender;

			if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
			{
				CourseMessage.COMMAND_CONFIG_NOITEM.say(sender);
				return true;
			}

			((MobRacersSetup)game.getSetup()).setSpectatorItem(player.getItemInHand());
			CourseMessage.COMMAND_CONFIG_ITEMSET.say(sender);
			return true;
		}

		return false;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
			return TextUtil.getStringsThatStartWith(args[0], Arrays.asList("setlobby", "setexit", "setregion", "vehiclelist", "setvehicle", "tpvehicle", "resetvehicles", "testvehicles", "killvehicles", "removevehicle", "permissions", "setleaveitem", "setarenaitem", "setvehicleitem", "setspectatoritem"));

		if(args.length == 2 && (args[0].equalsIgnoreCase("setvehicle") || args[0].equalsIgnoreCase("tpvehicle")) || args[0].equalsIgnoreCase("removevehicle"))
		{
			ArrayList<String> vehicleList = new ArrayList<>();

			for(VehicleType type : VehicleType.values())
				vehicleList.add(type.name().toLowerCase().replaceAll("_", "-"));

			return TextUtil.getStringsThatStartWith(args[1], vehicleList);
		}
		return null;
	}

}
