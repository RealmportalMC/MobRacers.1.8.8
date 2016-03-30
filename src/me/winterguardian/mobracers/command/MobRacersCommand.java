package me.winterguardian.mobracers.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.*;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class MobRacersCommand extends CommandSplitter
{
	private MobRacersGame game;
	
	public MobRacersCommand(MobRacersGame game)
	{
		this.game = game;
		this.getSubCommands().add(new JoinGameSubCommand(this.game, "join", Arrays.asList("joindre", "rejoindre", "entrer", "enter"), MobRacersPlugin.PLAY, CourseMessage.COMMAND_INVALID_PERMISSION.toString(), null, CourseMessage.COMMAND_INVALID_SENDER));
		this.getSubCommands().add(new LeaveGameSubCommand(this.game, "leave", Arrays.asList("quitter", "partir", "sortir", "exit", "quit"), null, null, null, CourseMessage.COMMAND_INVALID_SENDER)
		{
			@Override
			public boolean onSubCommand(CommandSender sender, String label, String[] args)
			{
				if(sender instanceof Player && MobRacersCommand.this.game.getConfig().isAutoJoin() && !sender.hasPermission(MobRacersCommand.this.game.getNoAutoJoinPermission()))
				{
					Core.getBungeeMessager().sendToServer((Player)sender, ((MobRacersConfig)MobRacersCommand.this.game.getConfig()).getExitServer());
					return true;
				}

				return super.onSubCommand(sender, label, args);
			}
		});

		this.getSubCommands().add(new VoteSubCommand(this.game));
		this.getSubCommands().add(new SetupSubCommand(this.game));
		this.getSubCommands().add(new ArenaSubCommand());

		if(((MobRacersConfig)game.getConfig()).enableStats())
		{
			this.getSubCommands().add(new AchievementSubCommand());
			this.getSubCommands().add(new StatsSubCommand(game.getPlugin()));
			this.getSubCommands().add(new BuySubCommand());
			this.getSubCommands().add(new RankingSubCommand());
			this.getSubCommands().add(new ConvertUDSubCommand(MobRacersPlugin.ADMIN));
		}

		this.getSubCommands().add(new InfoSubCommand(this.game));
		this.getSubCommands().add(new VersionSubCommand(game.getPlugin()));
		this.getSubCommands().add(new ReloadSubCommand(this.game));
		
		this.getSubCommands().add(new OpenStateGameSubCommand(this.game, "open", Arrays.asList("ouvrir", "ouverture", "opening"), MobRacersPlugin.STAFF, CourseMessage.COMMAND_INVALID_PERMISSION.toString(), null, CourseMessage.COMMAND_OPEN)
		{
			@Override
			public Collection<? extends Player> getRecipients()
			{
				return ((MobRacersConfig)MobRacersCommand.this.game.getConfig()).getBroadcastRecipients();
			}
		});
		this.getSubCommands().add(new CloseStateGameSubCommand(this.game, "close", Arrays.asList("fermer", "fermeture", "ending"), MobRacersPlugin.STAFF, CourseMessage.COMMAND_INVALID_PERMISSION.toString(), null, CourseMessage.COMMAND_CLOSE));
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("course", "tcourse", "race", "circuit", "mobrace", "mobracing", "mr");
	}

	@Override
	public String getDescription()
	{
		return "MobRacers command for playing and management.";
	}

	@Override
	public String getName()
	{
		return "mobracers";
	}

	@Override
	public Permission getPermission()
	{
		return null;
	}

	@Override
	public boolean index(CommandSender sender, Command cmd, String label)
	{
		
		if(!(sender instanceof Player) || game.contains((Player)sender))
			return this.onCommand(sender, cmd, label, new String[]{"info"});

		return this.onCommand(sender, cmd, label, new String[]{"join"});
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		return Arrays.asList(MobRacersPlugin.VIP, MobRacersPlugin.STAFF, MobRacersPlugin.ADMIN, MobRacersPlugin.ALL_UNLOCKED, MobRacersPlugin.PLAY);
	}

	@Override
	public String getUsage()
	{
		return CourseMessage.COMMAND_INVALID_ARGUMENT.toString() + " §f/mobracers help";
	}

}
