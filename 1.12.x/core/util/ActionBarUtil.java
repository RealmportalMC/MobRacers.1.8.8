package me.winterguardian.core.util;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ActionBarUtil
{
	private static HashMap<Player, Integer> playersTasks = new HashMap<Player, Integer>();
	
	private ActionBarUtil(){}
	
	public static void sendActionMessage(Player player, String message)
	{
		try
		{
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutChat").newInstance();
			
			Field content = packet.getClass().getDeclaredField("a");
			if(!content.isAccessible())
				content.setAccessible(true);
			content.set(packet, ReflectionUtil.getSerialized("{text:\"" + message + "\"}"));

			Field data = packet.getClass().getDeclaredField("b");
			if(!data.isAccessible())
				data.setAccessible(true);
			data.set(packet, (byte)2);
					
			ReflectionUtil.sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void sendActionMessageContinuously(final Player p, final String message, Plugin plugin)
	{
		if(p != null)
		{
			if(playersTasks.containsKey(p))
				Bukkit.getScheduler().cancelTask(playersTasks.remove(p));
			
			playersTasks.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
			{
	
				@Override
				public void run()
				{
					if(p.isOnline())
						sendActionMessage(p, message);
					else
						clear(p);
				}
				
			}
			, 0, 40));
		}
	}
	
	public static void clear(Player p)
	{
		if(playersTasks.containsKey(p))
			Bukkit.getScheduler().cancelTask(playersTasks.remove(p));
		
		sendActionMessage(p, "");
	}
}
