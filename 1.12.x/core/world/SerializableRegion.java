package me.winterguardian.core.world;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


public class SerializableRegion extends Region
{
	private SerializableLocation loc1;
	private SerializableLocation loc2;
	
	public SerializableRegion(SerializableRegion region)
	{
		this.loc1 = region.loc1;
		this.loc2 = region.loc2;
	}
	
	public SerializableRegion(SerializableLocation loc1, SerializableLocation loc2)
	{
	
		if(loc1.getWorld() == loc2.getWorld())
		{
			this.loc1 = loc1;
			this.loc2 = loc2;
		}
	}
	
	public SerializableRegion(Location loc1, Location loc2)
	{
		if(loc1.getWorld() == loc2.getWorld())
		{
			this.loc1 = new SerializableLocation(loc1);
			this.loc2 = new SerializableLocation(loc2);
		}
	}
	
	public SerializableRegion(Player p, Wand wand)
	{
		this(wand.getPos1(p), wand.getPos2(p));
	}
	
	public Location getMinimum()
	{
		if(loc1 == null || loc2 == null || loc1.getLocation() == null || loc2.getLocation() == null)
			return null;
		
		return new Location(loc1.getWorld(), 
				Math.min(loc1.getLocation().getBlockX(), loc2.getLocation().getBlockX()), 
				Math.min(loc1.getLocation().getBlockY(), loc2.getLocation().getBlockY()), 
				Math.min(loc1.getLocation().getBlockZ(), loc2.getLocation().getBlockZ()));
	}
	
	public Location getMaximum()
	{
		if(loc1 == null || loc2 == null || loc1.getLocation() == null || loc2.getLocation() == null)
			return null;
		
		return new Location(loc1.getWorld(), 
				Math.max(loc1.getLocation().getBlockX(), loc2.getLocation().getBlockX()) + 1, 
				Math.max(loc1.getLocation().getBlockY(), loc2.getLocation().getBlockY()) + 1, 
				Math.max(loc1.getLocation().getBlockZ(), loc2.getLocation().getBlockZ()) + 1);
	}

	public void expand(BlockFace direction, int count)
	{
		switch(direction)
		{
			case UP:
				//+Y
				if(this.loc1.getLocation().getY() >= this.loc2.getLocation().getY())
					this.loc1 = new SerializableLocation(this.loc1.getLocation().add(0, count, 0));
				else
					this.loc2 = new SerializableLocation(this.loc2.getLocation().add(0, count, 0));
				break;

			case DOWN:
				//-Y
				if(this.loc1.getLocation().getY() <= this.loc2.getLocation().getY())
					this.loc1 = new SerializableLocation(this.loc1.getLocation().add(0, -count, 0));
				else
					this.loc2 = new SerializableLocation(this.loc2.getLocation().add(0, -count, 0));
				break;

			case EAST:
				//+X
				if(this.loc1.getLocation().getX() >= this.loc2.getLocation().getX())
					this.loc1 = new SerializableLocation(this.loc1.getLocation().add(count, 0, 0));
				else
					this.loc2 = new SerializableLocation(this.loc2.getLocation().add(count, 0, 0));
				break;

			case WEST:
				//-X
				if(this.loc1.getLocation().getX() <= this.loc2.getLocation().getX())
					this.loc1 = new SerializableLocation(this.loc1.getLocation().add(-count, 0, 0));
				else
					this.loc2 = new SerializableLocation(this.loc2.getLocation().add(-count, 0, 0));
				break;

			case SOUTH:
				//+Z
				if(this.loc1.getLocation().getZ() >= this.loc2.getLocation().getZ())
					this.loc1 = new SerializableLocation(this.loc1.getLocation().add(0, 0, count));
				else
					this.loc2 = new SerializableLocation(this.loc2.getLocation().add(0, 0, count));
				break;

			case NORTH:
				//-Z
				if(this.loc1.getLocation().getZ() <= this.loc2.getLocation().getZ())
					this.loc1 = new SerializableLocation(this.loc1.getLocation().add(0, 0, -count));
				else
					this.loc2 = new SerializableLocation(this.loc2.getLocation().add(0, 0, -count));
				break;
		}
	}
	
	@Override
	public boolean contains(Location loc)
	{
		if(loc1 == null || loc2 == null || loc1.getLocation() == null || loc2.getLocation() == null)
			return false;
		
		if(loc.getWorld() != this.getMinimum().getWorld())
			return false;
		
		if(loc.getX() < this.getMinimum().getX())
			return false;
		
		if(loc.getX() >= this.getMaximum().getX())
			return false;
		
		if(loc.getY() < this.getMinimum().getY())
			return false;
		
		if(loc.getY() >= this.getMaximum().getY())
			return false;
		
		if(loc.getZ() < this.getMinimum().getZ())
			return false;

		return loc.getZ() < this.getMaximum().getZ();

	}
	
	public void setBiome(Biome biome)
	{
		for(Chunk chunk : this.getChunks())
			chunk.load();
		
		for(int x = this.getMinimum().getBlockX(); x <= this.getMaximum().getBlockX(); x++)
			for(int z = this.getMinimum().getBlockZ(); z <= this.getMaximum().getBlockZ(); z++)
				this.loc1.getWorld().setBiome(x, z, biome);
	}
	
	public Set<Chunk> getChunks()
	{		
		Set<Chunk> chunks = new HashSet<Chunk>();
		for(int x = this.getMinimum().getBlockX(); x <= this.getMaximum().getBlockX(); x += 16)
				for(int z = this.getMinimum().getBlockZ(); z <= this.getMaximum().getBlockZ(); z += 16)
					chunks.add(this.getMinimum().getWorld().getChunkAt(x, z));
		return chunks;
	}
	
	@Override
	public String toString()
	{
		return this.loc1.toString() + "&&" + this.loc2.toString();
	}
	
	public static SerializableRegion fromString(String string)
	{
		try
		{
			if(string.contains("TO"))
				return new SerializableRegion(SerializableLocation.fromString(string.split("TO")[0]), SerializableLocation.fromString(string.split("TO")[1]));
			return new SerializableRegion(SerializableLocation.fromString(string.split("&&")[0]), SerializableLocation.fromString(string.split("&&")[1]));
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public World getWorld()
	{
		return loc1.getWorld();
	}
}
