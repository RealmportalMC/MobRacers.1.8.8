package me.winterguardian.core.inventorygui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by 1541869 on 2015-11-19.
 */
public class InventoryGUI implements Listener
{
    private String name;
	private int size;
    private List<GUIItem> items;

    public InventoryGUI(String name, int size)
    {
        this(name, size, new ArrayList<GUIItem>());
    }

    public InventoryGUI(String name, int size, List<GUIItem> items)
    {
	    this.name = name;
	    this.size = size;
		this.items = items;

	    if(this.size % 9 != 0)
		    this.size = 27;
    }

    public void open(Player player)
    {
	    Inventory inventory = Bukkit.createInventory(null, size, name);
	    player.openInventory(inventory);

	    refresh(player);
    }

	public void refresh(Player player)
	{
		if(player.getOpenInventory() == null)
			return;

		for(GUIItem item : items)
			if(item.getItemStack(player) != null && item.getSlot() < size)
				player.getOpenInventory().setItem(item.getSlot(), item.getItemStack(player));

		player.updateInventory();
	}

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
	    if(!(event.getWhoClicked() instanceof Player))
		    return;

        if(event.getClickedInventory() == null || event.getClickedInventory().getName() == null || !event.getClickedInventory().getName().equals(this.name))
            return;

	    for(GUIItem item : items)
	        if(event.getSlot() == item.getSlot())
		        item.click((Player)event.getWhoClicked(), event.getClick());

	    event.setCancelled(true);
    }

	public List<GUIItem> getItems()
	{
		return this.items;
	}
}
