package me.winterguardian.mobracers.item.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.winterguardian.core.particle.ParticleData;
import me.winterguardian.core.particle.ParticleType;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ShieldItem extends Item
{
	private static GameState game;
	
	public static void init(Plugin plugin)
	{
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new ShieldAnimation(), 0, 1);
	}
	
	private static List<Player> protectedPlayers = new ArrayList<Player>();
	private static ParticleData shieldParticle = new ParticleData(ParticleType.VILLAGER_HAPPY, 0, 0, 0, 0, 5, (int[])null);
	private static HashMap<Player, Integer> taskIds = new HashMap<Player, Integer>();
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SHIELD.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.SHIELD;
	}

	@Override
	public ItemResult onUse(final Player player, Vehicle vehicle, GameState game)
	{
		ShieldItem.game = game;
		protectedPlayers.add(player);
		
		if(taskIds.containsKey(player))
			Bukkit.getScheduler().cancelTask(taskIds.get(player));
		
		taskIds.put(player, Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				protectedPlayers.remove(player);
				taskIds.remove(player);
			}
		}, 160).getTaskId());
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§a" + CourseMessage.ITEM_SHIELD.toString());
		item.setItemMeta(itemMeta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		return item;
	}
	
	private static class ShieldAnimation implements Runnable
	{
		private double angle;
		
		public ShieldAnimation()
		{
			this.angle = 0;
		}
		
		@Override
		public void run()
		{
			for(Player p : new ArrayList<Player>(protectedPlayers))
				if(game.getPlayerData(p) != null && !game.getPlayerData(p).isFinished())
				shieldParticle.apply(p.getLocation().clone().add(-1.5f * Math.sin(Math.toRadians(this.angle)), 0.5, -1.5f * Math.cos(Math.toRadians(this.angle))));
			this.angle += 15;
		}
		
	}

	public static boolean protect(Player p)
	{
		if(!protectedPlayers.contains(p))
			return false;
		
		protectedPlayers.remove(p);
		new SoundEffect(Sound.ZOMBIE_WOOD, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), p.getLocation());
		
		for(int i = 0; i < 360; i += 15)
		{
			Location loc = p.getLocation().clone().add(-1.5f * Math.sin(Math.toRadians(i)), 0, -1.5f * Math.cos(Math.toRadians(i)));
			shieldParticle.apply(loc);
			shieldParticle.apply(loc.add(0, 0.5, 0));
			shieldParticle.apply(loc.add(0, 0.5, 0));
		}
		
		return true;
	}

	@Override
	public void cancel()
	{
		for(Player p : taskIds.keySet())
			Bukkit.getScheduler().cancelTask(taskIds.get(p));
		
		taskIds.clear();
		protectedPlayers.clear();
	}
}
