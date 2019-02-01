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
import me.winterguardian.mobracers.item.types.special.MushroomCowSpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Player;

public class MushroomCowVehicle extends WinnableVehicle
{
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.MUSHROOM_COW;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.MUSHROOM_COW;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p);
	}

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_MUSHROOMCOW_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_MUSHROOMCOW_DESC.toString();
	}

	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.COW_PLAY;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.COW_HURT, 1, 0.9f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.HUGE_MUSHROOM_2, (byte)14)), new SoundEffect(Sound.COW_HURT, 1, 0.8f));
		case SPECIAL:
			return new MushroomCowSpecialItem();
		default:
			return type.getDefault();
		
		}
	}
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(getEntity() != null && getEntity() instanceof MushroomCow)
		{
			((MushroomCow)this.getEntity()).setAdult();
			((MushroomCow)this.getEntity()).setAgeLock(true);
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 12, Material.MONSTER_EGG, 1, (short)96, "§c§l" + getName(), new ArrayList<String>());
	}
}