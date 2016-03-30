package me.winterguardian.mobracers.arena;

import org.bukkit.Material;
import org.bukkit.block.BlockState;

public class RoadType 
{
	private Material material;
	private byte data;
	private boolean anyData;
	
	public RoadType(Material material)
	{
		this.material = material;
		this.anyData = true;
	}
	
	public RoadType(Material material, byte data)
	{
		this.material = material;
		this.data = data;
		this.anyData = false;
	}
	
	@SuppressWarnings("deprecation")
	public RoadType(BlockState state)
	{
		if(state == null)
		{
			this.material = Material.AIR;
			this.anyData = true;
			return;
		}
		this.material = state.getType();
		this.data = state.getData().getData();
		this.anyData = false;
	}
	
	public RoadType(String serialized)
	{
		this.fromString(serialized);
	}
	
	@SuppressWarnings("deprecation")
	public boolean match(BlockState state)
	{
		if(state == null)
			return false;
		
		if(this.anyData)
			return state.getType() == material;
		return state.getType() == material && state.getData().getData() == data;
	}
	
	public boolean match(RoadType type)
	{
		if(type == null)
			return false;
		
		if(this.anyData)
			return type.getMaterial() == material;
		return type.getMaterial() == material && type.getData() == data;
	}
	
	@Override
	public String toString()
	{
		if(this.anyData)
			return this.material.name().toLowerCase();
		return this.material.name().toLowerCase() + ":" + this.data;
	}
	
	@SuppressWarnings("deprecation")
	public void fromString(String string)
	{
		if(string == null)
		{
			this.material = Material.AIR;
			this.anyData = true;
			return;
		}
		
		if(string.contains(":"))
		{
			try
			{
				this.material = Material.getMaterial(Integer.parseInt(string.split(":")[0]));
			}
			catch(Exception e)
			{
				this.material = Material.getMaterial(string.split(":")[0].toUpperCase());
			}
		
			try
			{
				this.data = Byte.parseByte(string.split(":")[1]);
				this.anyData = false;
			}
			catch(Exception e)
			{
				this.anyData = true;
			}
		}
		else
		{
			try
			{
				this.material = Material.getMaterial(Integer.parseInt(string));
			}
			catch(Exception e)
			{
				this.material = Material.getMaterial(string.toUpperCase());
			}
			
			this.anyData = true;
		}
	}
	
	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public byte getData()
	{
		return data;
	}

	public void setData(byte data)
	{
		this.data = data;
	}

	public boolean isAnyData()
	{
		return anyData;
	}

	public void setAnyData(boolean anyData)
	{
		this.anyData = anyData;
	}
}
