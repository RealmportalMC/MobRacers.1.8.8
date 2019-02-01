package me.winterguardian.mobracers.listener;

import me.winterguardian.core.entity.custom.rideable.RideableEntityUtil;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersSetup;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameSpectatorData;
import me.winterguardian.mobracers.state.game.GameState;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class RegionProtectionListener implements Listener
{
	private MobRacersGame game;
	private List<Arena> arenas;
	
	public RegionProtectionListener(MobRacersGame game)
	{
		this.game = game;
		this.arenas = Arena.getArenaList();
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
		
		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onBlockForm(BlockFormEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}

		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onBlockGrow(BlockGrowEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
		
		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onBlockFade(BlockFadeEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
		
		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onBlockFormTo(BlockFromToEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}

		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}

		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onBlockSpread(BlockSpreadEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
		
		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event)
	{
		if(!game.getConfig().protectArenasAndLobby())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getBlock().getLocation()))
		{
			event.setCancelled(true);
			return;
		}
		
		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getBlock().getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;
		
		if(game.getRegion().contains(event.getTo()))
			return;
		
		if(game.getState() instanceof GameState)
		{
			GameSpectatorData data = ((GameState)game.getState()).getSpectatorData(event.getPlayer());
			GamePlayerData player = ((GameState)game.getState()).getPlayerData(event.getPlayer());
		
			if(data != null && player == null)
				data.useCompass();
			else if(player != null && data == null && player.isFinished())
				player.useItem();
			return;
		}
		
		event.getPlayer().teleport(game.getSetup().getLobby());
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event)
	{
		if(!game.getConfig().cancelSpawning())
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getLocation()))
		{
			event.setCancelled(true);
			return;
		}

		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(!game.getConfig().cancelSpawning())
			return;

		if(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM)
			return;

		if(game.getSetup().getRegion() != null && game.getSetup().getRegion().contains(event.getLocation()))
		{
			event.setCancelled(true);
			return;
		}

		for(Arena arena : this.arenas)
		{
			if(arena.getRegion() != null && arena.getRegion().contains(event.getLocation()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}
