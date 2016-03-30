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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkeletonHorseSpecialItem extends SpecialItem implements Listener
{
	private GameState game;
	private Vehicle vehicle;
	private Player player;
	
	private boolean active;
	private boolean finish;
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SKELETONHORSE.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(this.active)
			return ItemResult.USEANDKEEP;
		
		if(this.finish)
			return ItemResult.DISCARD;
		
		this.game = game;
		this.vehicle = vehicle;
		this.player = player;
	
		this.active = true;
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		new SoundEffect(Sound.HORSE_SADDLE, 1f, 1f).play(player);
		player.getInventory().setArmorContents(new ItemStack[]{null, null, new ItemStack(Material.CHAINMAIL_CHESTPLATE), null});
		player.getInventory().setItem(27, new ItemStack(Material.ARROW, 64));
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.3f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 160);
		return ItemResult.USEANDKEEP;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.BOW, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r" + CourseMessage.ITEM_SPECIAL_SKELETONHORSE.toString());
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
			player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
			player.getInventory().setItem(27, null);
			player.getInventory().clear(0);
			game.getPlayerData(player).useItem();
			vehicle.setSpeed(vehicle.getSpeed() - 0.3f);
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Arrow))
			return;
		
		if(((Arrow) event.getDamager()).getShooter() != this.player)
			return;
		
		final GamePlayerData victim;
		
		if(event.getEntity() instanceof Player && (victim = game.getPlayerData((Player)event.getEntity())) != null && !victim.isFinished())
		{
			if(ShieldItem.protect(victim.getPlayer()))
				return;
			
			event.setCancelled(false);
			event.setDamage(0.0);
			
			victim.getVehicle().setSpeed(victim.getVehicle().getSpeed() - 0.4f);
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					victim.getVehicle().setSpeed(victim.getVehicle().getSpeed() + 0.4f);
				}
				
			}, 80);
		}
				
	}
}
