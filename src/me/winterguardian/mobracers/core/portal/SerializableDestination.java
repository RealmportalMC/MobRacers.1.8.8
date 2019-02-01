package me.winterguardian.core.portal;

public abstract class SerializableDestination implements PortalDestination
{
	public static SerializableDestination fromString(String string)
	{
		try
		{
			String[] args = string.split(":");
			String className = args[0];
			Class<?> clazz = Class.forName("me.winterguardian.core.portal." + className);
			return (SerializableDestination)clazz.getDeclaredMethod("fromString", String.class).invoke(null, args[1]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}