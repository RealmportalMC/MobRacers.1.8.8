package me.winterguardian.mobracers.item.types;

import me.winterguardian.core.util.SoundEffect;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SugarItem extends Item
{
	private Vehicle vehicle;
	private int taskId;
	
	private SoundEffect sound;
	
	public SugarItem()
	{
		this.taskId = -1;
		
		this.sound = new SoundEffect(Sound.ORB_PICKUP, 1, 1);
	}
	
	public SugarItem(SoundEffect sound)
	{
		this.taskId = -1;
		
		this.sound = sound;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SUGAR.toString();
	}

	@Override
	public ItemType getType()
	{
		return ItemType.SUGAR;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.vehicle = vehicle;
		
		vehicle.setSpeed(vehicle.getSpeed() + 0.4f);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, true));
		sound.play(player, player.getEyeLocation());
		this.taskId = Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 100).getTaskId();
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.SUGAR, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§a" + CourseMessage.ITEM_SUGAR.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(this.taskId == -1)
			return;
		
		if(vehicle == null)
			return;
		
		vehicle.setSpeed(vehicle.getSpeed() - 0.4f);
		Bukkit.getScheduler().cancelTask(this.taskId);
		this.taskId = -1;
	}

}
