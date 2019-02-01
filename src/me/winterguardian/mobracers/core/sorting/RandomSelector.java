package me.winterguardian.core.sorting;

import java.util.Random;

public abstract class RandomSelector<A> extends Selector<A>
{
	protected Random random;
	
	public RandomSelector(A[] elements)
	{
		super(elements);
		this.random = new Random();
	}
	
}
