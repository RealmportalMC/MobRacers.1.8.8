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
import me.winterguardian.mobracers.item.types.special.SuperSheepSpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

public class SuperSheepVehicle extends WinnableVehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.SUPER_SHEEP;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return getWinAchievement().isComplete(p);
	}
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && getEntity() instanceof Sheep)
		{
			((Sheep)this.getEntity()).setAdult();
			((Sheep)this.getEntity()).setAgeLock(true);
			this.getEntity().setCustomName("jeb_");
			this.getEntity().setCustomNameVisible(false);
		}
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.SHEEP_IDLE, 1, 1.65f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.WOOL, (byte)14), new WallBlock(Material.WOOL, (byte)1), new WallBlock(Material.WOOL, (byte)3), new WallBlock(Material.WOOL, (byte)4), new WallBlock(Material.WOOL, (byte)5), new WallBlock(Material.WOOL, (byte)3), new WallBlock(Material.WOOL, (byte)11), new WallBlock(Material.WOOL, (byte)10)), new SoundEffect(Sound.SHEEP_IDLE, 1, 0.8f));
		case SPECIAL:
			return new SuperSheepSpecialItem();
		default:
			return type.getDefault();
		
		}
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SUPERSHEEP_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SUPERSHEEP_DESC.toString();
	}

	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.SHEEP_BESTTIME;
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.SHEEP;
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 14, Material.MONSTER_EGG, 1, (short)91, "§e§l" + getName(), new ArrayList<String>());
	}
}