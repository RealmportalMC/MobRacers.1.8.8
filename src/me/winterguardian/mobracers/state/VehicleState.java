package me.winterguardian.mobracers.state;

import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface VehicleState extends MobRacersState
{
	Player getOwner(Vehicle vehicle);
	Vehicle getVehicle(Entity entity);
	boolean containsVehicle(Entity entity);
}
