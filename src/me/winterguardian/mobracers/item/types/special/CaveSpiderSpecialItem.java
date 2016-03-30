package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class CaveSpiderSpecialItem extends SpecialItem implements Runnable, Listener
{
	private GameState game;
	private Vehicle vehicle;
	private Player player;

	private List<Player> poisonVictims;
	
	private int i, taskId;
	
	public CaveSpiderSpecialItem()
	{
		this.poisonVictims = new ArrayList<Player>();
		this.i = 0;
		this.taskId = -1;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_CAVESPIDER.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.player = player;
		this.vehicle = vehicle;
		this.game = game;
		
		vehicle.setSpeed(vehicle.getSpeed() + 0.3f);
		
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), this, 0, 1);
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.POISONOUS_POTATO, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§5" + CourseMessage.ITEM_SPECIAL_CAVESPIDER.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(vehicle == null)
			return;
		
		if(this.taskId == -1)
			return;
		
		vehicle.setSpeed(vehicle.getSpeed() - 0.3f);
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.taskId = -1;
	}

	@Override
	public void run()
	{
		if(this.i >= 120)
		{
			cancel();
			return;
		}
		
		Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation().add(player.getLocation().getDirection()), player.getLocation().getDirection(), 2, 1.5f);
		arrow.setShooter(player);
		arrow.setCritical(false);
		new SoundEffect(Sound.SPIDER_IDLE, 1, 1).play(player);
		
		this.i++;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		
		if(!MobRacersPlugin.getGame().contains((Player) event.getEntity()))
			return;
		
		if(!(event.getDamager() instanceof Arrow))
			return;
		
		if(((Arrow) event.getDamager()).getShooter() != this.player)
			return;
		
		if(ShieldItem.protect((Player) event.getEntity()))
			return;
		
		this.poisonVictims.add((Player) event.getEntity());
		((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 80, 2), true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		
		if(!MobRacersPlugin.getGame().contains((Player) event.getEntity()))
			return;
		
		if(event.getCause() != DamageCause.POISON)
			return;
		
		if(!this.poisonVictims.contains(event.getEntity()))
			return;
		
		event.setCancelled(false);
		this.game.getPlayerData((Player)event.getEntity()).getVehicle().getEntity().setVelocity(new Vector(-Math.sin(Math.toRadians(-event.getEntity().getLocation().getYaw())) * 0.2, 0.1, -Math.cos(Math.toRadians(-event.getEntity().getLocation().getYaw())) * 0.2));
		
	}
}
