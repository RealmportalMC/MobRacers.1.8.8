package me.winterguardian.core.entity.custom;

import org.bukkit.Material;

/**
 *
 * Created by Alexander Winter on 2015-12-25.
 */
public interface BlockHolder
{
	void setBlock(Material block, int data);
	Material getType();
	int getData();
}
