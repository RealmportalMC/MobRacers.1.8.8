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
import me.winterguardian.mobracers.item.types.special.SilverfishSpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SilverfishVehicle extends WinnableVehicle
{
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.SILVERFISH;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.SILVERFISH;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p);
	}

	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SILVERFISH_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SILVERFISH_DESC.toString();
	}

	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.SILVERFISH_ITEMS;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.SILVERFISH_IDLE, 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.SMOOTH_BRICK, (byte)0), new WallBlock(Material.SMOOTH_BRICK, (byte)1), new WallBlock(Material.SMOOTH_BRICK, (byte)2)), new SoundEffect(Sound.SILVERFISH_KILL, 1, 1f));
		case SPECIAL:
			return new SilverfishSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 32, Material.MONSTER_EGG, 1, (short)60, "§8§l" + getName(), new ArrayList<String>());
	}
}
