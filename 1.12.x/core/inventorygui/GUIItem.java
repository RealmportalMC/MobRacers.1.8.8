package me.winterguardian.core.inventorygui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * Created by Alexander Winter on 2015-12-19.
 */
public abstract class GUIItem
{
	private int slot;

	public GUIItem(int slot)
	{
		this.slot = slot;
	}

	public abstract void click(Player player, ClickType click);
	public abstract ItemStack getItemStack(Player player);

	public int getSlot()
	{
		return slot;
	}
}
