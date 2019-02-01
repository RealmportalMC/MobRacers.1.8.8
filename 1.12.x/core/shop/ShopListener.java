package me.winterguardian.core.shop;

import me.winterguardian.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The listener listening to the events to execute purchases and
 * create new purchases signs.aa
 *
 * Keep in mind purchases are only initiated in the listener and
 * executed asynchroniously.
 *
 * Created by 1541869 on 2015-11-24.
 */
public class ShopListener implements Listener
{
	private Shop shop;

	public ShopListener(Shop shop)
	{
		this.shop = shop;
	}

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event)
    {
		PurchaseType usedType = shop.getCreationType(event.getLines());

	    if(usedType == null)
		    return;

	    if(!event.getPlayer().hasPermission(usedType.getCreationPermission()))
		    return;

	    String[] sign = usedType.create(event.getLines());

	    for(int i = 0; i < 4; i++)
		    event.setLine(i, sign[i]);
    }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignClick(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK
		|| event.getClickedBlock() == null
		|| !(event.getClickedBlock().getState() instanceof Sign))
			return;

		shop.executePurchase(event.getPlayer(), ((Sign)event.getClickedBlock().getState()).getLines());
	}
}
