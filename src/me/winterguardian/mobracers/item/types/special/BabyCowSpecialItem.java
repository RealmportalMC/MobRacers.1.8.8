package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

public class BabyCowSpecialItem extends SpecialItem implements Listener, Runnable
{
	private List<Player> protectedPlayers;
	
	private GameState game;
	private Player player;
	private Vehicle vehicle;

	private boolean active;
	
	public BabyCowSpecialItem()
	{
		protectedPlayers = new ArrayList<Player>();
	}

	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_BABYCOW.toString();
	}
	
	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(player.getItemInHand().getType() == Material.MILK_BUCKET)
			return ItemResult.USEANDKEEP;
		
		this.game = game;
		this.vehicle = vehicle;
		this.player = player;
	
		this.active = true;
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.5f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				for(Player current : MobRacersPlugin.getGame().getPlayers())
					current.playSound(BabyCowSpecialItem.this.player.getLocation(), Sound.COW_HURT, 1, 1.5f);
				cancel();
			}
		}, 100);
		
		for(int i = 0; i < 10; i++)
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), this, 10);
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.MILK_BUCKET, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§f" + CourseMessage.ITEM_SPECIAL_BABYCOW.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(active)
		{
			this.active = false;
			if(vehicle != null)
				vehicle.setSpeed(vehicle.getSpeed() - 0.5f);
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(this.active && this.player == event.getPlayer())
		{	
			for(GamePlayerData p : this.game.getPlayerDatas())
			{
				if(p.getPlayer() == this.player)
					continue;
				
				if(p.isFinished())
					continue;
				
				Entity vehicle = p.getVehicle().getEntity();
				
				if(vehicle == null)
					continue;
				
				if(this.protectedPlayers.contains(p.getPlayer()))
					continue;
				
				if(this.vehicle.getEntity().getLocation().distance(vehicle.getLocation()) > 1.5)
					continue;
				
				this.vehicle.getEntity().setVelocity(new Vector(Math.sin(Math.toRadians(-p.getPlayer().getLocation().getYaw())) * 1.5f, 0.75, Math.cos(Math.toRadians(-p.getPlayer().getLocation().getYaw())) * 1.5f));
			}
		}
	}
	
	@Override
	public void run()
	{
		for(Player current : MobRacersPlugin.getGame().getPlayers())
			current.playSound(this.player.getLocation(), Sound.COW_IDLE, 5, new Random().nextFloat() + 1.1f);
	}
}
