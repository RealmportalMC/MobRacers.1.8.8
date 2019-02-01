package me.winterguardian.core.world;

import org.bukkit.Location;
import org.bukkit.World;

public class MultiRegion extends Region
{
	private Region[] regions;
	
	public MultiRegion(Region... regions)
	{
		this.regions = regions;
	}
	
	
	@Override
	public boolean contains(Location loc)
	{
		for(Region region : regions)
			if(region != null && region.contains(loc))
				return true;
		
		return false;
	}


	@Override
	public World getWorld()
	{
		return regions[0].getWorld();
	}

}
