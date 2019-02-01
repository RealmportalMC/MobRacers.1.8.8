package me.winterguardian.core.util;

import java.lang.reflect.Field;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundEffect
{
	private String sound;
	private float volume, pitch;
	
	public SoundEffect(String vanillaName, float volume, float pitch)
	{
		this.sound = vanillaName;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public SoundEffect(Sound bukkitSound, float volume, float pitch)
	{
		this.sound = getSound(bukkitSound);
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public void play(Player p)
	{
		play(p, p.getEyeLocation());
	}
	
	public void play(Player player, Location loc)
	{
		if(player == null || loc == null)
			return;
		
		try
		{
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutNamedSoundEffect").newInstance();
			
			Field content = packet.getClass().getDeclaredField("a");
			if(!content.isAccessible())
				content.setAccessible(true);
			content.set(packet, this.sound);

			Field x = packet.getClass().getDeclaredField("b");
			if(!x.isAccessible())
				x.setAccessible(true);
			x.set(packet, ((int)(loc.getX() * 8D)));
					
			Field y = packet.getClass().getDeclaredField("c");
			if(!y.isAccessible())
				y.setAccessible(true);
			y.set(packet, ((int)(loc.getY() * 8D)));
			
			Field z = packet.getClass().getDeclaredField("d");
			if(!z.isAccessible())
				z.setAccessible(true);
			z.set(packet, ((int)(loc.getZ() * 8D)));
			
			Field volume = packet.getClass().getDeclaredField("e");
			if(!volume.isAccessible())
				volume.setAccessible(true);
			volume.set(packet, this.volume);
			
			Field pitch = packet.getClass().getDeclaredField("f");
			if(!pitch.isAccessible())
				pitch.setAccessible(true);
			pitch.set(packet, ((int)(this.pitch * 63.0F)));
			
			ReflectionUtil.sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void play(Collection<Player> players)
	{
		for(Player p : players)
			this.play(p);
	}
	
	public void play(Collection<Player> players, Location loc)
	{
		for(Player p : players)
			this.play(p, loc);
	}
	
	public static String getSound(Sound bukkitSound)
	{
		try
		{
			return (String)Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.getVersion() + ".CraftSound").getDeclaredMethod("getSound", Sound.class).invoke(null, bukkitSound);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		} 
	}
}
