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
import me.winterguardian.mobracers.item.types.special.MagmaCubeSpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;

public class MagmaCubeVehicle extends WinnableVehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.MAGMA_CUBE;
	}
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof MagmaCube)
		{
			((MagmaCube)this.getEntity()).setSize(2);
		}
	}

	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p);
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.MAGMA_CUBE;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_MAGMACUBE_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_MAGMACUBE_DESC.toString();
	}

	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.SLIME_BESTTIME;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect("liquid.lava", 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.NETHERRACK, (byte)0)), new SoundEffect("liquid.lavapop", 1, 1f));
		case SPECIAL:
			return new MagmaCubeSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 34, Material.MONSTER_EGG, 1, (short)62, "§6§l" + getName(), new ArrayList<String>());
	}
}
