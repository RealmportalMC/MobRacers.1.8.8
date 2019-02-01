package me.winterguardian.mobracers;

import me.winterguardian.core.message.LangMessage;
import me.winterguardian.core.util.TextUtil;

import org.bukkit.configuration.file.YamlConfiguration;


public class CourseMessage extends LangMessage
{
	public static final CourseMessage
	SEPARATOR_CANTSELECTVEHICLE = new CourseMessage("separator-cantselectvehicle", false),
	SEPARATOR_SELECTVEHICLE = new CourseMessage("separator-selectvehicle", false),
	SEPARATOR_ACHIEVEMENT = new CourseMessage("separator-achievement", false),
	SEPARATOR_PURCHASE = new CourseMessage("separator-purchase", false),
	
	JOIN = new CourseMessage("join", true),
	JOIN_ALREADYINGAME = new CourseMessage("join-alreadyingame", true),
	JOIN_GAMENOTREADY = new CourseMessage("join-gamenotready", true),
	JOIN_GAMECLOSED = new CourseMessage("join-gameclosed", true),
	JOIN_MAXPLAYERS = new CourseMessage("join-maxplayers", true),
	JOIN_AUTOJOINKICK = new CourseMessage("join-gamenotready", false, MessageType.KICK),
	JOIN_INVITATIONMESSAGE = new CourseMessage("join-someonehasjoin", true, null, null, "run_command", "\"/mobracers join\""),
	
	LEAVE_NOTINGAME = new CourseMessage("leave-notingame", true),
	LEAVE = new CourseMessage("leave", true),
	LEAVE_CANTLEAVEINAUTOJOIN = new CourseMessage("leave-cantleaveautojoin", true),
	
	STANDBY_STATUS = new CourseMessage("standby-status", false),
	STANDBY_NOTENOUGHTPLAYERS = new CourseMessage("standby-notenoughtplayers", true),
	STANDBY_NOTENOUGHTPLAYERSTOCONTINUE = new CourseMessage("standby-notenoughtplayerstocontinue", true),
	
	STANDBY_BOARD_HEADER = new CourseMessage("standby-board-header", false),
	STANDBY_BOARD = new CourseMessage("standby-board", false),
	
	STANDBY_TABHEADER = new CourseMessage("standby-tabheader", false),
	STANDBY_TABFOOTER = new CourseMessage("standby-tabfooter", false),
	
	ARENASELECT_STARTORJOIN = new CourseMessage("arenaselect-startorjoin", true),
	ARENASELECT_INVALIDARENA = new CourseMessage("arenaselect-invalidarena", true),
	ARENASELECT_FIRSTVOTE = new CourseMessage("arenaselect-firstvote", true),
	ARENASELECT_SAMEARENA = new CourseMessage("arenaselect-samearena", true),
	ARENASELECT_CHANGEVOTE = new CourseMessage("arenaselect-changevote", true),
	ARENASELECT_STATUS = new CourseMessage("arenaselect-status", false),
	ARENASELECT_TIMER = new CourseMessage("arenaselect-timer", false, MessageType.ACTIONBAR),
	ARENASELECT_ARENASELECTED = new CourseMessage("arenaselect-arenaselected", true),
	
	ARENASELECT_TABHEADER = new CourseMessage("arenaselect-tabheader", false),
	ARENASELECT_TABFOOTER_NOVOTE = new CourseMessage("arenaselect-tabfooter-novote", false),
	ARENASELECT_TABFOOTER_VOTED = new CourseMessage("arenaselect-tabfooter-voted", false),
	
	ARENASELECT_BOARD_HEADER = new CourseMessage("arenaselect-board-header", false),
	ARENASELECT_BOARD_ARENA = new CourseMessage("arenaselect-board-arena", false),
	ARENASELECT_BOARD_ARENAAUTHOR = new CourseMessage("arenaselect-board-arenaauthor", false),
	ARENASELECT_BOARD_OTHERVOTES = new CourseMessage("arenaselect-board-othervotes", false),

	ARENASELECT_GUI_INV = new CourseMessage("arenaselect-gui-inv", false),

	VEHICLESELECT_TABHEADER = new CourseMessage("vehicleselect-tabheader", false),
	VEHICLESELECT_TABFOOTER_NOVEHICLE = new CourseMessage("vehicleselect-tabfooter-novehicle", false),
	VEHICLESELECT_TABFOOTER_SELECTED = new CourseMessage("vehicleselect-tabfooter-selected", false),
	
	VEHICLESELECT_BOARD_HEADER = new CourseMessage("vehicleselect-board-header", false),
	VEHICLESELECT_BOARD_ARENA = new CourseMessage("vehicleselect-board-arena", false),
	VEHICLESELECT_BOARD_VEHICLES = new CourseMessage("vehicleselect-board-vehicles", false),
	VEHICLESELECT_BOARD_SELECTEDVEHICLE = new CourseMessage("vehicleselect-board-selectedvehicle", false),
	VEHICLESELECT_BOARD_RECORDS = new CourseMessage("vehicleselect-board-records", false),
	VEHICLESELECT_BOARD_POINTS = new CourseMessage("vehicleselect-board-points", false),
		
	VEHICLESELECT_CANTSELECT_VIP = new CourseMessage("vehicleselect-cantselect-vip", false),
	VEHICLESELECT_CANTSELECT_ACHIEVEMENT = new CourseMessage("vehicleselect-cantselect-achievement", false),
	VEHICLESELECT_CANTSELECT_PURCHASE = new CourseMessage("vehicleselect-cantselect-purchase", false),
	VEHICLESELECT_CANTSELECT_UNKNOW = new CourseMessage("vehicleselect-cantselect-unknow", false),
	
	VEHICLESELECT_CLICKTOBUY = new CourseMessage("vehicleselect-clicktobuy", false),
	VEHICLESELECT_JOIN = new CourseMessage("vehicleselect-join", true),
	VEHICLESELECT_JUSTSELECTED = new CourseMessage("vehicleselect-justselected", false),
	VEHICLESELECT_STATUS = new CourseMessage("vehicleselect-status", false),
	VEHICLESELECT_TIMER = new CourseMessage("vehicleselect-timer", false, MessageType.ACTIONBAR),
	
	VEHICLESELECT_SHIFTTIP = new CourseMessage("vehicleselect-shifttip", false),
	
	VEHICLESELECT_BEGININGSOON = new CourseMessage("vehicleselect-beginingsoon", false),

	VEHICLESELECT_GUI_INV = new CourseMessage("vehicleselect-gui-inv", false),

	GAME_SPECTATORJOIN = new CourseMessage("game-spectatorjoin", true),
	GAME_FINISHED_FIRST = new CourseMessage("game-finished-first", true),
	GAME_FINISHED_NORMAL = new CourseMessage("game-finished-normal", true),
	GAME_STATUS = new CourseMessage("game-status", false),
	GAME_END = new CourseMessage("game-end", true),
	GAME_FORCEFNISH = new CourseMessage("game-forcefnish", true),
	GAME_START_GOODLUCK = new CourseMessage("game-start-goodluck", true),
	GAME_PASSMAINLINE_NORMAL = new CourseMessage("game-passmainline-normal", true),
	GAME_PASSMAINLINE_FINAL = new CourseMessage("game-passmainline-final", true),
	GAME_FINISHTIME_NORMAL = new CourseMessage("game-finishtime-normal", true),
	GAME_FINISHTIME_NEWRECORD = new CourseMessage("game-finishtime-newrecord", true),
	GAME_BESTTIME = new CourseMessage("game-besttime", false),
	GAME_TITLE_LINE1 = new CourseMessage("game-title-line1", false),
	GAME_TITLE_LINE2 = new CourseMessage("game-title-line2", false),
	GAME_GO = new CourseMessage("game-go", false),
	GAME_RELOCATE = new CourseMessage("game-relocate", true),
	
	GAME_FINISH_ACTIONBAR = new CourseMessage("game-finish-actionbar", false, MessageType.ACTIONBAR),
	GAME_WIN = new CourseMessage("game-win", true),
	
	GAME_BOARD_HEADER = new CourseMessage("game-board-header", false),
	GAME_BOARD_POSITION = new CourseMessage("game-board-position", false),
	GAME_BOARD_SPECTATOR = new CourseMessage("game-board-spectator", false),
	GAME_BOARD_ITEM = new CourseMessage("game-board-item", false),
	GAME_BOARD_RANKING = new CourseMessage("game-board-ranking", false),
	GAME_BOARD_LAPS = new CourseMessage("game-board-laps", false),
	
	GAME_TABHEADER = new CourseMessage("game-tabheader", false),
	GAME_TABFOOTER = new CourseMessage("game-tabfooter", false),
	
	COMMAND_INVALID_PERMISSION = new CourseMessage("command-invalid-permission", true),
	COMMAND_INVALID_SENDER = new CourseMessage("command-invalid-sender", true),
	COMMAND_INVALID_ARGUMENT = new CourseMessage("command-invalid-argument", true),
	COMMAND_INVALID_SELECTION = new CourseMessage("command-invalid-selection", true),
	COMMAND_INVALID_PLAYER = new CourseMessage("command-invalid-player", true),
	COMMAND_USAGE = new CourseMessage("command-usage", false),
	
	COMMAND_BLOCKED_IN_GAME = new CourseMessage("command-blocked-in-game", true),
	
	COMMAND_OPEN = new CourseMessage("command-open", true),
	COMMAND_CLOSE = new CourseMessage("command-close", true),
	
	COMMAND_ACHIEVEMENT_LIST_TODO = new CourseMessage("command-achievement-list-todo", true),
	COMMAND_ACHIEVEMENT_LIST_OTHERS = new CourseMessage("command-achievement-list-others", true),
	COMMAND_ACHIEVEMENT_LIST_DONE = new CourseMessage("command-achievement-list-done", true),
	COMMAND_ACHIEVEMENT_NOTHINGDONE = new CourseMessage("command-achievement-nothingdone", true),
	COMMAND_ACHIEVEMENT_ALLDONE = new CourseMessage("command-achievement-alldone", true),
	
	COMMAND_ARENA_HELP_MENU = new CourseMessage("command-arena-help-menu", true),
	COMMAND_ARENA_HELP_NEXT = new CourseMessage("command-arena-help-next", true),
	COMMAND_ARENA_HELP_NONEXT = new CourseMessage("command-arena-help-nonext", true),
	
	COMMAND_ARENA_HELP_LIST_INFO = new CourseMessage("command-arena-help-list-info", false),
	COMMAND_ARENA_HELP_CREATE_INFO = new CourseMessage("command-arena-help-create-info", false),
	COMMAND_ARENA_HELP_DELETE_INFO = new CourseMessage("command-arena-help-delete-info", false),
	COMMAND_ARENA_HELP_INFO_INFO = new CourseMessage("command-arena-help-info-info", false),
	
	COMMAND_ARENA_HELP_LIST_COMMAND = new CourseMessage("command-arena-help-list-command", false),
	COMMAND_ARENA_HELP_CREATE_COMMAND = new CourseMessage("command-arena-help-create-command", false),
	COMMAND_ARENA_HELP_DELETE_COMMAND = new CourseMessage("command-arena-help-delete-command", false),
	COMMAND_ARENA_HELP_INFO_COMMAND = new CourseMessage("command-arena-help-info-command", false),
	
	COMMAND_ARENA_HELP_SPAWN_ADD_INFO = new CourseMessage("command-arena-help-spawn-add-info", false),
	COMMAND_ARENA_HELP_SPAWN_REMOVE_INFO = new CourseMessage("command-arena-help-spawn-remove-info", false),
	COMMAND_ARENA_HELP_SPAWN_RESET_INFO = new CourseMessage("command-arena-help-spawn-reset-info", false),
	COMMAND_ARENA_HELP_SPAWN_SHOW_INFO = new CourseMessage("command-arena-help-spawn-show-info", false),
	
	COMMAND_ARENA_HELP_SPAWN_ADD_COMMAND = new CourseMessage("command-arena-help-spawn-add-command", false),
	COMMAND_ARENA_HELP_SPAWN_REMOVE_COMMAND = new CourseMessage("command-arena-help-spawn-remove-command", false),
	COMMAND_ARENA_HELP_SPAWN_RESET_COMMAND = new CourseMessage("command-arena-help-spawn-reset-command", false),
	COMMAND_ARENA_HELP_SPAWN_SHOW_COMMAND = new CourseMessage("command-arena-help-spawn-show-command", false),
	
	COMMAND_ARENA_HELP_ITEM_ADD_INFO = new CourseMessage("command-arena-help-item-add-info", false),
	COMMAND_ARENA_HELP_ITEM_REMOVE_INFO = new CourseMessage("command-arena-help-item-remove-info", false),
	COMMAND_ARENA_HELP_ITEM_RESET_INFO = new CourseMessage("command-arena-help-item-reset-info", false),
	COMMAND_ARENA_HELP_ITEM_SHOW_INFO = new CourseMessage("command-arena-help-item-show-info", false),
	
	COMMAND_ARENA_HELP_ITEM_ADD_COMMAND = new CourseMessage("command-arena-help-item-add-command", false),
	COMMAND_ARENA_HELP_ITEM_REMOVE_COMMAND = new CourseMessage("command-arena-help-item-remove-command", false),
	COMMAND_ARENA_HELP_ITEM_RESET_COMMAND = new CourseMessage("command-arena-help-item-reset-command", false),
	COMMAND_ARENA_HELP_ITEM_SHOW_COMMAND = new CourseMessage("command-arena-help-item-show-command", false),
	
	COMMAND_ARENA_HELP_SPECTATOR_ADD_INFO = new CourseMessage("command-arena-help-spectator-add-info", false),
	COMMAND_ARENA_HELP_SPECTATOR_REMOVE_INFO = new CourseMessage("command-arena-help-spectator-remove-info", false),
	COMMAND_ARENA_HELP_SPECTATOR_RESET_INFO = new CourseMessage("command-arena-help-spectator-reset-info", false),
	COMMAND_ARENA_HELP_SPECTATOR_SHOW_INFO = new CourseMessage("command-arena-help-spectator-show-info", false),
	
	COMMAND_ARENA_HELP_SPECTATOR_ADD_COMMAND = new CourseMessage("command-arena-help-spectator-add-command", false),
	COMMAND_ARENA_HELP_SPECTATOR_REMOVE_COMMAND = new CourseMessage("command-arena-help-spectator-remove-command", false),
	COMMAND_ARENA_HELP_SPECTATOR_RESET_COMMAND = new CourseMessage("command-arena-help-spectator-reset-command", false),
	COMMAND_ARENA_HELP_SPECTATOR_SHOW_COMMAND = new CourseMessage("command-arena-help-spectator-show-command", false),
	
	COMMAND_ARENA_HELP_JUKEBOX_ADD_INFO = new CourseMessage("command-arena-help-jukebox-add-info", false),
	COMMAND_ARENA_HELP_JUKEBOX_REMOVE_INFO = new CourseMessage("command-arena-help-jukebox-remove-info", false),
	COMMAND_ARENA_HELP_JUKEBOX_RESET_INFO = new CourseMessage("command-arena-help-jukebox-reset-info", false),
	COMMAND_ARENA_HELP_JUKEBOX_PLAY_INFO = new CourseMessage("command-arena-help-jukebox-play-info", false),
	
	COMMAND_ARENA_HELP_JUKEBOX_ADD_COMMAND = new CourseMessage("command-arena-help-jukebox-add-command", false),
	COMMAND_ARENA_HELP_JUKEBOX_REMOVE_COMMAND = new CourseMessage("command-arena-help-jukebox-remove-command", false),
	COMMAND_ARENA_HELP_JUKEBOX_RESET_COMMAND = new CourseMessage("command-arena-help-jukebox-reset-command", false),
	COMMAND_ARENA_HELP_JUKEBOX_PLAY_COMMAND = new CourseMessage("command-arena-help-jukebox-play-command", false),
	
	COMMAND_ARENA_HELP_LINE_ADD_INFO = new CourseMessage("command-arena-help-line-add-info", false),
	COMMAND_ARENA_HELP_LINE_REMOVE_INFO = new CourseMessage("command-arena-help-line-remove-info", false),
	COMMAND_ARENA_HELP_LINE_RESET_INFO = new CourseMessage("command-arena-help-line-reset-info", false),
	COMMAND_ARENA_HELP_LINE_TELEPORT_INFO = new CourseMessage("command-arena-help-line-teleport-info", false),
	COMMAND_ARENA_HELP_LINE_TEST_INFO = new CourseMessage("command-arena-help-line-test-info", false),
	
	COMMAND_ARENA_HELP_LINE_ADD_COMMAND = new CourseMessage("command-arena-help-line-add-command", false),
	COMMAND_ARENA_HELP_LINE_REMOVE_COMMAND = new CourseMessage("command-arena-help-line-remove-command", false),
	COMMAND_ARENA_HELP_LINE_RESET_COMMAND = new CourseMessage("command-arena-help-line-reset-command", false),
	COMMAND_ARENA_HELP_LINE_TELEPORT_COMMAND = new CourseMessage("command-arena-help-line-teleport-command", false),
	COMMAND_ARENA_HELP_LINE_TEST_COMMAND = new CourseMessage("command-arena-help-line-test-command", false),
	
	COMMAND_ARENA_HELP_REGION_INFO = new CourseMessage("command-arena-help-region-info", false),
	COMMAND_ARENA_HELP_REGION_COMMAND = new CourseMessage("command-arena-help-region-command", false),
	COMMAND_ARENA_HELP_WEATHER_INFO = new CourseMessage("command-arena-help-weather-info", false),
	COMMAND_ARENA_HELP_WEATHER_COMMAND = new CourseMessage("command-arena-help-weather-command", false),
	COMMAND_ARENA_HELP_AUTHOR_INFO = new CourseMessage("command-arena-help-author-info", false),
	COMMAND_ARENA_HELP_AUTHOR_COMMAND = new CourseMessage("command-arena-help-author-command", false),
	
	
	COMMAND_ARENA_HELP_LAPS_INFO = new CourseMessage("command-arena-help-laps-info", false),
	COMMAND_ARENA_HELP_TIMELIMIT_INFO = new CourseMessage("command-arena-help-timelimit-info", false),
	COMMAND_ARENA_HELP_ITEMREGENDELAY_INFO = new CourseMessage("command-arena-help-itemregendelay-info", false),
	COMMAND_ARENA_HELP_SPEEDMODIFIER_INFO = new CourseMessage("command-arena-help-speedmodifier-info", false),

	COMMAND_ARENA_HELP_LAPS_COMMAND = new CourseMessage("command-arena-help-laps-command", false),
	COMMAND_ARENA_HELP_TIMELIMIT_COMMAND = new CourseMessage("command-arena-help-timelimit-command", false),
	COMMAND_ARENA_HELP_ITEMREGENDELAY_COMMAND = new CourseMessage("command-arena-help-itemregendelay-command", false),
	COMMAND_ARENA_HELP_SPEEDMODIFIER_COMMAND = new CourseMessage("command-arena-help-speedmodifier-command", false),
	
	COMMAND_ARENA_HELP_DEATHZONE_ADD_INFO = new CourseMessage("command-arena-help-deathzone-add-info", false),
	COMMAND_ARENA_HELP_DEATHZONE_ADD_COMMAND = new CourseMessage("command-arena-help-deathzone-add-command", false),
	COMMAND_ARENA_HELP_DEATHZONE_REMOVE_INFO = new CourseMessage("command-arena-help-deathzone-remove-info", false),
	COMMAND_ARENA_HELP_DEATHZONE_REMOVE_COMMAND = new CourseMessage("command-arena-help-deathzone-remove-command", false),
	COMMAND_ARENA_HELP_DEATHZONE_RESET_INFO = new CourseMessage("command-arena-help-deathzone-reset-info", false),
	COMMAND_ARENA_HELP_DEATHZONE_RESET_COMMAND = new CourseMessage("command-arena-help-deathzone-reset-command", false),
	COMMAND_ARENA_HELP_DEATHZONE_TEST_INFO = new CourseMessage("command-arena-help-deathzone-test-info", false),
	COMMAND_ARENA_HELP_DEATHZONE_TEST_COMMAND = new CourseMessage("command-arena-help-deathzone-test-command", false),
	
	COMMAND_ARENA_INVALIDARENA = new CourseMessage("command-arena-invalidarena", false),
	
	COMMAND_ARENA_LIST_LIST = new CourseMessage("command-arena-list-list", true),
	COMMAND_ARENA_LIST_NOARENA = new CourseMessage("command-arena-list-noarena", true),
	
	COMMAND_ARENA_INFO_HEAD = new CourseMessage("command-arena-info-head", true),
	COMMAND_ARENA_INFO_BREAKLINES = new CourseMessage("command-arena-info-breaklines", false),
	COMMAND_ARENA_INFO_WEATHER = new CourseMessage("command-arena-info-weather", false),
	COMMAND_ARENA_INFO_LAPSANDTIMELIMIT = new CourseMessage("command-arena-info-lapsandtimelimit", false),
	COMMAND_ARENA_INFO_SPAWNSANDITEMS = new CourseMessage("command-arena-info-spawnsanditems", false),
	COMMAND_ARENA_INFO_REGION = new CourseMessage("command-arena-info-region", false),
	COMMAND_ARENA_INFO_JUKEBOXES = new CourseMessage("command-arena-info-jukeboxes", false),
	COMMAND_ARENA_INFO_SPECTATORS = new CourseMessage("command-arena-info-spectators", false),
	
	COMMAND_ARENA_INFO_ITEMREGENDELAY = new CourseMessage("command-arena-info-itemregendelay", false),
	COMMAND_ARENA_INFO_DEATHZONES = new CourseMessage("command-arena-info-deathzones", false),
	
	COMMAND_ARENA_INFO_ROADSPEED_NORMAL = new CourseMessage("command-arena-info-roadspeed-normal", false),
	COMMAND_ARENA_INFO_ROADSPEED_EMPTY = new CourseMessage("command-arena-info-roadspeed-empty", false),
	
	COMMAND_ARENA_INFO_READY = new CourseMessage("command-arena-info-ready", true),
	COMMAND_ARENA_INFO_NOTREADY = new CourseMessage("command-arena-info-notready", true),
	
	COMMAND_ARENA_INFO_NOTREADY_NOSPAWNS = new CourseMessage("command-arena-info-notready-nospawns", false),
	COMMAND_ARENA_INFO_NOTREADY_NOSPECTATORS = new CourseMessage("command-arena-info-notready-nospectators", false),
	COMMAND_ARENA_INFO_NOTREADY_NOLINES = new CourseMessage("command-arena-info-notready-nolines", false),
	COMMAND_ARENA_INFO_NOTREADY_NOREGION = new CourseMessage("command-arena-info-notready-noregion", false),
	COMMAND_ARENA_INFO_NOTREADY_BADREGION = new CourseMessage("command-arena-info-notready-badregion", false),
	
	COMMAND_ARENA_NEW_ALREADYCREATED = new CourseMessage("command-arena-new-alreadycreated", true),
	COMMAND_ARENA_NEW_DONE = new CourseMessage("command-arena-new-done", true),
	
	COMMAND_ARENA_WEATHER_DONE = new CourseMessage("command-arena-weather-done", true),
	COMMAND_ARENA_RACESETTINGS_DONE = new CourseMessage("command-arena-racesettings-done", true),
	COMMAND_ARENA_AUTHOR_DONE = new CourseMessage("command-arena-author-done", true),
	
	COMMAND_ARENA_BREAKLINE_ADD = new CourseMessage("command-arena-breakline-add", true),
	COMMAND_ARENA_BREAKLINE_REMOVE = new CourseMessage("command-arena-breakline-remove", true),
	COMMAND_ARENA_BREAKLINE_TELEPORT = new CourseMessage("command-arena-breakline-teleport", true),
	COMMAND_ARENA_BREAKLINE_RESET = new CourseMessage("command-arena-breakline-reset", true),
	
	COMMAND_ARENA_ADDSPAWN = new CourseMessage("command-arena-addspawn", true),
	COMMAND_ARENA_REMOVESPAWN = new CourseMessage("command-arena-removespawn", true),
	COMMAND_ARENA_REMOVESPAWN_FAIL = new CourseMessage("command-arena-removespawn-fail", true),
	COMMAND_ARENA_RESETSPAWNS = new CourseMessage("command-arena-resetspawns", true),
	COMMAND_ARENA_SHOWSPAWNS = new CourseMessage("command-arena-showspawns", true),
	
	COMMAND_ARENA_ADDSPECTATOR = new CourseMessage("command-arena-addspectator", true),
	COMMAND_ARENA_REMOVESPECTATOR = new CourseMessage("command-arena-removespectator", true),
	COMMAND_ARENA_REMOVESPECTATOR_FAIL = new CourseMessage("command-arena-removespectator-fail", true),
	COMMAND_ARENA_RESETSPECTATORS = new CourseMessage("command-arena-resetspectators", true),
	COMMAND_ARENA_SHOWSPECTATORS = new CourseMessage("command-arena-showspectators", true),
	
	COMMAND_ARENA_ADDJUKEBOX = new CourseMessage("command-arena-addjukebox", true),
	COMMAND_ARENA_REMOVEJUKEBOX = new CourseMessage("command-arena-removejukebox", true),
	COMMAND_ARENA_REMOVEJUKEBOX_FAIL = new CourseMessage("command-arena-removejukebox-fail", true),
	COMMAND_ARENA_RESETJUKEBOXES = new CourseMessage("command-arena-resetjukeboxes", true),
	COMMAND_ARENA_PLAYJUKEBOXES = new CourseMessage("command-arena-playjukeboxes", true),
	
	COMMAND_ARENA_ADDITEM = new CourseMessage("command-arena-additem", true),
	COMMAND_ARENA_REMOVEITEM = new CourseMessage("command-arena-removeitem", true),
	COMMAND_ARENA_REMOVEITEM_FAIL = new CourseMessage("command-arena-removeitem-fail", true),
	COMMAND_ARENA_RESETITEMS = new CourseMessage("command-arena-resetitems", true),
	COMMAND_ARENA_SHOWITEMS = new CourseMessage("command-arena-showitems", true),
	
	COMMAND_ARENA_SETSPEEDMODIFIER = new CourseMessage("command-arena-setspeedmodifier", true),
	
	COMMAND_ARENA_RIDE_NOBREAKLINE = new CourseMessage("command-arena-ride-nobreakline", true),
	
	COMMAND_ARENA_REGIONSET = new CourseMessage("command-arena-regionset", true),
	REGIONWARNING = new CourseMessage("regionwarning", false),
	
	COMMAND_ARENA_DELETE_DONE = new CourseMessage("command-arena-delete-done", true),
	COMMAND_ARENA_DELETE_FAILED = new CourseMessage("command-arena-delete-failed", true),
	
	COMMAND_ARENA_ADDDEATHZONE = new CourseMessage("command-arena-adddeathzone", true),
	COMMAND_ARENA_REMOVEDEATHZONE = new CourseMessage("command-arena-removedeathzone", true),
	COMMAND_ARENA_RESETDEATHZONES = new CourseMessage("command-arena-resetdeathzones", true),
	COMMAND_ARENA_TESTDEATHZONES = new CourseMessage("command-arena-testdeathzones", true),

	COMMAND_ARENA_ICON_SET = new CourseMessage("command-arena-icon-set", true),
	COMMAND_ARENA_ICON_NOITEM = new CourseMessage("command-arena-icon-noitem", true),
	
	COMMAND_ARENASTATS_NOTREADY = new CourseMessage("command-arenastats-notready", true),
	COMMAND_ARENASTATS_NOARGS = new CourseMessage("command-arenastats-noargs", true),
	COMMAND_ARENASTATS_HEADER = new CourseMessage("command-arenastats-header", true),
	COMMAND_ARENASTATS_ENTRY = new CourseMessage("command-arenastats-entry", false),
	COMMAND_ARENASTATS_SELF = new CourseMessage("command-arenastats-self", false),
	COMMAND_ARENASTATS_INVALIDARENA = new CourseMessage("command-arenastats-invalidarena", true),
	
	COMMAND_CONFIG_MENU = new CourseMessage("command-config-menu", true),
	COMMAND_CONFIG_MENU_WARNING = new CourseMessage("command-config-menu-warning", true),
	COMMAND_CONFIG_MENU_LOCATION = new CourseMessage("command-config-menu-location", false),
	COMMAND_CONFIG_MENU_DEFINED = new CourseMessage("command-config-menu-defined", false),
	COMMAND_CONFIG_MENU_UNDEFINED = new CourseMessage("command-config-menu-undefined", false),
	COMMAND_CONFIG_MENU_LOBBY = new CourseMessage("command-config-menu-lobby", false),
	COMMAND_CONFIG_MENU_EXIT = new CourseMessage("command-config-menu-exit", false),
	COMMAND_CONFIG_MENU_REGION = new CourseMessage("command-config-menu-region", false),
	COMMAND_CONFIG_MENU_VEHICLES = new CourseMessage("command-config-menu-vehicles", false),
	COMMAND_CONFIG_MENU_HELP = new CourseMessage("command-config-menu-help", false),
	
	COMMAND_CONFIG_MENU_READY = new CourseMessage("command-config-menu-ready", true),
	COMMAND_CONFIG_MENU_ALMOSTREADY = new CourseMessage("command-config-menu-almostready", true),
	COMMAND_CONFIG_MENU_NOTREADY = new CourseMessage("command-config-menu-notready", true),
	COMMAND_CONFIG_MENU_NOTREADY_BADREGION = new CourseMessage("command-config-menu-notready-badregion", false),
	COMMAND_CONFIG_MENU_NOTREADY_NOREGION = new CourseMessage("command-config-menu-notready-noregion", false),
	COMMAND_CONFIG_MENU_NOTREADY_NOLOBBY = new CourseMessage("command-config-menu-notready-nolobby", false),
	COMMAND_CONFIG_MENU_NOTREADY_NOEXIT = new CourseMessage("command-config-menu-notready-noexit", false),
	COMMAND_CONFIG_MENU_NOTREADY_NOVEHICLES = new CourseMessage("command-config-menu-notready-novehicles", false),
	
	COMMAND_CONFIG_HELP1 = new CourseMessage("command-config-help1", true),
	COMMAND_CONFIG_HELP2 = new CourseMessage("command-config-help2", false),
	COMMAND_CONFIG_HELP3 = new CourseMessage("command-config-help3", false),
	COMMAND_CONFIG_HELP4 = new CourseMessage("command-config-help4", false),
	COMMAND_CONFIG_HELP5 = new CourseMessage("command-config-help5", false),
	COMMAND_CONFIG_HELP6 = new CourseMessage("command-config-help6", false),
	COMMAND_CONFIG_HELP7 = new CourseMessage("command-config-help7", false),
	COMMAND_CONFIG_HELP8 = new CourseMessage("command-config-help8", false),
	COMMAND_CONFIG_HELP9 = new CourseMessage("command-config-help9", false),
	COMMAND_CONFIG_HELP10 = new CourseMessage("command-config-help10", false),
	COMMAND_CONFIG_HELP11 = new CourseMessage("command-config-help11", false),
	COMMAND_CONFIG_HELP12 = new CourseMessage("command-config-help12", false),
	COMMAND_CONFIG_HELP13 = new CourseMessage("command-config-help13", false),
	COMMAND_CONFIG_HELP14 = new CourseMessage("command-config-help14", false),
	COMMAND_CONFIG_HELP15 = new CourseMessage("command-config-help15", false),
	COMMAND_CONFIG_HELP16 = new CourseMessage("command-config-help16", false),
	
	COMMAND_CONFIG_LOBBYSET = new CourseMessage("command-config-lobbyset", true),
	COMMAND_CONFIG_EXITSET = new CourseMessage("command-config-exitset", true),
	COMMAND_CONFIG_REGIONSET = new CourseMessage("command-config-regionset", true),
	COMMAND_CONFIG_VEHICLESET = new CourseMessage("command-config-vehicleset", true),
	COMMAND_CONFIG_INVALIDVEHICLE = new CourseMessage("command-config-invalidvehicle", true),
	COMMAND_CONFIG_VEHICLELIST = new CourseMessage("command-config-vehiclelist", true),
	COMMAND_CONFIG_VEHICLEREMOVED = new CourseMessage("command-config-removevehicle", true),
	COMMAND_CONFIG_ITEMSET = new CourseMessage("command-config-itemset", true),
	COMMAND_CONFIG_NOITEM = new CourseMessage("command-config-noitem", true),

	COMMAND_CONFIG_PERMISSION_PLAY = new CourseMessage("command-config-permission-play", false),
	COMMAND_CONFIG_PERMISSION_VOTE = new CourseMessage("command-config-permission-vote", false),
	COMMAND_CONFIG_PERMISSION_VIP_FULL = new CourseMessage("command-config-permission-vip-full", false),
	COMMAND_CONFIG_PERMISSION_VIP_VEHICLE = new CourseMessage("command-config-permission-vip-vehicle", false),
	COMMAND_CONFIG_PERMISSION_VIP_MUSIC = new CourseMessage("command-config-permission-vip-music", false),
	COMMAND_CONFIG_PERMISSION_STAFF = new CourseMessage("command-config-permission-staff", false),
	COMMAND_CONFIG_PERMISSION_ADMIN = new CourseMessage("command-config-permission-admin", false),
	COMMAND_CONFIG_PERMISSION_CMDBYPASS = new CourseMessage("command-config-permission-cmdbypass", false),
	COMMAND_CONFIG_PERMISSION_AUTOJOINBYPASS = new CourseMessage("command-config-permission-autojoinbypass", false),
	COMMAND_CONFIG_PERMISSION_ALLUNLOCKED = new CourseMessage("command-config-permission-allunlocked", false),

	COMMAND_INFO_TITLE = new CourseMessage("command-info-title", true),
	COMMAND_INFO_COMMAND_JOIN = new CourseMessage("command-info-command-join", false),
	COMMAND_INFO_COMMAND_LEAVE = new CourseMessage("command-info-command-leave", false),
	COMMAND_INFO_COMMAND_VOTE = new CourseMessage("command-info-command-vote", false),
	COMMAND_INFO_COMMAND_STATS = new CourseMessage("command-info-command-stats", false),
	COMMAND_INFO_COMMAND_ACHIEVEMENTS = new CourseMessage("command-info-command-achievements", false),
	COMMAND_INFO_COMMAND_RANKING = new CourseMessage("command-info-command-ranking", false),
	COMMAND_INFO_COMMAND_BUY = new CourseMessage("command-info-command-buy", false),
	COMMAND_INFO_COMMAND_ARENA = new CourseMessage("command-info-command-arena", false),
	COMMAND_INFO_COMMAND_CONFIG = new CourseMessage("command-info-command-config", false),
	COMMAND_INFO_COMMAND_VERSION = new CourseMessage("command-info-command-version", false),
	
	COMMAND_RELOAD_DONE = new CourseMessage("command-reload-done", true),
	COMMAND_RELOAD_ERROR = new CourseMessage("command-reload-error", true),
	
	COMMAND_STATS_HEADER = new CourseMessage("command-stats-header", true),
	COMMAND_STATS_VICTORIES = new CourseMessage("command-stats-victories", false),
	COMMAND_STATS_GAMESPLAYED = new CourseMessage("command-stats-gamesplayed", false),
	COMMAND_STATS_BESTVEHICLE = new CourseMessage("command-stats-bestvehicle", false),
	COMMAND_STATS_BESTVEHICLE_GAMESPLAYED_HOVER = new CourseMessage("command-stats-bestvehicle-gamesplayed-hover", false),
	COMMAND_STATS_ITEMS = new CourseMessage("command-stats-items", false),
	COMMAND_STATS_PASSINGS = new CourseMessage("command-stats-passings", false),
	COMMAND_STATS_POINTS = new CourseMessage("command-stats-points", false),
	COMMAND_STATS_SCORE = new CourseMessage("command-stats-score", false),
	
	COMMAND_BUY_INVALIDPURCHASE = new CourseMessage("command-buy-invalidpurchase", true),
	COMMAND_BUY_LIST = new CourseMessage("command-buy-list", true),
	COMMAND_BUY_NOTHINGLEFT = new CourseMessage("command-buy-nothingleft", true),
	
	COMMAND_VOTE_CANTVOTENOW = new CourseMessage("command-vote-cantvotenow", true),
	COMMAND_VOTE_CANTOUTOFGAME = new CourseMessage("command-vote-cantoutofgame", true),
	COMMAND_VOTE_LIST = new CourseMessage("command-vote-list", true),
	COMMAND_VOTE_HOVER = new CourseMessage("command-vote-hover", false),
	
	ACHIEVEMENT_COMPLETE = new CourseMessage("achievement-complete", true),
	
	ACHIEVEMENT_VICTORIES_DESC = new CourseMessage("achievement-victories-desc", false),
	
	ACHIEVEMENT_VICTORIES_NAME_7 = new CourseMessage("achievement-victories-name-7", false),
	ACHIEVEMENT_VICTORIES_NAME_6 = new CourseMessage("achievement-victories-name-6", false),
	ACHIEVEMENT_VICTORIES_NAME_5 = new CourseMessage("achievement-victories-name-5", false),
	ACHIEVEMENT_VICTORIES_NAME_4 = new CourseMessage("achievement-victories-name-4", false),
	ACHIEVEMENT_VICTORIES_NAME_3 = new CourseMessage("achievement-victories-name-3", false),
	ACHIEVEMENT_VICTORIES_NAME_2 = new CourseMessage("achievement-victories-name-2", false),
	ACHIEVEMENT_VICTORIES_NAME_1 = new CourseMessage("achievement-victories-name-1", false),
	
	ACHIEVEMENT_GAMESPLAYED_DESC = new CourseMessage("achievement-gamesplayed-desc", false),
	
	ACHIEVEMENT_GAMESPLAYED_NAME_7 = new CourseMessage("achievement-gamesplayed-name-7", false),
	ACHIEVEMENT_GAMESPLAYED_NAME_6 = new CourseMessage("achievement-gamesplayed-name-6", false),
	ACHIEVEMENT_GAMESPLAYED_NAME_5 = new CourseMessage("achievement-gamesplayed-name-5", false),
	ACHIEVEMENT_GAMESPLAYED_NAME_4 = new CourseMessage("achievement-gamesplayed-name-4", false),
	ACHIEVEMENT_GAMESPLAYED_NAME_3 = new CourseMessage("achievement-gamesplayed-name-3", false),
	ACHIEVEMENT_GAMESPLAYED_NAME_2 = new CourseMessage("achievement-gamesplayed-name-2", false),
	ACHIEVEMENT_GAMESPLAYED_NAME_1 = new CourseMessage("achievement-gamesplayed-name-1", false),
	
	ACHIEVEMENT_CAVESPIDER_DESC = new CourseMessage("achievement-cavespider-desc", false),
	ACHIEVEMENT_CAVESPIDER_NAME = new CourseMessage("achievement-cavespider-name", false),
	ACHIEVEMENT_CAVESPIDER_UNLOCK = new CourseMessage("achievement-cavespider-unlock", false),
	
	ACHIEVEMENT_DONKEY_DESC = new CourseMessage("achievement-donkey-desc", false),
	ACHIEVEMENT_DONKEY_NAME = new CourseMessage("achievement-donkey-name", false),
	ACHIEVEMENT_DONKEY_UNLOCK = new CourseMessage("achievement-donkey-unlock", false),
	
	ACHIEVEMENT_ELDERGUARDIAN_DESC = new CourseMessage("achievement-elderguardian-desc", false),
	ACHIEVEMENT_ELDERGUARDIAN_NAME = new CourseMessage("achievement-elderguardian-name", false),
	ACHIEVEMENT_ELDERGUARDIAN_UNLOCK = new CourseMessage("achievement-elderguardian-unlock", false),
	
	ACHIEVEMENT_MAGMACUBE_DESC = new CourseMessage("achievement-magmacube-desc", false),
	ACHIEVEMENT_MAGMACUBE_NAME = new CourseMessage("achievement-magmacube-name", false),
	ACHIEVEMENT_MAGMACUBE_UNLOCK = new CourseMessage("achievement-magmacube-unlock", false),
	
	ACHIEVEMENT_MUSHROOMCOW_DESC = new CourseMessage("achievement-mushroomcow-desc", false),
	ACHIEVEMENT_MUSHROOMCOW_NAME = new CourseMessage("achievement-mushroomcow-name", false),
	ACHIEVEMENT_MUSHROOMCOW_UNLOCK = new CourseMessage("achievement-mushroomcow-unlock", false),
	
	ACHIEVEMENT_SILVERFISH_DESC = new CourseMessage("achievement-silverfish-desc", false),
	ACHIEVEMENT_SILVERFISH_NAME = new CourseMessage("achievement-silverfish-name", false),
	ACHIEVEMENT_SILVERFISH_UNLOCK = new CourseMessage("achievement-silverfish-unlock", false),
	
	ACHIEVEMENT_SUPERSHEEP_DESC = new CourseMessage("achievement-supersheep-desc", false),
	ACHIEVEMENT_SUPERSHEEP_NAME = new CourseMessage("achievement-supersheep-name", false),
	ACHIEVEMENT_SUPERSHEEP_UNLOCK = new CourseMessage("achievement-supersheep-unlock", false),
	
	ACHIEVEMENT_BLOCK_DESC = new CourseMessage("achievement-block-desc", false),
	ACHIEVEMENT_BLOCK_NAME = new CourseMessage("achievement-block-name", false),
	ACHIEVEMENT_BLOCK_UNLOCK = new CourseMessage("achievement-block-unlock", false),
	
	ACHIEVEMENT_DISCMELLOHI_DESC = new CourseMessage("achievement-discmellohi-desc", false),
	ACHIEVEMENT_DISCMELLOHI_NAME = new CourseMessage("achievement-discmellohi-name", false),
	ACHIEVEMENT_DISCMELLOHI_UNLOCK = new CourseMessage("achievement-discmellohi-unlock", false),
	
	ACHIEVEMENT_DISCSTAL_DESC = new CourseMessage("achievement-discstal-desc", false),
	ACHIEVEMENT_DISCSTAL_NAME = new CourseMessage("achievement-discstal-name", false),
	ACHIEVEMENT_DISCSTAL_UNLOCK = new CourseMessage("achievement-discstal-unlock", false),
	
	ACHIEVEMENT_DISCWAIT_DESC = new CourseMessage("achievement-discwait-desc", false),
	ACHIEVEMENT_DISCWAIT_NAME = new CourseMessage("achievement-discwait-name", false),
	ACHIEVEMENT_DISCWAIT_UNLOCK = new CourseMessage("achievement-discwait-unlock", false),
	
	ITEM_FAKEITEM = new CourseMessage("item-fakeitem", false),
	ITEM_GROUNDPOUND = new CourseMessage("item-groundpound", false),
	ITEM_MISSILE = new CourseMessage("item-missile", false),
	ITEM_SHIELD = new CourseMessage("item-shield", false),
	ITEM_SUGAR = new CourseMessage("item-sugar", false),
	ITEM_WALL = new CourseMessage("item-wall", false),
	ITEM_BOMB = new CourseMessage("item-bomb", false),
	ITEM_GRAPPLINGHOOK = new CourseMessage("item-grapplinghook", false),
	ITEM_CLOUD = new CourseMessage("item-cloud", false),
	ITEM_FIREBALL = new CourseMessage("item-fireball", false),
	
	ITEM_SPECIAL_COW = new CourseMessage("item-special-cow", false),
	ITEM_SPECIAL_GUARDIAN = new CourseMessage("item-special-guardian", false),
	ITEM_SPECIAL_DONKEY = new CourseMessage("item-special-donkey", false),
	ITEM_SPECIAL_PIG = new CourseMessage("item-special-pig", false),
	ITEM_SPECIAL_WOLF = new CourseMessage("item-special-wolf", false),
	ITEM_SPECIAL_CHICKEN = new CourseMessage("item-special-chicken", false),
	ITEM_SPECIAL_HORSE = new CourseMessage("item-special-horse", false),
	ITEM_SPECIAL_UNDEADHORSE = new CourseMessage("item-special-undeadhorse", false),
	ITEM_SPECIAL_SKELETONHORSE = new CourseMessage("item-special-skeletonhorse", false),
	ITEM_SPECIAL_SLIME = new CourseMessage("item-special-slime", false),
	ITEM_SPECIAL_MAGMACUBE = new CourseMessage("item-special-magmacube", false),
	ITEM_SPECIAL_OCELOT = new CourseMessage("item-special-ocelot", false),
	ITEM_SPECIAL_SPIDER = new CourseMessage("item-special-spider", false),
	ITEM_SPECIAL_CAVESPIDER = new CourseMessage("item-special-cavespider", false),
	ITEM_SPECIAL_SQUID = new CourseMessage("item-special-squid", false),
	ITEM_SPECIAL_SUPERSHEEP = new CourseMessage("item-special-supersheep", false),
	ITEM_SPECIAL_SHEEP = new CourseMessage("item-special-sheep", false),
	ITEM_SPECIAL_MUSHROOMCOW = new CourseMessage("item-special-mushroomcow", false),
	ITEM_SPECIAL_SILVERFISH = new CourseMessage("item-special-silverfish", false),
	ITEM_SPECIAL_RABBIT = new CourseMessage("item-special-rabbit", false),
	
	ITEM_SPECIAL_BABYCOW = new CourseMessage("item-special-babycow", false),
	ITEM_SPECIAL_ELDERGUARDIAN = new CourseMessage("item-special-elderguardian", false),
	ITEM_SPECIAL_MINECART = new CourseMessage("item-special-minecart", false),
	ITEM_SPECIAL_BLOCK = new CourseMessage("item-special-block", false),
	
	ITEM_SPECIAL_WOLF_TITLE = new CourseMessage("item-special-wolf-title", false),
	ITEM_SPECIAL_RABBIT_TITLE = new CourseMessage("item-special-rabbit-title", false),
	ITEM_SPECIAL_SILVERFISH_TITLE = new CourseMessage("item-special-silverfish-title", false),
	ITEM_CLOUD_TITLE = new CourseMessage("item-cloud-title", false),
	
	PURCHASE_VEHICLE = new CourseMessage("purchase-vehicle", false),
	PURCHASE_VEHICLE_DONE = new CourseMessage("purchase-vehicle-done", true),
	PURCHASE_MUSIC = new CourseMessage("purchase-music", false),
	PURCHASE_MUSIC_DONE = new CourseMessage("purchase-music-done", true),
	
	POINTS_ADD = new CourseMessage("points-add", true),
	POINTS_REMOVE = new CourseMessage("points-remove", true),
	
	PURCHASE_NOTENOUGHTMONEY = new CourseMessage("purchase-notenoughtmoney", true),
	PURCHASE_ALREADYPURCHASED = new CourseMessage("purchase-alreadypurchased", true),
	PURCHASE_CLICKTOBUY = new CourseMessage("purchase-clicktobuy", false),
	PURCHASE_NOTENOUGHTTOBUY = new CourseMessage("purchase-notenoughttobuy", false),
	PURCHASE_PRICE = new CourseMessage("purchase-price", false),
	
	PURCHASE_CAT = new CourseMessage("purchase-cat", false),
	PURCHASE_BLOCKS = new CourseMessage("purchase-blocks", false),
	PURCHASE_CHIRP = new CourseMessage("purchase-chirp", false),
	PURCHASE_FAR = new CourseMessage("purchase-far", false),
	PURCHASE_MALL = new CourseMessage("purchase-mall", false),
	PURCHASE_MELLOHI = new CourseMessage("purchase-mellohi", false),
	PURCHASE_STAL = new CourseMessage("purchase-stal", false),
	PURCHASE_STRAD = new CourseMessage("purchase-strad", false),
	PURCHASE_WARD = new CourseMessage("purchase-ward", false),
	PURCHASE_WAIT = new CourseMessage("purchase-wait", false),
	
	RECORD_CAT = new CourseMessage("record-cat", false),
	RECORD_BLOCKS = new CourseMessage("record-blocks", false),
	RECORD_CHIRP = new CourseMessage("record-chirp", false),
	RECORD_FAR = new CourseMessage("record-far", false),
	RECORD_MALL = new CourseMessage("record-mall", false),
	RECORD_MELLOHI = new CourseMessage("record-mellohi", false),
	RECORD_STAL = new CourseMessage("record-stal", false),
	RECORD_STRAD = new CourseMessage("record-strad", false),
	RECORD_WARD = new CourseMessage("record-ward", false),
	RECORD_WAIT = new CourseMessage("record-wait", false),
	
	RECORD_NOMUSIC = new CourseMessage("record-nomusic", false),
	
	RECORD_LORE = new CourseMessage("record-lore", false),
	
	VEHICLE_BOAT_NAME = new CourseMessage("vehicle-boat-name", false),
	VEHICLE_BOAT_DESC = new CourseMessage("vehicle-boat-desc", false),
	
	VEHICLE_CAVESPIDER_NAME = new CourseMessage("vehicle-cavespider-name", false),
	VEHICLE_CAVESPIDER_DESC = new CourseMessage("vehicle-cavespider-desc", false),
	
	VEHICLE_COW_NAME = new CourseMessage("vehicle-cow-name", false),
	VEHICLE_COW_DESC = new CourseMessage("vehicle-cow-desc", false),
	
	VEHICLE_DONKEY_NAME = new CourseMessage("vehicle-donkey-name", false),
	VEHICLE_DONKEY_DESC = new CourseMessage("vehicle-donkey-desc", false),
	
	VEHICLE_ELDERGUARDIAN_NAME = new CourseMessage("vehicle-elderguardian-name", false),
	VEHICLE_ELDERGUARDIAN_DESC = new CourseMessage("vehicle-elderguardian-desc", false),
	
	VEHICLE_GUARDIAN_NAME = new CourseMessage("vehicle-guardian-name", false),
	VEHICLE_GUARDIAN_DESC = new CourseMessage("vehicle-guardian-desc", false),
	
	VEHICLE_HORSE_NAME = new CourseMessage("vehicle-horse-name", false),
	VEHICLE_HORSE_DESC = new CourseMessage("vehicle-horse-desc", false),
	
	VEHICLE_MAGMACUBE_NAME = new CourseMessage("vehicle-magmacube-name", false),
	VEHICLE_MAGMACUBE_DESC = new CourseMessage("vehicle-magmacube-desc", false),
	
	VEHICLE_MINECART_NAME = new CourseMessage("vehicle-minecart-name", false),
	VEHICLE_MINECART_DESC = new CourseMessage("vehicle-minecart-desc", false),
	
	VEHICLE_MUSHROOMCOW_NAME = new CourseMessage("vehicle-mushroomcow-name", false),
	VEHICLE_MUSHROOMCOW_DESC = new CourseMessage("vehicle-mushroomcow-desc", false),
	
	VEHICLE_OCELOT_NAME = new CourseMessage("vehicle-ocelot-name", false),
	VEHICLE_OCELOT_DESC = new CourseMessage("vehicle-ocelot-desc", false),
	
	VEHICLE_PIG_NAME = new CourseMessage("vehicle-pig-name", false),
	VEHICLE_PIG_DESC = new CourseMessage("vehicle-pig-desc", false),
	
	VEHICLE_RABBIT_NAME = new CourseMessage("vehicle-rabbit-name", false),
	VEHICLE_RABBIT_DESC = new CourseMessage("vehicle-rabbit-desc", false),
	
	VEHICLE_SHEEP_NAME = new CourseMessage("vehicle-sheep-name", false),
	VEHICLE_SHEEP_DESC = new CourseMessage("vehicle-sheep-desc", false),
	
	VEHICLE_SILVERFISH_NAME = new CourseMessage("vehicle-silverfish-name", false),
	VEHICLE_SILVERFISH_DESC = new CourseMessage("vehicle-silverfish-desc", false),
	
	VEHICLE_SKELETONHORSE_NAME = new CourseMessage("vehicle-skeletonhorse-name", false),
	VEHICLE_SKELETONHORSE_DESC = new CourseMessage("vehicle-skeletonhorse-desc", false),
	
	VEHICLE_SLIME_NAME = new CourseMessage("vehicle-slime-name", false),
	VEHICLE_SLIME_DESC = new CourseMessage("vehicle-slime-desc", false),
	
	VEHICLE_SPIDER_NAME = new CourseMessage("vehicle-spider-name", false),
	VEHICLE_SPIDER_DESC = new CourseMessage("vehicle-spider-desc", false),
	
	VEHICLE_SQUID_NAME = new CourseMessage("vehicle-squid-name", false),
	VEHICLE_SQUID_DESC = new CourseMessage("vehicle-squid-desc", false),
	
	VEHICLE_SUPERSHEEP_NAME = new CourseMessage("vehicle-supersheep-name", false),
	VEHICLE_SUPERSHEEP_DESC = new CourseMessage("vehicle-supersheep-desc", false),
	
	VEHICLE_UNDEADHORSE_NAME = new CourseMessage("vehicle-undeadhorse-name", false),
	VEHICLE_UNDEADHORSE_DESC = new CourseMessage("vehicle-undeadhorse-desc", false),
	
	VEHICLE_WOLF_NAME = new CourseMessage("vehicle-wolf-name", false),
	VEHICLE_WOLF_DESC = new CourseMessage("vehicle-wolf-desc", false),
	
	VEHICLE_CHICKEN_NAME = new CourseMessage("vehicle-chicken-name", false),
	VEHICLE_CHICKEN_DESC = new CourseMessage("vehicle-chicken-desc", false),
	
	VEHICLE_BLOCK_NAME = new CourseMessage("vehicle-block-name", false),
	VEHICLE_BLOCK_DESC = new CourseMessage("vehicle-block-desc", false),
	
	VEHICLE_BABYCOW_NAME = new CourseMessage("vehicle-babycow-name", false),
	VEHICLE_BABYCOW_DESC = new CourseMessage("vehicle-babycow-desc", false),
	
	SIGN_HEADER = new CourseMessage("sign-header", false),
	
	SIGN_JOIN = new CourseMessage("sign-join", false),
	SIGN_LEAVE = new CourseMessage("sign-leave", false),
	SIGN_STATS = new CourseMessage("sign-stats", false),
	SIGN_ARENASTATS = new CourseMessage("sign-arenastats", false),
	SIGN_INFO = new CourseMessage("sign-info", false),
	SIGN_BUY = new CourseMessage("sign-buy", false),
	SIGN_VOTE = new CourseMessage("sign-vote", false),
	SIGN_ACHIEVEMENT = new CourseMessage("sign-achievement", false),
	
	SIGN_CLICK = new CourseMessage("sign-click", false),
	
	WAND = new CourseMessage("wand", true),
	
	NUMBER_SEPARATOR = new CourseMessage("number-separator", false),
	
	UPDATE_NOTIFICATION = new CourseMessage("update-notification", true)
	;
	
	public CourseMessage(String path)
	{
		super(path);
	}
	
	private CourseMessage(String path, boolean prefix)
	{
		super(path, prefix);
	}

	public CourseMessage(String path, boolean prefix, MessageType type)
	{
		super(path, prefix, type);
	}

	public CourseMessage(String path, int delay, int fadeIn, int fadeOut)
	{
		super(path, delay, fadeIn, fadeOut);
	}
	
	public CourseMessage(String path, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		super(path, prefix, hoverEvent, hoverEventContent, clickEvent,
				clickEventContent);
	}

	public void sayPlayers(String... replacements)
	{
		this.sayMembers(MobRacersPlugin.getGame(), replacements);
	}

	@Override
	protected YamlConfiguration getLangConfig()
	{
		return MobRacersPlugin.getLang();
	}
	
	public static String toString(int integer)
	{
		return TextUtil.toString(integer, CourseMessage.NUMBER_SEPARATOR.toString());
	}
}
