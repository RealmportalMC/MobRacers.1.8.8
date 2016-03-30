package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.TitleUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class SilverfishSpecialItem extends SpecialItem implements Listener
{
	private Player player;
	private Vehicle vehicle;
	private GameState game;
	
	private boolean finished, active;
	
	public SilverfishSpecialItem()
	{
		this.finished = false;
		this.active = false;
	}
	
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SILVERFISH.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§8" + CourseMessage.ITEM_SPECIAL_SILVERFISH.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.player = player;
		this.vehicle = vehicle;
		this.game = game;
		
		if(active)
			return ItemResult.KEEP;
		
		if(finished)
			return ItemResult.DISCARD;
		
		TitleUtil.displayTitle(player, "{text:\"\"}", JsonUtil.toJson(CourseMessage.ITEM_SPECIAL_SILVERFISH_TITLE.toString()), 10, 30, 10);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 10), true);
		
		active = true;
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 100);
		
		return ItemResult.KEEP;
	}

	@Override
	public void cancel()
	{
		if(active)
		{
			this.active = false;
			HandlerList.unregisterAll(this);
		
			this.finished = true;
			game.getPlayerData(this.player).useItem();
		}
		
	}
	
	private void steal(final GamePlayerData data)
	{
		this.game.getForceMountExceptions().add(this.player);
		this.game.getForceMountExceptions().add(data.getPlayer());
		
		this.game.getPlayerData(this.player).setVehicle(data.getVehicle());
		data.setVehicle(this.vehicle);
		
		this.game.getPlayerData(this.player).getVehicle().getEntity().eject();
		player.teleport(game.getPlayerData(player).getVehicle().getEntity());
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				game.getPlayerData(player).getVehicle().getEntity().setPassenger(player);
			}
			
		}, 5);
		data.getPlayer().setVelocity(new Vector(0, 2, 0));
		final Vehicle newVehicle = this.game.getPlayerData(this.player).getVehicle();
		newVehicle.setSpeed(newVehicle.getSpeed() + 0.3f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				data.getPlayer().teleport(data.getVehicle().getEntity());
			}
			
		}, 40);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				data.getVehicle().getEntity().setPassenger(data.getPlayer());
				game.getForceMountExceptions().remove(player);
				game.getForceMountExceptions().remove(data.getPlayer());
			}
			
		}, 45);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				newVehicle.setSpeed(newVehicle.getSpeed() - 0.3f);
			}
			
		}, 120);
		
		cancel();
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if(event.getPlayer() != player)
			return;
		
		if(game.getVehicle(event.getRightClicked()) == null)
			return;
		
		GamePlayerData data;
		
		if((data = game.getPlayerData(game.getOwner(game.getVehicle(event.getRightClicked())))) == null)
			return;
		
		if(data.getPlayer() == player)
			return;
		
		event.setCancelled(true);
		steal(data);
	}
	
	@EventHandler
	public void onPlayerInteractPlayer(PlayerInteractEntityEvent event)
	{
		if(event.getPlayer() != player)
			return;
		
		if(!(event.getRightClicked() instanceof Player))
			return;
		
		if(MobRacersPlugin.getGame().contains((Player)event.getRightClicked()))
			return;
		
		GamePlayerData data;
		
		if((data = game.getPlayerData((Player)event.getRightClicked())) == null)
			return;
		
		event.setCancelled(true);
		steal(data);
	}
}
