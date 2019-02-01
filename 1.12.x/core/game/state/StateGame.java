package me.winterguardian.core.game.state;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.winterguardian.core.command.CommandSplitter;
import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.GameConfig;
import me.winterguardian.core.game.GameManager;
import me.winterguardian.core.listener.*;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.world.Region;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public abstract class StateGame implements Game
{
	private Plugin plugin;
	private List<Player> players;
	private State state;
	private CommandSplitter command;
	private StateGameSetup setup;
	private GameConfig config;
	private PlayerProtectionListener playerListener;
	private long lastInvitation;
	
	private boolean open;
	
	public StateGame(Plugin plugin)
	{
		this.plugin = plugin;
		this.lastInvitation = 0;
	}
	
	public void onEnable()
	{
		GameManager.registerGame(this);
		this.players = new ArrayList<>();
		this.playerListener = new PlayerProtectionListener()
		{

			@Override
			public boolean isAffected(Player player)
			{
				return contains(player);
			}
			
		};
		
		this.config = this.getNewConfig();
		if(this.config != null && !this.config.isLoaded())
			this.config.load();
		
		this.setup = this.getNewSetup();
		if(this.setup != null)
		{
			this.setup.load();
			if(this.setup.getRegion() != null)
				Bukkit.getPluginManager().registerEvents(new SimpleRegionProtectionListener(this.setup.getRegion()), this.plugin);
		}
		
		this.command = getNewCommand();
		if(this.command != null)
			this.command.register(this.plugin);
		
		this.state = getDefaultState();
		if(this.state != null)
			this.state.start();
		
		Bukkit.getPluginManager().registerEvents(this.playerListener, this.plugin);
		
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler
			public void onPlayerQuit(PlayerQuitEvent event)
			{
				if(contains(event.getPlayer()))
					leave(event.getPlayer());
			}
		}, this.plugin);

		if(this.config != null && this.config.bindSubCommands())
			Bukkit.getPluginManager().registerEvents(new GameSubCommandsBinder(this, this.command), this.plugin);
		
		Bukkit.getPluginManager().registerEvents(new CommandBlockerListener()
		{

			@Override
			public boolean isEnabled()
			{
				return config.blockCommands();
			}

			@Override
			public Permission getCommandBlockingBypassPermission()
			{
				return StateGame.this.getCommandBlockingBypassPermission();
			}

			@Override
			public boolean isAffected(Player p)
			{
				return players.contains(p);
			}

			@Override
			public Collection<String> getAllowedCommands()
			{
				List<String> list = new ArrayList<String>(config.getAllowedCommands());
				
				if(command != null)
					list.addAll(command.getAliasesWithName());
						
				return list;
			}

			@Override
			public Message getCommandBlockedMessage()
			{
				return StateGame.this.getCommandBlockedMessage();
			}
			
		}, this.plugin);
		
		Bukkit.getPluginManager().registerEvents(new GameAutoJoinListener(this)
		{

			@Override
			public boolean isEnabled()
			{
				return config.isAutoJoin();
			}

			@Override
			public Permission getNoAutoJoinPermission()
			{
				return StateGame.this.getNoAutoJoinPermission();
			}

			@Override
			public Permission getNoKickPermission()
			{
				return StateGame.this.getNoKickAutoJoinPermission();
			}

			@Override
			public Message getKickMessage()
			{
				return getAutoJoinKickMessage();
			}
			
		}, plugin);
		
		Bukkit.getPluginManager().registerEvents(new GameTeleportJoinListener(this)
		{

			@Override
			public boolean isEnabled()
			{
				if(StateGame.this.config == null)
					return false;
				return StateGame.this.config.isTeleportJoin();
			}

			@Override
			public Region getRegion()
			{
				return StateGame.this.getRegion();
			}

			@Override
			public Permission getNoTeleportJoinPermission()
			{
				return StateGame.this.getNoTeleportJoinPermission();
			}
			
		}, plugin);
		
		this.open = false;
	}

	public void onDisable()
	{
		HandlerList.unregisterAll(this.plugin);
		
		for(Player p : new ArrayList<>(this.players))
			this.leave(p);
		
		this.state = null;
		
		if(this.setup != null)
			this.setup.save();
		this.setup = null;
		
		this.config = null;

		this.command.unregister();
		this.command = null;
		
		this.players = null;
		GameManager.unregisterGame(this);
	}
	
	@Override
	public boolean contains(Player p)
	{
		return players.contains(p);
	}

	@Override
	public void join(Player p)
	{
		if(this.contains(p))
		{
			this.getAlreadyInGameMessage().say(p);
			return;
		}

		if(!this.open)
		{
			this.getGameClosedMessage().say(p);
			return;
		}
		
		if(!this.isReady())
		{
			this.getGameNotReadyMessage().say(p);
			return;
		}
		
		if(this.players.size() >= this.getMaxPlayers())
		{
			this.getGameFullMessage().say(p, "<max>", "" + this.getMaxPlayers(), "<min>", "" + this.getMinPlayers());
			return;
		}

		GameManager.leaveAll(p);
		
		players.add(p);
		
		String name = p.getName();
		
		if(this.config != null && this.config.useDisplaynames())
			name = p.getDisplayName();
		
		this.getJoinMessage().say(this.players, "<player>", name);
		
		if(state != null)
			this.state.join(p);
		
		if(this.getInvitationMessage() != null && this.players.size() < this.getMinPlayers() && this.lastInvitation + this.getInvitationMessageAntiSpamDelay() * 1000 < System.currentTimeMillis())
		{
			this.getInvitationMessage().say(this.getInvitationMessageRecipients(), "<player>", name);
			this.lastInvitation = System.currentTimeMillis();
		}
	}

	@Override
	public void leave(Player p)
	{
		if(!this.contains(p))
		{
			this.getNotInGameMessage().say(p);
			return;
		}
		
		String name = p.getName();
		
		if(this.config != null && this.config.useDisplaynames())
			name = p.getDisplayName();
		
		this.getLeaveMessage().say(players, "<player>", name);
		players.remove(p);
		this.state.leave(p);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Location getWarp()
	{
		if(setup != null)
			return setup.getLobby();
		
		return null;
	}
	
	@Override
	public String getStatus()
	{
		if(state == null)
			return null;
		
		return state.getStatus();
	}
	@SuppressWarnings("deprecation")
	public String getChatName(Player p)
	{
		if(this.config != null && this.config.useDisplaynames())
			return p.getDisplayName();
		return p.getName();
	}
	
	public File getDataFolder()
	{
		return this.plugin.getDataFolder();
	}
	
	public Plugin getPlugin()
	{
		return this.plugin;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public CommandSplitter getCommand()
	{
		return command;
	}

	public void setCommand(CommandSplitter command)
	{
		this.command = command;
	}

	public PlayerProtectionListener getPlayerListener()
	{
		return playerListener;
	}
	
	public boolean isOpen()
	{
		return open;
	}

	public void setOpen(boolean open)
	{
		this.open = open;
	}
	
	public StateGameSetup getSetup()
	{
		return setup;
	}

	public GameConfig getConfig()
	{
		return config;
	}
	
	public int getMaxPlayers()
	{
		if(this.config != null)
			return this.config.getMaximumPlayers();
		
		return 0;
	}
	
	public int getMinPlayers()
	{
		if(this.config != null)
			return this.config.getMinimumPlayers();
		
		return 0;
	}
	
	protected int getInvitationMessageAntiSpamDelay()
	{
		return 30;
	}
	
	protected Collection<? extends Player> getInvitationMessageRecipients()
	{
		return Bukkit.getOnlinePlayers();
	}
	
	protected Region getRegion()
	{
		if(setup != null)
			return setup.getRegion();
		
		return null;
	}

	protected abstract boolean isReady();
	
	protected abstract CommandSplitter getNewCommand();
	protected abstract GameConfig getNewConfig();
	protected abstract StateGameSetup getNewSetup();
	protected abstract State getDefaultState();
	
	public abstract Message getJoinMessage();
	public abstract Message getLeaveMessage();
	public abstract Message getAlreadyInGameMessage();
	public abstract Message getNotInGameMessage();
	public abstract Message getGameFullMessage();
	public abstract Message getGameNotReadyMessage();
	public abstract Message getGameClosedMessage();
	public abstract Message getCommandBlockedMessage();
	public abstract Message getAutoJoinKickMessage();
	public abstract Message getCantLeaveInAutoJoinMessage();
	
	protected Message getInvitationMessage()
	{
		return null;
	}
	
	public abstract Permission getCommandBlockingBypassPermission();
	public abstract Permission getNoKickAutoJoinPermission();
	public abstract Permission getNoAutoJoinPermission();
	public abstract Permission getNoTeleportJoinPermission();
}
