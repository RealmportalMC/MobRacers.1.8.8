package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SheepSpecialItem extends SpecialItem
{
	private Vehicle vehicle;
	private boolean active;
	
	public SheepSpecialItem()
	{
		this.active = true;
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SHEEP.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.SHEARS, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§f" + CourseMessage.ITEM_SPECIAL_SHEEP.toString());
		itemMeta.spigot().setUnbreakable(true);
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(((Sheep)vehicle.getEntity()).isSheared())
			return ItemResult.KEEP;
		
		
		this.vehicle = vehicle;
		this.active = true;
		
		new SoundEffect(Sound.SHEEP_SHEAR, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), player.getLocation());
		
		((Sheep)vehicle.getEntity()).setSheared(true);
		vehicle.setSpeed(vehicle.getSpeed() + 0.4f);
		
		Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					cancel();
				}
			}, 200);
		
		return ItemResult.DISCARD;
	}

	@Override
	public void cancel()
	{
		if(!active)
			return;
		
		if(this.vehicle != null && this.vehicle.getEntity() != null && this.vehicle.getEntity().isValid())
		{
			((Sheep)vehicle.getEntity()).setSheared(false);
			vehicle.setSpeed(vehicle.getSpeed() - 0.4f);
		}
		
		this.active = false;
	}
}
