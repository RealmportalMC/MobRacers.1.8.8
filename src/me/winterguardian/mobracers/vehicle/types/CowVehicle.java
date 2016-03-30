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
import me.winterguardian.mobracers.item.types.special.CowSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CowVehicle extends PurchasableVehicle
{
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.COW_IDLE, 1, 1.3f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.GRASS, (byte)0), new WallBlock(Material.LEAVES, (byte)0)), new SoundEffect(Sound.COW_IDLE, 1, 0.8f));
		case SPECIAL:
			return new CowSpecialItem();
		default:
			return type.getDefault();
		
		}
	}
	
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.COW;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.COW;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_COW_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_COW_DESC.toString();
	}

	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.COW;
	}
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Cow)
		{
			((Cow)this.getEntity()).setAdult();
			((Cow)this.getEntity()).setAgeLock(true);
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 10, Material.MONSTER_EGG, 1, (short)92, "§6§l" + getName(), new ArrayList<String>());
	}
}
