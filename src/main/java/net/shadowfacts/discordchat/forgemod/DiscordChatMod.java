package net.shadowfacts.discordchat.forgemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.core.DiscordChat;

import java.io.File;
import java.io.IOException;

/**
 * @author shadowfacts
 */
@Mod(modid = DiscordChatMod.MOD_ID, name = DiscordChatMod.NAME, version = DiscordChatMod.VERSION, acceptableRemoteVersions = "*")
public class DiscordChatMod {

	public static final String MOD_ID = "discordchat";
	public static final String NAME = "Discord Chat";
	public static final String VERSION = "@VERSION@";

	public static IMinecraftAdapter minecraftAdapter = new DiscordChatModAdapter();
	public static IConfig config;
	public static IDiscordChat discordChat;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		discordChat = new DiscordChat(new File(event.getModConfigurationDirectory(), "shadowfacts/DiscordChat/"), minecraftAdapter);
		config = discordChat.getConfig();
		discordChat.connect();

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDC(discordChat));
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		discordChat.start();
		if (config.sendServerOnlineOfflineMessages()) discordChat.sendMessage("Server is online");
	}

	@Mod.EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		if (config.sendServerOnlineOfflineMessages()) discordChat.sendMessage("Server is offline");
		discordChat.stop();
	}

}
