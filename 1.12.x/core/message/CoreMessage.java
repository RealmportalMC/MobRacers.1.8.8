package me.winterguardian.core.message;

public class CoreMessage extends HardcodedMessage
{
	public static final CoreMessage USERDATA_USELESSRELOAD = new CoreMessage("§cVous ne pouvez pas recharger un userdata non chargé.");
	public static final CoreMessage USERDATA_RELOAD = new CoreMessage("§aL'userdata sélectionné a été rechargé.");
	public static final CoreMessage USERDATA_SETDATA = new CoreMessage("§aLes données spécifiées ont été modifiées et enregistrées.");
	public static final CoreMessage USERDATA_SETDATA_ERROR = new CoreMessage("§cLes données spécifiés n'ont pas pu être modifiées et enregistrées à cause d'une erreur interne.");
	
	public static final CoreMessage WAND_POSITIONSET = new CoreMessage("§aPosition # placé à §f(<x>, <y>, <z>)");  

	public static final HardcodedMessage PURCHASE_NOTENOUGHPOINTS = new HardcodedMessage("§cVous n'avez pas assez de points pour acheter ceci.");
	public static final HardcodedMessage PURCHASE_SUCCESS = new HardcodedMessage("§cPoints -<price> §f(Solde: <bal>)");


	private CoreMessage(String content)
	{
		super(content);
	}

	private CoreMessage(String content, boolean prefix)
	{
		super(content, prefix);
	}
	
	private CoreMessage(String content, boolean prefix, MessageType type)
	{
		super(content, prefix, type);
	}

	private CoreMessage(String content, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		super(content, prefix, hoverEvent, hoverEventContent, clickEvent, clickEventContent);
	}

	private CoreMessage(String title, String subTitle, int delay, int fadeIn, int fadeOut)
	{
		super(title, subTitle, delay, fadeIn, fadeOut);
	}

	@Override
	protected String getPrefix()
	{
		return "§3§lCore §f§l>§7 ";
	}
}
