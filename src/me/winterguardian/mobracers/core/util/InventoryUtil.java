package me.winterguardian.core.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * Created by Alexander Winter on 2015-12-10.
 */
public class InventoryUtil
{
	private InventoryUtil(){}

	public static boolean canAdd(Inventory inventory, ItemStack item)
	{
		if(inventory.firstEmpty() != -1)
			return true;

		int remaining = item.getAmount();

		for(ItemStack stack : inventory.getContents())
			if(stack.isSimilar(item) && stack.getMaxStackSize() > stack.getAmount())
			{
				remaining -= stack.getMaxStackSize() - stack.getAmount();
				if(remaining <= 0)
					return true;
			}

		return false;
	}

	public static boolean canAddAtLeastOne(Inventory inventory, ItemStack item)
	{
		if(inventory.firstEmpty() != -1)
			return true;

		for(ItemStack stack : inventory.getContents())
			if(stack.isSimilar(item) && stack.getMaxStackSize() > stack.getAmount())
				return true;

		return false;
	}
}
