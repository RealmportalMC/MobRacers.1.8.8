package me.winterguardian.core.inventorygui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-03.
 */
public class ExitGuiButton extends StaticGUIItem
{
	public ExitGuiButton(int slot, Material material, int amount, short durability)
	{
		super(slot, material, amount, durability, null, null);
	}

	public ExitGuiButton(int slot, Material material, int amount, short durability, String name, List<String> lore, ItemFlag... flags)
	{
		super(slot, material, amount, durability, name, lore, flags);
	}

	@Override
	public void click(Player player, ClickType click)
	{
		player.closeInventory();
	}
}
