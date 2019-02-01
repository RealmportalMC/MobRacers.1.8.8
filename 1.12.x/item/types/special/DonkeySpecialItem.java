package me.winterguardian.mobracers.item.types.special;

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
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class DonkeySpecialItem extends SpecialItem implements Listener
{
	private GameState game;
	private Player player;
	private Vehicle vehicle;

	private boolean active;

	private GamePlayerData victim;
	
	private int timeLimitTaskId;
	
	public DonkeySpecialItem()
	{
		this.vehicle = null;
		this.victim = null;
		
		this.active = false;
		
		this.timeLimitTaskId = -1;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_DONKEY.toString();
	}
	
	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.game = game;
		this.vehicle = vehicle;
		this.player = player;
	
		this.active = true;
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.4f);
		new SoundEffect(Sound.DONKEY_ANGRY, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), player.getLocation());
		
		
		this.timeLimitTaskId = Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 100).getTaskId();
		return ItemResult.DISCARD;
	}
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(event.getPlayer() != this.player)
			return;
		
		if(this.game.getPlayerData(event.getPlayer()).isFinished())
		{
			cancel();
			return;
		}
		
		if(!this.active)
			throw new RuntimeException("DonkeySpecialItem NOT SUPPOSED TO BE ACTIVE IN LISTENER");
		
		Entity closest = null;
		
		for(Entity entity : vehicle.getEntity().getNearbyEntities(2, 2, 2))
		{
			if(entity == player.getPassenger())
				continue;
			
			if(game.getVehicle(entity) == null)
				continue;
			
			if(ShieldItem.protect(game.getOwner(game.getVehicle(entity))))
				continue;
			
			if(closest == null || closest.getLocation().distanceSquared(vehicle.getEntity().getLocation()) > entity.getLocation().distanceSquared(vehicle.getEntity().getLocation()))
				closest = entity;
		}
		
		if(closest == null)
			return;
		
		this.victim = this.game.getPlayerData(this.game.getOwner(this.game.getVehicle(closest)));
		
		new SoundEffect(Sound.DONKEY_IDLE, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), player.getLocation());
		victim.getPlayer().setPassenger(vehicle.getEntity());
		event.setCancelled(true);
		
		this.cancel();
		
		this.timeLimitTaskId = Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				if(vehicle == null)
					return;
				
				if(vehicle.getEntity() == null)
					return;
				
				cancel();
				vehicle.getEntity().setVelocity(new Vector(Math.sin(Math.toRadians(-player.getLocation().getYaw())) * 2, 0.1, Math.cos(Math.toRadians(-player.getLocation().getYaw())) * 2));
				new SoundEffect(Sound.DONKEY_IDLE, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), player.getLocation());
			}
		}, 120).getTaskId();
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.SADDLE, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§3" + CourseMessage.ITEM_SPECIAL_DONKEY.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(this.vehicle == null)
			return;
		
		if(this.vehicle.getEntity() == null)
			return;
		
		if(this.vehicle.getEntity().getVehicle() == null)
			return;
		
		if(this.timeLimitTaskId != -1)
		{
			Bukkit.getScheduler().cancelTask(this.timeLimitTaskId);
			this.timeLimitTaskId = -1;
		}
		
		if(this.active)
		{
			this.active = false;
			HandlerList.unregisterAll(this);
			this.vehicle.setSpeed(this.vehicle.getSpeed() - 0.4f);
		}
		else
		{
			this.vehicle.getEntity().leaveVehicle();
		}
	}
	
}
