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
public class CommandButton extends StaticGUIItem
{
	private String command;

	public CommandButton(String command, int slot, Material material, int amount, short durability)
	{
		super(slot, material, amount, durability, null, null);
		this.command = command;
	}

	public CommandButton(String command, int slot, Material material, int amount, short durability, String name, List<String> lore, ItemFlag... flags)
	{
		super(slot, material, amount, durability, name, lore, flags);
		this.command = command;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(this.getCommand().startsWith("/"))
			player.performCommand(this.getCommand().substring(1));
		else
			player.chat(this.getCommand());
	}

	public String getCommand()
	{
		return this.command;
	}
}
