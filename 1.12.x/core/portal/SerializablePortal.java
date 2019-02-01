package me.winterguardian.core.portal;

import me.winterguardian.core.world.Region;
import me.winterguardian.core.world.SerializableRegion;

public class SerializablePortal extends AutoRegistrationPortal
{
	private SerializableRegion region;
	private SerializableDestination dest;
	
	public SerializablePortal(SerializableRegion region, SerializableDestination dest)
	{
		this.region = region;
		this.dest = dest;
	}
	
	@Override
	public Region getRegion()
	{
		return region;
	}
	
	@Override
	public PortalDestination getDestination()
	{
		return dest;
	}
	
	@Override
	public String toString()
	{
		return region.toString() + "->" + dest.toString();
	}
	
	public static SerializablePortal fromString(String string)
	{
		return new SerializablePortal(
			SerializableRegion.fromString(string.split("->")[0]),
			SerializableDestination.fromString(string.split("->")[1]));
	}
}