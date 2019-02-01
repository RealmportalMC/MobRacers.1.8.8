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
import me.winterguardian.mobracers.item.types.special.BabyCowSpecialItem;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.VipVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class BabyCowVehicle extends VipVehicle
{
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.COW_IDLE, 1, 1.6f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.STAINED_GLASS, (byte)0)), new SoundEffect(Sound.COW_IDLE, 1, 1.4f));
		case SPECIAL:
			return new BabyCowSpecialItem();
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
		return VehicleType.BABY_COW;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_BABYCOW_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_BABYCOW_DESC.toString();
	}
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Cow)
		{
			((Cow)this.getEntity()).setBaby();
			((Cow)this.getEntity()).setAgeLock(true);
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 11, Material.MONSTER_EGG, 1, (short)92, "§f§l" + getName(), new ArrayList<String>());
	}
}
