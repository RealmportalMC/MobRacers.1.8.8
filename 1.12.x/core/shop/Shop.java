package me.winterguardian.core.shop;

import me.winterguardian.core.DynamicComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

/**
 * Core's component permitting plugins to create shops to sells whatever they want.
 *
 * Created by 1541869 on 2015-11-24.
 */
public class Shop extends DynamicComponent
{
    private Set<PurchaseType> purchaseTypes;
	private ShopListener listener;

    public Shop()
    {
        this.purchaseTypes = new HashSet<>();
	    this.listener = new ShopListener(this);
    }

    @Override
    protected void register(Plugin plugin)
    {
	    Bukkit.getPluginManager().registerEvents(this.listener, plugin);
    }

    @Override
    protected void unregister(Plugin plugin)
    {
	    HandlerList.unregisterAll(this.listener);
    }

	public void executePurchase(final Player player, String[] args)
	{
        PurchaseType type;

        if((type = getPurchaseType(args)) != null)
		    executePurchase(player, type, args);
	}

	public void executePurchase(final Player player, PurchaseType type, String[] args)
	{
		final Purchase purchase = new Purchase(type, args);

		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				purchase.purchase(player);
			}
		});
	}

    public PurchaseType getPurchaseType(String[] sign)
    {
        for(PurchaseType current : purchaseTypes)
            if(current.match(sign))
                return current;
        return null;
    }

    public PurchaseType getCreationType(String[] sign)
    {
        for(PurchaseType current : purchaseTypes)
            if(current.canCreate(sign))
                return current;
        return null;
    }

    public void registerPurchaseType(PurchaseType type)
    {
        purchaseTypes.add(type);
    }

    public void unregisterPurchaseType(PurchaseType type)
    {
        purchaseTypes.remove(type);
    }
}

