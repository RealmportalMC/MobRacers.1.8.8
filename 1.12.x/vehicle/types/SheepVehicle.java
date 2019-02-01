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
import me.winterguardian.mobracers.item.types.special.SheepSpecialItem;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

public class SheepVehicle extends Vehicle
{
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Sheep)
		{
			((Sheep)this.getEntity()).setColor(DyeColor.WHITE);
			((Sheep)this.getEntity()).setAdult();
			((Sheep)this.getEntity()).setAgeLock(true);
		}
	}
	
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.SHEEP;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.SHEEP;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return true;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.SHEEP_IDLE, 1, 1.5f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.WOOL, (byte)0)), new SoundEffect(Sound.SHEEP_IDLE, 1, 0.8f));
		case SPECIAL:
			return new SheepSpecialItem();
		default:
			return type.getDefault();
		
		}
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SHEEP_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SHEEP_DESC.toString();
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 13, Material.MONSTER_EGG, 1, (short)91, "§f§l" + getName(), new ArrayList<String>());
	}
}
