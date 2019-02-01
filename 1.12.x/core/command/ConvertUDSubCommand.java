package me.winterguardian.core.command;

import me.winterguardian.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

/**
 * Used by MobRacers and BlockFarmers to convert sub commands
 *
 * Created by Alexander Winter on 2015-11-30.
 */
public class ConvertUDSubCommand extends SubCommand
{
	public ConvertUDSubCommand(Permission permission)
	{
		super("convert", Arrays.asList("convertUD", "convertuserdatas"), permission, "§cYou are missing permission " + permission.getName() + " to do that.", "/<plugin> convert");
	}

	@Override
	public boolean onSubCommand(final CommandSender sender, String label, String[] args)
	{
		Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugins()[0], new Runnable()
		{
			@Override
			public void run()
			{
				sender.sendMessage("§aConvertion started, please wait.");
				sender.sendMessage("§eFinished. If there's no error in the console, everything should be fine.");
			}
		});
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
}
