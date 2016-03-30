package me.winterguardian.mobracers.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.state.lobby.VehicleSelectionState;

public class VehicleSelectionListener implements Listener
{
	private MobRacersGame game;
	
	public VehicleSelectionListener(MobRacersGame game)
	{
		this.game = game;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(!this.game.getPlayers().contains(event.getPlayer()))
			return;
		
		if(this.game.getState() instanceof VehicleSelectionState)
			if(((VehicleSelectionState) this.game.getState()).containsStatue(event.getRightClicked()))
				((VehicleSelectionState) this.game.getState()).onChoose(event.getPlayer(), event.getRightClicked());
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!this.game.getPlayers().contains(event.getPlayer()))
			return;
		
		if(!(this.game.getState() instanceof VehicleSelectionState))
			return;

		if(event.getAction() == Action.PHYSICAL)
			return;

		if(event.getItem() == null)
			return;

		switch(event.getPlayer().getInventory().getHeldItemSlot())
		{
			case 0:
				((VehicleSelectionState) this.game.getState()).openGUI(event.getPlayer());
				break;

			case 1:
				((VehicleSelectionState) this.game.getState()).changeMusic(event.getPlayer(), event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
				break;

			case 8:
				event.getPlayer().performCommand("mobracers leave");
		}

	}
}
