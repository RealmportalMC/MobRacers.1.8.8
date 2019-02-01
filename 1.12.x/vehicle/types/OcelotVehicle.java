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
import me.winterguardian.mobracers.item.types.special.OcelotSpecialItem;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.VipVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

public class OcelotVehicle extends VipVehicle
{
	private static RandomSelector<Type> catType = new AntiRecursiveRandomSelector<Type>(Type.values());
	
	@Override
	public void spawn(Location loc, Player p)
	{
		super.spawn(loc, p);
		if(this.getEntity() != null && this.getEntity() instanceof Ocelot)
		{
			((Ocelot)this.getEntity()).setAdult();
			((Ocelot)this.getEntity()).setAgeLock(true);
			
			if(p != null)
			{
				((Ocelot)this.getEntity()).setCatType(catType.next());
				((Ocelot)this.getEntity()).setSitting(false);
			}
			else
			{
				((Ocelot)this.getEntity()).setCatType(Type.RED_CAT);
				((Ocelot)this.getEntity()).setSitting(true);
			}
		}
	}
	
	@Override
	protected EntityType getEntityType()
	{
		return EntityType.OCELOT;
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.OCELOT;
	}

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_OCELOT_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_OCELOT_DESC.toString();
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.CAT_MEOW, 1, 1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.HUGE_MUSHROOM_1, (byte)0)), new SoundEffect(Sound.CAT_HIT, 1, 1f));
		case SPECIAL:
			return new OcelotSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 22, Material.MONSTER_EGG, 1, (short)98, "§e§l" + getName(), new ArrayList<String>());
	}
}
