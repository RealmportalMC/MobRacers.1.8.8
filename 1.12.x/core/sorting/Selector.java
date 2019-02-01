package me.winterguardian.core.sorting;

public abstract class Selector<A>
{
	protected A[] elements;
	
	public Selector(A[] elements)
	{
		this.elements = elements.clone();
	}
	
	public abstract A next();
}
