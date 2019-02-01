package me.winterguardian.mobracers.item.rarity;

import me.winterguardian.mobracers.item.ItemRarity;

public class IncreasingRarity implements ItemRarity
{
	private boolean reverse;
	private int baseOdds;
	private int factor;
	
	public IncreasingRarity(boolean reverse, int baseOdds, int factor)
	{
		this.reverse = reverse;
		this.baseOdds = baseOdds;
		this.factor = factor;
	}

	@Override
	public int getOdds(int position, int players)
	{
		if(this.reverse)
			return (int) ((float)this.baseOdds + ((float)players - position) / ((float)players - 1) * (float)factor);
		
		return (int) ((float)this.baseOdds + ((float)position - 1) / ((float)players - 1) * (float)factor);
	}

}
