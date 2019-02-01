package me.winterguardian.core.game.state;

import me.winterguardian.core.message.Message;

import org.bukkit.entity.Player;

public abstract class StandbyState extends LobbyState
{
	public StandbyState(StateGame game)
	{
		super(game);
	}

	@Override
	public void join(Player p)
	{
		super.join(p);
		if(getGame().getPlayers().size() >= getGame().getMinPlayers())
		{
			this.end();
			getGame().setState(getNextState());
			getGame().getState().start();
		}
		else
			getNotEnoughtPlayersToPlayMessage().say(p, "<current-players>", getGame().getPlayers() + "", "<min>", getGame().getMinPlayers() + "", "<max>", getGame().getMaxPlayers() + "");
	}
	
	protected abstract State getNextState();
	protected abstract Message getNotEnoughtPlayersToPlayMessage();
	protected abstract Message getNotEnoughtPlayersToStartMessage();
	
	@Override
	public void start()
	{
		super.start();
		getNotEnoughtPlayersToStartMessage().say(getGame().getPlayers(), "<current-players>", getGame().getPlayers() + "", "<min>", getGame().getMinPlayers() + "", "<max>", getGame().getMaxPlayers() + "");
	}

}