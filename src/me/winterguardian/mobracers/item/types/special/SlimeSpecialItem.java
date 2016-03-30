package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

public class SlimeSpecialItem extends SpecialItem implements Listener
{
	private Player player;
	private GameState game;
	
	private List<Location> traps;
	private List<Player> protecteds;
	
	public SlimeSpecialItem()
	{
		this.traps = new ArrayList<Location>();
		this.protecteds = new ArrayList<Player>();
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SLIME.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.SLIME_BALL, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§a" + CourseMessage.ITEM_SPECIAL_SLIME.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.player = player;
		this.game = game;
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		for(GamePlayerData victim : this.game.getPlayerDatas())
		{
			if(victim.getPlayer() == player)
				continue;
				
			if(victim.isFinished())
				continue;
			
			if(victim.getPosition() > game.getPosition(player))
				continue;
			
			Location loc = new Location(victim.getPlayer().getWorld(), victim.getPlayer().getLocation().getX() + 2 * Math.sin(Math.toRadians(-victim.getPlayer().getLocation().getYaw())), victim.getVehicle().getEntity().getLocation().getY() - 1, victim.getPlayer().getLocation().getZ() + 2 * Math.cos(Math.toRadians(-victim.getPlayer().getLocation().getYaw())));
			
			List<Location> locs = Arrays.asList(loc, loc.clone().add(0, 0, 1), loc.clone().add(0, 0, -1), loc.clone().add(1, 0, 0), loc.clone().add(-1, 0, 0));
			
			for(Location current : locs)
			{
				game.addBlockToRegen(current);
				this.traps.add(current);
				current.getBlock().setType(Material.SLIME_BLOCK);
			}
		}
		
		return ItemResult.DISCARD;
	}
	
	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event)
	{
		if(!MobRacersPlugin.getGame().contains(event.getPlayer()))
			return;
		
		if(event.getPlayer() == this.player)
			return;
		
		if(protecteds.contains(event.getPlayer()))
			return;
		
		final GamePlayerData data;
		
		if((data = game.getPlayerData(event.getPlayer())) == null || data.isFinished())
			return;
		
		Location current = null;
		
		for(Location loc : this.traps)
			if(loc.getBlockX() == data.getVehicle().getEntity().getLocation().getBlockX() && loc.clone().add(0, 1, 0).getBlockY() == data.getVehicle().getEntity().getLocation().getBlockY() && loc.getBlockZ() == data.getVehicle().getEntity().getLocation().getBlockZ())
			{
				current = loc;
				break;
			}
		
		if(ShieldItem.protect(event.getPlayer()))
		{
			protecteds.add(event.getPlayer());
			return;
		}
		
		if(current != null)
		{
			data.getVehicle().stop();
			protecteds.add(event.getPlayer());
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					data.getVehicle().start();
				}
			}, 60);
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					protecteds.remove(event.getPlayer());
				}
			}, 100);
		}
	}

	@Override
	public void cancel()
	{
		HandlerList.unregisterAll(this);
	}
}
