package me.winterguardian.mobracers.item.types.special;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ElderGuardianSpecialItem extends SpecialItem implements Listener
{
	private GameState game;
	private Player shooter;
	private List<BlockState> backup;
	
	public ElderGuardianSpecialItem()
	{	backup = new ArrayList<BlockState>();
	}
	
	@Override
	public String getName()
	{	return CourseMessage.ITEM_SPECIAL_ELDERGUARDIAN.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{	if(shooter == null)
		{	this.shooter = player;
			this.game = game;
			Bukkit.getPluginManager().registerEvents(this, game.getGame().getPlugin());
		}
		
		if(player.getItemInHand().getAmount() <= 1)
			return ItemResult.USEANDDISCARD;
		else
			return ItemResult.USEANDKEEP;
	}

	@Override
	public ItemStack getItemStack()
	{	ItemStack item = new ItemStack(Material.SNOW_BALL, 5);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§f" + CourseMessage.ITEM_SPECIAL_ELDERGUARDIAN.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		for(BlockState state : this.backup)
			state.update(true, false);
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{	if(!(event.getDamager() instanceof Snowball))
			return;
		
		if(((Snowball)event.getDamager()).getShooter() != this.shooter)
			return;
		
		GamePlayerData victim = null;
		
		if(event.getEntity() instanceof Player)
			victim = game.getPlayerData((Player)event.getEntity());
		else if(game.getVehicle(event.getEntity()) != null)
			victim = game.getPlayerData(game.getOwner(game.getVehicle(event.getEntity())));
		
		
		if(victim == null || victim.getPlayer() == this.shooter)
			return;
		
		if(ShieldItem.protect(victim.getPlayer()))
			return;
		
		final Vehicle victimVehicle = victim.getVehicle();
		
		victimVehicle.stop();
		Location loc = victimVehicle.getEntity().getLocation();
		for(int x = (int)(loc.getX() - 1.5); x < (int)(loc.getX() + 1.5); x++)
			for(int y = (int)(loc.getY()); y < (int)(loc.getY() + 4); y++)
				for(int z = (int)(loc.getZ() - 1.5); z < (int)(loc.getZ() + 1.5); z++)
				{	Location current = new Location(loc.getWorld(), x, y, z);
					game.addBlockToRegen(current);
					this.backup.add(current.getBlock().getState());
					current.getBlock().setType(Material.ICE);
				}
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{	@Override
			public void run()
			{
				victimVehicle.start();
				cancel();
			}
		}, 80);
	}
}
