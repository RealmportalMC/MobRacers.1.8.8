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
import me.winterguardian.mobracers.item.types.special.GuardianSpecialItem;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.VipVehicle;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class GuardianVehicle extends VipVehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.GUARDIAN;
	}

	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_GUARDIAN_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_GUARDIAN_DESC.toString();
	}

	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect("mob.guardian.land.idle", 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.PRISMARINE, (byte)0), new WallBlock(Material.PRISMARINE, (byte)2)), new SoundEffect("mob.guardian.hit", 1, 1f));
		case SPECIAL:
			return new GuardianSpecialItem();
		default:
			return type.getDefault();
		
		}
	}


	@Override
	protected EntityType getEntityType()
	{
		return EntityType.GUARDIAN;
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 40, Material.MONSTER_EGG, 1, (short)68, "§b§l" + getName(), new ArrayList<String>());
	}
}
