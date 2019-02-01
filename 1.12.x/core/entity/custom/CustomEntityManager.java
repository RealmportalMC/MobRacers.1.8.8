package me.winterguardian.core.entity.custom;

import me.winterguardian.core.DynamicComponent;
import me.winterguardian.core.entity.custom.CustomEntityType;
import org.bukkit.plugin.Plugin;

/**
 *
 * Created by 1541869 on 2015-12-03.
 */
public class CustomEntityManager extends DynamicComponent
{
	@Override
	protected void register(Plugin plugin)
	{
		CustomEntityType.registerEntities();
	}

	@Override
	protected void unregister(Plugin plugin)
	{
		if(!this.isEnabled())
			CustomEntityType.unregisterEntities();
	}
}
