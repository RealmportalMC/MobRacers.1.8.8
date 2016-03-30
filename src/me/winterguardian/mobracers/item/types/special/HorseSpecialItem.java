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
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class HorseSpecialItem extends SpecialItem implements Listener
{
	private GameState game;
	private Vehicle vehicle;
	private Player player;
	
	private boolean active;
	private boolean finish;
	
	public HorseSpecialItem()
	{
		this.active = false;
		this.finish = false;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_HORSE.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(this.active)
			return ItemResult.KEEP;
		
		if(this.finish)
			return ItemResult.DISCARD;
		
		this.game = game;
		this.vehicle = vehicle;
		this.player = player;
	
		this.active = true;
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		new SoundEffect(Sound.HORSE_SADDLE, 1f, 1f).play(player);
		player.getInventory().setArmorContents(new ItemStack[]
				{
					new ItemStack(Material.IRON_BOOTS), 
					new ItemStack(Material.IRON_LEGGINGS), 
					new ItemStack(Material.IRON_CHESTPLATE), 
					new ItemStack(Material.CHAINMAIL_HELMET)
				});
		
		if(this.vehicle.getEntity() != null && !this.vehicle.getEntity().isDead() && !this.vehicle.getEntity().isValid())
			((Horse)this.vehicle.getEntity()).getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.3f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 160);
		return ItemResult.KEEP;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§9" + CourseMessage.ITEM_SPECIAL_HORSE.toString());
		itemMeta.spigot().setUnbreakable(true);
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(active)
		{
			this.finish = true;
			this.active = false;
			if(game.getPlayerData(player) != null)
			{
				player.getInventory().clear(0);
				game.getPlayerData(player).useItem();
				vehicle.setSpeed(vehicle.getSpeed() - 0.3f);
			}
			player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
			HandlerList.unregisterAll(this);
			
			if(this.vehicle.getEntity() != null && !this.vehicle.getEntity().isDead() && !this.vehicle.getEntity().isValid())
				((Horse)this.vehicle.getEntity()).getInventory().setArmor(null);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() != this.player)
			return;
		
		GamePlayerData victim;
		
		if(event.getEntity() instanceof Player && (victim = game.getPlayerData((Player)event.getEntity())) != null && !victim.isFinished())
		{
			if(ShieldItem.protect(victim.getPlayer()))
				return;
			
			event.setCancelled(false);
			event.setDamage(0.0);
			
			double x, z;
			
			try
			{
				x = 0.5 / (event.getDamager().getLocation().getX() - event.getEntity().getLocation().getX());
			}
			catch(Exception e)
			{
				x = 2;
			}
			
			if(x > 2)
				x = 2;
			
			try
			{
				z = 0.5 / (event.getDamager().getLocation().getZ() - event.getEntity().getLocation().getZ());
			}
			catch(Exception e)
			{
				z = 2;
			}
			
			if(z > 2)
				z = 2;
			
			victim.getVehicle().getEntity().setVelocity(new Vector(x, 0.5, z));
		}
				
	}
}
