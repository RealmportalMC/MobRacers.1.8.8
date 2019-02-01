package me.winterguardian.core.util;

public class MathUtil
{
	private MathUtil(){}
	
	public static double round(double value, int decimals)
	{
		return Math.round(value * Math.pow(10, decimals)) / Math.pow(10, decimals);
	}
	
	public static int getMin(int...values)
	{
		if(values == null || values.length == 0)
			throw new IllegalArgumentException();
		
		int min = values[0];
		
		for(int value : values)
			if(value < min)
				min = value;
		
		return min;
	}
	
	public static int getMax(int...values)
	{
		if(values == null || values.length == 0)
			throw new IllegalArgumentException();
		
		int max = values[0];
		
		for(int value : values)
			if(value > max)
				max = value;
		
		return max;
	}
}
