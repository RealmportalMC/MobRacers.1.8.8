package me.winterguardian.core.particle;

import java.util.Collection;
import java.util.HashMap;

import me.winterguardian.core.util.ReflectionUtil;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtil
{
	public static HashMap<Player, ParticleData> followParticlesInAir = new HashMap<Player, ParticleData>();
	
	private ParticleUtil() {}
	
	public static void playParticle(Player p, String particleName, boolean far, float x, float y, float z, float offsetX, float offsetY, float offsetZ, float speed, int amount, int... data)
	{
		try
		{
			Class<?> enumParticleClass = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".EnumParticle");
			Object particleType = enumParticleClass.getDeclaredMethod("valueOf", String.class).invoke(null, particleName);
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutWorldParticles").
					getConstructor(enumParticleClass, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class).
					newInstance(particleType, far, x, y, z, offsetX, offsetY, offsetZ, speed, amount, data);
			
			ReflectionUtil.sendPacket(p, packet);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void playParticle(Collection<? extends Player> players, ParticleType type, Location loc, Vector offset, float speed, int amount, int... data)
	{
		for(Player p : players)
			playParticle(p, type.name(), p.getLocation().distance(loc) > 256, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), (float)offset.getX(), (float)offset.getY(), (float)offset.getZ(), speed, amount, data);
	}
	
	public static void playSimpleParticles(Player p, Location loc, ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	{
		if(p.getWorld() != loc.getWorld())
			return;
		
		playParticle(p, type.name(), p.getLocation().distance(loc) > 256, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), offsetX, offsetY, offsetZ, speed, amount, null);
	}
	
	public static void playSimpleParticles(Location loc, ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount)
	{
		for(Player p : loc.getWorld().getPlayers())
			playSimpleParticles(p, loc, type, offsetX, offsetY, offsetZ, speed, amount);
	}
	
	public static void playBlockParticles(Player p, Location loc, ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount, int blockId, int data)
	{
		if(p.getWorld() != loc.getWorld())
			return;
		
		playParticle(p, type.name(), p.getLocation().distance(loc) > 256, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), offsetX, offsetY, offsetZ, speed, amount, getParticleBlockData(blockId, data));
	}
	
	public static void playBlockParticles(Location loc, ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount, int blockId, int data)
	{
		for(Player p : loc.getWorld().getPlayers())
			playBlockParticles(p, loc, type, offsetX, offsetY, offsetZ, speed, amount, blockId, data);
	}
	
	@Deprecated
	public static void followPlayerInAir(Player p, ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount) { }
	
	public static int getParticleBlockData(int blockId, int data)
	{
		return blockId + (data << 12);
	}
}
