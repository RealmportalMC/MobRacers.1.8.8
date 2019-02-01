package me.winterguardian.mobracers.state.lobby;

import java.util.*;

import me.winterguardian.core.*;
import me.winterguardian.core.entity.EntityUtil;
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
import me.winterguardian.core.util.TitleUtil;
import me.winterguardian.core.util.Weather;
import me.winterguardian.core.world.MultiRegion;
import me.winterguardian.core.world.Region;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.MobRacersSetup;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.music.CourseMusic;
import me.winterguardian.mobracers.state.VehicleState;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.stats.CourseAchievement;
import me.winterguardian.mobracers.stats.CoursePurchase;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.PurchasableVehicle;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleType;
import me.winterguardian.mobracers.vehicle.VipVehicle;
import me.winterguardian.mobracers.vehicle.WinnableVehicle;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VehicleSelectionState extends WaitingState implements VehicleState
{
	private HashMap<Player, Vehicle> vehicles;
	private HashMap<Player, CourseMusic> musics;
	private HashMap<Entity, VehicleType> statues;
	
	private List<Player> forceMountExceptions;

	private InventoryGUI vehicleGUI;
	
	private Arena arena;
	private int forceMountTask;
	
	
	public VehicleSelectionState(StateGame stateGame, Arena arena)
	{
		super(stateGame, ((MobRacersConfig) stateGame.getConfig()).getVehicleSelectionTimer());
		this.arena = arena;
		
		this.vehicles = new HashMap<>();
		this.musics = new HashMap<>();
		this.statues = new HashMap<>();
		
		this.forceMountExceptions = new ArrayList<>();

		Set<VehicleType> ready = ((MobRacersSetup) stateGame.getSetup()).getVehicleLocations().keySet();

		for(VehicleType type : new ArrayList<>(ready))
			if(((MobRacersSetup) stateGame.getSetup()).getVehicleLocations().get(type) == null && !((MobRacersConfig)stateGame.getConfig()).isNoVehicleSpawns())
				ready.remove(type);

		this.vehicleGUI = new InventoryGUI(CourseMessage.VEHICLESELECT_GUI_INV.toString(), 54);

		for(VehicleType type : ready)
			this.vehicleGUI.getItems().add(type.createNewVehicle().getGUIItem());
	}
	
	@Override
	public void join(Player p)
	{
		((MobRacersGame) getGame()).savePlayerState(p);
		super.join(p);
		CourseMessage.VEHICLESELECT_JOIN.say(p, "<arena>", this.arena.getName());
	
		for(Entity entity : this.statues.keySet())
			EntityUtil.setYawPitch(p, entity, 
					((MobRacersSetup) getGame().getSetup()).getVehicleLocations().get(this.statues.get(entity)).getLocation().getYaw(), 
					((MobRacersSetup) getGame().getSetup()).getVehicleLocations().get(this.statues.get(entity)).getLocation().getPitch());
		
		if(getGame().getConfig().isColorInTab())
			p.setPlayerListName("§2" + p.getName());

		giveStuff(p);
	}

	@Override
	public void leave(Player p)
	{
		if(this.vehicles.containsKey(p))
		{
			this.vehicles.get(p).remove();
			this.vehicles.remove(p);
		}

		this.musics.remove(p);

		if(getGame().getConfig().isColorInTab())
			p.setPlayerListName(null);

		super.leave(p);
		
		((MobRacersGame) getGame()).applyPlayerState(p);
	}

	private void killAll()
	{
		for(Entity entity : getGame().getSetup().getRegion().getMinimum().getWorld().getEntities())
			if(getGame().getSetup().getRegion().contains(entity.getLocation()))
				if(!(entity instanceof Player) && entity instanceof LivingEntity)
					entity.remove();
	}

	public void giveStuff(Player player)
	{
		PlayerUtil.clearInventory(player);

		if(((MobRacersConfig) this.getGame().getConfig()).enableMusic())
		{
			this.musics.put(player, CourseMusic.getAvailable(player).get(0));
			player.getInventory().setItem(1, this.musics.get(player).getItemStack());
		}
		else
			this.musics.put(player, CourseMusic.NOMUSIC);

		player.getInventory().setItem(0, ((MobRacersSetup)getGame().getSetup()).getVehicleItem());

		player.getInventory().setItem(8, ((MobRacersSetup)getGame().getSetup()).getLeaveItem());
	}

	@Override
	public void start()
	{
		super.start();

		Bukkit.getPluginManager().registerEvents(this.vehicleGUI, this.getGame().getPlugin());
		getGame().getPlayerListener().setCancelInventoryClick(true);

		this.forceMountTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), new ForceMountTask(), 0, 1);
		
		killAll();

		for(Player player : getGame().getPlayers())
			giveStuff(player);

		if(((MobRacersConfig)getGame().getConfig()).isNoVehicleSpawns())
			return;

		
		for(VehicleType type : ((MobRacersSetup)getGame().getSetup()).getVehicleLocations().keySet())
		{
			Vehicle vehicle = type.createNewVehicle();

			if(vehicle == null)
				continue;

			vehicle.spawn(((MobRacersSetup)getGame().getSetup()).getVehicleLocations().get(type).getLocation(), null);
			Entity entity = vehicle.getEntity();
			if(entity == null)
				continue;
			EntityUtil.setNoAI(entity, true);
			EntityUtil.setYawPitch(getGame().getPlayers(), entity, ((MobRacersSetup) getGame().getSetup()).getVehicleLocations().get(type).getLocation().getYaw(), ((MobRacersSetup) getGame().getSetup()).getVehicleLocations().get(type).getLocation().getPitch());

			if(entity instanceof LivingEntity)
			{
				((LivingEntity)entity).setRemoveWhenFarAway(true);
				((LivingEntity)entity).setHealth(((LivingEntity)entity).getMaxHealth());
			}
			
			if(entity.getCustomName() == null)
			{
				entity.setCustomName(vehicle.getName());
				entity.setCustomNameVisible(true);
			}
			
			this.statues.put(entity, type);
		}
	}
	
	public boolean containsStatue(Entity entity)
	{
		return this.statues.containsKey(entity);
	}
	
	public void onChoose(Player p, Entity entity)
	{
		if(this.vehicles.containsKey(p))
			return;
		
		if(!this.containsStatue(entity))
			return;
		
		this.onChoose(p, this.statues.get(entity), false);
	}

	public void openGUI(Player player)
	{
		this.vehicleGUI.open(player);
	}
	
	
	public void onChoose(final Player p, VehicleType type, boolean force)
	{
		final Vehicle vehicle = type.createNewVehicle();

		if(vehicle == null)
			return;

		if(!((MobRacersConfig) getGame().getConfig()).enableStats() || p.hasPermission(MobRacersPlugin.ALL_UNLOCKED) || vehicle.canChoose(p)) 
		{
			CourseMessage.SEPARATOR_SELECTVEHICLE.say(p);
			p.sendMessage(CourseMessage.SEPARATOR_SELECTVEHICLE.getPrefix() + "§f§l" + vehicle.getName());
			p.sendMessage("§e§l" + vehicle.getDescription());
			p.sendMessage(" ");
			
			p.setPlayerTime(this.arena.getTime(), !this.arena.isTimeLocked());
			p.setPlayerWeather(this.arena.isRaining() ? WeatherType.DOWNFALL : WeatherType.CLEAR);

			if(this.vehicles.size() == getGame().getPlayers().size() && time > 5)
			{
				for(Entity entity : this.statues.keySet())
					entity.remove();
				time = 5;
			}

			Location loc;

			if(this.vehicles.containsKey(p))
			{
				loc = this.vehicles.get(p).getEntity().getLocation();

				p.leaveVehicle();
				this.vehicles.get(p).remove();
				this.vehicles.remove(p);
			}
			else
			{
				loc = this.arena.getSpawn();
			}


			p.teleport(loc);
			vehicles.put(p, vehicle);

			final Location finalLoc = loc;
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					vehicle.spawn(finalLoc, p);
					if(vehicle.getEntity() instanceof LivingEntity)
					{
						((LivingEntity)vehicle.getEntity()).setRemoveWhenFarAway(true);
						((LivingEntity)vehicle.getEntity()).setHealth(((LivingEntity)vehicle.getEntity()).getMaxHealth());
					}
					vehicle.stop();
				}
			}, 20);
			
			TitleUtil.displayTitle(p, JsonUtil.toJson(CourseMessage.VEHICLESELECT_BEGININGSOON.toString()), JsonUtil.toJson(CourseMessage.VEHICLESELECT_SHIFTTIP.toString()), 10, 65, 15);
			
			CourseMessage.VEHICLESELECT_JUSTSELECTED.say(p);
			CourseMessage.SEPARATOR_SELECTVEHICLE.say(p);
			
			if(this.getBoard() instanceof UpdatableBoard)
				((UpdatableBoard)this.getBoard()).update();
			for(Player current : getGame().getPlayers())
				if(getGame().getConfig().isColorInTab())
					TabUtil.sendInfos(current, getTabHeader(current), getTabFooter(current));
			
			return;
		}

		CourseMessage.SEPARATOR_CANTSELECTVEHICLE.say(p);
		p.sendMessage(CourseMessage.SEPARATOR_SELECTVEHICLE.getPrefix() + " §f§l" + vehicle.getName());
		p.sendMessage(vehicle.getDescription());
		p.sendMessage(" ");

		if(vehicle instanceof WinnableVehicle)
		{
			CourseAchievement achievement = ((WinnableVehicle)vehicle).getWinAchievement();
			CourseMessage.VEHICLESELECT_CANTSELECT_ACHIEVEMENT.say(p);
			p.sendMessage(achievement.getName() + " §r- " + achievement.getDescription() + " §f" + achievement.getProgression(p));
		}
		else if(vehicle instanceof PurchasableVehicle)
		{
			CoursePurchase purchase = ((PurchasableVehicle)vehicle).getPurchase();
			p.sendMessage(CourseMessage.VEHICLESELECT_CANTSELECT_PURCHASE.toString().replaceAll("<points>", "" + purchase.getPrice()));
			JsonUtil.sendJsonMessage(p, "{\"text\":\"" + CourseMessage.VEHICLESELECT_CLICKTOBUY.toString() + "\",\"color\":\"white\",\"bold\":\"true\",\"underlined\":\"true\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/mobracers buy " + vehicle.getName().replaceAll(" ", "-") + "\"}}");
			
		}
		else if(vehicle instanceof VipVehicle)
			CourseMessage.VEHICLESELECT_CANTSELECT_VIP.say(p);
		
		else
			CourseMessage.VEHICLESELECT_CANTSELECT_UNKNOW.say(p);
		
		CourseMessage.SEPARATOR_CANTSELECTVEHICLE.say(p);
	}

	@Override
	public String getStatus()
	{
		return CourseMessage.VEHICLESELECT_STATUS.toString("<arena>", this.arena.getName());
	}
	
	@Override
	public Player getOwner(Vehicle vehicle)
	{
		for(Player p : this.vehicles.keySet())
			if(this.vehicles.get(p) == vehicle)
				return p;
		
		return null;
	}

	@Override
	public Vehicle getVehicle(Entity entity)
	{
		for(Player p : this.vehicles.keySet())
			if(this.vehicles.get(p).getEntity() == entity)
				return this.vehicles.get(p);
		
		return null;
	}

	public void changeMusic(Player player, boolean forward)
	{
		if(!this.musics.containsKey(player))
			return;
		
		int index = CourseMusic.getAvailable(player).indexOf(this.musics.get(player)) + (forward ? 1 : -1);
		while(index >= CourseMusic.getAvailable(player).size())
			index -= CourseMusic.getAvailable(player).size();
		
		while(index < 0)
			index += CourseMusic.getAvailable(player).size();
		
		this.musics.put(player, CourseMusic.getAvailable(player).get(index));
		
		player.getInventory().setItemInHand(this.musics.get(player).getItemStack());
		player.playSound(player.getEyeLocation(), Sound.CLICK, 1f, 1f);
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
			for(Player p : MobRacersPlugin.getGame().getPlayers())
			{
				if(forceMountExceptions.contains(p))
					return;
				
				if(p.getVehicle() == null)
				{
					if(vehicles.get(p) != null && vehicles.get(p).getEntity() != null)
					{
						p.teleport(vehicles.get(p).getEntity());
						vehicles.get(p).getEntity().setPassenger(p);
					}
				}
			}
		}
	}
	
	public boolean containsVehicle(Entity entity)
	{
		return getVehicle(entity) != null;
	}

	@Override
	public void end()
	{
		for(Entity entity : this.statues.keySet())
			entity.remove();

		super.end();
		Bukkit.getScheduler().cancelTask(this.forceMountTask);
		HandlerList.unregisterAll(this.vehicleGUI);
	}
	
	public Weather getPlayerWeather(Player p)
	{
		return ((MobRacersConfig) getGame().getConfig()).getLobbyWeather();
	}

	@Override
	protected Message getCountdownFinishMessage()
	{
		return Message.NULL;
	}

	@Override
	protected Message getCountdownMessage(int i)
	{
		return CourseMessage.VEHICLESELECT_TIMER;
	}

	@Override
	protected State getNextState()
	{
		for(Player p : getGame().getPlayers())
		{
			if(this.vehicles.containsKey(p))
				continue;
			this.onChoose(p, VehicleType.PIG, true);
		}

		return new GameState(this.getGame(), this.arena, vehicles, musics);
	}

	@Override
	protected State getStandbyState()
	{
		return new StandbyState(this.getGame());
	}

	@Override
	protected Message getStartMessage()
	{
		return CourseMessage.VEHICLESELECT_JOIN;
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
				elements[0] = CourseMessage.VEHICLESELECT_BOARD_HEADER.toString();
				elements[1] = CourseMessage.VEHICLESELECT_BOARD_ARENA.toString();
				elements[2] = arena.getName();
				elements[3] = "  ";
				elements[4] = CourseMessage.VEHICLESELECT_BOARD_VEHICLES.toString();
				int availableVehicles = 0;
				
				for(VehicleType type : VehicleType.values())
					if(!((MobRacersConfig) getGame().getConfig()).enableStats() || type.createNewVehicle().canChoose(p))
						availableVehicles++;
				
				elements[5] = availableVehicles + " / " + VehicleType.values().length;
				
				elements[6] = "   ";
				elements[7] = CourseMessage.VEHICLESELECT_BOARD_SELECTEDVEHICLE.toString();
				if(vehicles.get(p) != null)
					elements[8] = vehicles.get(p).getName();
				else
					elements[8] = "---";
				
				elements[9] = " ";
				elements[10] = CourseMessage.VEHICLESELECT_BOARD_RECORDS.toString();
				
				int availableRecords = 0;
				
				for(CourseMusic music : CourseMusic.values())
					if(music.isAvailable(p))
						availableRecords++;
				
				elements[11] = availableRecords + " / " + CourseMusic.values().length;

				if(((MobRacersConfig)getGame().getConfig()).enableStats())
				{
					elements[12] = "    ";
					elements[13] = CourseMessage.VEHICLESELECT_BOARD_POINTS.toString();
					elements[14] = CourseMessage.toString(CourseStats.get(p).getPoints());
					elements[15] = "     ";
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
		
		return JsonUtil.toJson(CourseMessage.VEHICLESELECT_TABHEADER.toString("<player>", name));
	}
	
	@Override
	public String getTabFooter(Player p)
	{
		String name = p.getName();
		
		if(this.getGame().getConfig() != null && this.getGame().getConfig().useDisplaynames())
			name = p.getDisplayName();
		
		if(this.vehicles.containsKey(p) && this.vehicles.get(p) != null)
			return JsonUtil.toJson(CourseMessage.VEHICLESELECT_TABFOOTER_SELECTED.toString("<vehicle>", this.vehicles.get(p).getName(), "<player>", name));
		return JsonUtil.toJson(CourseMessage.VEHICLESELECT_TABFOOTER_NOVEHICLE.toString("<arena>", this.arena.getName(), "<player>", name));
	}

	public Arena getArena()
	{
		return this.arena;
	}

	@Override
	public boolean keepScoreboardAndWeather()
	{
		return true;
	}
	
	@Override
	public Region getRegion()
	{
		return new MultiRegion(this.arena.getRegion(), this.getGame().getSetup().getRegion());
	}
}
