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
import me.winterguardian.mobracers.item.types.special.BlockSpecialItem;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.vehicle.VehicleGUIItem;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class BlockVehicle extends WinnableVehicle
{

	@Override
	public String getName()
	{
		return CourseMessage.VEHICLE_BLOCK_NAME.toString();
	}

	@Override
	public String getDescription()
	{
		return CourseMessage.VEHICLE_BLOCK_DESC.toString();
	}

	@Override
	public VehicleType getType()
	{
		return VehicleType.BLOCK;
	}


	@Override
	public CourseAchievement getWinAchievement()
	{
		return CourseAchievement.BLOCK_BUILD;
	}
	
	@Override
	public Item getItem(ItemType type)
	{
		switch(type)
		{
		case SUGAR:
			return new SugarItem(new SoundEffect(Sound.DIG_STONE, 1, 1.1f));
		case WALL:
			return new WallItem(Arrays.asList(new WallBlock(Material.BRICK, (byte)0)), new SoundEffect(Sound.PISTON_EXTEND, 1, 1f));
		case SPECIAL:
			return new BlockSpecialItem();
		default:
			return type.getDefault();
		
		}
	}

	@Override
	protected EntityType getEntityType()
	{
		return EntityType.FALLING_BLOCK;
	}
	
	public void spawn(Location loc, Player p)
	{
		if(loc != null && this.getEntity() == null)
		{
			this.entity = RideableEntityUtil.spawnRideable(this.getEntityType(), loc);
			RideableEntityUtil.setBlock((FallingBlock)this.entity, Material.SNOW_BLOCK, 0);

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
		return new VehicleGUIItem(getType(), 24, Material.SMOOTH_BRICK, 1, (short)0, "§b§l" + getName(), new ArrayList<String>());
	}
}
