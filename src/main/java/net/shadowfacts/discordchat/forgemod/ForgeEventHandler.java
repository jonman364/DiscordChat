package net.shadowfacts.discordchat.forgemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * @author shadowfacts
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void onServerChat(ServerChatEvent event) {
		String message = DiscordChatMod.discordChat.filterMCMessage(event.getMessage());
		if (message != null) {
			DiscordChatMod.discordChat.sendMessage(DiscordChatMod.discordChat.getFormatter().fromMC(event.getPlayer().getName(), message));
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (DiscordChatMod.config.sendDeathMessages() && event.getEntityLiving() instanceof EntityPlayer && !(event.getEntityLiving() instanceof FakePlayer) && !event.getEntity().world.isRemote) {
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			DiscordChatMod.discordChat.sendMessage(DiscordChatMod.discordChat.getFormatter().death(player.getName(), player.getCombatTracker().getDeathMessage().getUnformattedText()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (DiscordChatMod.config.sendJoinLeaveMessages()) {
			DiscordChatMod.discordChat.sendMessage(DiscordChatMod.discordChat.getFormatter().join(event.player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (DiscordChatMod.config.sendJoinLeaveMessages()) {
			DiscordChatMod.discordChat.sendMessage(DiscordChatMod.discordChat.getFormatter().leave(event.player.getName()));
		}
	}

}
