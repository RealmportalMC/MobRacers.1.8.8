package me.winterguardian.mobracers.item.rarity;

import me.winterguardian.mobracers.item.ItemRarity;

public class ExemptionRarity implements ItemRarity
{
	private int odds;
	
	private int positionExempted;

	public ExemptionRarity(int position, int odds)
	{
		this.positionExempted = position;
		
		this.odds = odds;
	}
	
	public ExemptionRarity(int odds)
	{
		this.positionExempted = -1;
		
		this.odds = odds;
	}
	
	@Override
	public int getOdds(int position, int players)
	{
		if(this.positionExempted == position)
			return 0;
		else if(this.positionExempted == -1)
			if(position == players)
				return 0;
		return odds;
		
	}

}
