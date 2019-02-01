package me.winterguardian.core.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.winterguardian.core.Component;

import me.winterguardian.core.Core;
import me.winterguardian.core.DynamicComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 *
 * Created by Alexander Winter on 2015-11-17.
 */
public class BungeeMessager extends DynamicComponent implements PluginMessageListener
{
	private String serverName;
	private List<String> servers;
	private HashMap<String, Integer> playerCount;
	private boolean safe;

	public BungeeMessager()
	{
		this.playerCount = new HashMap<>();
		this.serverName = "---";
		this.servers = new ArrayList<>();
		this.safe = true;
	}

	@Override
	public void register(Plugin plugin)
	{
		getPlugin().getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
		getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	}

	@Override
	public void unregister(Plugin plugin)
	{
		plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, "BungeeCord");
		plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, "BungeeCord");
	}

	public boolean isSafe()
	{
		return safe;
	}

	public void setSafe(boolean safe)
	{
		this.safe = safe;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes)
	{
		if(!channel.equals("BungeeCord"))
			return;

		ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
		String subChannel = in.readUTF();
		
		if(subChannel.equals("ExecuteConsole") && !safe)
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), in.readUTF());
			return;
		}

		if(subChannel.equals("PlayerCount"))
		{
			playerCount.put(in.readUTF(), in.readInt());
			return;
		}
		
		if(subChannel.equals("GetServer"))
		{
			serverName = in.readUTF();
			return;
		}
		
		if(subChannel.equals("GetServers"))
		{
			servers = Arrays.asList(in.readUTF().split(", "));
		}
	}

	public void sendToServer(Player player, String server)
	{
		if(Core.getUserDatasManager().isEnabled())
			Core.getUserDatasManager().asyncSaveUserData(player);

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);

		player.sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}

	public void sendMessage(String player, String message)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(player);
		out.writeUTF(message);

		getPlugin().getServer().getOnlinePlayers().iterator().next().
				sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}
	
	public void executeEverywhere(String command)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		executeEverywhereElse(command);
	}
	
	public void executeEverywhereElse(String command)
	{
		execute("ALL", command);
	}
	
	public void execute(String server, String command)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward"); 
		out.writeUTF(server);
		out.writeUTF("ExecuteConsole"); 
		out.writeUTF(command); 
		
		getPlugin().getServer().getOnlinePlayers().iterator().next().
			sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}
	
	public void requestServerList()
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		
		getPlugin().getServer().getOnlinePlayers().iterator().next().
			sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}
	
	public void requestServerName()
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");
		
		getPlugin().getServer().getOnlinePlayers().iterator().next().
			sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}
	
	public void requestPlayerCount(String server)
	{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);

		getPlugin().getServer().getOnlinePlayers().iterator().next().
				sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
	}
	
	public int getPlayerCount(String server, int defaultValue)
	{
		if(playerCount.containsKey(server))
			return playerCount.get(server);
		
		return defaultValue;
	}
	
	public String getServerName()
	{
		return serverName;
	}
	
	public List<String> getServers()
	{
		return servers;
	}
}
