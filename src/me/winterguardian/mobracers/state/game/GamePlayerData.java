package me.winterguardian.mobracers.state.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.PlayerData;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersSetup;
import me.winterguardian.mobracers.arena.Breakline;
import me.winterguardian.mobracers.arena.RoadType;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.RandomItem;
import me.winterguardian.mobracers.music.CourseMusic;
import me.winterguardian.mobracers.stats.ArenaStats;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GamePlayerData extends PlayerData
{
	private RoadType lastRoad;
	
	private int changeItemTaskId;
	private GameState currentGame;
	
	private Vehicle vehicle;
	private int currentBreakline;
	private Item currentItem;
	private int collectedItems;
	private int passings;
	private long lastPassing;
	private int lap;
	
	private long finishRaceTime;
	private int finishPosition;
	private int spectatorLocationId;
	
	private CourseMusic music;
	
	private Location lastLoc;
	
	private long lastItemUse;
	private Location spawn;
	
	private long lastRelocateMessage;
	
	public GamePlayerData(GameState game, Player p, Vehicle v, CourseMusic music)
	{
		super(p);
		this.currentGame = game;
		this.changeItemTaskId = -1;
		
		this.vehicle = v;
		this.currentBreakline = 0;
		this.currentItem = null;
		this.collectedItems = 0;
		this.passings = 0;
		this.lastPassing = 0;
		this.lap = 0;
		
		this.finishRaceTime = -1;
		this.finishPosition = -1;
		
		this.spectatorLocationId = 0;
		
		this.music = music;
		
		this.lastLoc = null;
		
		this.lastItemUse = 0;
		
		if(v.getEntity() != null)
			this.spawn = v.getEntity().getLocation();
		else
			this.spawn = p.getLocation();
		
		this.lastRelocateMessage = 0;
	}
	
	
	public Vehicle getVehicle()
	{
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle)
	{
		this.vehicle = vehicle;
	}
	
	public Breakline getCurrentBreakline()
	{
		return getBreakline(this.currentBreakline, this.currentGame);
	}
	
	public static Breakline getBreakline(int id, GameState game)
	{
		int arenaBreakline = id;
		while(arenaBreakline >= game.getArena().getLines().size())
			arenaBreakline -= game.getArena().getLines().size();
		if(arenaBreakline < 0)
			return null;
		
		return game.getArena().getLines().get(arenaBreakline);
	}
	
	public int getCurrentBreaklineId()
	{
		return currentBreakline;
	}
	
	private void passOver(GamePlayerData playerData)
	{
		if(this.lastPassing + 500_000_000 < System.nanoTime())
		{
			this.passings++;
			this.lastPassing = System.nanoTime();
		}
	}
	
	private void passBreakline()
	{
		if(this.currentGame.getArena().getLaps() <= 0)
		{
			this.currentBreakline++;
			if(this.currentBreakline == this.currentGame.getArena().getLines().size())
				this.finish();
		}
		else if(this.currentBreakline % this.currentGame.getArena().getLines().size() == 0)
		{
			this.currentBreakline++;
			
			this.lap = this.getCurrentLap();

			if(lap == 0)
			{
				CourseMessage.GAME_START_GOODLUCK.say(getPlayer());
			}
			else if(lap >= this.currentGame.getArena().getLaps())
			{
				this.finish();
			}
			else
			{
				if(lap == this.currentGame.getArena().getLaps() - 1)
					CourseMessage.GAME_PASSMAINLINE_FINAL.say(getPlayer());
				else
					getPlayer().sendMessage(CourseMessage.GAME_PASSMAINLINE_NORMAL.toString().replace("<current>", "" + (this.lap + 1)).replace("<laps>", "" + this.currentGame.getArena().getLaps()));
				
				getPlayer().playSound(getPlayer().getEyeLocation(), Sound.BLOCK_ANVIL_USE, 10f, 0.9f);
			}
			
		}
		else
			this.currentBreakline++;
		
			
	}
	
	public int getCurrentLap()
	{
		return (this.currentBreakline - 1) / this.currentGame.getArena().getLines().size();
	}
	
	public void move(Location to)
	{
		
		BlockState state = null;
		
		for(double y = to.getY(); y >= 0; y--)
		{
			Block block = new Location(to.getWorld(), to.getX(), y, to.getZ()).getBlock();
			if(!block.isEmpty() && !block.isLiquid() && block.getPistonMoveReaction() != PistonMoveReaction.BREAK)
			{
				state = block.getState();
				break;
			}
		}
		
		if(this.lastRoad == null || !new RoadType(state).match(this.lastRoad))
		{
			if(this.lastRoad != null)
				for(RoadType type : this.currentGame.getArena().getRoadSpeedModifier().keySet())
					if(type.match(this.lastRoad))
						this.getVehicle().setSpeed((float) (this.getVehicle().getSpeed() - this.currentGame.getArena().getRoadSpeedModifier().get(type)));
			
			this.lastRoad = new RoadType(state);
				
			for(RoadType type : this.currentGame.getArena().getRoadSpeedModifier().keySet())
				if(state != null && type.match(state))
					this.getVehicle().setSpeed((float) (this.getVehicle().getSpeed() + this.currentGame.getArena().getRoadSpeedModifier().get(type)));
		}
		
		if(this.lastLoc == null)
		{
			this.lastLoc = to;
			return;
		}
		
		boolean update = false;
		Breakline baseLine = this.getCurrentBreakline();
		
		while(this.getCurrentBreakline().isPassingThrough(this.lastLoc, to) && !(update && baseLine == this.getCurrentBreakline()))
		{
			for(GamePlayerData data : this.currentGame.getPlayerDatas())
				if(data.getCurrentBreaklineId() == this.getCurrentBreaklineId())
					if(data.getPosition() < this.getPosition(this.lastLoc))
						this.passOver(data);
			
			this.passBreakline();
			update = true;
		}

		if(!update && this.getPosition(to) < this.getPosition(this.lastLoc))
		{
			this.passOver(this.currentGame.getPlayerData(this.getPosition(to)));
		}
		
		this.lastLoc = to;
		
		if(!this.currentGame.isInRegion(lastLoc) && !this.isFinished())
		{
			this.relocate();
			return;
		}
			
		for(SerializableRegion zone : this.currentGame.getArena().getDeathZones())
			if(zone.contains(lastLoc))
			{
				this.relocate();
				return;
			}
	}
	
	public void playMusic()
	{
		if(this.music != null)
			this.music.play(getPlayer(), this.currentGame.getArena());
	}
	
	public long getRaceTime()
	{
		if(this.isFinished())
			return this.getFinishRaceTime();
		
		return System.nanoTime() - this.currentGame.getRaceStart();
	}
	
	public long getFinishRaceTime()
	{
		return finishRaceTime;
	}
	
	public String getStringRaceTime()
	{
		return CourseStats.timeToString(this.getRaceTime());
	}
	
	public int getPosition()
	{
		if(this.finishPosition != -1 || getPlayer() == null)
			return this.finishPosition;

		return this.getPosition(getPlayer().getLocation());
	}
	
	private int getPosition(Location loc)
	{
		if(this.finishPosition != -1)
			return this.finishPosition;
		
		int position = 1;
		for(GamePlayerData p : this.currentGame.getPlayerDatas())
			if(p == this)
				continue;
			else if(p.isFinished() || p.getCurrentBreaklineId() > this.currentBreakline)
				position++;
			else if(p.getCurrentBreaklineId() == this.currentBreakline)
			{
				if(this.getCurrentBreakline().getDistance(loc) > this.getCurrentBreakline().getDistance(p.getPlayer().getLocation()))
				{
					position++;
				}
				else if(this.getCurrentBreakline().getDistance(loc) == this.getCurrentBreakline().getDistance(p.getPlayer().getLocation()))
				{
					if(this.getPlayer().getName().compareToIgnoreCase(p.getPlayer().getName()) < 0)
						position++;
				}
			}
		return position;
	}

	private void finish()
	{
		if(this.isFinished())
			return;
		
		this.currentItem = null;
		
		this.finishRaceTime = this.getRaceTime();
		this.finishPosition = this.currentGame.getFinishedPlayers();
		
		float pitch = 2F - 0.2F * (this.finishPosition - 1F);
		if(pitch < 0.6F)
			pitch = 0.6F;
		
		for(Player p : currentGame.getGame().getPlayers())
			p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_PLING, 10f, pitch);
		
		String name = getPlayer().getName();
		
		if(this.currentGame.getGame().getConfig().useDisplaynames())
			name = getPlayer().getDisplayName();

		CourseMessage.GAME_FINISH_ACTIONBAR.sayPlayers("<player>", name);
		if(this.finishPosition == 1)
		{
			CourseMessage.GAME_WIN.say(((MobRacersConfig) this.currentGame.getGame().getConfig()).getBroadcastRecipients(), "<player>", name, "<arena>", this.currentGame.getArena().getName());
			CourseMessage.GAME_FINISHED_FIRST.say(getPlayer());
			
		}
		else
			CourseMessage.GAME_FINISHED_NORMAL.say(getPlayer(), "#", this.finishPosition + "");
		
		if(((MobRacersConfig) this.currentGame.getGame().getConfig()).enableStats())
		{
			List<CourseAchievement> uncompletedAchievements = new ArrayList<CourseAchievement>();
			for(CourseAchievement achiev : CourseAchievement.values())
				if(!achiev.isComplete(getPlayer()))
					uncompletedAchievements.add(achiev);


			CourseStats stats = new CourseStats(getPlayer(), Core.getUserDatasManager().getUserData(getPlayer()));

			long previousTime = stats.getBestTime(this.currentGame.getArena().getName());
			
			stats.finish(this.finishPosition, this.currentGame.getArena(), this.vehicle, this.finishRaceTime, this.collectedItems, this.passings);
			ArenaStats.update(this.currentGame.getArena().getName(), getPlayer(), this.finishRaceTime, vehicle);
			
			int points = CourseRewards.getReward(this.finishPosition, this.currentGame.getPlayerDatas().size());
			
			if(previousTime <= 0)
			{
				CourseMessage.GAME_FINISHTIME_NORMAL.say(getPlayer(), "<arena>", this.currentGame.getArena().getName(), "<time>", this.getStringRaceTime());
				
				points *= 2;
			}
			else if(this.getRaceTime() < previousTime)
			{
				CourseMessage.GAME_FINISHTIME_NEWRECORD.say(getPlayer(), "<arena>", this.currentGame.getArena().getName());
				CourseMessage.GAME_FINISHTIME_NORMAL.say(getPlayer(), "<arena>", this.currentGame.getArena().getName(), "<time>", this.getStringRaceTime());
				
				points += 25;
			}
			else
			{
				VehicleType previousVehicle = VehicleType.valueOf(stats.getBestVehicle(this.currentGame.getArena().getName()));
				
				CourseMessage.GAME_FINISHTIME_NORMAL.say(getPlayer(), "<arena>", this.currentGame.getArena().getName(), "<time>", this.getStringRaceTime());
				CourseMessage.GAME_BESTTIME.say(getPlayer(), "<arena>", this.currentGame.getArena().getName(), "<time>", CourseStats.timeToString(stats.getBestTime(this.currentGame.getArena().getName())), "<vehicle>", previousVehicle.createNewVehicle().getName());
			}
			
			stats.addGamePoints((int) (points * ((MobRacersConfig) currentGame.getGame().getConfig()).getPointCoefficient()));
			
			getPlayer().sendMessage("");
			
			for(CourseAchievement achiev : uncompletedAchievements)
				if(achiev.isComplete(getPlayer()))
					achiev.onComplete(getPlayer());
		}
		
		this.afterFinish();
		this.currentGame.lookForFinish();
	}
	
	public void forceFinish()
	{
		this.finishPosition = this.getPosition();
		this.afterFinish();
		
		CourseMessage.GAME_FORCEFNISH.say(getPlayer());
	}
	
	private void afterFinish()
	{
		this.vehicle.remove();
		getPlayer().teleport(this.currentGame.getArena().getSpectatorLocations().get(this.spectatorLocationId).getLocation());
		
		PlayerUtil.clearInventory(getPlayer());
		getPlayer().getInventory().setItem(0, ((MobRacersSetup)currentGame.getGame().getSetup()).getSpectatorItem());
		getPlayer().getInventory().setItem(8, ((MobRacersSetup)currentGame.getGame().getSetup()).getLeaveItem());
		if(((MobRacersConfig) currentGame.getGame().getConfig()).gamemode3Spectators())
			getPlayer().setGameMode(GameMode.SPECTATOR);
		
		Bukkit.getScheduler().runTaskLater(currentGame.getGame().getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				music.stop(getPlayer(), currentGame.getArena());
			}
		}, 5);
	}
	
	public boolean isFinished()
	{
		return this.finishRaceTime != -1;
	}

	public Item getCurrentItem()
	{
		return currentItem;
	}
	
	public boolean pickUpItem()
	{
		if(this.currentItem != null)
			return false;
		
		this.currentItem = new RandomItem();
		this.getPlayer().getInventory().setItem(0, this.currentItem.getItemStack());
		
		
		this.changeItemTaskId = Bukkit.getScheduler().runTaskLater(currentGame.getGame().getPlugin(), new ChangeItem(), 1).getTaskId();
		
		this.collectedItems++;
		return true;
	}
	
	public ItemResult useItem(int slot)
	{
		if(this.isFinished())
		{
			switch(slot)
			{
			case 0:
				this.spectatorLocationId++;
				if(this.spectatorLocationId >= this.currentGame.getArena().getSpectatorLocations().size())
					this.spectatorLocationId -= this.currentGame.getArena().getSpectatorLocations().size();
				getPlayer().teleport(this.currentGame.getArena().getSpectatorLocations().get(this.spectatorLocationId).getLocation());
				break;
				
			case 8:
				getPlayer().performCommand("mobracers leave");
				break;
			}
			
			return ItemResult.KEEP;
		}
		
		if(this.currentItem == null)
			return ItemResult.DISCARD;
		
		ItemResult result = this.currentItem.onUse(getPlayer(), this.vehicle, this.currentGame);
		
		if(result.deleteItem())
			this.currentItem = null;

		
		this.lastItemUse = System.nanoTime();
		return result;
	}
	
	public ItemResult useItem()
	{
		return useItem(0);
	}

	public int getCollectedItems()
	{
		return collectedItems;
	}
	
	public void cancel()
	{
		if(changeItemTaskId != -1)
			Bukkit.getScheduler().cancelTask(changeItemTaskId);
	}
	
	public void relocate()
	{
		if(this.vehicle.getEntity() == null || !this.vehicle.getEntity().isValid())
			return;
		
		Breakline line = getBreakline(this.getCurrentBreaklineId() - 1, this.currentGame);
		
		Location to;
		
		if(line != null)
			to = line.getCenter().clone();
		else
			to = this.spawn;
		Location next = this.getCurrentBreakline().getCenter().clone();
		
		to.setYaw((float) -Math.toDegrees(Math.atan2(next.getX() - to.getX(), next.getZ() - to.getZ())));
		while(to.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
		{
			if(!this.currentGame.getArena().getRegion().contains(to.clone().add(0, -1, 0)))
				break;

			to.add(0, -1, 0);
		}
		to.setPitch(0);

		if(!this.currentGame.getArena().getRegion().contains(to.clone().add(0, -1, 0)))
		{
			lastRelocateMessage = System.currentTimeMillis();
			new RuntimeException("MOBRACERS > Could not replace a player because the replace location is out of the arena region. YOUR ARENA SETUP IS INCORRECT").printStackTrace();
			return;
		}

		this.vehicle.getEntity().setVelocity(new Vector(0, 0, 0));
		
		if(this.vehicle.getEntity().getPassenger() != null)
			this.vehicle.getEntity().getPassenger().leaveVehicle();
		
		this.vehicle.getEntity().teleport(to);
		getPlayer().teleport(to);
		
		this.vehicle.getEntity().setPassenger(getPlayer());
		
		if(lastRelocateMessage + 500 < System.currentTimeMillis())
		{
			CourseMessage.GAME_RELOCATE.say(getPlayer());
			lastRelocateMessage = System.currentTimeMillis();
		}
		
	}

	private class ChangeItem implements Runnable
	{
		private float switchChances;
		private int delay;
		
		public ChangeItem()
		{
			this.switchChances = 1f;
			this.delay = 1;
		}
		
		public ChangeItem(float switchChances, int delay)
		{
			this.switchChances = switchChances;
			this.delay = delay;
		}
		
		@Override
		public void run()
		{
			if(isFinished())
				return;

			if(new Random().nextFloat() > this.switchChances)
			{
				currentItem = ((RandomItem)currentItem).getCurrentItem();
				currentGame.getItems().add(currentItem);
				
				getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 5, 2);
				changeItemTaskId = -1;
				
				return;
			}
			
			((RandomItem)currentItem).next(vehicle, currentGame.getPosition(getPlayer()), currentGame.getPlayerDatas().size());
			getPlayer().getInventory().setItem(0, currentItem.getItemStack());
			changeItemTaskId = Bukkit.getScheduler().runTaskLater(currentGame.getGame().getPlugin(), new ChangeItem(this.switchChances - 0.03f, this.delay + 1), 1).getTaskId();
			
			getPlayer().playSound(getPlayer().getLocation(), Sound.UI_BUTTON_CLICK , 5, 1.4f);
		}
		
	}

	public CourseMusic getMusic()
	{
		return this.music;
	}

	@Override
	public void onJoin()
	{

	}

	@Override
	public void onLeave()
	{
		getVehicle().remove();
	}


	public long getLastItemUse() {
		return lastItemUse;
	}
}
