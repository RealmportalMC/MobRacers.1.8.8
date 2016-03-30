package me.winterguardian.mobracers.arena;

import java.util.Collection;

import me.winterguardian.core.Core;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
 
public class Breakline
{
	private SerializableLocation loc1;
	private SerializableLocation loc2;
	
	
	public Breakline(SerializableLocation loc1, SerializableLocation loc2)
	{
		this.loc1 = loc1;
		this.loc2 = loc2;
	}
	
	public Breakline(Location loc1, Location loc2)
	{
		this.loc1 = new SerializableLocation(loc1);
		this.loc2 = new SerializableLocation(loc2);
	}
	
	public Breakline(Player p)
	{
		this.loc1 = Core.getWand().getPos1(p);
		this.loc2 = Core.getWand().getPos2(p);
	}
	
	public Breakline(String serialized)
	{
		this.fromString(serialized);
	}
	
	public double getDistance(Location loc)
	{
		/*
		if(getLoc1().getX() == getLoc2().getX() && getLoc1().getZ() == getLoc2().getZ()) 
			distance2D(getLoc1(), loc);
		
		if(getLoc1().getX() == getLoc2().getX()) //si c'est une droite verticale
			return Math.abs(getLoc1().getX() - loc.getX());
		
		if(getLoc1().getZ() == getLoc2().getZ())
			return Math.abs(getLoc1().getZ() - loc.getZ());
		
		//si on est ici c'est une droite ax + b delta X != 0 delta Y != 0
		
		double a2 = (getLoc1().getX() - getLoc2().getX()) / (getLoc2().getZ() - getLoc1().getZ()); //pente droites parralèlles perpendiculaires à la pente du segment
		
		double b2 = getLoc1().getZ() - a2 * getLoc1().getX(); //droite 2 (une des bordures)
		double b3 = getLoc2().getZ() - a2 * getLoc2().getX(); //droite 3 (une autre bordure)
		
		if(b2 > b3) //si la droite 2 est plus haute de la droite 3
		{
			if(loc.getZ() > a2 * loc.getX() + b2)
				return distance2D(getLoc1(), loc);
				
			if(loc.getZ() < a2 * loc.getX() + b3)
				return distance2D(getLoc2(), loc);
		}	
		else//sinon si droite 3 est plus haute de la droite 2
		{
			if(loc.getZ() < a2 * loc.getX() + b2)
				return distance2D(getLoc1(), loc);
				
			if(loc.getZ() > a2 * loc.getX() + b3)
				return distance2D(getLoc2(), loc);
		}
		*/ //nut wurking ...
		//formule de distance avec une droite...
		return Math.abs((getLoc2().getZ() - getLoc1().getZ()) * loc.getX() + (getLoc1().getX() - getLoc2().getX()) * loc.getZ() + getLoc1().getZ() * (getLoc2().getX() - getLoc1().getX()) + getLoc1().getX() * (getLoc1().getZ() - getLoc2().getZ())) / Math.sqrt(Math.pow((getLoc2().getZ() - getLoc1().getZ()), 2) + Math.pow((getLoc1().getX() - getLoc2().getX()), 2));

	}
	
	public static double distance2D(Location loc1, Location loc2)
	{
		return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) +  Math.pow(loc1.getZ() - loc2.getZ(), 2));
	}

	public Location getNearest(Collection<Location> locations)
	{
		Location bestLocation = (Location) locations.toArray()[0];
		double bestDistance = getDistance(bestLocation);
		
		for(Location loc : locations)
			if(getDistance(loc) < bestDistance)
			{
				bestDistance = getDistance(loc);
				bestLocation = loc;
			}
		return bestLocation;
	}
	
	public boolean isPassingThrough(Location previous, Location next)
	{
		if(getLoc1().getX() == getLoc2().getX() && getLoc1().getZ() == getLoc2().getZ()) //s'il ne forme pas de droite 
			return false;
		
		if(getLoc1().getX() == getLoc2().getX()) //si c'est une droite verticale
		{
		
			return previous.getX() > getLoc1().getX() && next.getX() < getLoc1().getX()
				|| previous.getX() < getLoc1().getX() && next.getX() > getLoc1().getX();
		}
		else if(getLoc1().getZ() == getLoc2().getZ()) //si c'est une droite horizontale
		{
		
			return previous.getZ() > getLoc1().getZ() && next.getZ() < getLoc1().getZ()
				|| previous.getZ() < getLoc1().getZ() && next.getZ() > getLoc1().getZ();
		}
		
		//si on est ici c'est une droite ax + b delta X != 0 delta Y != 0
		
		double a1 = (getLoc2().getZ() - getLoc1().getZ()) / (getLoc2().getX() - getLoc1().getX()); //pente droite du segment
		
		double b1 = getLoc1().getZ() - a1 * getLoc1().getX(); //valeur initiale de la droite 1 (droite du segment)
		
		return previous.getZ() < a1 * previous.getX() + b1 && next.getZ() > a1 * next.getX() + b1
			|| previous.getZ() > a1 * previous.getX() + b1 && next.getZ() < a1 * next.getX() + b1;
	}

	public Location getLoc1()
	{
		if(this.loc1 != null)
			return this.loc1.getLocation();
		return null;
	}

	public void setLoc1(Location loc1)
	{
		if(loc1 != null)
			this.loc1 = new SerializableLocation(loc1);
		this.loc1 = null;
	}

	public Location getLoc2()
	{
		if(this.loc2 != null)
			return this.loc2.getLocation();
		return null;
	}

	public void setLoc2(Location loc2)
	{
		if(loc2 != null)
			this.loc2 = new SerializableLocation(loc2);
		this.loc2 = null;
	}
	
	public Location getCenter()
	{
		return new Location(this.getLoc1().getWorld(), (this.getLoc1().getX() + this.getLoc2().getX()) / 2, (this.getLoc1().getY() + this.getLoc2().getY()) / 2, (this.getLoc1().getZ() + this.getLoc2().getZ()) / 2);
	}
	
	@Override
	public String toString()
	{
		return this.loc1.toString() + "&&" + this.loc2.toString();
	}
	
	public void fromString(String string)
	{
		try
		{
			this.loc1 = SerializableLocation.fromString(string.split("&&")[0]);
			this.loc2 = SerializableLocation.fromString(string.split("&&")[1]);
		}
		catch(Exception e)
		{
			this.loc1 = null;
			this.loc2 = null;
		}
	}
}
