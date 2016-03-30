package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.core.entity.custom.rideable.RideableEntityUtil;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.TitleUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RabbitSpecialItem extends SpecialItem
{
	private Vehicle vehicle;
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_RABBIT.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.CARROT_ITEM, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§6" + CourseMessage.ITEM_SPECIAL_RABBIT.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		this.vehicle = vehicle;
		
		TitleUtil.displayTitle(player, "{text:\"\"}", JsonUtil.toJson(CourseMessage.ITEM_SPECIAL_RABBIT_TITLE.toString()), 10, 30, 10);
		
		RideableEntityUtil.setJumpThrust(vehicle.getEntity(), 1.25f);
		RideableEntityUtil.setJumpHeight(vehicle.getEntity(), 0.75f);
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(MobRacersPlugin.getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		}, 140);
		return ItemResult.DISCARD;
	}

	@Override
	public void cancel()
	{
		if(this.vehicle == null || this.vehicle.getEntity() == null || !this.vehicle.getEntity().isValid())
			return;
		
		RideableEntityUtil.setJumpThrust(vehicle.getEntity(), 0f);
		RideableEntityUtil.setJumpHeight(vehicle.getEntity(), 0f);
	}
}
