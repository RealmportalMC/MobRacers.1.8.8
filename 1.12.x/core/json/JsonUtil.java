package me.winterguardian.core.json;

import java.lang.reflect.Field;
import java.util.ArrayList;

import me.winterguardian.core.util.ColorUtil;
import me.winterguardian.core.util.ReflectionUtil;

import org.bukkit.entity.Player;

public class JsonUtil
{
	private JsonUtil(){}
	
	public static void sendJsonMessage(Player player, String jsonMessage)
	{
		if(player == null || jsonMessage == null)
			return;
		
		try
		{
			Object packet = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".PacketPlayOutChat").newInstance();
			
			Field content = packet.getClass().getDeclaredField("a");
			if(!content.isAccessible())
				content.setAccessible(true);
			content.set(packet, ReflectionUtil.getSerialized(jsonMessage));

			Field data = packet.getClass().getDeclaredField("b");
			if(!data.isAccessible())
				data.setAccessible(true);
			data.set(packet, (byte)1);
					
			ReflectionUtil.sendPacket(player, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String toJson(String minecraftMessage)
	{
		return toJson(minecraftMessage, null, null, null, null);
	}
	
	public static String toJson(String minecraftMessage, String hoverEventType, String hoverEventValue, String clickEventType, String clickEventValue)
	{
		ArrayList<JsonMessage> jsonParts = new ArrayList<>();
		String result = "[";
		for(String part : ("§f" + minecraftMessage).split("§"))
		{
			if(part.length() != 0)
				if(part.charAt(0) == 'l' || part.charAt(0) == 'L')
					if(jsonParts.size() > 0)
					{
						if(jsonParts.get(jsonParts.size() - 1).getMessage().length() == 0)
						{
							jsonParts.get(jsonParts.size() - 1).setBold(true);
							jsonParts.get(jsonParts.size() - 1).setMessage(jsonParts.get(jsonParts.size() - 1).getMessage() + part.substring(1));
							continue;
						}
						
						jsonParts.add(new JsonMessage(part.substring(1), 
								jsonParts.get(jsonParts.size() - 1).getColor(), 
								true, 
								jsonParts.get(jsonParts.size() - 1).isItalic(), 
								jsonParts.get(jsonParts.size() - 1).isUnderlined(), 
								jsonParts.get(jsonParts.size() - 1).isStrikethrough(), 
								jsonParts.get(jsonParts.size() - 1).isOfuscated(),
								hoverEventType,
								hoverEventValue,
								clickEventType,
								clickEventValue));
					}
					else
						jsonParts.add(new JsonMessage(part.substring(1), "white", true, false, false, false, false));
			
				else if(part.charAt(0) == 'o' || part.charAt(0) == 'O')
					if(jsonParts.size() > 0)
					{
						if(jsonParts.get(jsonParts.size() - 1).getMessage().length() == 0)
						{
							jsonParts.get(jsonParts.size() - 1).setItalic(true);
							jsonParts.get(jsonParts.size() - 1).setMessage(jsonParts.get(jsonParts.size() - 1).getMessage() + part.substring(1));
							continue;
						}
						
						jsonParts.add(new JsonMessage(part.substring(1), 
								jsonParts.get(jsonParts.size() - 1).getColor(), 
								jsonParts.get(jsonParts.size() - 1).isBold(), 
								true, 
								jsonParts.get(jsonParts.size() - 1).isUnderlined(), 
								jsonParts.get(jsonParts.size() - 1).isStrikethrough(), 
								jsonParts.get(jsonParts.size() - 1).isOfuscated(),
								hoverEventType,
								hoverEventValue,
								clickEventType,
								clickEventValue));
					}
					else
						jsonParts.add(new JsonMessage(part.substring(1), "white", false, true, false, false, false));
			
				else if(part.charAt(0) == 'n' || part.charAt(0) == 'N')
					if(jsonParts.size() > 0)
					{
						if(jsonParts.get(jsonParts.size() - 1).getMessage().length() == 0)
						{
							jsonParts.get(jsonParts.size() - 1).setUnderlined(true);
							jsonParts.get(jsonParts.size() - 1).setMessage(jsonParts.get(jsonParts.size() - 1).getMessage() + part.substring(1));
							continue;
						}
						
						jsonParts.add(new JsonMessage(part.substring(1), 
								jsonParts.get(jsonParts.size() - 1).getColor(), 
								jsonParts.get(jsonParts.size() - 1).isBold(), 
								jsonParts.get(jsonParts.size() - 1).isItalic(), 
								true, 
								jsonParts.get(jsonParts.size() - 1).isStrikethrough(), 
								jsonParts.get(jsonParts.size() - 1).isOfuscated(),
								hoverEventType,
								hoverEventValue,
								clickEventType,
								clickEventValue));
					}
					else
						jsonParts.add(new JsonMessage(part.substring(1), "white", false, false, true, false, false));
			
				else if(part.charAt(0) == 'm' || part.charAt(0) == 'M')
					if(jsonParts.size() > 0)
					{
						if(jsonParts.get(jsonParts.size() - 1).getMessage().length() == 0)
						{
							jsonParts.get(jsonParts.size() - 1).setStrikethrough(true);
							jsonParts.get(jsonParts.size() - 1).setMessage(jsonParts.get(jsonParts.size() - 1).getMessage() + part.substring(1));
							continue;
						}
						
						jsonParts.add(new JsonMessage(part.substring(1), 
								jsonParts.get(jsonParts.size() - 1).getColor(), 
								jsonParts.get(jsonParts.size() - 1).isBold(), 
								jsonParts.get(jsonParts.size() - 1).isItalic(), 
								jsonParts.get(jsonParts.size() - 1).isUnderlined(), 
								true, 
								jsonParts.get(jsonParts.size() - 1).isOfuscated(),
								hoverEventType,
								hoverEventValue,
								clickEventType,
								clickEventValue));
					}
					else
						jsonParts.add(new JsonMessage(part.substring(1), "white", false, false, false, true, false));
			
				else if(part.charAt(0) == 'k' || part.charAt(0) == 'K')
					if(jsonParts.size() > 0)
					{
						if(jsonParts.get(jsonParts.size() - 1).getMessage().length() == 0)
						{
							jsonParts.get(jsonParts.size() - 1).setOfuscated(true);
							jsonParts.get(jsonParts.size() - 1).setMessage(jsonParts.get(jsonParts.size() - 1).getMessage() + part.substring(1));
							continue;
						}
						
						jsonParts.add(new JsonMessage(part.substring(1), 
								jsonParts.get(jsonParts.size() - 1).getColor(), 
								jsonParts.get(jsonParts.size() - 1).isBold(), 
								jsonParts.get(jsonParts.size() - 1).isItalic(), 
								jsonParts.get(jsonParts.size() - 1).isUnderlined(), 
								jsonParts.get(jsonParts.size() - 1).isStrikethrough(), 
								true,
								hoverEventType,
								hoverEventValue,
								clickEventType,
								clickEventValue));
					}
					else
						jsonParts.add(new JsonMessage(part.substring(1), "white", false, false, false, false, true));
			
				else if(part.charAt(0) == 'r' || part.charAt(0) == 'R')
				{
					if(jsonParts.size() > 0 && jsonParts.get(jsonParts.size() - 1).getMessage().length() == 0)
					{
						jsonParts.get(jsonParts.size() - 1).setColor("white");
						jsonParts.get(jsonParts.size() - 1).setBold(false);
						jsonParts.get(jsonParts.size() - 1).setItalic(false);
						jsonParts.get(jsonParts.size() - 1).setUnderlined(false);
						jsonParts.get(jsonParts.size() - 1).setStrikethrough(false);
						jsonParts.get(jsonParts.size() - 1).setOfuscated(false);
						jsonParts.get(jsonParts.size() - 1).setMessage(jsonParts.get(jsonParts.size() - 1).getMessage() + part.substring(1));
						continue;
					}
					
					jsonParts.add(new JsonMessage(part.substring(1),
							hoverEventType,
							hoverEventValue,
							clickEventType,
							clickEventValue));
				}
				else
					jsonParts.add(new JsonMessage(part.substring(1), ColorUtil.getJsonColor(part.charAt(0)),
							hoverEventType,
							hoverEventValue,
							clickEventType,
							clickEventValue));
		}
		
		for(JsonMessage jsonM : jsonParts)
			if(jsonM.getMessage().length() != 0)
				result += jsonM.toString() + ",";
				
		
		return result.substring(0, result.length() - 1) + "]";
	}
}
