package me.winterguardian.core.util;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;

public class TitleUtil
{
	public static void displayTitle(Player p, String jsonTitle, String jsonSubTitle, int fadeIn, int delay, int fadeOut)
	{
		if(p == null)
			return;
		try
		{
			Object times = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutTitle").getConstructor(int.class, int.class, int.class).newInstance(fadeIn, delay, fadeOut);
			
			Object subTitle = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutTitle").newInstance();
			
			Field subTitleAction = subTitle.getClass().getDeclaredField("a");
			if(!subTitleAction.isAccessible())
				subTitleAction.setAccessible(true);
			subTitleAction.set(subTitle, Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutTitle$EnumTitleAction").getField("SUBTITLE").get(null));

			Field subTitleContent = subTitle.getClass().getDeclaredField("b");
			if(!subTitleContent.isAccessible())
				subTitleContent.setAccessible(true);
			subTitleContent.set(subTitle, ReflectionUtil.getSerialized(jsonSubTitle));
			
			Object title = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutTitle").newInstance();
			
			Field titleAction = title.getClass().getDeclaredField("a");
			if(!titleAction.isAccessible())
				titleAction.setAccessible(true);
			
			titleAction.set(title, Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutTitle$EnumTitleAction").getField("TITLE").get(null));

			Field titleContent = title.getClass().getDeclaredField("b");
			if(!titleContent.isAccessible())
				titleContent.setAccessible(true);
			titleContent.set(title, ReflectionUtil.getSerialized(jsonTitle));
			
			ReflectionUtil.sendPacket(p, times);
			ReflectionUtil.sendPacket(p, subTitle);
			ReflectionUtil.sendPacket(p, title);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
