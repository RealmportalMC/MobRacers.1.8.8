package me.winterguardian.core.listener;

import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * Created by Alexander Winter on 2015-12-30.
 */
public class GameSubCommandsBinder implements Listener
{
	private Game game;
	private CommandSplitter command;

	public GameSubCommandsBinder(Game game, CommandSplitter command)
	{
		this.game = game;
		this.command = command;
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		if(!game.contains(event.getPlayer()))
			return;


		String label = event.getMessage().substring(1).split(" ")[0];
		for(SubCommand sub : this.command.getSubCommands())
			if(sub.doesExecute(label))
			{
				event.setMessage("/" + this.command.getName() + " " + event.getMessage().substring(1));
				return;
			}
	}
}
