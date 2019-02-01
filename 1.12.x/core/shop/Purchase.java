package me.winterguardian.core.shop;

import me.winterguardian.core.Core;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.playerstats.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * Created by 1541869 on 2015-11-24.
 */
public class Purchase
{
    private PurchaseType type;
    private String[] sign;
    private int price;

    public Purchase(PurchaseType type, String[] sign)
    {
        this.type = type;
        this.sign = sign;
        this.price = this.getBasePrice();
    }

    public boolean purchase(final Player player)
    {
	    if(!type.canGive(player))
		    return false;

        if(!Core.getUserDatasManager().isEnabled())
            return false;

	    PlayerStats newStats = new PlayerStats(Core.getUserDatasManager().getUserData(player));
        int points = newStats.getPoints();

        PlayerPurchaseEvent event = new PlayerPurchaseEvent(player, this);

        event.setErrorMessage(type.getNotEnoughPointsMessage());
        event.setSuccessMessage(type.getPurchaseSuccessMessage());

        if(points < getPrice())
            event.setCancelled(true);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if(event.isCancelled())
        {
            event.getErrorMessage().say(player, "<price>", "" + getPrice());
            return false;
        }
	    newStats.removePoints(getPrice());
	    points = newStats.getPoints();

	    Bukkit.getScheduler().runTask(Core.getShop().getPlugin(), new Runnable()
	    {
		    @Override
		    public void run()
		    {
			    type.give(sign, player);
		    }
	    });

        event.getSuccessMessage().say(player, "<price>", "" + getPrice(), "<bal>", "" + points);
        return true;
    }

    public int getBasePrice()
    {
        return Math.abs(type.getPrice(sign));
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }
}
