package me.winterguardian.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class TextUtil
{
	private TextUtil() { }
	
	public static String toString(int integer)
	{
		return toString(integer, " ");
	}
	
	public static String toString(int integer, String separator)
	{
		String product = "";
		for(int a = 0, i = ("" + integer).toCharArray().length - 1; i >= 0; a++, i--)
		{
			product = ("" + integer).toCharArray()[i] + product;
			if(a >= 2 && i != 0)
			{
				product = separator + product;
				a = -1;
			}
		}
		return product;
	}
	
	public static String toString(String[] stringEnum, String separator)
	{
		String result = "";
		
		int i;
		for(i = 0; i < stringEnum.length - 1; i++)
			result += stringEnum[i] + separator;
		
		return result += stringEnum[i];
	}

	public static String toString(double point)
	{
		return "" + point;
	}
	
	public static String[] getLetters(String message)
	{
		String[] letters =  new String[message.length() - countChar('§', message) * 2];
		
		for(int i = 0, c = 0; i < message.length(); i++, c++)
		{
			if(message.charAt(i) == '§' && i + 2 < message.length())
			{
				letters[c] = "§" + message.charAt(i + 1) + message.charAt(i + 2);
				i += 2;
			}
			else
				letters[c] = "" + message.charAt(i);
		}
		
		return letters;
	}
	
	public static int countChar(char c, String string)
	{
		int chars = 0;
		for(int i = 0; i < string.length(); i++)
			if(string.charAt(i) == c)
				chars++;
		return chars;
	}
	
	public static String generateInsultReplacement(String insult)
	{
		String s = "";
		char previous = ' ';
		char[] specials = new char[]{'!', '"', '*', '@', '#', '&'};
		for(int i = 0; i < insult.length(); i++)
		{
			char c = ' ';
			do c = specials[new Random().nextInt(specials.length)]; while(c == previous);
			s += (previous = c);
		}
		return s;
	}
	
	public static String generateWelcomeMessage(String playerName, String exception)
	{
		String[][] messageParts = new String[][]{{"Bienvenue "},
				{"", "à toi ", "parmi nous ", "sur SekaiMC ", "et bon jeu sur Sekai ", "", "sur ce serveur tout nouveau "},
				{playerName + " "},
				{"! ", ""},
				{":)", ":D", "=)", "=D", ""}};
		
		String welcomeMessage;
		do
		{
			welcomeMessage = "";
			for(int i = 0; i < messageParts.length; i++)
			{
				welcomeMessage += messageParts[i][new Random().nextInt(messageParts[i].length)];
			}
		}
		while(welcomeMessage == exception);
		return welcomeMessage;
	}

	public static String getRandomText(int caracters)
	{
		String message = "";
		char[] charArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		for(int j = 0; j < caracters; j++)
			message += charArray[new Random().nextInt(charArray.length)];
		return message;
	}
	
	public static String minecraftSubstring(String string, int beginindex)
	{
		return minecraftSubstring(string, beginindex, string.length());
	}
	
	public static String minecraftSubstring(String string, int beginindex, int endindex)
	{
		String sub = string.substring(beginindex, endindex);
		if(beginindex > 0 && string.charAt(beginindex - 1) == '§')
			sub = sub.substring(1);
		
		for(int i = beginindex; i >= 0; i--)
			if(string.charAt(i) == '§')
			{
				sub = "" + string.charAt(i) + string.charAt(i + 1) + sub;
				if(string.charAt(i + 1) >= '0'&& string.charAt(i + 1) <= '9'
				|| string.charAt(i + 1) >= 'a' && string.charAt(i + 1) <= 'f')
					break;
			}
		return sub;
	}

	public static int romanToArabicNumbers(String value) 
	{
		int result = 0;
		
		HashMap<Character, Integer> symbols = new LinkedHashMap<Character, Integer>();
		
		symbols.put('M', 1000);
		symbols.put('D', 500);
		symbols.put('C', 100);
		symbols.put('L', 50);
		symbols.put('X', 10);
		symbols.put('V', 5);
		symbols.put('I', 1);
		
		value = value.replaceAll(" ", "");
		
		for(int position = 0; position < value.length(); position++)
		{
			if(position + 1 >= value.length() || symbols.get(value.charAt(position)) >= symbols.get(value.charAt(position + 1)))
				result += symbols.get(value.charAt(position));
			
			else
				result -= symbols.get(value.charAt(position));
		}
		
		return result;
	}
	
	public static String arabicToRomanNumbers(int value) 
	{
		String result = "";
		
		HashMap<Integer, String> symbols = new LinkedHashMap<Integer, String>();
		
		symbols.put(1000, "M");
		symbols.put(999, "IM");
		symbols.put(995, "VM");
		symbols.put(990, "XM");
		symbols.put(950, "LM");
		symbols.put(900, "CM");
		symbols.put(500, "D");
		symbols.put(499, "ID");
		symbols.put(495, "VD");
		symbols.put(490, "XD");
		symbols.put(450, "LD");
		symbols.put(400, "CD");
		symbols.put(100, "C");
		symbols.put(99, "IC");
		symbols.put(95, "VC");
		symbols.put(90, "XC");
		symbols.put(50, "L");
		symbols.put(49, "IL");
		symbols.put(45, "VL");
		symbols.put(40, "XL");
		symbols.put(10, "X");
		symbols.put(9, "IX");
		symbols.put(5, "V");
		symbols.put(4, "IV");
		symbols.put(1, "I");
		
		for(Integer i : symbols.keySet())
			while(value >= i)
			{
				value -= i;
				result += symbols.get(i);
			}
		
		return result;
	}
	
	public static List<String> getStringsThatStartWith(String string, List<String> list)
	{
		List<String> result = new ArrayList<String>();
		for(String element : list)
			if(element.startsWith(string))
				result.add(element);
		return result;
	}
	
	public static String insertFormat(String minecraftColoredText, String format)
	{
		String result = minecraftColoredText;
		for(int i = 0; i < result.length(); i++)
			if(result.charAt(i) == '§' && i + 1 < result.length())
			{
				result = result.substring(0, i + 2) + format + result.substring(i + 2);
				i += format.length();
			}
		return result;
	}
	
	public static String[] getPluralPlaceHolders(int i)
	{
		return new String[]{"<plural>", (i > 1 ? "s" : ""),
				"<plural-s>", (i > 1 ? "s" : ""),
				"<plural-x>", (i > 1 ? "x" : ""),
				"<plural-es>", (i > 1 ? "es" : "")};
	}
	
	/**
	 * This is creating an HashMap of strings from the specified strings
	 * All string must have a "->" charsequence in it to delimit the key and the value
	 * You can escape this char sequence with \\ if you need
	 * 
	 * @param strings
	 * @return a new map with the strings
	 */
	public static HashMap<String, String> map(String... strings)
	{
		HashMap<String, String> map = new HashMap<>();
		
		for(int i = 0; i < strings.length; i++)
			if(strings[i].contains("->"))
				map.put(strings[i].split("->")[0], strings[i].split("->")[1]);
		
		return map;
	}
	
	public static String toString(Collection<String> strings, String separator)
	{
		return toString(strings.toArray(new String[strings.size()]), separator);
	}
	
	public static List<String> fromString(String string, String separator)
	{
		return Arrays.asList(string.split(separator));
	}
	
	public static <K, V> Entry<K, V> newEntry(K key, V value)
	{
		HashMap<K, V> map = new HashMap<>();
		
		map.put(key, value);
		
		return map.entrySet().iterator().next();
	}

	public static String removeColorCodes(String coloredMessage, char colorChar)
	{
		return coloredMessage.replaceAll(colorChar + "[a-fA-F0-9k-oK-OrR]", "");
	}

	public static String swap(String text, String swap1, String swap2)
	{
		return text.replace(swap1, "[inter-change]").replace(swap2, swap1).replace("[inter-change]", swap2);
	}

	//source http://www.dotnetperls.com/uppercase-first-letter-java
	public static String capitalize(String value)
	{
		char[] array = value.toCharArray();
		// Uppercase first letter.
		array[0] = Character.toUpperCase(array[0]);

		// Uppercase all letters that follow a whitespace character.
		for (int i = 1; i < array.length; i++) {
			if (Character.isWhitespace(array[i - 1])) {
				array[i] = Character.toUpperCase(array[i]);
			}
		}

		// Result.
		return new String(array);
	}
}
