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
import me.winterguardian.mobracers.item.types.special.SlimeSpecialItem;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

public class SlimeVehicle extends PurchasableVehicle
{

	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Slime)
		{
			((Slime)this.getEntity()).setSize(2);
		}
	}
	
	@Override
	public VehicleType getType()
	{
		return VehicleType.SLIME;
	}


	@Override
	public boolean canChoose(Player p)
	{
		return getPurchase().hasPurchased(p);
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.SLIME;
	}

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_SLIME_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_SLIME_DESC.toString();
	}

	@Override
	public CoursePurchase getPurchase()
	{
		return CoursePurchase.SLIME;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.SLIME_WALK, 1, 1.2f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.SLIME_BLOCK, (byte)0)), new SoundEffect(Sound.SLIME_WALK, 1, 0.8f));
		case SPECIAL:
			return new SlimeSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 33, Material.MONSTER_EGG, 1, (short)55, "§a§l" + getName(), new ArrayList<String>());
	}
}
