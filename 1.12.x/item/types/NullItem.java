package me.winterguardian.mobracers.item.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemResult;
import me.winterguardian.mobracers.item.ItemType;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

public class NullItem extends Item
{
	@Override
	public String getName()
	{
		return "Null";
	}

	@Override
	public ItemType getType()
	{
		return null;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		return ItemResult.DISCARD;
	}

	@Override
	public ItemStack getItemStack()
	{
		return new ItemStack(Material.BARRIER);
	}

	@Override
	public void cancel()
	{

	}

}
