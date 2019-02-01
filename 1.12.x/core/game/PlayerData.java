package me.winterguardian.core.game;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;


public abstract class PlayerData
{
	private UUID player;

	public PlayerData(OfflinePlayer player)
	{
		this.player = player.getUniqueId();
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(player);
	}

	public UUID getUUID()
	{
		return player;
	}

	public abstract void onJoin();
	public abstract void onLeave();
}
