package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.ShieldItem;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GamePlayerData;
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

public class SquidSpecialItem extends SpecialItem
{
	private Vehicle vehicle;
	private boolean active;
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SQUID.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.vehicle = vehicle;
		this.active = true;
		
		this.vehicle.setSpeed(this.vehicle.getSpeed() + 0.15f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
		{

			@Override
			public void run()
			{
				cancel();
			}
				
		}, 140);
		
		for(Player p : MobRacersPlugin.getGame().getPlayers())
		{
			if(p == player)
				continue;
				
			GamePlayerData data;
			if((data = game.getPlayerData(p)) == null)
				continue;
			
			if(data.isFinished())
				continue;
			
			if(ShieldItem.protect(p))
				continue;
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 140, 0, false, false), true);
			new SoundEffect(Sound.AMBIENCE_THUNDER, 1f, 1f).play(p);
		}
	
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.INK_SACK, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§8" + CourseMessage.ITEM_SPECIAL_SQUID.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{
		if(this.vehicle == null)
			return;
		
		if(active)
		{
			this.vehicle.setSpeed(this.vehicle.getSpeed() - 0.15f);
			this.active = false;
		}
	}
}
