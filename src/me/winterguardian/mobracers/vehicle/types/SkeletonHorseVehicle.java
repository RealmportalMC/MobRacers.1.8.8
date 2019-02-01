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
import me.winterguardian.mobracers.item.types.special.SkeletonHorseSpecialItem;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.VipVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;

public class SkeletonHorseVehicle extends VipVehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.SKELETON_HORSE;
	}

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Horse)
		{
			((Horse) this.getEntity()).setAdult();
			((Horse) this.getEntity()).setAgeLock(true);
			((Horse) this.getEntity()).setVariant(Variant.SKELETON_HORSE);
		}
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SKELETONHORSE_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SKELETONHORSE_DESC.toString();
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.ENTITY_SKELETON_AMBIENT, 1, 1.1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.HUGE_MUSHROOM_2, (byte)10)), new SoundEffect(Sound.ENTITY_SKELETON_HURT, 1, 0.8f));
		case SPECIAL:
			return new SkeletonHorseSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.HORSE;
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 28, Material.MONSTER_EGG, 1, (short)51, "§7§l" + getName(), new ArrayList<String>());
	}
}