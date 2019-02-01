package me.winterguardian.core;

import org.bukkit.plugin.Plugin;

/**
 * Created by Alexander Winter on 2015-11-17.
 */
public interface Component
{
	void enable(Plugin plugin, Object... args);
	void disable(Plugin plugin);
	boolean isEnabled();
}
