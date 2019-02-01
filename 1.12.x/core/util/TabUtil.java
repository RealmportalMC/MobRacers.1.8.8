package me.winterguardian.core.util;

import java.lang.reflect.Field;

import me.winterguardian.core.json.JsonUtil;

import org.bukkit.entity.Player;

public class TabUtil
{
	@Deprecated
	public static final String DEFAULT_HEADER = JsonUtil.toJson(" §c✦ §9✦ §f§lSekai§3§lMC §e✦ §a✦ ");
	@Deprecated
	public static final String DEFAULT_FOOTER = JsonUtil.toJson("§9§nplay.sekaimc.net");
	
	public static void sendInfos(Player player, String jsonHeader, String jsonFooter)
	{
		if(player == null)
			return;
		
		try
		{
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutPlayerListHeaderFooter").newInstance();

			Field headerField = packet.getClass().getDeclaredField("a");
			if(!headerField.isAccessible())
				headerField.setAccessible(true);
			headerField.set(packet, ReflectionUtil.getSerialized(jsonHeader));

			Field footerField = packet.getClass().getDeclaredField("b");
			if(!footerField.isAccessible())
				footerField.setAccessible(true);
			footerField.set(packet, ReflectionUtil.getSerialized(jsonFooter));

			ReflectionUtil.sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void resetTab(Player p)
	{
		sendInfos(p, JsonUtil.toJson(" "), JsonUtil.toJson(" "));
	}
}
