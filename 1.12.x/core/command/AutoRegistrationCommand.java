package me.winterguardian.core.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.winterguardian.core.message.ErrorMessage;

import me.winterguardian.core.message.HardcodedMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public abstract class AutoRegistrationCommand implements CommandExecutor, TabCompleter, CommandData
{
	private Plugin plugin;

	public void register(Plugin plugin)
	{
		if(this.plugin != null)
			return;

		this.plugin = plugin;
		try
		{
			PluginCommand cmd = createCommand(getName(), plugin);
			cmd.setExecutor(this);
			cmd.setTabCompleter(this);
			if(this.getPermission() != null)
			{
				cmd.setPermission(this.getPermission().getName());
				if(Bukkit.getPluginManager().getPermission(this.getPermission().getName()) == null)
					Bukkit.getPluginManager().addPermission(this.getPermission());
			}
			cmd.setPermissionMessage(this.getPermissionMessage());
			String usage = this.getUsage();

			if(usage == null)
				usage = "/" + getName();

			cmd.setUsage(usage);
			cmd.setDescription(this.getDescription() == null ? "No description" : this.getDescription());
			cmd.setAliases(this.getAliasesWithName());
			getCommandMap().register(plugin.getDescription().getName(), cmd);

			if(getOtherPermissions() != null)
				for(Permission perm : getOtherPermissions())
					if(perm != null)
						if(Bukkit.getPluginManager().getPermission(perm.getName()) == null)
							Bukkit.getPluginManager().addPermission(perm);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void unregister()
	{
		if(plugin == null)
			return;

		try
		{
			Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
			if(!field.isAccessible())
				field.setAccessible(true);

			Map<String, Command> knownCommands = (Map<String, Command>)field.get(getCommandMap());

			for(String key : new ArrayList<>(knownCommands.keySet()))
				if(getAliasesWithName().contains(key) || getAliasesWithName().contains(this.plugin.getDescription().getName().toLowerCase() + ":" + key))
					knownCommands.remove(key);

			field.set(getCommandMap(), knownCommands);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		this.plugin = null;
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

	@Override
	public String getPermissionMessage()
	{
		return ErrorMessage.COMMAND_INVALID_PERMISSION.toString();
	}

	@Deprecated
	public List<String> getAlisases()
	{
		return null;
	}

	@Override
	public List<String> getAliases()
	{
		return getAlisases();
	}

	public List<String> getAliasesWithName()
	{
		List<String> aliases = new ArrayList<>(this.getAliases() != null ? this.getAliases() : new ArrayList<String>());
		if(!aliases.contains(this.getName()))
			aliases.add(this.getName());
		return aliases;
	}

	public Collection<Permission> getOtherPermissions()
	{
		return null;
	}

	public static PluginCommand createCommand(String name, Plugin plugin)
	{
		try
		{
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);

			return c.newInstance(name, plugin);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static SimpleCommandMap getCommandMap()
	{
		try
		{
			Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
			f.setAccessible(true);

			return (SimpleCommandMap) f.get(Bukkit.getPluginManager());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
