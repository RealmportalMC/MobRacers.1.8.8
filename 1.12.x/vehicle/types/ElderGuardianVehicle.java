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
import me.winterguardian.mobracers.item.types.special.ElderGuardianSpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;

public class ElderGuardianVehicle extends WinnableVehicle
{

	@Override
	public void spawn(Location loc, Player p)
	{	super.spawn(loc, p);
		
		if(this.getEntity() != null && this.getEntity() instanceof Guardian)
			((Guardian)this.getEntity()).setElder(true);
	}
	
	@Override
	public VehicleType getType()
	{
		return VehicleType.ELDER_GUARDIAN;
	}

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_ELDERGUARDIAN_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_ELDERGUARDIAN_DESC.toString();
	}

	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.GUARDIAN_WIN;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect("mob.guardian.elder.idle", 1, 1.2f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.SEA_LANTERN, (byte)0), new WallBlock(Material.PRISMARINE, (byte)1)), new SoundEffect("mob.guardian.elder.hit", 1, 1f));
		case SPECIAL:
			return new ElderGuardianSpecialItem();
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
		return new VehicleGUIItem(getType(), 41, Material.MONSTER_EGG, 1, (short)68, "§3§l" + getName(), new ArrayList<String>());
	}
}
