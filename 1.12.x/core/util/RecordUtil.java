package me.winterguardian.core.util;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RecordUtil
{
	private RecordUtil(){ }
	
	@SuppressWarnings("deprecation")
	public static void playRecord(Player p, Location loc, Material record)
	{
		playRecord(p, loc, record.getId(), false);
	}
	
	public static void playRecord(Player player, Location loc, int id, boolean param)
	{
		try
		{
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutWorldEvent").newInstance();
			
			Field type = packet.getClass().getDeclaredField("a");
			if(!type.isAccessible())
				type.setAccessible(true);
			type.set(packet, 1005);
			
			Field position = packet.getClass().getDeclaredField("b");
			if(!position.isAccessible())
				position.setAccessible(true);
			position.set(packet, Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".BlockPosition").getDeclaredConstructor(double.class, double.class, double.class).newInstance(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
			
			Field record = packet.getClass().getDeclaredField("c");
			if(!record.isAccessible())
				record.setAccessible(true);
			record.set(packet, id);
			
			Field bool = packet.getClass().getDeclaredField("d");
			if(!bool.isAccessible())
				bool.setAccessible(true);
			bool.set(packet, param);
					
			ReflectionUtil.sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void stopRecord(Player p, Location loc)
	{
		playRecord(p, loc, 0, false);
	}
}
