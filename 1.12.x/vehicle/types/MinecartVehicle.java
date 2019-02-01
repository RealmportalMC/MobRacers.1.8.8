package me.winterguardian.mobracers.vehicle.types;

import java.util.ArrayList;
import java.util.Arrays;

import me.winterguardian.core.entity.custom.rideable.RideableEntityUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.item.types.SugarItem;
import me.winterguardian.mobracers.item.types.WallItem;
import me.winterguardian.mobracers.item.types.WallItem.WallBlock;
import me.winterguardian.mobracers.item.types.special.MinecartSpecialItem;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MinecartVehicle extends Vehicle
{
	@Override
	public VehicleType getType()
	{
		return VehicleType.MINECART;
	}

	@Override
	public boolean canChoose(Player p)
	{
		return true;
	}

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_MINECART_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_MINECART_DESC.toString();
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect("minecart.base", 1, 1.3f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.DOUBLE_STEP, (byte)8), new WallBlock(Material.IRON_BLOCK, (byte)0)), new SoundEffect("mob.zombie.metal", 1, 0.5f));
		case SPECIAL:
			return new MinecartSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.MINECART;
	}
	
	public void spawn(Location loc, Player p)
	{
		if(loc != null && this.getEntity() == null)
		{
			this.entity = RideableEntityUtil.spawnRideable(this.getEntityType(), loc);
			
			if(this.entity == null)
				return;
			
			if(RideableEntityUtil.isRideable(this.entity))
			{
				setSpeed(MobRacersPlugin.getGame().getConfig().getBaseSpeed());
				RideableEntityUtil.setClimbHeight(entity, 1f);
				RideableEntityUtil.setJumpHeight(entity, 0);
				RideableEntityUtil.setJumpThrust(entity, 0);
			}
		}
	}

	@Override
	public VehicleGUIItem getGUIItem()
	{
		return new VehicleGUIItem(getType(), 25, Material.MINECART, 1, (short)0, "§a§l" + getName(), new ArrayList<String>());
	}
}
