package me.winterguardian.mobracers.vehicle.types;

import java.util.ArrayList;
import java.util.Arrays;

import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.RandomSelector;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.item.types.SugarItem;
import me.winterguardian.mobracers.item.types.WallItem;
import me.winterguardian.mobracers.item.types.WallItem.WallBlock;
import me.winterguardian.mobracers.item.types.special.WolfSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class WolfVehicle extends PurchasableVehicle
{
	private static RandomSelector<DyeColor> collarColor = new AntiRecursiveRandomSelector<DyeColor>(DyeColor.values());
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Wolf)
		{
			((Wolf)this.getEntity()).setAdult();
			((Wolf)this.getEntity()).setAgeLock(true);
			((Wolf)this.getEntity()).setCollarColor(collarColor.next());
			((Wolf)this.getEntity()).setSitting(false);
		}
	}
	
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.WOLF;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.WOLF;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return getPurchase().hasPurchased(p);
	}

	
	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_WOLF_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_WOLF_DESC.toString();
	}

	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.WOLF;
	}

	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.WOLF_BARK, 1, 1.1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.SNOW_BLOCK, (byte)0)), new SoundEffect(Sound.WOLF_BARK, 1, 0.8f));
		case SPECIAL:
			return new WolfSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 21, Material.MONSTER_EGG, 1, (short)95, "§7§l" + getName(), new ArrayList<String>());
	}
}
