package me.winterguardian.mobracers.stats;

import me.winterguardian.mobracers.CourseMessage;

public enum PurchaseType
{
	VEHICLE(CourseMessage.PURCHASE_VEHICLE, CourseMessage.PURCHASE_VEHICLE_DONE),
	MUSIC(CourseMessage.PURCHASE_MUSIC, CourseMessage.PURCHASE_MUSIC_DONE),
	;
	
	private String name;
	private String message;

	PurchaseType(CourseMessage name, CourseMessage message)
	{
		this.name = name.toString();
		this.message = message.toString();
	}
	
	public String getPurchaseMessage(String productName)
	{
		return message.replace("<product>", productName);
	}

	public String getName()
	{
		return name;
	}

}
