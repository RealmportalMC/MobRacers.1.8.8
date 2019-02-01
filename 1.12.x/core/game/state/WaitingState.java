package me.winterguardian.core.game.state;

import me.winterguardian.core.message.Message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class WaitingState extends LobbyState implements Runnable
{
	protected abstract State getStandbyState();
	protected abstract State getNextState();
	protected abstract Message getCountdownMessage(int seconds); 
	protected abstract Message getStartMessage(); 
	protected abstract Message getCountdownFinishMessage(); 
	
	private int taskId;
	protected int time;
	
	public WaitingState(StateGame game, int time)
	{
		super(game);
		this.taskId = -1;
		this.time = time;
	}

	@Override
	public void leave(Player p)
	{
		super.leave(p);
		if(getGame().getPlayers().size() < getGame().getMinPlayers())
		{
			this.end();
			getGame().setState(getStandbyState());
			getGame().getState().start();
		}
	}
	
	@Override
	public void start()
	{
		super.start();
		this.taskId = Bukkit.getScheduler().runTaskTimer(getGame().getPlugin(), this, 0, this.getSecondDuration()).getTaskId();
	}
	
	@Override
	public void end()
	{
		super.end();
		if(this.taskId != -1)
		{
			Bukkit.getScheduler().cancelTask(this.taskId);
			this.taskId = -1;
		}
	}
	
	@Override
	public void run()
	{
		if(this.time == 0)
		{
			if(getCountdownFinishMessage() != null)
				getCountdownFinishMessage().say(getGame().getPlayers());
			this.end();
			getGame().setState(getNextState());
			getGame().getState().start();
		}
		else
		{
			getCountdownMessage(this.time).say(getGame().getPlayers(), "<time>", "" + this.time, "<plural>", (this.time > 1 ? "s" : ""), "<plurial>", (this.time > 1 ? "s" : ""));
		}
		this.time--;
	}
	
	protected int getSecondDuration()
	{
		return 20;
	}
}
