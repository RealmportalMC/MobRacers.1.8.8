package me.winterguardian.mobracers.listener;

import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameSpectatorData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.state.game.ItemBox;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameListener implements Listener
{
	private MobRacersGame game;
	
	public GameListener(MobRacersGame game)
	{
		this.game = game;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(!this.game.contains(event.getPlayer()) || !(this.game.getState() instanceof GameState))
			return;
		
		GamePlayerData data;
		if((data = ((GameState)this.game.getState()).getPlayerData(event.getPlayer())) == null)
			return;
		
		for(ItemBox item : ((GameState)this.game.getState()).getItemBoxes())
			if(item.collide(data.getVehicle()) && data.pickUpItem())
				item.pickUp();
		
		data.move(event.getTo()); //Attention, peut changer le MobRacers.getPlugin().getState()
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(!(event.getWhoClicked() instanceof Player))
			return;
		
		if(event.getSlot() < 0)
			return;
		
		onClickItem((Player) event.getWhoClicked(), event.getSlot(), true, null);
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(event.getPlayer().getItemInHand() == null)
			return;
	
		onClickItem(event.getPlayer(), event.getPlayer().getInventory().first(event.getPlayer().getItemInHand()), false, null);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getItem() == null)
			return;
		
		onClickItem(event.getPlayer(), event.getPlayer().getInventory().first(event.getItem()), false, event);
	}
	
	public void onClickItem(Player player, int slot, boolean inventoryOpen, Cancellable event)
	{
		if(!this.game.contains(player))
			return;
		
		if(!(this.game.getState() instanceof GameState))
			return;
		
		GamePlayerData playerData = ((GameState)this.game.getState()).getPlayerData(player);
		GameSpectatorData spectatorData = ((GameState)this.game.getState()).getSpectatorData(player);
		
		if(playerData != null)
		{
			if(playerData.getLastItemUse() + 500000000 > System.nanoTime())
				return;
			
			if(playerData.isFinished() && ((MobRacersConfig) this.game.getConfig()).gamemode3Spectators() && !inventoryOpen)
				return;
			
			ItemResult result = playerData.useItem(slot);
			
			if(result.deleteItem() && slot >= 0)
				player.getInventory().clear(slot);
			if(event != null)
				event.setCancelled(!result.useItem());
		}
		else if(spectatorData != null)
		{
			if(((MobRacersConfig) this.game.getConfig()).gamemode3Spectators() && !inventoryOpen)
				return;
			spectatorData.useItem(slot);
			if(event != null)
				event.setCancelled(true);
		}
		
				
		
	}
}
