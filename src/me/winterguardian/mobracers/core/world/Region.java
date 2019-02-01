package me.winterguardian.core.world;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public abstract class Region
{
	public abstract boolean contains(Location loc);
	public abstract World getWorld();
	
	public Set<Entity> getEntities()
	{
		Set<Entity> entities = new HashSet<>();
		for(Entity entity : this.getWorld().getEntities())
			if(contains(entity.getLocation()))
				entities.add(entity);
		return entities;
	}
}
