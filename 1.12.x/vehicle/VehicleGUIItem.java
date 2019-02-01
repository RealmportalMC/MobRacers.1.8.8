package me.winterguardian.mobracers.vehicle;

import me.winterguardian.core.inventorygui.StaticGUIItem;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.state.lobby.VehicleSelectionState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 *
 * Created by Alexander Winter on 2016-01-18.
 */
public class VehicleGUIItem extends StaticGUIItem
{
	private VehicleType type;

	public VehicleGUIItem(VehicleType type, int slot, ItemStack stack)
	{
		super(slot, stack);
		this.type = type;
	}

	public VehicleGUIItem(VehicleType type, int slot, Material material, int amount, short durability)
	{
		super(slot, material, amount, durability);
		this.type = type;
	}

	public VehicleGUIItem(VehicleType type, int slot, Material material, int amount, short durability, String name, List<String> lore, ItemFlag... flags)
	{
		super(slot, material, amount, durability, name, lore, flags);
		this.type = type;
	}

	@Override
	public void click(Player player, ClickType click)
	{
		if(!(MobRacersPlugin.getGame().getState() instanceof VehicleSelectionState))
			return;

		VehicleSelectionState selection = (VehicleSelectionState)MobRacersPlugin.getGame().getState();
		selection.onChoose(player, type, false);
	}
}
