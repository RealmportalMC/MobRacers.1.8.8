package me.winterguardian.core.shop;

import me.winterguardian.core.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

/**
 * Represents a type of purchase.
 *
 * To create a new type of purchase, make a class implementing it and
 * register it using the Shop component.
 *
 * Created by 1541869 on 2015-11-24.
 * (1541869) is Alexander Winter's school id.
 */
public interface PurchaseType
{
	/**
	 * Gives the player the purchase from the sign content without any
	 * verifications or points transactions. This should only be called
	 * by the Purchase class.
	 *
	 * @param sign
	 * @param player
	 */
    void give(String[] sign, Player player);

	/**
	 * Checks if the purchase type is able to give this player's purchase.
	 * This should sent an error message to the player by itself if it can't.
	 *
	 * If you want to block some players from purchasing in a game system,
	 * you better use the PlayerPurchaseEvent. It should be blocked if
	 * the player really can't receive it's purchase to prevent future
	 * errors. Example, can't purchase a friend-zombie in a peaceful world.
	 *
	 * @param player
	 * @return true if can purchase, otherwise false
	 */
	boolean canGive(Player player);


	/**
	 * Checks if a sign can be associated to this purchase. Don't try to
	 * get the price or give the purchase to a player if it doesn't match.
	 *
	 * @param sign
	 * @return true is it matches, otherwise false
	 */
    boolean match(String[] sign);

	/**
	 * Gets you the price of the purchase from a sign content.
	 *
	 * @param sign
	 * @return the price
	 */
    int getPrice(String[] sign);


	/**
	 * Checks if a new purchase sign of this type can be created from the
	 * raw lines entered by the user.
	 *
	 * @param sign
	 * @return true if you can create the sign, otherwise false
	 */
	boolean canCreate(String[] sign);

	/**
	 * Creates a sign from the raw lines entered by the user. You should
	 * always check with the canCreate(String[]) method before invoking
	 * this method.
	 *
	 * @param sign
	 * @return the lines of the created sign
	 */
	String[] create(String[] sign);


	/**
	 * @return the permission needed to create a sign for this purchase type
	 */
    Permission getCreationPermission();


	/**
	 * @return the default error message when a player tries to buy without enough points
	 */
    Message getNotEnoughPointsMessage();
	/**
	 * @return the default message when a player purchased his article
	 */
    Message getPurchaseSuccessMessage();
}
