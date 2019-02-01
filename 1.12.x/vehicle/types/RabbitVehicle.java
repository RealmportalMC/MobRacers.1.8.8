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
import me.winterguardian.mobracers.item.types.special.RabbitSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

public class RabbitVehicle extends PurchasableVehicle
{

	@Override
	public VehicleType getType()
	{
		return VehicleType.RABBIT;
	}


	@Override
	public boolean canChoose(Player p)
	{
		return getPurchase().hasPurchased(p);
	}


	@Override
	protected EntityType getEntityType()
	{
		return EntityType.RABBIT;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_RABBIT_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_RABBIT_DESC.toString();
	}


	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.RABBIT;
	}

	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect("mob.rabbit.hurt", 1, 0.5f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.LOG_2, (byte)0)), new SoundEffect("mob.rabbit.death", 1, 0.6f));
		case SPECIAL:
			return new RabbitSpecialItem();
		default:
			return type.getDefault();
		
		}
	}
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(getEntity() != null && getEntity() instanceof Rabbit)
		{
			((Rabbit)this.getEntity()).setAdult();
			((Rabbit)this.getEntity()).setAgeLock(true);
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 23, Material.MONSTER_EGG, 1, (short)101, "§4§l" + getName(), new ArrayList<String>());
	}
}
