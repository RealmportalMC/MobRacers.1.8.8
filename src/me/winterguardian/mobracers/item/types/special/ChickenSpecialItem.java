package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.particle.ParticleData;
import me.winterguardian.core.particle.ParticleType;
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
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class ChickenSpecialItem extends SpecialItem implements Listener, Runnable
{
	private Player player;
	private Vehicle vehicle;
	private GameState game;
	private int taskId;
	
	public ChickenSpecialItem()
	{
		this.taskId = -1;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_CHICKEN.toString();
	}
	
	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.player = player;
		this.vehicle = vehicle;
		this.game = game;
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.5f);
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), this, 0, 1);
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 120);
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.EGG, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§7" + CourseMessage.ITEM_SPECIAL_CHICKEN.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(vehicle == null)
			return;
		
		HandlerList.unregisterAll(this);
		
		if(this.taskId == -1)
			return;
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() - 0.5f);
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.taskId = -1;
	}
	
	@Override
	public void run()
	{
		Location loc = player.getLocation().clone().add(-Math.sin(-Math.toRadians(-player.getLocation().getYaw())), 0.25, -Math.cos(Math.toRadians(-player.getLocation().getYaw())));
		Egg egg = (Egg) vehicle.getEntity().getWorld().spawnEntity(loc, EntityType.EGG);
		egg.setShooter(player);
		egg.setVelocity(new Vector(-Math.sin(Math.toRadians(-player.getLocation().getYaw())) * 3, 0.5, -Math.cos(Math.toRadians(-player.getLocation().getYaw())) * 3));
		new SoundEffect(Sound.CHICKEN_EGG_POP, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), loc);
		new ParticleData(ParticleType.EXPLOSION_LARGE, 0, 0, 0, 0, 1, new int[0]).apply(loc);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Egg))
			return;
		
		if(!(event.getEntity() instanceof Player))
			return;
		
		if(!MobRacersPlugin.getGame().contains((Player) event.getEntity()))
			return;
		
		if(event.getEntity() == this.player)
			return;
		
		GamePlayerData data;
		
		if((data = game.getPlayerData((Player)event.getEntity())) == null)
			return;
		
		if(data.isFinished())
			return;
		
		if(ShieldItem.protect((Player) event.getEntity()))
		{
			event.setCancelled(true);
			return;
		}
		
		data.getVehicle().setSpeed(data.getVehicle().getSpeed() - 0.3f);
		new SoundEffect(Sound.CHICKEN_HURT, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), data.getPlayer().getEyeLocation());
		
		final Vehicle vehicle = data.getVehicle();
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{

			@Override
			public void run()
			{
				vehicle.setSpeed(vehicle.getSpeed() + 0.3f);
			}
			
		}, 40);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(event.getEntity() instanceof Chicken && game.isInRegion(event.getLocation()))
		{
			event.setCancelled(true);
		}
	}
}
