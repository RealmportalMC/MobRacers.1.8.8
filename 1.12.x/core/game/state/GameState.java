package me.winterguardian.core.game.state;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.game.Arena;
import me.winterguardian.core.game.PlayerData;
import me.winterguardian.core.scoreboard.Board;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.core.util.Weather;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class GameState implements State
{
	private List<PlayerData> playerDatas;
	private StateGame game;
	private Arena arena;
	private StateGameSetup setup;
	private Board board;
	
	public GameState(StateGame game, StateGameSetup setup, Arena arena, Board board)
	{
		this.playerDatas = new ArrayList<PlayerData>();
		this.game = game;
		this.arena = arena;
		this.setup = setup;
		this.board = board;
	}
	
	public void join(Player p)
	{
		PlayerData data = getNewSpectatorData(p);
		if(data == null)
			data = getNewPlayerData(p);
		
		this.playerDatas.add(data);
		if(this.board != null)
			this.board.startDisplay(p);
		
		if(this.getJoinLocation(p) != null)
			p.teleport(this.getJoinLocation(p));
	}
	
	public void leave(Player p)
	{
		
		if(this.board != null)
			this.board.stopDisplay(p);
		
		this.getPlayerData(p).onLeave();
		
		p.teleport(this.setup.getExit());
		
		if(keepPlayerData(p))
			return;
		
		this.playerDatas.remove(this.getPlayerData(p));
	}
	
	public void start()
	{
		for(Player p : this.game.getPlayers())
			playerDatas.add(getNewPlayerData(p));
		
		if(this.arena != null && this.arena.getWeather() != null)
			for(Player p : this.game.getPlayers())
				this.arena.getWeather().apply(p);
			
		if(this.board != null)
			this.board.startDisplay(this.game.getPlayers());
		for(Player p : this.game.getPlayers())
		{
			if(this.game.getConfig().isColorInTab() && getTabHeader(p) != null && getTabFooter(p) != null)
				TabUtil.sendInfos(p, getTabHeader(p), getTabFooter(p));
			if(getPlayerListName(p) != null)
				p.setPlayerListName(getPlayerListName(p));
		}
	}
	
	public void end()
	{
		if(this.arena != null && this.arena.getWeather() != null)
			for(Player p : this.game.getPlayers())
				Weather.reset(p);
	}
	
	protected abstract PlayerData getNewPlayerData(Player p);
	protected abstract PlayerData getNewSpectatorData(Player p);
	protected abstract Location getJoinLocation(Player p);
	protected abstract boolean keepPlayerData(Player p);
	
	public abstract String getTabHeader(Player p);
	public abstract String getTabFooter(Player p);
	public abstract String getPlayerListName(Player p);
	
	public Arena getArena()
	{
		return this.arena;
	}
	
	protected void setArena(Arena arena)
	{
		this.arena = arena;
	}
	
	public StateGame getGame()
	{
		return this.game;
	}
	
	public StateGameSetup getSetup()
	{
		return this.setup;
	}
	
	public List<PlayerData> getPlayerDatas()
	{
		return this.playerDatas;
	}
	
	public PlayerData getPlayerData(Player p)
	{
		for(PlayerData data : new ArrayList<PlayerData>(this.playerDatas))
			if(data.getPlayer() == p)
				return data;
			
		return null;
	}
}