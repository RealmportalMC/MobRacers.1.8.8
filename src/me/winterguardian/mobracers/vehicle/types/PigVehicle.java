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
import me.winterguardian.mobracers.item.types.special.PigSpecialItem;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

public class PigVehicle extends Vehicle
{

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Pig)
		{
			((Pig)this.getEntity()).setSaddle(true);
			((Pig)this.getEntity()).setAdult();
			((Pig)this.getEntity()).setAgeLock(true);
		}
	}
	
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.PIG;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.PIG;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return true;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_PIG_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_PIG_DESC.toString();
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.PIG_IDLE, 1, 1));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.DIRT, (byte)0), new WallBlock(Material.DIRT, (byte)2), new WallBlock(Material.SOUL_SAND, (byte)0)), new SoundEffect(Sound.PIG_IDLE, 1, 0.8f));
		case SPECIAL:
			return new PigSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 15, Material.MONSTER_EGG, 1, (short)90, "§d§l" + getName(), new ArrayList<String>());
	}
}
