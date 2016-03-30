package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TitleUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.state.game.ItemBox;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WolfSpecialItem extends SpecialItem implements Listener
{
	private Player player;
	private Vehicle vehicle;
	private GameState game;
	
	private int taskId;
	
	public WolfSpecialItem()
	{
		this.taskId = -1;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_WOLF.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.BONE, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§4" + CourseMessage.ITEM_SPECIAL_WOLF.toString());
		item.setItemMeta(itemMeta);
		return item;
	}
	
	@Override
	public ItemResult onUse(Player p, Vehicle v, GameState g)
	{
		if(!(v.getEntity() instanceof Wolf))
			return ItemResult.DISCARD;
		this.player = p;
		this.vehicle = v;
		this.game = g;
		
		TitleUtil.displayTitle(p, "{text:\"\"}", JsonUtil.toJson(CourseMessage.ITEM_SPECIAL_WOLF_TITLE.toString()), 10, 30, 10);
		
		((Wolf)v.getEntity()).setAngry(true);
		v.setSpeed(v.getSpeed() + 0.8f);
		new SoundEffect(Sound.WOLF_HOWL, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), p.getLocation());
		
		this.report();
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		return ItemResult.DISCARD;
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(event.getPlayer() == this.player)
		{
			if(game.getPlayerData(this.player) != null)
			{
				if(!game.getPlayerData(this.player).isFinished())
				{
					for(ItemBox item : ((GameState)MobRacersPlugin.getGame().getState()).getItemBoxes())
						if(item.collide(this.vehicle))
						{
							item.pickUp();
							new SoundEffect(Sound.ITEM_BREAK, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), event.getTo());
						}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() == this.player)
		{
			if(game.getPlayerData(this.player) != null)
			{
				if(!game.getPlayerData(this.player).isFinished())
				{
					Player victim = null;
					if(event.getEntity() instanceof Player)
						victim = (Player) event.getEntity();

					else
						victim = game.getOwner(game.getVehicle(event.getEntity()));

					if(game.getPlayerData(victim) != null)
					{
						if(!game.getPlayerData(victim).isFinished())
						{
							if(ShieldItem.protect(victim))
								return;
							
							event.setCancelled(false);
							event.setDamage(0);
							new SoundEffect(Sound.WOLF_GROWL, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), this.player.getLocation());
							this.report();
						}
					}
				}
			}
		}
	}
	
	@Override
	public void cancel()
	{
		if(this.taskId != -1)
		{
			Bukkit.getScheduler().cancelTask(this.taskId);
			this.taskId = -1;
		}
		
		HandlerList.unregisterAll(this);
		
		if(vehicle.getEntity() == null)
			return;
		
		((Wolf)vehicle.getEntity()).setAngry(false);
		vehicle.setSpeed(vehicle.getSpeed() - 0.8f);
		
	}
	
	public void report()
	{
		if(this.taskId != -1)
			Bukkit.getScheduler().cancelTask(this.taskId);
		
		this.taskId = Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{

			@Override
			public void run()
			{
				cancel();
			}
			
		}, 80).getTaskId();
	}
}
