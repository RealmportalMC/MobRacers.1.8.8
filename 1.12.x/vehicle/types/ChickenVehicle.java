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
import me.winterguardian.mobracers.item.types.special.ChickenSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ChickenVehicle extends PurchasableVehicle
{

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(getEntity() != null && getEntity() instanceof Chicken)
		{
			((Chicken)this.getEntity()).setAdult();
			((Chicken)this.getEntity()).setAgeLock(true);
		}
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_CHICKEN_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_CHICKEN_DESC.toString();
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.CHICKEN;
	}


	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.CHICKEN;
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.CHICKEN;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.CHICKEN_IDLE, 1, 1.2f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.SOIL, (byte)0)), new SoundEffect(Sound.CHICKEN_IDLE, 1, 0.8f));
		case SPECIAL:
			return new ChickenSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 16, Material.MONSTER_EGG, 1, (short)93, "§7§l" + getName(), new ArrayList<String>());
	}
}
