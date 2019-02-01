package me.winterguardian.core.sorting;

import java.util.Collection;

public class OrderedSelector<A> extends Selector<A>
{
	private int index;

	public OrderedSelector(A[] elements)
	{
		super(elements);
		this.index = 0;
	}

	@SuppressWarnings("unchecked")
	public OrderedSelector(Collection<A> elements)
	{
		super((A[]) elements.toArray());
		this.index = 0;
	}
	
	@Override
	public A next()
	{
		if(this.index == this.elements.length)
			this.index = 0;
		
		return this.elements[this.index++];
	}

}
