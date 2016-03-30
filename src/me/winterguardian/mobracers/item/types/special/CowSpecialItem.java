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
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

public class CowSpecialItem extends SpecialItem implements Listener, Runnable
{
	private List<Player> protectedPlayers;
	
	private GameState game;
	private Player player;
	private Vehicle vehicle;

	private boolean active;
	
	public CowSpecialItem()
	{
		protectedPlayers = new ArrayList<Player>();
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_COW.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.game = game;
		this.vehicle = vehicle;
		this.player = player;
	
		this.active = true;
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.6f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				for(Player current : MobRacersPlugin.getGame().getPlayers())
					current.playSound(CowSpecialItem.this.player.getLocation(), Sound.COW_HURT, 1, 1);
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
		ItemStack item = new ItemStack(Material.LEATHER, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§6" + CourseMessage.ITEM_SPECIAL_COW.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(active)
		{
			this.active = false;
			vehicle.setSpeed(vehicle.getSpeed() - 0.6f);
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
				
				if(this.vehicle.getEntity().getLocation().distance(vehicle.getLocation()) < 1.5)
				{
					if(ShieldItem.protect(p.getPlayer()))
					{
						this.protectedPlayers.add(p.getPlayer());
						continue;
					}
					
					double x = this.vehicle.getEntity().getLocation().getX() - vehicle.getLocation().getX();
					if(x == 0)
						x = new Random().nextBoolean() ? 2 : -2;
					else
						x = 1 / x;
					
					if(x > 2)
						x = 2;
						
					if(x < -2)
						x = -2;
						
					double y = 1;
					double z = this.vehicle.getEntity().getLocation().getZ() - vehicle.getLocation().getZ();
					
					if(z == 0)
						z = new Random().nextBoolean() ? 2 : -2;
					else
						z = 1 / z;
					
					if(z > 2)
						z = 2;
						
					if(z < -2)
						z = -2;
						
					double reverseX = -Math.sin(Math.toRadians(-p.getPlayer().getLocation().getYaw()));
					double reverseZ = -Math.cos(Math.toRadians(-p.getPlayer().getLocation().getYaw()));
					
					if(x * reverseX < 0)
						x = x + reverseX;
					
					if(z * reverseZ < 0)
						z = z + reverseZ;
					
					vehicle.setVelocity(new Vector(x / 2, y, z / 2));
				}
			}
		}
	}
	
	@Override
	public void run()
	{
		for(Player current : MobRacersPlugin.getGame().getPlayers())
			current.playSound(this.player.getLocation(), Sound.COW_IDLE, 5, new Random().nextFloat() + 0.6f);
	}
}