package me.winterguardian.core.sorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AntiRecursiveRandomSelector<A> extends RandomSelector<A>
{
	private int[] uses;
	
	public AntiRecursiveRandomSelector(A[] elements)
	{	
		super(elements);
		
		this.uses = new int[this.elements.length];
		
		for(int i = 0; i < this.elements.length; i++)
			this.uses[i] = 0;
	}
	
	@SuppressWarnings("unchecked")
	public AntiRecursiveRandomSelector(Collection<A> elements)
	{	
		this((A[]) elements.toArray());
	}

	@Override
	public A next()
	{
		List<A> lowests = new ArrayList<A>();
		int lowestUses = Integer.MAX_VALUE;
		for(int i = 0; i < this.elements.length; i++)
		{
			if(this.uses[i] == lowestUses)
				lowests.add(this.elements[i]);
			else if(this.uses[i] < lowestUses)
			{
				lowests.clear();
				lowests.add(this.elements[i]);
				lowestUses = this.uses[i];
			}
		}

		if(lowests.size() <= 0)
			return null;

		A object = lowests.get(this.random.nextInt(lowests.size()));
		
		for(int i = 0; i < this.elements.length; i++)
			if(this.elements[i] == object)
				this.uses[i]++;
		
		return object;
	}
}
