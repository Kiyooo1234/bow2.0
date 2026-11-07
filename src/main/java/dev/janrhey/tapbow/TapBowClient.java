package dev.janrhey.tapbow;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * While you are drawing a bow, once the draw has lasted the configured number of ticks the mod
 * releases it exactly like letting go of the right mouse button would. The release is a normal
 * one: the server counts the draw time itself and decides the arrow's strength from it, so a short
 * draw gives a weak arrow. After the release the use key is treated as up until you click again,
 * so holding the button does not keep firing.
 *
 * A keybind (Options > Controls > Tap Bow, unbound by default) turns the mod on and off.
 */
public class TapBowClient implements ClientModInitializer {
	public static final String MOD_ID = "tapbow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));

	private static KeyMapping toggleKey;

	@Override
	public void onInitializeClient() {
		TapBowConfig.load();

		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.tapbow.toggle",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, // unbound: pick a key in Options > Controls
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(TapBowClient::onTick);
		LOGGER.info("Tap Bow loaded: enabled={}, releases after {} ticks of draw",
				TapBowConfig.isEnabled(), TapBowConfig.chargeTicks());
	}

	private static void onTick(Minecraft client) {
		while (toggleKey.consumeClick()) {
			TapBowConfig.setEnabled(!TapBowConfig.isEnabled());
			TapBowConfig.save();
			if (client.player != null) {
				client.player.displayClientMessage(
						Component.literal("Tap Bow: " + (TapBowConfig.isEnabled() ? "ON" : "OFF")), true);
			}
		}

		LocalPlayer player = client.player;
		if (!TapBowConfig.isEnabled() || player == null || client.gameMode == null || client.screen != null) {
			return;
		}

		if (!player.isUsingItem() || !player.getUseItem().is(Items.BOW)) {
			return;
		}

		if (TapBowConfig.sneakCharges() && player.isShiftKeyDown()) {
			return;
		}

		if (player.getTicksUsingItem() >= TapBowConfig.chargeTicks()) {
			client.gameMode.releaseUsingItem(player);
			client.options.keyUse.setDown(false);
		}
	}
}
