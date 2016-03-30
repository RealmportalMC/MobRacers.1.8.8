package me.winterguardian.mobracers.item.types.special;

import com.google.common.reflect.Reflection;
import me.winterguardian.core.util.ReflectionUtil;
import me.winterguardian.core.util.SoundEffect;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpiderSpecialItem extends SpecialItem
{

	public SpiderSpecialItem()
	{
		
	}
	
	@Override
	public String getName()
	{
		return CourseMessage.ITEM_SPECIAL_SPIDER.toString();
	}
	
	@Override
	public ItemStack getItemStack()
	{
		ItemStack item = new ItemStack(Material.STRING, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§7" + CourseMessage.ITEM_SPECIAL_SPIDER.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		Location to = vehicle.getEntity().getLocation().add(player.getLocation().getDirection().clone().multiply(25));

		if(!game.getArena().getRegion().contains(to))
			return ItemResult.KEEP;

		for(int v = 0; v < 20; v += 5)
		{
			Block block = vehicle.getEntity().getLocation().clone().add(player.getLocation().getDirection().clone().multiply(v)).getBlock();
			game.addBlockToRegen(block.getLocation());
			block.setType(Material.WEB);
		}
		
		game.getForceMountExceptions().add(player);
		if(!ReflectionUtil.isPaper())
		{
			vehicle.getEntity().eject();
			player.teleport(to);
		}
		
		vehicle.getEntity().teleport(to);

		if(!ReflectionUtil.isPaper())
			vehicle.getEntity().setPassenger(player);

		game.getForceMountExceptions().remove(player);
		
		vehicle.getEntity().setVelocity(player.getLocation().getDirection());
		
		new SoundEffect(Sound.SPIDER_IDLE, 1, 1).play(MobRacersPlugin.getGame().getPlayers(), vehicle.getEntity().getLocation());
		
		return ItemResult.DISCARD;
	}

	@Override
	public void cancel()
	{
		
	}
}
