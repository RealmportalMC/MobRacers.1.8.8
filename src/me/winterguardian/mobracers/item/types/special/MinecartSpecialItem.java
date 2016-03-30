package me.winterguardian.mobracers.item.types.special;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.types.SpecialItem;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MinecartSpecialItem extends SpecialItem implements Listener, Runnable
{	
	private GameState game;
	private Player player;
	private Vehicle vehicle;
	private boolean finished;

	public MinecartSpecialItem()
	{
		finished = false;
		game = null;
	}

	@Override
	public String getName()
	{	
		return CourseMessage.ITEM_SPECIAL_MINECART.toString();
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		if(finished)
			return ItemResult.DISCARD;

		if(this.game != null)
			return ItemResult.KEEP;

		this.game = game;
		this.player = player;
		this.vehicle = vehicle;
		vehicle.setSpeed(vehicle.getSpeed() + 0.4f);
		game.getGame().getPlugin().getServer().getPluginManager().registerEvents(this, game.getGame().getPlugin());
		game.getGame().getPlugin().getServer().getScheduler().runTaskLater(game.getGame().getPlugin(), this, 150);
		player.setGameMode(GameMode.SURVIVAL);

		return ItemResult.KEEP;
	}

	@Override
	public ItemStack getItemStack()
	{	
		ItemStack item = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§r§f" + CourseMessage.ITEM_SPECIAL_MINECART.toString());
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	public void cancel()
	{	
		HandlerList.unregisterAll(this);
	}

	@Override
	public void run()
	{	
		vehicle.setSpeed(vehicle.getSpeed() - 0.4f);
		this.finished = true;
		player.getInventory().clear(0);
		game.getPlayerData(player).useItem();
		player.setGameMode(GameMode.ADVENTURE);

		cancel();
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{	
		if(event.getPlayer() != player)
			return;

		if(event.getAction() != Action.LEFT_CLICK_BLOCK || event.getClickedBlock() == null)
			return;
	
		game.addBlockToRegen(event.getClickedBlock().getLocation());
		event.getClickedBlock().breakNaturally();
		event.setCancelled(true);
	}
}
