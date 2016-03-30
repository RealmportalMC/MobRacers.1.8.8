package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.winterguardian.core.particle.ParticleData;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.Selector;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class MushroomCowSpecialItem extends SpecialItem implements Listener
{
	private static boolean[][] pattern = new boolean[][]{
		{false, false, false, false, true, true, false, false, false, true, false, false, false}, 
		{false, false, false, false, false, true, false, false, true, true, false, false, false}, 
		{false, false, true, false, false, true, true, false, false, true, true, false, false}, 
		{true, true, true, true, false, false, true, false, true, true, false, false, false}, 
		{false, true, false, true, true, false, true, true, true, false, false, false, true}, 
		{false, false, false, false, true, true, true, true, false, false, true, true, true}, 
		{false, false, true, true, true, true, true, true, true, true, true, false, false}, 
		{true, true, true, false, false, true, true, true, true, false, false, false, false}, 
		{true, false, false, false, true, true, true, false, true, true, false, true, false}, 
		{false, false, false, true, true, false, true, false, false, true, true, true, true}, 
		{false, false, true, true, false, false, true, true, false, false, true, false, false}, 
		{false, false, false, true, true, false, false, true, false, false, false, false, false}, 
		{false, false, false, true, false, false, false, true, true, false, false, false, false}};
	
	private Player player;
	private Vehicle vehicle;
	private GameState game;
	
	private List<Location> mycel;
	private HashMap<Drain, Integer> drains;
	private Selector<Material> mushroomSelector;
	
	public MushroomCowSpecialItem()
	{
		this.mycel = new ArrayList<Location>();
		this.drains = new HashMap<Drain, Integer>();
		this.mushroomSelector = new AntiRecursiveRandomSelector<Material>(new Material[]{Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.AIR, Material.AIR});
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_MUSHROOMCOW.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.RED_MUSHROOM);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§c" + CourseMessage.ITEM_SPECIAL_MUSHROOMCOW.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.player = player;
		this.vehicle = vehicle;
		this.game = game;
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		for(int x = 0; x < 13; x++)
			for(int z = 0; z < 13; z++)
			{
				Location loc = new Location(player.getWorld(), vehicle.getEntity().getLocation().getBlockX() - 6 + x, vehicle.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().getBlockY(), vehicle.getEntity().getLocation().getBlockZ() - 6 + z);
				
				if(loc.getBlockY() < 0)
					continue;
				
				if(pattern[x][z])
				{
					this.mycel.add(loc);
					game.addBlockToRegen(loc);
					loc.getBlock().setType(Material.MYCEL);
					
					loc.add(0, 1, 0);
					
					this.mycel.add(loc);
					game.addBlockToRegen(loc);
					loc.getBlock().setType(this.mushroomSelector.next());
					
				}
			}
		
		
		return ItemResult.DISCARD;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(player == null || game == null)
			return;
		
		if(event.getPlayer() == player)
			return;
		
		GamePlayerData data;
		if((data = game.getPlayerData(event.getPlayer())) == null || data.isFinished())
			return;
		
		Material mushroom = null;
		
		for(Location loc : this.mycel)
			if(loc.getBlockX() == event.getTo().getBlockX() && loc.getBlockZ() == event.getTo().getBlockZ() && (loc.getBlock().getType() == Material.RED_MUSHROOM || loc.getBlock().getType() == Material.BROWN_MUSHROOM))
			{
				mushroom = loc.getBlock().getType();
				break;
			}
		
		if(mushroom == null)
			return;
		
		if(ShieldItem.protect(event.getPlayer()))
			return;
		
		new SoundEffect(Sound.DIG_GRASS, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), event.getTo());
		for(Player current : MobRacersPlugin.getGame().getPlayers())
			ParticleUtil.playBlockParticles(current, event.getTo(), ParticleType.BLOCK_CRACK, 0, 0, 0, 0, 15, mushroom.getId(), 0);
		event.getTo().getBlock().setType(Material.AIR);
		drain(event.getPlayer());
	}

	@Override
	public void cancel()
	{
		for(Drain drain : new ArrayList<Drain>(this.drains.keySet()))
			cancelDrain(drain);
		
		HandlerList.unregisterAll(this);
	}
	
	public void cancelDrain(Drain drain)
	{
		if(!drains.containsKey(drain))
			return;
		
		if(game.getPlayerData(drain.victim) != null && !game.getPlayerData(drain.victim).isFinished())
			game.getPlayerData(drain.victim).getVehicle().setSpeed(game.getPlayerData(drain.victim).getVehicle().getSpeed() + 0.05f);
		
		if(this.drains.get(drain) != -1)
			Bukkit.getScheduler().cancelTask(drains.get(drain));
		drains.remove(drain);
	}
	
	public void drainEnd(final Drain drain)
	{
		if(!drains.containsKey(drain))
			return;
		
		Bukkit.getScheduler().cancelTask(drains.get(drain));
		drains.put(drain, -1);
		
		vehicle.setSpeed(vehicle.getSpeed() + 0.05f);
		new SoundEffect(Sound.LEVEL_UP, 1, 1).play(player);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(vehicle.getEntity() != null && vehicle.getEntity().isValid())
					vehicle.setSpeed(vehicle.getSpeed() - 0.05f);
				cancelDrain(drain);
			}
		}, 120);
	}
	
	private void drain(Player p)
	{	
		game.getPlayerData(p).getVehicle().setSpeed(game.getPlayerData(p).getVehicle().getSpeed() - 0.05f);
		
		Drain drain = new Drain(p);
		
		this.drains.put(drain, Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), drain, 0, 1));
	}
	
	private class Drain implements Runnable
	{
		private Player victim;
		private Location location;
		
		public Drain(Player victim)
		{
			this.victim = victim;
			this.location = victim.getEyeLocation();
		}
		
		@Override
		public void run()
		{
			if(this.location == null || player.getLocation() == null)
			{
				cancelDrain(this);
				return;
			}
			
			if(location.getWorld() != player.getWorld())
			{
				cancelDrain(this);
				return;
			}
				
			if(location.distance(player.getEyeLocation()) < 1)
			{
				drainEnd(this);
				return;
			}
			
			for(int i = 0; i < 4; i++)
			{
				this.location.add(new Vector(
						(player.getEyeLocation().getX() - this.location.getX()) / (this.location.distance(player.getEyeLocation()) * 4), 
						(player.getEyeLocation().getY() - this.location.getY()) / (this.location.distance(player.getEyeLocation()) * 4),
						(player.getEyeLocation().getZ() - this.location.getZ()) / (this.location.distance(player.getEyeLocation()) * 4)));
				new ParticleData(ParticleType.SPELL_WITCH, 0, 0, 0, 0, 1, new int[0]).apply(this.location);
			}
		}
	}
}
