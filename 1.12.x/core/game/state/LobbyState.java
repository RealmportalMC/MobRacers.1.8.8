package me.winterguardian.core.game.state;

import me.winterguardian.core.scoreboard.Board;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.core.util.Weather;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class LobbyState implements State
{
	private StateGame game;
	private Board board;
	
	public abstract String getTabHeader(Player p);
	public abstract String getTabFooter(Player p);

	public abstract Board getNewScoreboard();
	public abstract boolean keepScoreboardAndWeather();
	
	public LobbyState(StateGame game)
	{
		this.game = game;
		this.board = getNewScoreboard();
	}
	
	public void prepare(Player p, boolean joining)
	{
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
	}
	
	@Override
	public void join(Player p)
	{
		if(getLobby(p) != null)
			p.teleport(getLobby(p));
		
		if(getPlayerWeather(p) != null)
			getPlayerWeather(p).apply(p);

		this.prepare(p, true);
		if(this.getBoard() != null)
			this.getBoard().startDisplay(p);
		
		if(this.game.getConfig().isColorInTab())
			TabUtil.sendInfos(p, getTabHeader(p), getTabFooter(p));
	}

	@Override
	public void leave(Player p)
	{
		if(getPlayerWeather(p) != null)
			Weather.reset(p);
		this.prepare(p, false);
		if(this.getBoard() != null)
			this.getBoard().stopDisplay(p);
		if(this.game.getConfig().isColorInTab())
			TabUtil.resetTab(p);
		
		if(getExit(p) != null)
			p.teleport(getExit(p));
	}
	
	@Override
	public void start()
	{
		for(Player p : getGame().getPlayers())
		{
			if(this.getPlayerWeather(p) != null)
				this.getPlayerWeather(p).apply(p);
			
			if(this.getBoard() != null)
				this.getBoard().startDisplay(p);
			
			if(this.game.getConfig().isColorInTab())
				TabUtil.sendInfos(p, getTabHeader(p), getTabFooter(p));
		}
	}
	
	@Override
	public void end()
	{
		if(this.keepScoreboardAndWeather())
			return;
		
		for(Player p : getGame().getPlayers())
		{
			if(this.getPlayerWeather(p) != null)
				Weather.reset(p);
			
			if(this.getBoard() != null)
				this.getBoard().stopDisplay(p);
		}
	}
	
	public Location getLobby(Player p)
	{
		return this.game.getSetup().getLobby();
	}
	
	public Location getExit(Player p)
	{
		return this.game.getSetup().getExit();
	}
	
	public Weather getPlayerWeather(Player p)
	{
		return null;
	}
	
	public StateGame getGame()
	{
		return this.game;
	}
	
	protected Board getBoard()
	{
		return this.board;
	}
}
