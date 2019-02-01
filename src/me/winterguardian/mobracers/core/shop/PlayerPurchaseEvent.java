package me.winterguardian.core.shop;

import me.winterguardian.core.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Created by 1541869 on 2015-11-24.
 */
public class PlayerPurchaseEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private Message errorMessage, successMessage;
    private Purchase purchase;
    private boolean cancelled;

    public PlayerPurchaseEvent(Player player, Purchase purchase)
    {
        super(player);
        this.purchase = purchase;
        this.errorMessage = Message.NULL;
        this.successMessage = Message.NULL;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

    public Message getErrorMessage()
    {
        if(errorMessage == null)
            return errorMessage = Message.NULL;

        return errorMessage;
    }

    public void setErrorMessage(Message errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public Message getSuccessMessage()
    {
        if(successMessage == null)
            return successMessage = Message.NULL;

        return successMessage;
    }

    public void setSuccessMessage(Message successMessage)
    {
        this.successMessage = successMessage;
    }

    public Purchase getPurchase()
    {
        return purchase;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
