package me.winterguardian.mobracers.item.types;

import me.winterguardian.mobracers.item.Item;
import me.winterguardian.mobracers.item.ItemType;

public abstract class SpecialItem extends Item
{
	@Override
	public ItemType getType()
	{
		return ItemType.SPECIAL;
	}
}
