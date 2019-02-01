package me.winterguardian.mobracers.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.vehicle.Vehicle;

public class RandomItem extends Item
{
	private Item current;
	
	public RandomItem()
	{
		this.current = null;
	}
	
	@Override
	public String getName()
	{
		return "???";
	}

	@Override
	public ItemType getType()
	{
		return ItemType.RANDOM;
	}

	@Override
	public ItemResult onUse(Player player, Vehicle vehicle, GameState game)
	{
		return ItemResult.KEEP;
	}

	@Override
	public ItemStack getItemStack()
	{
		if(this.current == null)
			return null;
		return this.current.getItemStack();
	}

	@Override
	public void cancel()
	{

	}
	
	public Item next(Vehicle vehicle, int position, int players)
	{
		ItemType itemType;
		do
		{
			itemType = ItemType.getRandom(position, players);
			if(this.current == null)
				break;
		}
		while(itemType == this.current.getType());
		
		if(vehicle.getItem(itemType) != null)
			return this.current = vehicle.getItem(itemType);
		return this.current = itemType.getDefault();
	}
	
	public Item getCurrentItem()
	{
		return this.current;
	}

}
