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
import me.winterguardian.mobracers.item.types.special.SquidSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;

public class SquidVehicle extends PurchasableVehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.SQUID;
	}

	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.WATER, 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.WOOL, (byte)15), new WallBlock(Material.STAINED_GLASS, (byte)15)), new SoundEffect(Sound.AMBIENCE_RAIN, 1, 1f));
		case SPECIAL:
			return new SquidSpecialItem();
		default:
			return type.getDefault();
		
		}
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SQUID_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SQUID_DESC.toString();
	}

	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.SQUID;
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.SQUID;
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 39, Material.MONSTER_EGG, 1, (short)94, "§9§l" + getName(), new ArrayList<String>());
	}
}
