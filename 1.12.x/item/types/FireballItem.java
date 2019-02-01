package me.winterguardian.mobracers.item.types;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class FireballItem extends Item implements Listener
{	
	private GameState game;
	private Player sender;
	private Fireball ball;
	
	public FireballItem()
	{
	}
	
	@Override
	public String getName()
	{	return CourseMessage.ITEM_FIREBALL.toString();
	}

	@Override
	public ItemType getType()
	{	return ItemType.FIREBALL;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{	
		this.sender = player;
		this.game = game;
		game.getGame().getPlugin().getServer().getPluginManager().registerEvents(this, game.getGame().getPlugin());
		ball = (Fireball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.FIREBALL);
		ball.setShooter(player);
		ball.setYield(0);
		ball.setIsIncendiary(false);
		ball.setDirection(player.getLocation().getDirection());
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{	ItemStack item = new ItemStack(Material.FIREBALL, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§c" + CourseMessage.ITEM_FIREBALL.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{	HandlerList.unregisterAll(this);
	}

	
	@EventHandler
	public void onFireballHit(EntityExplodeEvent event)
	{	if(event.getEntity() != ball)
			return;
	
		for(Player player : MobRacersPlugin.getGame().getPlayers())
		{
			if(player == sender)
				continue;
			
			if(player.getLocation().distance(event.getLocation()) < 5)
			{	
				if(ShieldItem.protect(player))
					continue;
				game.getPlayerData(player).getVehicle().getEntity().setVelocity(new Vector(-Math.sin(Math.toRadians(-player.getLocation().getYaw())), 0.75, -Math.cos(Math.toRadians(-player.getLocation().getYaw()))));
			}
		}
		cancel();
		return;
	}
}
