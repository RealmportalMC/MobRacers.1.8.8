package me.winterguardian.core.util;

import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.Selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by Alexander Winter on 2015-12-15.
 */
public class CollectionsUtil
{
	private CollectionsUtil(){}

	public static <T> List<T> mixList(List<T> toMix)
	{
		Object[] array = new Object[toMix.size()];
		Integer[] positions = new Integer[toMix.size()];

		for(int i = 0; i < toMix.size(); i++)
			positions[i] = i;

		AntiRecursiveRandomSelector<Integer> select = new AntiRecursiveRandomSelector<>(positions);

		for(T object : toMix)
			array[select.next()] = object;

		return Arrays.asList((T[])array);
	}
}
