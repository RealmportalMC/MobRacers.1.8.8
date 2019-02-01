package me.winterguardian.core.portal;

import me.winterguardian.core.world.Region;

public interface Portal
{
	Region getRegion();
	PortalDestination getDestination();
}