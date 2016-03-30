package me.winterguardian.mobracers.listener;

import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.state.VehicleState;
import me.winterguardian.mobracers.state.lobby.VehicleSelectionState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;


public class VehicleProtectionListener implements Listener
{
	private MobRacersGame game;
	
	public VehicleProtectionListener(MobRacersGame game)
	{
		this.game = game;
	}
	
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event)
	{
		if(this.game.getState() instanceof VehicleState)
			if(((VehicleState)this.game.getState()).containsVehicle(event.getEntity()))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(this.game.getState() instanceof VehicleState)
		{
			if(((VehicleState)this.game.getState()).containsVehicle(event.getEntity()))
				event.setCancelled(true);
			else if(this.game.getState() instanceof VehicleSelectionState)
				if(((VehicleSelectionState)this.game.getState()).containsStatue(event.getEntity()))
					event.setCancelled(true);
		}
				
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(this.game.getState() instanceof VehicleState)
			if(((VehicleState)this.game.getState()).containsVehicle(event.getDamager()))
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event)
	{
		if(this.game.getState() instanceof VehicleState)
			if(((VehicleState)this.game.getState()).containsVehicle(event.getEntity()))
				event.setCancelled(true);
	}
}