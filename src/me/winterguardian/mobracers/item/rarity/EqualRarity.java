package me.winterguardian.mobracers.item.rarity;

import me.winterguardian.mobracers.item.ItemRarity;

public class EqualRarity implements ItemRarity
{
	private int odds;
	
	public EqualRarity(int odds)
	{
		this.odds = odds;
	}

	@Override
	public int getOdds(int position, int players)
	{
		return odds;
	}
	
	
}
