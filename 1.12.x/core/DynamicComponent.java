package me.winterguardian.core;

import me.winterguardian.core.Component;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 *
 * Created by 1541869 on 2015-11-24.
 */
public abstract class DynamicComponent implements Component
{
    private Set<Plugin> plugins;

    public DynamicComponent()
    {
        plugins = new HashSet<>();
    }

    @Override
    public void enable(Plugin plugin, Object... data)
    {
        this.plugins.add(plugin);

        if(plugins.size() > 1)
            return;

        register(getPlugin());
    }

    @Override
    public void disable(Plugin plugin)
    {
        if(!this.plugins.contains(plugin))
            return;

        this.plugins.remove(plugin);

        unregister(plugin);
        if(plugins.size() > 0)
        {
            try
            {
                register(getPlugin());
            }
            catch(Exception e) { }
        }

    }

    @Override
    public boolean isEnabled()
    {
        return plugins.size() != 0;
    }

    public Plugin getPlugin()
    {
        if(plugins.size() == 0)
            return null;

        return plugins.iterator().next();
    }

    protected abstract void register(Plugin plugin);
    protected abstract void unregister(Plugin plugin);
}
