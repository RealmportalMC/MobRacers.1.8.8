package me.winterguardian.mobracers.music;

import me.winterguardian.mobracers.stats.CoursePurchase;

import org.bukkit.entity.Player;

public class PurchasableMusic extends CourseMusic
{
	private CoursePurchase purchase;
	
	public PurchasableMusic(CourseRecord record, CoursePurchase purchase)
	{
		super(record);
		this.purchase = purchase;
	}

	@Override
	public boolean isAvailable(Player p)
	{
		return this.purchase.hasPurchased(p);
	}

}
