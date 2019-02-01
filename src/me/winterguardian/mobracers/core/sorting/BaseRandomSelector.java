package me.winterguardian.core.sorting;

import java.util.Collection;

public class BaseRandomSelector<A> extends RandomSelector<A>
{

	public BaseRandomSelector(A[] elements)
	{
		super(elements);
	}

	@SuppressWarnings("unchecked")
	public BaseRandomSelector(Collection<A> elements)
	{
		super((A[]) elements.toArray());
	}
	
	@Override
	public A next()
	{
		return this.elements[random.nextInt(this.elements.length)];
	}

}
