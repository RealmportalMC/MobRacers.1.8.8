package me.winterguardian.mobracers;

import java.io.File;
import java.util.*;

import me.winterguardian.core.game.GameConfig;
import me.winterguardian.core.util.Weather;

import org.bukkit.Bukkit;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MobRacersConfig extends GameConfig
{
	private boolean enableStats, enableMusic, invitationMessage, enableRotation, enableVault, enableMOTD;

	private String exitServer;

	private List<String> endGameCommands;
	private double pointCoefficient;
	
	private int time;
	private boolean timeLocked, raining;

	private int arenaSelectionTimer, vehicleSelectionTimer, fireworksTimer;
	private int scoreboardUpdatePeriod;
	
	private float baseSpeed;
	private List<World> broadcastWorlds;
	
	private boolean keepPlayerStates;
	private boolean updateNotifications;
	private boolean gamemode3Spectators;

	private boolean injectLangFile;
	private String injectFrom;

	private boolean sqlEnabled;
	private String sqlDriver, sqlURL, sqlUser, sqlPassword;

	private boolean cancelSpawning, protectArenasAndLobby;

	private boolean disableVanillaMessages;
	private boolean noVehicleSpawns;

	public MobRacersConfig(File file)
	{
		super(file);
		
		this.enableStats = true;
		this.enableMusic = true;
		this.enableRotation = true;
		this.enableVault = false;
        this.enableMOTD = false;

		this.exitServer = "hub";
		
		this.pointCoefficient = 1;
		this.time = 6000;
		this.timeLocked = true;
		this.raining = false;
		
		this.arenaSelectionTimer = 45;
		this.vehicleSelectionTimer = 30;
		this.fireworksTimer = 5;
		
		this.scoreboardUpdatePeriod = 1;
		
		this.baseSpeed = 1f;
		
		this.broadcastWorlds = new ArrayList<>();
		this.invitationMessage = false;
		
		this.endGameCommands = new ArrayList<>();
		
		this.keepPlayerStates = false;
		this.updateNotifications = true;
		this.gamemode3Spectators = false;
		this.injectLangFile = true;
		this.injectFrom = "langEN.yml";
		
		this.sqlEnabled = false;
		
		this.sqlDriver = null;
		this.sqlURL = null;
		this.sqlUser = null;
		this.sqlPassword = null;

		this.cancelSpawning = true;
		this.protectArenasAndLobby = true;

		this.disableVanillaMessages = false;
		this.noVehicleSpawns = false;
	}

	@Override
	protected void load(YamlConfiguration config)
	{
		super.load(config);
	
		this.arenaSelectionTimer = config.getInt("arena-selection-timer", this.arenaSelectionTimer);
		this.vehicleSelectionTimer = config.getInt("vehicle-selection-timer", this.vehicleSelectionTimer);
		this.fireworksTimer = config.getInt("fireworks-timer", this.fireworksTimer);
	
		this.scoreboardUpdatePeriod = config.getInt("scoreboard-update-period", this.scoreboardUpdatePeriod);
	
		this.baseSpeed = (float) config.getDouble("base-speed", this.baseSpeed);
		this.pointCoefficient = config.getDouble("point-coefficient", this.pointCoefficient);
	
		this.time = config.getInt("time", this.time);
		this.timeLocked = config.getBoolean("time-locked", this.timeLocked);
		this.raining = config.getBoolean("raining", this.raining);
	
		this.enableStats = config.getBoolean("enable-stats", this.enableStats);
		this.enableMusic = config.getBoolean("enable-music", this.enableMusic);
		this.enableRotation = config.getBoolean("enable-rotation", this.enableRotation);
		this.enableVault = config.getBoolean("enable-vault", this.enableVault);
        this.enableMOTD = config.getBoolean("enable-motd", this.enableMOTD);

		this.cancelSpawning = config.getBoolean("cancel-spawning", this.cancelSpawning);
		this.protectArenasAndLobby = config.getBoolean("protect-arenas-and-lobby", this.protectArenasAndLobby);

		this.exitServer = config.getString("exit-server", this.exitServer);
		
		this.invitationMessage = config.getBoolean("invitation-message", this.invitationMessage);
		
		if(config.isList("endgame-commands"))
			this.endGameCommands = config.getStringList("endgame-commands");
		
		if(config.isList("broadcast-worlds"))
		{
			for(String string : config.getStringList("broadcast-worlds"))
			{
				World world = Bukkit.getWorld(string);
				if(world == null)
				{
					Bukkit.getLogger().warning("[MobRacers] Invalid world found in the broadcast world list: \"" + string + "\"");
					continue;
				}
				broadcastWorlds.add(world);
			}
		}
		else if(config.isString("broadcast-worlds"))
		{
			String string = config.getString("broadcast-worlds");
			if(string.equalsIgnoreCase("*"))
				this.addAllWorlds();
			else if(!string.equalsIgnoreCase("null"))
			{
				World world = Bukkit.getWorld(string);
				if(world == null)
					Bukkit.getLogger().warning("[MobRacers] Invalid world found in the broadcast world list: \"" + string + "\"");
				else
					broadcastWorlds.add(world);
			}
				
		}
		
		this.keepPlayerStates = config.getBoolean("keep-player-states", this.keepPlayerStates);
		this.updateNotifications = config.getBoolean("update-notifications", this.updateNotifications);
		this.gamemode3Spectators = config.getBoolean("gamemode-3-spectators", this.gamemode3Spectators);

		this.injectLangFile = config.getBoolean("inject-lang-file", this.injectLangFile);
		this.injectFrom = config.getString("inject-from", this.injectFrom);

		this.sqlEnabled = config.getBoolean("sql-enabled", this.sqlEnabled);
		this.sqlDriver = config.getString("sql-driver", this.sqlDriver);
		this.sqlURL = config.getString("sql-url", this.sqlURL);
		this.sqlUser = config.getString("sql-user", this.sqlUser);
		this.sqlPassword = config.getString("sql-password", this.sqlPassword);

		this.disableVanillaMessages = config.getBoolean("disable-vanilla-messages", this.disableVanillaMessages);
		this.noVehicleSpawns = config.getBoolean("no-vehicle-spawns", this.noVehicleSpawns);
	}
	
	private void addAllWorlds()
	{
		this.broadcastWorlds.addAll(Bukkit.getWorlds());
	}
	
	public Collection<? extends Player> getBroadcastRecipients()
	{
		Set<Player> set = new HashSet<>(MobRacersPlugin.getGame().getPlayers());
		for(World world : this.broadcastWorlds)
			set.addAll(world.getPlayers());
		return set;
	}

	public boolean enableStats()
	{
		return enableStats;
	}

	public boolean enableMusic()
	{
		return enableMusic;
	}
	
	public boolean isInvitationMessage()
	{
		return invitationMessage;
	}
	
	public boolean enableRotation()
	{
		return this.enableRotation;
	}

	public String getExitServer()
	{
		return this.exitServer;
	}

	public boolean enableVault()
	{
		return enableVault;
	}

	public boolean enableMOTD() { return enableMOTD; }

	public List<String> getEndGameCommands()
	{
		return endGameCommands;
	}

	public double getPointCoefficient()
	{
		return pointCoefficient;
	}

	public Weather getLobbyWeather()
	{
		return new Weather(this.time, this.raining ? WeatherType.DOWNFALL : WeatherType.CLEAR, this.timeLocked);
	}

	public int getArenaSelectionTimer()
	{
		return arenaSelectionTimer;
	}

	public int getVehicleSelectionTimer()
	{
		return vehicleSelectionTimer;
	}

	public int getFireworksTimer()
	{
		return fireworksTimer;
	}

	public int getScoreboardUpdatePeriod()
	{
		return scoreboardUpdatePeriod;
	}

	public float getBaseSpeed()
	{
		return baseSpeed;
	}
	
	public boolean keepPlayerStates()
	{
		return this.keepPlayerStates;
	}

	public boolean sendUpdateNotifications()
	{
		return this.updateNotifications;
	}
	
	public boolean gamemode3Spectators()
	{
		return this.gamemode3Spectators;
	}

	public boolean injectLangFile()
	{
		return this.injectLangFile;
	}

	public String getInjectFrom()
	{
		return injectFrom;
	}

	public boolean isSqlEnabled()
	{
		return sqlEnabled;
	}

	public String getSqlDriver()
	{
		return sqlDriver;
	}

	public String getSqlURL()
	{
		return sqlURL;
	}

	public String getSqlUser()
	{
		return sqlUser;
	}

	public String getSqlPassword()
	{
		return sqlPassword;
	}

	public boolean cancelSpawning()
	{
		return this.cancelSpawning;
	}

	public boolean protectArenasAndLobby()
	{
		return this.protectArenasAndLobby;
	}

	public boolean disableVanillaMessages()
	{
		return this.disableVanillaMessages;
	}

	public boolean isNoVehicleSpawns()
	{
		return noVehicleSpawns;
	}
}
