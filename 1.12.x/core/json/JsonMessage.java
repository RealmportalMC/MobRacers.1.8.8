package me.winterguardian.core.json;

public class JsonMessage
{
	private String message;
	private String color;
	private boolean bold;
	private boolean italic;
	private boolean underlined;
	private boolean strikethrough;
	private boolean ofuscated;
	private String hoverEvent;
	private String clickEvent;
	
	public JsonMessage(String message)
	{
		this.message = message;
		this.color = "white";
		this.bold = false;
		this.italic = false;
		this.underlined = false;
		this.strikethrough = false;
		this.ofuscated = false;
		
		this.insertEvents(null, null, null, null);
	}
	
	public JsonMessage(String message, String color)
	{
		this(message);
		this.color = color;
	}
	
	public JsonMessage(String message, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean ofuscated)
	{
		this(message, color);
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.ofuscated = ofuscated;
	}
	
	public JsonMessage(String message, String hoverEventType, String hoverEventValue, String clickEventType, String clickEventValue)
	{
		this(message);
		this.insertEvents(hoverEventType, hoverEventValue, clickEventType, clickEventValue);
	}
	
	public JsonMessage(String message, String color, String hoverEventType, String hoverEventValue, String clickEventType, String clickEventValue)
	{
		this(message, color);
		this.insertEvents(hoverEventType, hoverEventValue, clickEventType, clickEventValue);
	}
	
	public JsonMessage(String message, String color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean ofuscated, String hoverEventType, String hoverEventValue, String clickEventType, String clickEventValue)
	{
		this(message, color, bold, italic, underlined, strikethrough, ofuscated);
		
		this.insertEvents(hoverEventType, hoverEventValue, clickEventType, clickEventValue);
	}
	
	private void insertEvents(String hoverEventAction, String hoverEventValue, String clickEventAction, String clickEventValue)
	{
		if(hoverEventAction == null || hoverEventValue == null)
			this.hoverEvent = "{}";
		else
			this.hoverEvent = "{action:\"" + hoverEventAction + "\", value:" + hoverEventValue + "}";
		
		if(clickEventAction == null || clickEventValue == null)
			this.clickEvent = "{}";
		else
			this.clickEvent = "{action:\"" + clickEventAction + "\", value:" + clickEventValue + "}";
	}
	
	public String toString()
	{
		return "{text:\"" + this.message + "\", color:" + this.color + ", bold:" + this.bold + ", italic:" + this.italic + ", underlined:" + this.underlined + ", strikethrough:" + this.strikethrough + ", ofuscated:" + this.ofuscated + ", " + "hoverEvent:" + this.hoverEvent + ", clickEvent:" + this.clickEvent +"}";
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}
	
	public boolean isBold()
	{
		return bold;
	}
	
	public void setBold(boolean bold)
	{
		this.bold = bold;
	}
	
	public boolean isItalic()
	{
		return italic;
	}
	
	public void setItalic(boolean italic)
	{
		this.italic = italic;
	}
	
	public boolean isUnderlined()
	{
		return underlined;
	}
	
	public void setUnderlined(boolean underlined)
	{
		this.underlined = underlined;
	}
	
	public boolean isStrikethrough()
	{
		return strikethrough;
	}
	
	public void setStrikethrough(boolean strikethrough)
	{
		this.strikethrough = strikethrough;
	}
	
	public boolean isOfuscated()
	{
		return ofuscated;
	}
	
	public void setOfuscated(boolean ofuscated)
	{
		this.ofuscated = ofuscated;
	}
}