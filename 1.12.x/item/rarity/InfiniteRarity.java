package me.winterguardian.mobracers.item.rarity;

import me.winterguardian.mobracers.item.ItemRarity;

public class InfiniteRarity implements ItemRarity
{
	@Override
	public int getOdds(int position, int players)
	{
		return 0;
	}
}
