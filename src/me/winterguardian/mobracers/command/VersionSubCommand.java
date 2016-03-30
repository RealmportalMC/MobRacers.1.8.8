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
		
		
		sender.sendMessage("§f§lMob§2§lRacers §f§l> §fPlugin designed by WinterGuardian and now Maintained and updated by MajorProbes ");
		sender.sendMessage("§6Version: §r" + MobRacersPlugin.getPlugin().getDescription().getVersion());
		sender.sendMessage(version);
		
		if(sender instanceof Player)
			JsonUtil.sendJsonMessage((Player) sender, JsonUtil.toJson("§3Click me to visit the spigot MobRacers page.", null, null, "open_url", "\"https://www.spigotmc.org/resources/mobracers-mariokart-on-mobs-now-free-1-8-8.20626//\""));
	
	}

	public static String getLatestVersion()
	{
		try
		{
			return version = WebCommunicationUtil.post("http://www.spigotmc.org/api/general.php", ImmutableMap.of("resource", "20626"));
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
