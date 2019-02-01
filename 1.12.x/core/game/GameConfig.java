package me.winterguardian.core.game;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class GameConfig extends Config
{
	private boolean autoJoin, teleportJoin, colorInTab, blockCommands, useDisplaynames, bindSubCommands;
	private int maxPlayers, minPlayers;
	
	private ArrayList<String> allowedCommands;
	
	public GameConfig(File file)
	{
		super(file);

		this.minPlayers = 2;
		this.maxPlayers = 8;

		this.allowedCommands = new ArrayList<>();

		this.autoJoin = false;
		this.teleportJoin = false;
		this.colorInTab = true;
		this.blockCommands = true;
		this.useDisplaynames = false;
		this.bindSubCommands = true;
	}

	@Override
	protected void load(YamlConfiguration config)
	{
		this.autoJoin = config.getBoolean("auto-join", this.autoJoin);
		this.teleportJoin = config.getBoolean("teleport-join", this.teleportJoin);
		this.colorInTab = config.getBoolean("color-in-tab", this.colorInTab);
		this.blockCommands = config.getBoolean("block-commands", this.blockCommands);
		this.useDisplaynames = config.getBoolean("use-displaynames", this.useDisplaynames);
		if(config.isList("allowed-commands"))
			this.allowedCommands = new ArrayList<>(config.getStringList("allowed-commands"));
		this.minPlayers = config.getInt("min-players", this.minPlayers);
		this.maxPlayers = config.getInt("max-players", this.maxPlayers);
		this.bindSubCommands = config.getBoolean("bind-subcommands", this.bindSubCommands);
	}

	public boolean isAutoJoin()
	{
		return autoJoin;
	}

	public boolean isTeleportJoin()
	{
		return teleportJoin;
	}

	public boolean isColorInTab()
	{
		return colorInTab;
	}

	public boolean blockCommands()
	{
		return blockCommands;
	}

	public ArrayList<String> getAllowedCommands()
	{
		return allowedCommands;
	}
	
	public int getMinimumPlayers()
	{
		return this.minPlayers;
	}
	
	public int getMaximumPlayers()
	{
		return this.maxPlayers;
	}

	public boolean useDisplaynames()
	{
		return useDisplaynames;
	}

	public boolean bindSubCommands() { return bindSubCommands; }
}
