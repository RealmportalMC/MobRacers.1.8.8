package me.winterguardian.core.command;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import me.winterguardian.core.Core;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.playerstats.MappedData;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public abstract class PlayerStatsAsyncSubCommand extends AsyncSubCommand
{
	private Message invalidSender, invalidPlayer;
	
	public PlayerStatsAsyncSubCommand(Plugin plugin)
	{
		this(plugin, "stats", Arrays.asList("statistiques", "skill", "skills", "score", "scores"), null, null, null);
	}
	
	public PlayerStatsAsyncSubCommand(Plugin plugin, String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		this(plugin, name, aliases, permission, permissionMessage, usage, ErrorMessage.COMMAND_INVALID_SENDER, ErrorMessage.COMMAND_INVALID_PLAYER);
	}
	
	public PlayerStatsAsyncSubCommand(Plugin plugin, String name, List<String> aliases, Permission permission, String permissionMessage, String usage, Message invalidSender, Message invalidPlayer)
	{
		super(plugin, name, aliases, permission, permissionMessage, usage);
		this.invalidSender = invalidSender;
		this.invalidPlayer = invalidPlayer;
	}

	@Override
	protected void onAsyncSubCommand(CommandSender sender, String label, String[] args)
	{
		if(args.length <= 0)
		{
			if(!(sender instanceof Player))
			{
				invalidSender.say(sender);
				return;
			}
			displayStats(sender, ((Player) sender).getUniqueId(), Core.getUserDatasManager().getUserData((Player) sender));
			return;
		}
		else
		{
            Player player;
            if((player = Bukkit.getPlayer(args[0])) != null)
            {
                displayStats(sender, player.getUniqueId(), Core.getUserDatasManager().getUserData(player));
                return;
            }

			Entry<UUID, MappedData> entry = Core.getUserDatasManager().getFirstByValue("core.name", args[0]);
			
			if(entry == null)
			{
				sender.sendMessage(invalidPlayer.toString());
				return;
			}
			displayStats(sender, entry.getKey(), entry.getValue());
			return;
		}
	}
	

	public abstract void displayStats(CommandSender sender, UUID player, MappedData data);
}
