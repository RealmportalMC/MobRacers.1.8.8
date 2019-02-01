package me.winterguardian.mobracers.state.lobby;

import java.util.*;

import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.game.state.WaitingState;
import me.winterguardian.core.inventorygui.InventoryGUI;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.scoreboard.Board;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.scoreboard.UpdatableBoard;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.core.util.Weather;
import me.winterguardian.core.world.Region;
import me.winterguardian.mobracers.*;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.arena.ArenaGUIItem;
import me.winterguardian.mobracers.music.CourseMusic;
import me.winterguardian.mobracers.state.MobRacersState;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaSelectionState extends WaitingState implements MobRacersState
{
	private HashMap<Player, String> votes;

	private InventoryGUI gui;
	
	public ArenaSelectionState(StateGame stateGame)
	{
		super(stateGame, ((MobRacersConfig) stateGame.getConfig()).getArenaSelectionTimer());
		this.votes = new HashMap<>();

		List<Arena> ready = Arena.getReadyArenaList();

		int invSize = ((ready.size() / 7 + (ready.size() % 7 != 0 ? 1 : 0)) + 2) * 9;

		this.gui = new InventoryGUI(CourseMessage.ARENASELECT_GUI_INV.toString(), invSize);

		int i = 0;
		for(Arena arena : ready)
		{
			this.gui.getItems().add(new ArenaGUIItem(this, arena, ((i / 7) + 1) * 9 + (i % 7) + 1));
			i++;
		}
	}

	public InventoryGUI getGui()
	{
		return gui;
	}
	
	@Override
	public void join(Player p)
	{
		((MobRacersGame) getGame()).savePlayerState(p);
		super.join(p);
		CourseMessage.ARENASELECT_STARTORJOIN.say(p);
		if(getGame().getConfig().isColorInTab())
			p.setPlayerListName("§2" + p.getName());

		giveStuff(p);
	}
	
	@Override
	public void leave(Player p)
	{
		if(getGame().getConfig().isColorInTab())
			p.setPlayerListName(null);
		super.leave(p);

		((MobRacersGame) getGame()).applyPlayerState(p);
	}

	public void giveStuff(Player player)
	{

		PlayerUtil.clearInventory(player);

		player.getInventory().setItem(0, ((MobRacersSetup)getGame().getSetup()).getArenaItem());
		player.getInventory().setItem(8, ((MobRacersSetup)getGame().getSetup()).getLeaveItem());
	}
	
	public int getVotes(String arena)
	{
		int votes = 0;
		
		for(Player p : this.votes.keySet())
			if(this.votes.get(p).equalsIgnoreCase(arena))
				votes++;
			
		return votes;
	}
	
	public int getTotalVotes()
	{
		int votes = 0;
		
		for(Player p : this.votes.keySet())
			if(this.votes.get(p) != null)
				votes++;
			
		return votes;
	}
	
	public boolean vote(Player p, String arena)
	{
		String validArena = null;
		for(Arena current : Arena.getReadyArenaList())
			if(current.getName().equalsIgnoreCase(arena))
			{
				validArena = current.getName();
				break;
			}
		
		if(validArena == null)
		{
			CourseMessage.ARENASELECT_INVALIDARENA.say(p);
			return false;
		}
		
		if(this.votes.get(p) != null && this.votes.get(p).equalsIgnoreCase(validArena))
		{
			CourseMessage.ARENASELECT_SAMEARENA.say(p);
			return true;
		}
		
		if(!this.votes.containsKey(p))
			CourseMessage.ARENASELECT_FIRSTVOTE.say(p);
		else
			CourseMessage.ARENASELECT_CHANGEVOTE.say(p);
		
		this.votes.put(p, validArena);
		if(this.getBoard() instanceof UpdatableBoard)
			((UpdatableBoard)this.getBoard()).update();
		for(Player current : getGame().getPlayers())
			if(getGame().getConfig().isColorInTab())
				TabUtil.sendInfos(current, getTabHeader(current), getTabFooter(current));
		
		if(this.votes.size() == getGame().getPlayers().size())
		{
			this.end();
			getGame().setState(getNextState());
			getGame().getState().start();
		}
		
		return true;
	}

	@Override
	public String getStatus()
	{
		return CourseMessage.ARENASELECT_STATUS.toString();
	}

	@Override
	public Board getNewScoreboard()
	{
		return new UpdatableBoard()
		{
			@Override
			protected void update(Player p)
			{
				String[] elements = new String[16];
				elements[0] = CourseMessage.ARENASELECT_BOARD_HEADER.toString();
				
				Arena arena;
				int topLenght;
				boolean onlyVoted;
				
				if(votes.containsKey(p))
				{
					arena = new Arena(votes.get(p));
					arena.load();
					
					elements[1] = CourseMessage.ARENASELECT_BOARD_ARENA.toString();
					elements[2] = arena.getName();
					elements[3] = " ";
					elements[4] = CourseMessage.ARENASELECT_BOARD_ARENAAUTHOR.toString();
					elements[5] = arena.getAuthor();
					elements[6] = "  ";
					elements[7] = CourseMessage.ARENASELECT_BOARD_OTHERVOTES.toString();
					
					topLenght = 8;
					onlyVoted = true;
				}
				else
				{
					arena = null;
					topLenght = 14;
					onlyVoted = false;
					elements[1] = CourseMessage.ARENASELECT_BOARD_OTHERVOTES.toString();
				}
				
				for(int i = 0; i < topLenght; i++)
					{
						Arena bestArena = null;
						int bestArenaVotes = -1;
						for(Arena current : Arena.getReadyArenaList())
						{
							boolean alreadyInTop = false;
							for(int j = 0; j < topLenght; j++)
								if(elements[j + 16 - topLenght] != null && elements[j + 16 - topLenght].substring(2).startsWith(current.getName()))
								{
									alreadyInTop = true;
									break;
								}
							if(alreadyInTop)
								continue;
							
							int votes = getVotes(current.getName());
							if(votes > bestArenaVotes)
							{
								bestArena = current;
								bestArenaVotes = votes;
							}
						}
						if(bestArena == null || (bestArenaVotes == 0 && onlyVoted))
							break;
						
						int percentage = Math.round((float)bestArenaVotes / (float)getTotalVotes() * 100f);
						String color = "§8";
						
						if(percentage >= 10)
							color = "§4";
						
						if(percentage >= 25)
							color = "§c";
							
						if(percentage >= 50)
							color = "§e";
						
						if(percentage >= 75)
							color = "§a";
							
						if(percentage >= 100)
							color = "§2";
						
						elements[i + 16 - topLenght] = (arena == null || bestArena.getName().equalsIgnoreCase(arena.getName()) ? "§f" : "§7") + bestArena.getName() + " " + color + percentage + "%";
					}
				
				ScoreboardUtil.unrankedSidebarDisplay(p, elements);
			}
		};
	}

	@Override
	public String getTabHeader(Player p)
	{
		String name = p.getName();
		
		if(this.getGame().getConfig() != null && this.getGame().getConfig().useDisplaynames())
			name = p.getDisplayName();
		
		return JsonUtil.toJson(CourseMessage.ARENASELECT_TABHEADER.toString("<player>", name));
	}
	
	@Override
	public String getTabFooter(Player p)
	{
		String name = p.getName();
		
		if(this.getGame().getConfig() != null && this.getGame().getConfig().useDisplaynames())
			name = p.getDisplayName();
		
		if(!this.votes.containsKey(p))
			return JsonUtil.toJson(CourseMessage.ARENASELECT_TABFOOTER_NOVOTE.toString("<player>", name));
		else
			return JsonUtil.toJson(CourseMessage.ARENASELECT_TABFOOTER_VOTED.toString("<player>", name, "<arena>", this.votes.get(p)));
	}

	@Override
	public boolean keepScoreboardAndWeather()
	{
		return true;
	}

	@Override
	protected Message getCountdownFinishMessage()
	{
		return Message.NULL;
	}

	@Override
	protected Message getCountdownMessage(int i)
	{
		return CourseMessage.ARENASELECT_TIMER;
	}

	@Override
	public void end()
	{
		super.end();
		HandlerList.unregisterAll(this.getGui());
	}

	@Override
	protected State getNextState()
	{
		List<String> arenas = new ArrayList<>();


		for(Player p : this.votes.keySet())
			arenas.add(this.votes.get(p));


		Arena arena;

		if(arenas.size() != 0)
		{
			arena = new Arena(arenas.get(new Random().nextInt(arenas.size())));
			arena.load();
		}
		else
			arena = Arena.getReadyArenaList().get(new Random().nextInt(Arena.getReadyArenaList().size()));

		CourseMessage.ARENASELECT_ARENASELECTED.sayPlayers("<arena>", arena.getName());

		return new VehicleSelectionState(this.getGame(), arena);
	}

	@Override
	protected State getStandbyState()
	{
		return new StandbyState(this.getGame());
	}

	@Override
	protected Message getStartMessage()
	{
		return CourseMessage.ARENASELECT_STARTORJOIN;
	}
	
	public Weather getPlayerWeather(Player p)
	{
		return ((MobRacersConfig) getGame().getConfig()).getLobbyWeather();
	}
	
	@Override
	public Region getRegion()
	{
		return this.getGame().getSetup().getRegion();
	}

	@Override
	public void start()
	{
		super.start();
		Bukkit.getPluginManager().registerEvents(this.getGui(), this.getGame().getPlugin());

		for(Player player : getGame().getPlayers())
			giveStuff(player);

		try
		{
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					if(MobRacersPlugin.getGame().getSetup().getRegion() != null)
						for(Entity entity : MobRacersPlugin.getGame().getSetup().getRegion().getMinimum().getWorld().getEntities())
							if(MobRacersPlugin.getGame().getSetup().getRegion().contains(entity.getLocation()))
								if(!(entity instanceof Player) && entity instanceof LivingEntity)
									entity.remove();
				}
			}, 20);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
