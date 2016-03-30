package me.winterguardian.mobracers.arena;

import me.winterguardian.core.inventorygui.GUIItem;
import me.winterguardian.mobracers.state.lobby.ArenaSelectionState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 *
 * Created by Alexander Winter on 2016-01-23.
 */
public class ArenaGUIItem extends GUIItem
{
	private ArenaSelectionState state;
	private Arena arena;

	public ArenaGUIItem(ArenaSelectionState state, Arena arena, int slot)
	{
		super(slot);
		this.state = state;
		this.arena = arena;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		state.vote(player, arena.getName());
		player.closeInventory();
	}

	@Override
	public ItemStack getItemStack(Player player)
	{
		return arena.getIcon();
	}
}
