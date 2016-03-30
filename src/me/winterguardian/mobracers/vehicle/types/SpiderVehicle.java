package me.winterguardian.mobracers.vehicle.types;

import java.util.ArrayList;
import java.util.Arrays;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.item.types.SugarItem;
import me.winterguardian.mobracers.item.types.WallItem;
import me.winterguardian.mobracers.item.types.WallItem.WallBlock;
import me.winterguardian.mobracers.item.types.special.SpiderSpecialItem;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpiderVehicle extends Vehicle 
{
	
	@Override
	public VehicleType getType()
	{
		return VehicleType.SPIDER;
	}

	
	@Override
	public boolean canChoose(Player p)
	{
		return true;
	}
	

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SPIDER_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SPIDER_DESC.toString();
	}


	@Override
	protected EntityType getEntityType()
	{
		return EntityType.SPIDER;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.SPIDER_IDLE, 1, 1.2f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.NETHER_BRICK, (byte)0)), new SoundEffect(Sound.SPIDER_IDLE, 1, 0.8f));
		case SPECIAL:
			return new SpiderSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 30, Material.MONSTER_EGG, 1, (short)52, "§5§l" + getName(), new ArrayList<String>());
	}
}
