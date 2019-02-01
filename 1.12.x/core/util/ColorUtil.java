package me.winterguardian.core.util;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public class ColorUtil
{
	public static Color getColor(String text)
	{
		if(text.equalsIgnoreCase("AQUA")) return Color.AQUA;
		if(text.equalsIgnoreCase("BLACK")) return Color.BLACK;
		if(text.equalsIgnoreCase("BLUE")) return Color.BLUE;
		if(text.equalsIgnoreCase("FUCHSIA")) return Color.FUCHSIA;
		if(text.equalsIgnoreCase("GRAY")) return Color.GRAY;
		if(text.equalsIgnoreCase("GREEN")) return Color.GREEN;
		if(text.equalsIgnoreCase("LIME")) return Color.LIME;
		if(text.equalsIgnoreCase("MAROON")) return Color.MAROON;
		if(text.equalsIgnoreCase("NAVY")) return Color.NAVY;
		if(text.equalsIgnoreCase("OLIVE")) return Color.OLIVE;
		if(text.equalsIgnoreCase("ORANGE")) return Color.ORANGE;
		if(text.equalsIgnoreCase("PURPLE")) return Color.PURPLE;
		if(text.equalsIgnoreCase("RED")) return Color.RED;
		if(text.equalsIgnoreCase("SILVER")) return Color.SILVER;
		if(text.equalsIgnoreCase("TEAL")) return Color.TEAL;
		if(text.equalsIgnoreCase("WHITE")) return Color.WHITE;
		if(text.equalsIgnoreCase("YELLOW")) return Color.YELLOW;
		
		return Color.WHITE;
	}
	
	public static String getColorCodeFromPotionEffectName(String type)
	{
		if(type.equalsIgnoreCase(PotionEffectType.SLOW.getName())
				|| type.equalsIgnoreCase(PotionEffectType.WEAKNESS.getName())
				|| type.equalsIgnoreCase(PotionEffectType.BLINDNESS.getName()) 
				|| type.equalsIgnoreCase(PotionEffectType.INCREASE_DAMAGE.getName()) 
				|| type.equalsIgnoreCase(PotionEffectType.CONFUSION.getName())
				|| type.equalsIgnoreCase(PotionEffectType.WITHER.getName())
				|| type.equalsIgnoreCase(PotionEffectType.HUNGER.getName())
				|| type.equalsIgnoreCase(PotionEffectType.POISON.getName())
				|| type.equalsIgnoreCase(PotionEffectType.SLOW_DIGGING.getName()))
			return "§c";
		return "§7";
	}
	
	public static String getJsonColor(char chatcolor)
	{
		switch(chatcolor)
		{
		case '0':
			return "black";
		case '1':
			return "dark_blue";
		case '2':
			return "dark_green";
		case '3':
			return "dark_aqua";
		case '4':
			return "dark_red";
		case '5':
			return "dark_purple";
		case '6':
			return "gold";
		case '7':
			return "gray";
			
		case '8':
			return "dark_gray";
		case '9':
			return "blue";
		case 'a':
			return "green";
		case 'b':
			return "aqua";
		case 'c':
			return "red";
		case 'd':
			return "light_purple";
		case 'e':
			return "yellow";
		case 'f':
			return "white";
		}
		return "white";
	}
}
