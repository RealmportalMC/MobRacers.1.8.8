package me.winterguardian.mobracers.item.types;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.particle.ParticleUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.core.util.TitleUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class CloudItem extends Item implements Listener
{
	private static List<Player> players = new ArrayList<>();

	public static void init(Plugin plugin)
	{
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new CloudAnimation(), 0, 1);
	}
	
	private Player holder;
	private GameState game;
	private Vehicle victimVehicle;
	private float deltaSpeed;
	
	public CloudItem()
	{

	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_CLOUD.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.CLOUD;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(players.contains(player))
			return ItemResult.DISCARD;

		this.holder = player;
		this.game = game;
		players.add(player);

		TitleUtil.displayTitle(player, "{text:\"\"}", JsonUtil.toJson(CourseMessage.ITEM_CLOUD_TITLE.toString()), 10, 30, 10);

		if(vehicle != null)
			vehicle.setSpeed(vehicle.getSpeed() + 0.1f);
		
		Bukkit.getPluginManager().registerEvents(this, MobRacersPlugin.getPlugin());
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				victimVehicle = CloudItem.this.game.getPlayerData(holder).getVehicle();
				victimVehicle.setSpeed(victimVehicle.getSpeed() - 0.1f);
				holder.getWorld().strikeLightningEffect(holder.getLocation());
				if(!ShieldItem.protect(holder))
				{
					deltaSpeed = victimVehicle.getSpeed() * 2 / 3;
					victimVehicle.setSpeed(victimVehicle.getSpeed() - deltaSpeed);
					Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
					{
						@Override
						public void run()
						{
							if(victimVehicle != null)
								victimVehicle.setSpeed(victimVehicle.getSpeed() + deltaSpeed);
						}
					}, 120);
				}
				cancel();
			}
		}, 200);
		
		
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{	
		ItemStack item = new ItemStack(Material.INK_SACK, 1, (short)7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§e" + CourseMessage.ITEM_CLOUD.toString());
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public void cancel()
	{	
		players.remove(this.holder);
		HandlerList.unregisterAll(this);
	}
	
	private static class CloudAnimation implements Runnable
	{	
		@Override
		public void run()
		{	
			for(Player p : new ArrayList<>(players))
				if(p != null && p.isOnline() && MobRacersPlugin.getGame().contains(p))
					ParticleUtil.playSimpleParticles(p.getEyeLocation().clone().add(0, 1, 0), ParticleType.CLOUD, 0.3f, 0.1f, 0.3f, 0, 25);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{	
		if(event.getDamager() != this.holder)
			return;
		
		if(!(event.getEntity() instanceof Player) || !MobRacersPlugin.getGame().contains((Player) event.getEntity()))
			return;
		
		this.holder = (Player) event.getEntity();

		Vehicle vehicle = game.getPlayerData((Player)event.getDamager()).getVehicle();
		vehicle.setSpeed(vehicle.getSpeed() - 0.1f);

		Vehicle newHolderVehicle = game.getPlayerData((Player)event.getEntity()).getVehicle();
		newHolderVehicle.setSpeed(newHolderVehicle.getSpeed() + 0.1f);

		players.remove(event.getDamager());
		players.add(this.holder);
		event.setCancelled(false);
		event.setDamage(0);
		
		new SoundEffect(Sound.HORSE_BREATHE, 1, 1f).play(MobRacersPlugin.getGame().getPlayers(), this.holder.getLocation());
				
	}
}
