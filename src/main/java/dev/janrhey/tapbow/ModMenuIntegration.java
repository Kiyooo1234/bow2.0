package dev.janrhey.tapbow;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** Adds the settings button for this mod in Mod Menu (only loaded when Mod Menu is installed). */
public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return TapBowConfigScreen::new;
	}
}
