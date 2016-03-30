package me.winterguardian.mobracers;

import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.game.GameConfig;
import me.winterguardian.core.game.SekaiGame;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.game.state.StateGameSetup;
import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.core.listener.ServerMOTDListener;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.util.PlayerState;
import me.winterguardian.core.world.Region;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.command.MobRacersCommand;
import me.winterguardian.mobracers.listener.*;
import me.winterguardian.mobracers.pluginsupport.SekaiHubItem;
import me.winterguardian.mobracers.state.MobRacersState;
import me.winterguardian.mobracers.state.lobby.StandbyState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MobRacersGame extends StateGame implements SekaiGame
{
	private MobRacersConfig baseConfig;
	private List<PlayerState> playerStates;
	
	public MobRacersGame(Plugin plugin, MobRacersConfig config)
	{
		super(plugin);
		this.playerStates = new ArrayList<>();
		this.baseConfig = config;
	}

	public void onEnable()
	{
		super.onEnable();
		
		getPlayerListener().setPriority(EventPriority.LOW);
		
		getPlayerListener().setCancelBreak(true);
		getPlayerListener().setCancelPlace(true);
		getPlayerListener().setCancelCombust(true);
		getPlayerListener().setCancelDamage(true);
		getPlayerListener().setCancelDamageOthers(true);
		getPlayerListener().setCancelHunger(true);
		getPlayerListener().setCancelInventoryClick(true);
		getPlayerListener().setCancelInteract(false);
		getPlayerListener().setCancelInteractEntity(false);
		getPlayerListener().setCancelPickupItem(true);
		getPlayerListener().setCancelDropItem(true);
		getPlayerListener().setCancelSneak(false);
		getPlayerListener().setCancelSprint(false);
		
		Bukkit.getPluginManager().registerEvents(new RegionProtectionListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new VehicleProtectionListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new StandbyListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new ArenaSelectionListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new VehicleSelectionListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new ArenaSelectionListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new StandbyListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new GameListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new CommandSignListener(), getPlugin());
		Bukkit.getPluginManager().registerEvents(new UpdateListener(this), getPlugin());
		Bukkit.getPluginManager().registerEvents(new VanillaMessagesListener(this), getPlugin());
        Bukkit.getPluginManager().registerEvents(new ServerMOTDListener()
        {
            @Override
            public String getMOTD()
            {
                return getStatus();
            }

            @Override
            public boolean isEnabled()
            {
				return ((MobRacersConfig)getConfig()).enableMOTD();
            }
        }, getPlugin());
	}
	
	public void savePlayerState(Player player)
	{
		if(((MobRacersConfig) this.getConfig()).keepPlayerStates())
			this.playerStates.add(new PlayerState(player));
	}
	
	public void applyPlayerState(Player player)
	{
		if(!((MobRacersConfig) this.getConfig()).keepPlayerStates())
			return;
		PlayerState toRemove = null;
		for(PlayerState state : this.playerStates)
			if(state.getUniqueId().equals(player.getUniqueId()))
			{
				state.apply();
				toRemove = state;
				break;
			}
		
		this.playerStates.remove(toRemove);
	}
	
	public void onDisable()
	{
		super.onDisable();
	}

	@Override
	public String getChatName(Player p)
	{
		return null;
	}
	
	@Override
	public String getChatPrefix(Player p)
	{
		return "§2§lCourse";
	}

	@Override
	public String getColoredName()
	{
		return "§f§lMob§2§lRacers";
	}

	@Override
	public String getStatus()
	{
		if(this.getState() == null)
			return null;
		return this.getState().getStatus();
	}

	@Override
	public Location getWarp()
	{
		return getSetup().getLobby();
	}
	
	@Override
	protected State getDefaultState()
	{
		return new StandbyState(this);
	}
	
	@Override
	protected CommandSplitter getNewCommand()
	{
		return new MobRacersCommand(this);
	}

	@Override
	protected GameConfig getNewConfig()
	{
		return baseConfig;
	}

	@Override
	protected StateGameSetup getNewSetup()
	{
		return new MobRacersSetup(new File(getPlugin().getDataFolder(), "setup.yml"));
	}

	@Override
	public Message getAlreadyInGameMessage()
	{
		return CourseMessage.JOIN_ALREADYINGAME;
	}

	@Override
	public Message getAutoJoinKickMessage()
	{
		return CourseMessage.JOIN_AUTOJOINKICK;
	}

	@Override
	public Message getCantLeaveInAutoJoinMessage()
	{
		return CourseMessage.LEAVE_CANTLEAVEINAUTOJOIN;
	}

	@Override
	public Message getCommandBlockedMessage()
	{
		return CourseMessage.COMMAND_BLOCKED_IN_GAME;
	}

	@Override
	public Message getGameClosedMessage()
	{
		return CourseMessage.JOIN_GAMECLOSED;
	}

	@Override
	public Message getGameFullMessage()
	{
		return CourseMessage.JOIN_MAXPLAYERS;
	}

	@Override
	public Message getGameNotReadyMessage()
	{
		return CourseMessage.JOIN_GAMENOTREADY;
	}

	@Override
	public Message getJoinMessage()
	{
		return CourseMessage.JOIN;
	}

	@Override
	public Message getLeaveMessage()
	{
		return CourseMessage.LEAVE;
	}

	@Override
	public Message getNotInGameMessage()
	{
		return CourseMessage.LEAVE_NOTINGAME;
	}
	
	protected Message getInvitationMessage()
	{
		if(((MobRacersConfig) this.getConfig()).isInvitationMessage())
			return CourseMessage.JOIN_INVITATIONMESSAGE;
		return null;
	}
	
	@Override
	public Permission getNoAutoJoinPermission()
	{
		return MobRacersPlugin.AUTOJOIN_BYPASS;
	}

	@Override
	public Permission getNoKickAutoJoinPermission()
	{
		return MobRacersPlugin.STAFF;
	}

	@Override
	public Permission getNoTeleportJoinPermission()
	{
		return MobRacersPlugin.AUTOJOIN_BYPASS;
	}
	
	@Override
	public Permission getCommandBlockingBypassPermission()
	{
		return MobRacersPlugin.CMD_PROTECT_BYPASS;
	}

	@Override
	public boolean isReady()
	{
		return isLobbyReady() && Arena.getReadyArenaList().size() > 0;
	}
	
	@Override
	public int getMinPlayers()
	{
		return super.getMinPlayers() < 2 ? 2 : super.getMinPlayers();
	}
	
	@Override
	public int getMaxPlayers()
	{
		return super.getMaxPlayers() == -1 ? Integer.MAX_VALUE : super.getMaxPlayers();
	}
	
	public boolean isLobbyReady()
	{
		if(getConfig() == null || getSetup() == null || !(getConfig() instanceof MobRacersConfig) || !(getSetup() instanceof MobRacersSetup))
			return false;
		
		if(getSetup().getRegion() == null)
			return false;

		if(!((MobRacersConfig)getConfig()).isNoVehicleSpawns())
			for(SerializableLocation loc : ((MobRacersSetup)getSetup()).getVehicleLocations().values())
			{
				if(loc == null)
					return false;
				if(!getSetup().getRegion().contains(loc.getLocation()))
					return false;
			}
		
		return getSetup().getLobby() != null && (getSetup().getExit() != null || getConfig().isAutoJoin() || ((MobRacersConfig) getConfig()).keepPlayerStates());
	}
	
	@Override
	public Region getRegion()
	{
		if(getState() instanceof MobRacersState)
			return ((MobRacersState)getState()).getRegion();
		
		return getSetup().getRegion();
	}

	@Override
	public GUIItem getGUIItem()
	{
		return new SekaiHubItem(this);
	}

	@Override
	public MobRacersConfig getConfig()
	{
		return (MobRacersConfig)super.getConfig();
	}

	@Override
	public MobRacersSetup getSetup()
	{
		return (MobRacersSetup)super.getSetup();
	}
}
