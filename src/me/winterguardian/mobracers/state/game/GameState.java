package me.winterguardian.mobracers.state.game;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.util.FireworkUtil;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.TabUtil;
import me.winterguardian.core.util.TitleUtil;
import me.winterguardian.core.world.MultiRegion;
import me.winterguardian.core.world.Region;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.music.CourseMusic;
import me.winterguardian.mobracers.state.VehicleState;
import me.winterguardian.mobracers.state.lobby.ArenaSelectionState;
import me.winterguardian.mobracers.state.lobby.StandbyState;
import me.winterguardian.mobracers.vehicle.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameState implements VehicleState
{
	private List<GameSpectatorData> spectatorDatas;
	private List<GamePlayerData> playerDatas;
	private Arena arena;
	private long raceStart;
	private int task, forceMountTask, scoreboardTask;
	private int fireworksTask;
	private boolean started;
	
	private List<ItemBox> itemBoxes;
	private List<Item> items;
	
	private List<BlockState> arenaBackup;
	
	private List<Player> forceMountExceptions;
	
	private StateGame game;
	
	public GameState(StateGame game, Arena arena, HashMap<Player, Vehicle> vehicles, HashMap<Player, CourseMusic> musics)
	{
		this.game = game;
		this.task = -1;
		this.fireworksTask = -1;
		this.scoreboardTask = -1;
		
		this.arenaBackup = new ArrayList<>();
		
		this.arena = arena;
		this.started = false;
		
		this.itemBoxes = new ArrayList<>();
		
		for(SerializableLocation loc : arena.getItems())
			this.itemBoxes.add(new ItemBox(loc.getLocation(), this.arena.getItemRegenDelay()));
		
		this.items = new ArrayList<>();
		this.playerDatas = new ArrayList<>();
		this.spectatorDatas = new ArrayList<>();
		for(Player p : vehicles.keySet())
			this.playerDatas.add(new GamePlayerData(this, p, vehicles.get(p), musics.get(p)));
		
		this.forceMountExceptions = new ArrayList<>();
	}
	
	
	@Override
	public void join(Player p)
	{
		((MobRacersGame) getGame()).savePlayerState(p);
		p.teleport(getGame().getSetup().getLobby());
		
		p.setPlayerTime(this.arena.getTime(), !this.arena.isTimeLocked());
		if(this.arena.isRaining())
			p.setPlayerWeather(WeatherType.DOWNFALL);
		else
			p.setPlayerWeather(WeatherType.CLEAR);
		if(getGame().getConfig().isColorInTab())
			p.setPlayerListName("§2§o" + p.getName());
		PlayerUtil.clearBoard(p);
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		
		GameSpectatorData data = new GameSpectatorData(this, p);
		
		this.spectatorDatas.add(data);
		data.onJoin();
		
		CourseMessage.GAME_SPECTATORJOIN.say(p);
		
		this.updateTab();
	}

	@Override
	public void leave(Player p)
	{
		boolean end = false;
		if(this.getPlayerData(p) != null)
		{
			this.getPlayerData(p).onLeave();
			if(!this.getPlayerData(p).isFinished())
			{
				if(this.getPosition(p) == this.getPlayerDatas().size())
					end = this.getFinishedPlayers() == this.getPlayerDatas().size() - 1;
				this.playerDatas.remove(this.getPlayerData(p));
			}
			
		}
		
		if(this.getSpectatorData(p) != null)
			this.playerDatas.remove(this.getSpectatorData(p));
		
		p.resetPlayerTime();
		p.resetPlayerWeather();
		
		PlayerUtil.clearBoard(p);
		PlayerUtil.clearInventory(p);
		PlayerUtil.heal(p);
		PlayerUtil.prepare(p);
		if(getGame().getConfig().isColorInTab())
		{
			TabUtil.resetTab(p);
			p.setPlayerListName(null);
		}
		
		if(game.getSetup().getExit() != null)
			p.teleport(game.getSetup().getExit());
		
		if(this.playerDatas.size() < game.getMinPlayers())
		{
			for(GamePlayerData data : this.playerDatas)
				data.getVehicle().remove();
			
			for(Player current : game.getPlayers())
			{
				if(((MobRacersConfig)game.getConfig()).enableRotation())
				{
					PlayerUtil.clearInventory(current);
					PlayerUtil.heal(current);
					PlayerUtil.prepare(current);
					current.teleport(game.getSetup().getLobby());
				}
				else if(!game.getConfig().isAutoJoin())
				{
					game.leave(current);
				}
				else
					Core.getBungeeMessager().sendToServer(current, ((MobRacersConfig)game.getConfig()).getExitServer());
			}
			
			clearArena();
			
			Bukkit.getScheduler().cancelTask(this.task);
			Bukkit.getScheduler().cancelTask(this.forceMountTask);
			Bukkit.getScheduler().cancelTask(this.fireworksTask);
			Bukkit.getScheduler().cancelTask(this.scoreboardTask);
			
			if(game.getPlayers().size() < game.getMinPlayers())
			{
				game.setState(new StandbyState(getGame()));
				game.getState().start();
			}
			else
			{
				game.setState(new ArenaSelectionState(getGame()));
				game.getState().start();
			}
		}
		else if(end)
		{
			this.end();
		}
		else
		{
			this.updateTab();
		}
		((MobRacersGame) getGame()).applyPlayerState(p);
	}

	@Override
	public void start()
	{
		for(ItemBox item : this.itemBoxes)
			item.spawn();
		
		for(GamePlayerData data : this.playerDatas)
		{
			data.playMusic();
			PlayerUtil.clearInventory(data.getPlayer());
		}
		
		this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new GameTask(this.getArena().getRaceTimeLimit()), 0, 20);
		this.forceMountTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new ForceMountTask(), 0, 1);
		this.scoreboardTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(game.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				for(Player p : game.getPlayers())
					updateBoard(p);
			}
		}, 0, ((MobRacersConfig)this.getGame().getConfig()).getScoreboardUpdatePeriod());
		this.updateTab();
	}

	public boolean isInRegion(Location loc)
	{
		return game.getSetup().getRegion().contains(loc) || this.arena.getRegion().contains(loc);
	}

	@Override
	public String getStatus()
	{
		return CourseMessage.GAME_STATUS.toString().replace("<arena>", this.arena.getName()).replace("<author>", this.arena.getAuthor());
	}
	
	public void clearArena()
	{
		for(Item item : this.items)
			try
			{
				item.cancel();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		
		for(BlockState state : this.arenaBackup)
			state.update(true, false);
		
		for(ItemBox item : this.itemBoxes)
			item.remove();
	}
	
	public void end()
	{
		for(GamePlayerData data : this.playerDatas)
		{
			if(!data.isFinished())
				data.forceFinish();
			data.getVehicle().remove();
		}
		
		clearArena();
		
		Bukkit.getScheduler().cancelTask(this.task);
		Bukkit.getScheduler().cancelTask(forceMountTask);
		
		CourseMessage.GAME_END.sayPlayers();
		
		if(this.getPlayer(1) == null)
		{
			new Exception("NO PLAYER IN FIRST POSITION MOBRACERS").printStackTrace();
			for(GamePlayerData data : this.playerDatas)
				Bukkit.getLogger().warning(data.getPlayer().getName() + " p" + data.getPosition() + " t" + data.getRaceTime());
		}
		else for(Player player : MobRacersPlugin.getGame().getPlayers())
			TitleUtil.displayTitle(player, JsonUtil.toJson(CourseMessage.GAME_TITLE_LINE1.toString().replace("<player>", this.getPlayer(1).getName())), JsonUtil.toJson(CourseMessage.GAME_TITLE_LINE2.toString().replace("<player>", this.getPlayer(1).getName())), 15, 65, 20);
			
		for(String command : ((MobRacersConfig) game.getConfig()).getEndGameCommands())
		{
			try
			{
				String first = "null", second = "null", third = "null", last = "null";
				
				if(this.getPlayer(1) != null)
					first = this.getPlayer(1).getName();
				
				if(this.getPlayer(2) != null)
					second = this.getPlayer(2).getName();
				
				if(this.getPlayer(3) != null)
					third = this.getPlayer(3).getName();
				
				if(this.getPlayer(this.playerDatas.size()) != null)
					last = this.getPlayer(this.playerDatas.size()).getName();
				
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<first>", first).replaceAll("<second>", second).replaceAll("<third>", third).replaceAll("<last>", last));
			}
			catch(Exception e)
			{
				Bukkit.getLogger().warning("§cAn internal error occured when MobRacers tried to execute a preconfigured command: " + command);
				e.printStackTrace();
			}
		}

		//for(Player player : game.getPlayers())
		//	Core.getUserDatasManager().asyncSaveUserData(player);
		
		this.fireworksTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(getGame().getPlugin(), new FireworksTask(), 0, 20);
		
		Bukkit.getScheduler().runTaskLater(getGame().getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.getScheduler().cancelTask(fireworksTask);
				Bukkit.getScheduler().cancelTask(scoreboardTask);

				for(Player current : new ArrayList<>(getGame().getPlayers()))
				{
					if(((MobRacersConfig)game.getConfig()).enableRotation())
					{
						PlayerUtil.clearInventory(current);
						PlayerUtil.heal(current);
						PlayerUtil.prepare(current);
						current.teleport(getGame().getSetup().getLobby());
						if(getGame().getConfig().isColorInTab())
							current.setPlayerListName("§2" + current.getName());
					}
					else if(!game.getConfig().isAutoJoin())
					{
						game.leave(current);
					}
					else
						Core.getBungeeMessager().sendToServer(current, ((MobRacersConfig)game.getConfig()).getExitServer());
				}
				
				if(getGame().getPlayers().size() >= getGame().getMinPlayers())
				{
					getGame().setState(new ArenaSelectionState(getGame()));
					getGame().getState().start();
					return;
				}
				
				getGame().setState(new StandbyState(getGame()));
				getGame().getState().start();
				
			}
		}, 20 * ((MobRacersConfig) getGame().getConfig()).getFireworksTimer());
	}
	
	private void updateTab()
	{
		if(!game.getConfig().isColorInTab())
			return;
		
		int IGPlayers = 0;
		
		for(GamePlayerData player : this.playerDatas)
			if(!player.isFinished())
				IGPlayers++;
		
		int spectators = game.getPlayers().size() - IGPlayers;
		String[] replacements = {"<players>", "" + IGPlayers, "<plural-player>", (IGPlayers > 1 ? "s" : ""), "<spectators>", "" + spectators, "<plural-spectator>", (spectators > 1 ? "s" : "")};
		
		for(Player p : game.getPlayers())
			TabUtil.sendInfos(p, JsonUtil.toJson(CourseMessage.GAME_TABHEADER.toString(replacements)), JsonUtil.toJson(CourseMessage.GAME_TABFOOTER.toString(replacements)));
	}
	
	private void updateBoard(Player p)
	{
		String[] elements = new String[16];
		elements[0] = CourseMessage.GAME_BOARD_HEADER.toString();
		
		String laps = "";
		
		if(this.getPlayerData(p) != null && this.arena.getLaps() > 0)
		{
			int lap = (this.getPlayerData(p).getCurrentLap() + 1);
			if(lap > this.arena.getLaps())
				lap = this.arena.getLaps();
			laps = CourseMessage.GAME_BOARD_LAPS.toString("<current>", "" + lap, "<laps>", "" + this.arena.getLaps());
		}
		
		elements[1] = CourseMessage.GAME_BOARD_POSITION.toString();
		if(this.getPlayerData(p) != null)
			elements[2] = this.getPosition(p) + " / " + this.playerDatas.size() + " " + laps;
		else
			elements[2] = CourseMessage.GAME_BOARD_SPECTATOR.toString();
		elements[3] = "  ";
		elements[4] = CourseMessage.GAME_BOARD_ITEM.toString();
		if(this.getPlayerData(p) == null || this.getPlayerData(p).getCurrentItem() == null)
			elements[5] = "---"; 
		else
			elements[5] = this.getPlayerData(p).getCurrentItem().getName(); 
		elements[6] = "   ";
		elements[7] = CourseMessage.GAME_BOARD_RANKING.toString();
		
		String[] colors = new String[]{"§4§l", "§c§l", "§6§l", "§e§l", "§a§l", "§b§l", "§d§l", "§5§l"};
		
		for(int i = 1; i <= 8; i++)
			if(this.getPlayer(i) != null)
				elements[7 + i] = colors[i - 1] + i + ": §f" + this.getPlayer(i).getName();
		
		ScoreboardUtil.unrankedSidebarDisplay(p, elements);
	}
	
	public int getPosition(Player p)
	{
		if(this.getPlayerData(p) != null)
			return this.getPlayerData(p).getPosition();
		return -1;
	}
	
	public int getFinishedPlayers()
	{
		int finished = 0;
		for(GamePlayerData data : this.playerDatas)
			if(data.isFinished())
				finished++;
		return finished;
	}
	
	public GamePlayerData getPlayerData(int position)
	{
		for(GamePlayerData data : this.playerDatas)
			if(data.getPosition() == position)
				return data;
		return null;
	}
	
	public Player getPlayer(int position)
	{
		if(this.getPlayerData(position) == null)
			return null;
		return this.getPlayerData(position).getPlayer();
	}
	
	public GamePlayerData getPlayerData(Player p)
	{
		for(GamePlayerData data : this.playerDatas)
			if(data.getPlayer() == p)
				return data;
		return null;
	}
	
	public Arena getArena()
	{
		return this.arena;
	}

	public long getRaceStart()
	{
		return raceStart;
	}
	
	public List<GamePlayerData> getPlayerDatas()
	{
		return this.playerDatas;
	}

	public List<Item> getItems()
	{
		return items;
	}
	
	public List<Player> getForceMountExceptions()
	{
		return forceMountExceptions;
	}
	
	private class ForceMountTask implements Runnable
	{
		@Override
		public void run()
		{
			for(Player p : game.getPlayers())
			{
				if(forceMountExceptions.contains(p))
					return;
				
				if(p.getVehicle() == null)
				{
					GamePlayerData data;
					if((data = getPlayerData(p)) != null && !data.isFinished())
						if(started)
							data.relocate();
						else if(data.getVehicle().getEntity() != null && data.getVehicle().getEntity().isValid())
						{
							p.teleport(data.getVehicle().getEntity());
							data.getVehicle().getEntity().setPassenger(p);
						}
				}
			}
		}
	}

	private class GameTask implements Runnable
	{
		private int i, max;
		
		public GameTask(int max)
		{
			this.max = max;
			this.i = 10 + max;
		}
		
		@Override
		public void run()
		{
			String color = null;
			boolean noteBass = true;
			switch(i - max)
			{
			case 10:
			case 9:
			case 8:
			case 7:
			case 6:
				color = "§7§l";
			case 5:
				if(color == null)
					color = "§a§l";
			case 4:
				if(color == null)
					color = "§2§l";
				noteBass = false;
			case 3:
				if(color == null)
					color = "§e§l";
			case 2:
				if(color == null)
					color = "§6§l";
			case 1:
				if(color == null)
					color = "§c§l";
				for(Player p : game.getPlayers())
				{
					TitleUtil.displayTitle(p, JsonUtil.toJson(color + (i - max)), null, 0, 30, 0);
					if(noteBass)
						p.playSound(p.getEyeLocation(), Sound.NOTE_BASS, 10f, 1f);
				}
				break;
			case 0:
				for(Player p : game.getPlayers())
				{
					TitleUtil.displayTitle(p, "{text:\"\"}", JsonUtil.toJson(CourseMessage.GAME_GO.toString()), 0, 30, 10);
					p.playSound(p.getEyeLocation(), Sound.FIREWORK_BLAST, 10f, 0.6f);
				}
				GameState.this.raceStart = System.nanoTime();
				started = true;
				for(GamePlayerData data : GameState.this.playerDatas)
					data.getVehicle().start();
			}
			
			for(GamePlayerData data : GameState.this.playerDatas)
			{
				if(data.isFinished())
					continue;
				
				Location loc = data.getCurrentBreakline().getCenter();
				loc.setY(data.getPlayer().getLocation().getY());
				ParticleUtil.playSimpleParticles(data.getPlayer(), loc, ParticleType.SMOKE_NORMAL, 0, 0, 0, 0, 50);
			}
			if(i == 0)
				GameState.this.end();
			i--;
		}
	}
	
	private class FireworksTask implements Runnable
	{
		@Override
		public void run()
		{
			FireworkUtil.generateRandom(arena.getSpawn());
		}
	}

	@Override
	public Player getOwner(Vehicle vehicle)
	{
		for(GamePlayerData data : this.playerDatas)
			if(data.getVehicle() == vehicle)
				return data.getPlayer();
		return null;
	}


	@Override
	public Vehicle getVehicle(Entity entity)
	{
		for(GamePlayerData data : this.playerDatas)
			if(data.getVehicle().getEntity() == entity)
				return data.getVehicle();
		return null;
	}
	
	public List<ItemBox> getItemBoxes()
	{
		return this.itemBoxes;
	}


	public GameSpectatorData getSpectatorData(Player player)
	{
		for(GameSpectatorData data : this.spectatorDatas)
			if(data.getPlayer() == player)
				return data;
		return null;
	}


	public boolean isCourseFinished()
	{
		for(GamePlayerData data : this.playerDatas)
			if(!data.isFinished())
				return false;
		
		return true;
	}
	
	public void addBlockToRegen(Location loc)
	{
		for(BlockState state : this.arenaBackup)
			if(state.getWorld() == loc.getWorld() && state.getLocation().getBlockX() == loc.getBlockX() && state.getLocation().getBlockY() == loc.getBlockY() && state.getLocation().getBlockZ() == loc.getBlockZ())
				return;
		
		arenaBackup.add(loc.getBlock().getState());
	}


	public void lookForFinish()
	{
		for(GamePlayerData data : this.playerDatas)
			if(!data.isFinished())
				return;
		
		this.end();
	}
	
	public boolean containsVehicle(Entity entity)
	{
		return getVehicle(entity) != null;
	}
	
	public StateGame getGame()
	{
		return this.game;
	}


	@Override
	public Region getRegion()
	{
		return new MultiRegion(this.getArena().getRegion(), this.getGame().getSetup().getRegion());
	}
}
