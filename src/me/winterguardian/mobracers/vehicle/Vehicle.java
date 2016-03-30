package me.winterguardian.mobracers.vehicle;

import me.winterguardian.core.entity.EntityUtil;
import me.winterguardian.core.entity.custom.rideable.RideableEntityUtil;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class Vehicle
{
	private boolean reverse;
	private float speed;
	protected Entity entity;

	public Vehicle()
	{

	}

	public abstract String getName();
	public abstract String getDescription();

	protected abstract EntityType getEntityType();
	public abstract VehicleType getType();
	public abstract Item getItem(ItemType type);
	public abstract boolean canChoose(Player p);
	public abstract VehicleGUIItem getGUIItem();

	
	public void spawn(Location loc, Player p)
	{
		if(loc != null && this.entity == null)
		{
			if(p != null)
				this.entity = RideableEntityUtil.spawnRideable(this.getEntityType(), loc);
			else
				this.entity = loc.getWorld().spawnEntity(loc, this.getEntityType());
			
			if(this.entity == null)
				return;
			
			if(this.entity instanceof Ageable)
			{
				((Ageable)this.getEntity()).setAdult();
				((Ageable)this.getEntity()).setAgeLock(true);
			}
			
			if(RideableEntityUtil.isRideable(this.entity))
			{
				setSpeed(((MobRacersConfig) MobRacersPlugin.getGame().getConfig()).getBaseSpeed());
				RideableEntityUtil.setClimbHeight(entity, 1f);
				RideableEntityUtil.setJumpHeight(entity, 0);
				RideableEntityUtil.setJumpThrust(entity, 0);
			}
		}
	}

	public void remove()
	{
		if(this.entity == null)
			return;
		
		this.entity.remove();
		this.entity = null;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
		RideableEntityUtil.setSpeed(entity, this.speed < 0.1 ? 0.1f : this.speed);
	}

	public float getSpeed()
	{
		return speed;
	}
	
	public Entity getEntity()
	{
		return this.entity;
	}
	
	public void start()
	{
		EntityUtil.setNoAI(this.getEntity(), false);
	}

	public void stop()
	{
		EntityUtil.setNoAI(this.getEntity(), true);
	}
	
	public static Vehicle newVehicle(VehicleType type)
	{
		return type.createNewVehicle();
	}

	public boolean isReverse()
	{
		return reverse;
	}

	public void setReverse(boolean reverse)
	{
		this.reverse = reverse;
	}
}
