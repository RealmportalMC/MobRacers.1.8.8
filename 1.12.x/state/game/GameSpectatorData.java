package me.winterguardian.mobracers.state.game;

import me.winterguardian.core.game.PlayerData;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersSetup;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameSpectatorData extends PlayerData
{
	private GameState currentGame;
	private int spectatorLocationId;
	
	private long lastItemUse;
	
	public GameSpectatorData(GameState state, Player p)
	{
		super(p);
		this.currentGame = state;
		this.spectatorLocationId = 0;
		
		this.lastItemUse = 0;
	}
	
	public void useItem(int slot)
	{
		switch(slot)
		{
		case 0:
			useCompass();
			break;
			
		case 8:
			getPlayer().performCommand("mobracers leave");
			break;
		}
	}
	
	public void useCompass()
	{
		if(this.lastItemUse + 1000000000 > System.nanoTime())
			return;
		
		this.spectatorLocationId++;
		if(this.spectatorLocationId >= this.currentGame.getArena().getSpectatorLocations().size())
			this.spectatorLocationId -= this.currentGame.getArena().getSpectatorLocations().size();
		
		this.getPlayer().teleport(this.currentGame.getArena().getSpectatorLocations().get(this.spectatorLocationId).getLocation());
		this.lastItemUse = System.nanoTime();
	}

	@Override
	public void onJoin()
	{
		PlayerUtil.clearInventory(getPlayer());
		getPlayer().getInventory().setItem(0, ((MobRacersSetup)currentGame.getGame().getSetup()).getSpectatorItem());
		getPlayer().getInventory().setItem(8, ((MobRacersSetup)currentGame.getGame().getSetup()).getLeaveItem());
		getPlayer().teleport(this.currentGame.getArena().getSpectatorLocations().get(this.spectatorLocationId).getLocation());
		if(((MobRacersConfig) currentGame.getGame().getConfig()).gamemode3Spectators())
			getPlayer().setGameMode(GameMode.SPECTATOR);
	}

	@Override
	public void onLeave()
	{
		
	}
}
