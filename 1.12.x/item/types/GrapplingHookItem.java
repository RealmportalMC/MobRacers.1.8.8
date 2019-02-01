package me.winterguardian.mobracers.item.types;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class GrapplingHookItem extends Item implements Listener
{
	private Player player;
	private GameState game;
	private int uses;
	
	private boolean destroy;
	
	public GrapplingHookItem()
	{
		this.destroy = false;
		this.uses = 0;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_GRAPPLINGHOOK.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.GRAPPLING_HOOK;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{	if(uses == 0)
		{	this.player = player;
			this.game = game;
			
			Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		}
		
		if(uses++ > 2)
		{
			new SoundEffect(Sound.ITEM_BREAK, 1, 1).play(player);
			return ItemResult.DISCARD;
		}
	
		if(destroy)
			return ItemResult.DISCARD;
		
		return ItemResult.USEANDKEEP;
	}

	@Override
	public ItemStack getItemStack()
	{	ItemStack rod = new ItemStack(Material.FISHING_ROD);
		ItemMeta meta = rod.getItemMeta();
		meta.setDisplayName("§d" + CourseMessage.ITEM_GRAPPLINGHOOK.toString());
		rod.setItemMeta(meta);
		return rod;
	}

	@Override
	public void cancel()
	{
		if(destroy)
			return;

		if(game == null)
			return;

		this.destroy = true;
		if(game.getPlayerData(player) != null)
		{
			game.getPlayerData(player).useItem();
			player.getInventory().clear(0);
		}
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof FishHook) || ((FishHook)event.getDamager()).getShooter() != this.player)
			return;
		
		GamePlayerData victim = null;
		
		if(event.getEntity() instanceof Player)
			victim = game.getPlayerData((Player)event.getEntity());
		else if(game.getVehicle(event.getEntity()) != null && game.getOwner(game.getVehicle(event.getEntity())) != player)
			victim = game.getPlayerData(game.getOwner(game.getVehicle(event.getEntity())));

		if(victim == null || victim.getVehicle() == null || victim.getVehicle().getEntity() == null)
			return;
		
		if(ShieldItem.protect(victim.getPlayer()))
		{
			cancel();
			return;
		}
		
		Vector vector = new Vector(victim.getPlayer().getLocation().getX() - player.getLocation().getX(), 0, victim.getPlayer().getLocation().getZ() - player.getLocation().getZ());
		vector.normalize();
		vector.multiply(1.5);
		
		game.getPlayerData(player).getVehicle().getEntity().setVelocity(vector.setY(0.5));
		victim.getVehicle().getEntity().setVelocity(vector.multiply(-1).setY(0.5));
		
		event.setCancelled(false);
		event.setDamage(0);
		
		cancel();
	}
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent event)
	{
		if(event.getPlayer() != player)
			return;
		
		event.getHook().setVelocity(event.getHook().getVelocity().multiply(2));
	}
}
