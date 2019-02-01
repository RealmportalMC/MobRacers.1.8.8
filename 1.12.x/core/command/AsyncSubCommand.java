package me.winterguardian.core.command;

import java.util.List;

import me.winterguardian.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public abstract class AsyncSubCommand extends SubCommand
{
	private Plugin plugin;

	public AsyncSubCommand(Plugin plugin, String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		super(name, aliases, permission, permissionMessage, usage);
		this.plugin = plugin;
	}


	protected abstract void onAsyncSubCommand(CommandSender sender, String label, String[] args);
	protected void onAsyncSubTabComplete(CommandSender sender, String label, String[] args) { }
	protected List<String> getTabCompleteValue() { return null; }


	@Override
	public boolean onSubCommand(final CommandSender sender, final String label, final String[] args)
	{
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				onAsyncSubCommand(sender, label, args);
			}
		});
		return true;
	}

	@Override
	public List<String> onSubTabComplete(final CommandSender sender, final String label, final String[] args)
	{
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				onAsyncSubTabComplete(sender, label, args);
			}
			
		});
		return getTabCompleteValue();
	}
}
