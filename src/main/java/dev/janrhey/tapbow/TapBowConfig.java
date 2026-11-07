package dev.janrhey.tapbow;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tiny JSON config: config/tapbow.json
 *
 * enabled:       master on/off switch (also in the Mod Menu settings screen).
 * chargeTicks:   how many ticks the bow is drawn before it releases by itself.
 *                Clamped to 4..20. A bow needs about 3 ticks of draw before it will
 *                shoot at all, and 20 ticks is a full draw. Arrow strength is whatever
 *                vanilla gives for that draw time.
 * sneakCharges:  when true, holding Sneak while drawing gives the normal hold-to-charge bow.
 */
public final class TapBowConfig {
	public static final int MIN_CHARGE = 4;
	public static final int FULL_CHARGE = 20;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("tapbow.json");

	/** Written to / read from the JSON file. */
	public boolean enabled = true;
	public int chargeTicks = 4;
	public boolean sneakCharges = true;

	private static TapBowConfig instance = new TapBowConfig();

	private TapBowConfig() {
	}

	public static boolean isEnabled() {
		return instance.enabled;
	}

	public static void setEnabled(boolean enabled) {
		instance.enabled = enabled;
	}

	public static int chargeTicks() {
		return instance.chargeTicks;
	}

	public static void setChargeTicks(int ticks) {
		instance.chargeTicks = clamp(ticks);
	}

	public static boolean sneakCharges() {
		return instance.sneakCharges;
	}

	public static void setSneakCharges(boolean value) {
		instance.sneakCharges = value;
	}

	private static int clamp(int ticks) {
		return Math.max(MIN_CHARGE, Math.min(FULL_CHARGE, ticks));
	}

	public static void load() {
		try {
			if (Files.exists(FILE)) {
				TapBowConfig loaded = GSON.fromJson(Files.readString(FILE), TapBowConfig.class);
				if (loaded != null) {
					instance = loaded;
				}
			}
		} catch (Exception e) {
			TapBowClient.LOGGER.warn("Could not read {}, using defaults", FILE, e);
		}

		instance.chargeTicks = clamp(instance.chargeTicks);
		save();
	}

	public static void save() {
		try {
			Files.writeString(FILE, GSON.toJson(instance));
		} catch (IOException e) {
			TapBowClient.LOGGER.warn("Could not write {}", FILE, e);
		}
	}
}
