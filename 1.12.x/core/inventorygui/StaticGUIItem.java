package me.winterguardian.core.inventorygui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-03.
 */
public abstract class StaticGUIItem extends GUIItem
{
	private ItemStack stack;

	public StaticGUIItem(int slot, Material material, int amount, short durability)
	{
		this(slot, material, amount, durability, null, null);
	}

	public StaticGUIItem(int slot, Material material, int amount, short durability, String name, List<String> lore, ItemFlag... flags)
	{
		super(slot);
		this.stack = new ItemStack(material, amount, durability);
		ItemMeta meta = this.stack.getItemMeta();
		if(name != null)
			meta.setDisplayName(name);
		if(lore != null)
			meta.setLore(lore);

		if(flags != null && flags.length > 0)
			meta.addItemFlags(flags);

		this.stack.setItemMeta(meta);
	}

	public StaticGUIItem(int slot, ItemStack stack)
	{
		super(slot);
		this.stack = stack;
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		return this.stack;
	}
}
