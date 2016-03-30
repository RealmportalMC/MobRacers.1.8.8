package me.winterguardian.mobracers.vehicle;

import org.bukkit.entity.Player;

import me.winterguardian.mobracers.stats.CoursePurchase;

public abstract class PurchasableVehicle extends Vehicle
{
	public abstract CoursePurchase getPurchase();
	
	@Override
	public boolean canChoose(Player p)
	{
		return getPurchase().hasPurchased(p);
	}
}
