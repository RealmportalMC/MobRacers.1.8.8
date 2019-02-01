package me.winterguardian.core.game.state;

import java.io.File;

import me.winterguardian.core.game.GameSetup;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class StateGameSetup extends GameSetup
{
	private SerializableLocation lobby, exit;
	private SerializableRegion region;
	
	public StateGameSetup(File file)
	{
		super(file);
	}

	@Override
	protected void load(YamlConfiguration config)
	{
		this.lobby = SerializableLocation.fromString(config.getString("lobby"));
		this.exit = SerializableLocation.fromString(config.getString("exit"));
		this.region = SerializableRegion.fromString(config.getString("region"));
	}

	@Override
	protected void save(YamlConfiguration config)
	{
		if(this.lobby != null)
			config.set("lobby", this.lobby.toString());
		else
			config.set("lobby", null);
		
		if(this.exit != null)
			config.set("exit", this.exit.toString());
		else
			config.set("exit", null);
		
		if(this.region != null)
			config.set("region", this.region.toString());
		else
			config.set("region", null);
	}

	public Location getLobby()
	{
		if(lobby == null)
			return null;
		return lobby.getLocation();
	}

	public void setLobby(Location lobby)
	{
		this.lobby = new SerializableLocation(lobby);
	}

	public Location getExit()
	{
		if(exit == null)
			return null;
		return exit.getLocation();
	}

	public void setExit(Location exit)
	{
		this.exit = new SerializableLocation(exit);
	}

	public SerializableRegion getRegion()
	{
		return region;
	}

	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}

}
