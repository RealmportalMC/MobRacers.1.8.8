package me.winterguardian.core.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class AsyncCommand extends AutoRegistrationCommand
{
	private Plugin plugin;
	
	public AsyncCommand(Plugin plugin)
	{
		this.plugin = plugin;
	}

	protected abstract void onAsyncCommand(CommandSender sender, String label, String[] args);
	protected void onAsyncTabComplete(CommandSender sender, String label, String[] args) { }
	protected List<String> getTabCompleteValue() { return null; }


	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, final String label, final String[] args)
	{
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				onAsyncCommand(sender, label, args);
			}
			
		});
		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, Command cmd, final String label, final String[] args)
	{
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				onAsyncTabComplete(sender, label, args);
			}
			
		});
		return getTabCompleteValue();
	}
}
