package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagmaCubeSpecialItem extends SpecialItem implements Listener, Runnable
{
	private GamePlayerData playerData;
	private Vehicle vehicle;
	private GameState game;
	
	private int taskId;
	
	public MagmaCubeSpecialItem()
	{
		
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_MAGMACUBE.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.playerData = game.getPlayerData(player);
		this.vehicle = vehicle;
		this.game = game;
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.6f);
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRacersPlugin.getPlugin(), this, 0, 1);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 140);
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.TORCH, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§6" + CourseMessage.ITEM_SPECIAL_MAGMACUBE.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		HandlerList.unregisterAll(this);

		if(this.taskId == -1)
			return;

		if(vehicle != null)
			this.vehicle.setSpeed(this.vehicle.getSpeed() - 0.6f);
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.taskId = -1;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(event.getEntity() == this.playerData.getPlayer())
		{
			if(game.getPlayerData(this.playerData.getPlayer()) != null && this.playerData.getVehicle().getEntity() != null)
			{
				this.playerData.getPlayer().setFireTicks(0);
				this.playerData.getVehicle().getEntity().setFireTicks(0);
			}
			event.setCancelled(true);
			return;
		}
		
		if(game.isInRegion(event.getEntity().getLocation()) && (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK))
		{
			event.setCancelled(false);
			event.setDamage(0.0);
		}
	}
	

	@Override
	public void run()
	{
		if(playerData.isFinished())
		{
			cancel();
			return;
		}
		
		final Location loc = playerData.getVehicle().getEntity().getLocation();
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				game.addBlockToRegen(loc);
				loc.getBlock().setType(Material.FIRE);
			}
		}, 5);
			
	}
}
