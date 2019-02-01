package me.winterguardian.core.playerstats;

import me.winterguardian.core.Component;
import me.winterguardian.core.util.TextUtil;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * Created by Alexander Winter on 2015-11-04.
 */
public class UserDatasManager implements Listener, Component
{
	
	private Set<Plugin> plugins;

	private Map<UUID, MappedData> loadedUserDatas;
	private UserDataLoader loader;
	private boolean async;

	public UserDatasManager()
	{
		this.plugins = new HashSet<>();
		this.loadedUserDatas = new HashMap<>();
		this.async = true;
	}
	
	public void enableYaml(Plugin plugin)
	{
		enable(plugin, new YamlUserDataLoader(new File(plugin.getServer().getWorldContainer(), "userdatas/")));
	}

	public void enableYaml(Plugin plugin, String directory)
	{
		enable(plugin, new YamlUserDataLoader(new File(directory)));
	}

	public void enableDB(Plugin plugin, String driver, String url, String user, String password)
	{
		DBUserDataLoader loader = new DBUserDataLoader(driver, url, user, password);
		loader.getTables().put("core",
				"points INTEGER, " +
				"lastlogin BIGINT, " +
				"lastlogout BIGINT, " +
				"firstconnect BIGINT, " +
				"name VARCHAR(16), " +
				"ip VARCHAR(16)");
		enable(plugin, loader);
	}

	public void enableDB(Plugin plugin, String driver, String url, String user, String password, HashMap<String, String> tables)
	{
		DBUserDataLoader loader = new DBUserDataLoader(driver, url, user, password);
		loader.getTables().put("core",
				"points INTEGER, " +
				"lastlogin BIGINT, " +
				"lastlogout BIGINT, " +
				"firstconnect BIGINT, " +
				"name VARCHAR(16), " +
				"ip VARCHAR(16)");
		loader.getTables().putAll(tables);
		enable(plugin, loader);
	}

	public void enable(Plugin plugin, UserDataLoader loader)
	{
		this.plugins.add(plugin);

		if(plugins.size() > 1)
		{
			this.loader.merge(loader);
			return;
		}
		
		Bukkit.getPluginManager().registerEvents(this, plugin);

		this.loader = loader;
		this.loader.init();

		for(Player p : plugin.getServer().getOnlinePlayers())
			Bukkit.getScheduler().runTask(plugin, new JoinTask(p));
	}

	@Override
	public void enable(Plugin plugin, Object... args)
	{
		if(args.length < 1 || !(args[0] instanceof UserDataLoader))
			return;

		enable(plugin, (UserDataLoader)args[0]);
	}

	@Override
	public void disable(Plugin plugin)
	{
		if(!this.plugins.contains(plugin))
			return;
	
		this.plugins.remove(plugin);

		if(plugins.size() == 0)
		{
			for(Player p : plugin.getServer().getOnlinePlayers())
				new LeaveTask(p).run();

			this.loader = null;
		}
		
		HandlerList.unregisterAll(this);
		if(plugins.size() > 0)
		{
			try
			{
				Bukkit.getPluginManager().registerEvents(this, plugins.iterator().next());
			}
			catch(Exception e) { }
		}
			
	}

	@Override
	public boolean isEnabled()
	{
		return plugins.size() != 0;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void join(PlayerJoinEvent event)
	{
		if(plugins.size() == 0)
			return;

		if(isAsync())
			Bukkit.getScheduler().runTaskAsynchronously(plugins.iterator().next(), new JoinTask(event.getPlayer()));
		else
			Bukkit.getScheduler().runTask(plugins.iterator().next(), new JoinTask(event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void leave(PlayerQuitEvent event)
	{
		if(plugins.size() == 0)
			return;

		if(isAsync())
			Bukkit.getScheduler().runTaskAsynchronously(plugins.iterator().next(), new LeaveTask(event.getPlayer()));
		else
			Bukkit.getScheduler().runTask(plugins.iterator().next(), new LeaveTask(event.getPlayer()));
	}

	public boolean isAsync()
	{
		return async;
	}

	public void setAsync(boolean async)
	{
		this.async = async;
	}

	private class JoinTask implements Runnable
	{
		private Player player;

		public JoinTask(Player player)
		{
			this.player = player;
		}

		@Override
		public void run()
		{
			MappedData data = loader.load(player.getUniqueId());
			new PlayerStats(data).join(player);
			loadedUserDatas.put(player.getUniqueId(), data);
		}
	}

	private class LeaveTask implements Runnable
	{
		private Player player;

		public LeaveTask(Player player)
		{
			this.player = player;
		}

		@Override
		public void run()
		{
			MappedData data = loadedUserDatas.get(player.getUniqueId());

			if(data == null)
			{
				new Exception("Player " + player.getName() + " left the server without any userdata").printStackTrace();
				return;
			}

			new PlayerStats(data).leave(player);
			loader.save(player.getUniqueId(), data);
			loadedUserDatas.remove(player.getUniqueId());
		}
	}

	public UserDataLoader getLoader()
	{
		return this.loader;
	}

	public MappedData getUserData(Player player)
	{
		return getUserData((OfflinePlayer)player);
	}

	public MappedData getUserData(OfflinePlayer player)
	{
		if(loadedUserDatas.containsKey(player.getUniqueId()))
			return this.loadedUserDatas.get(player.getUniqueId());

		return loadUserData(player.getUniqueId());
	}

	public boolean isLoaded(OfflinePlayer player)
	{
		return loadedUserDatas.containsKey(player.getUniqueId());
	}

	public MappedData loadUserData(UUID id)
	{
		return this.loader.load(id);
	}

	public void saveUserData(UUID id, MappedData data)
	{
		loader.save(id, data);
	}

	public void asyncSaveUserData(final Player player)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugins.iterator().next(), new Runnable()
		{
			@Override
			public void run()
			{
				saveUserData(player.getUniqueId(), loadedUserDatas.get(player.getUniqueId()));
			}
		});
	}

	public void asyncSaveUserData(final UUID id, final MappedData data)
	{
		Bukkit.getScheduler().runTaskAsynchronously(this.plugins.iterator().next(), new Runnable()
		{
			@Override
			public void run()
			{
				saveUserData(id, data);
			}
		});
	}

	public Entry<UUID, MappedData> getFirstByValue(String path, Object value)
	{
		return this.loader.getFirstByValue(path, value);
	}
	
	public Map<UUID, MappedData> getByValue(String path, Object value)
	{
		return this.loader.getByValue(path, value);
	}
}
