package me.winterguardian.core.particle;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleData
{
	private ParticleType type;
	private float offsetX, offsetY, offsetZ, speed;
	private int amount;
	private int[] data;
	
	public ParticleData(ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount, int[] data)
	{
		this.type = type;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.speed = speed;
		this.amount = amount;
		this.data = data;
	}
	
	@Deprecated
	public ParticleData(ParticleType type, float offsetX, float offsetY, float offsetZ, float speed, int amount, Collection<int[]> datas)
	{
		this.type = type;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.speed = speed;
		this.amount = amount;
		this.data = (int[]) datas.toArray()[0];
	}
	
	public void apply(Location loc)
	{
		ParticleUtil.playParticle(loc.getWorld().getPlayers(), type, loc, new Vector(offsetX, offsetY, offsetZ), speed, amount, data);
	}
}