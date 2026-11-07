package dev.janrhey.tapbow;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/** Settings screen (opened from Mod Menu): on/off, draw time before release, sneak option. */
public class TapBowConfigScreen extends Screen {
	private static final int[] CHARGES = {4, 6, 8, 10, 15, 20};

	private final Screen parent;

	public TapBowConfigScreen(Screen parent) {
		super(Component.literal("Tap Bow"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int cx = this.width / 2;
		int top = this.height / 2 - 40;

		this.addRenderableWidget(Button.builder(toggleText(), button -> {
			TapBowConfig.setEnabled(!TapBowConfig.isEnabled());
			button.setMessage(toggleText());
		}).bounds(cx - 100, top, 200, 20).build());

		this.addRenderableWidget(Button.builder(chargeText(), button -> {
			TapBowConfig.setChargeTicks(nextCharge(TapBowConfig.chargeTicks()));
			button.setMessage(chargeText());
		}).bounds(cx - 100, top + 24, 200, 20).build());

		this.addRenderableWidget(Button.builder(sneakText(), button -> {
			TapBowConfig.setSneakCharges(!TapBowConfig.sneakCharges());
			button.setMessage(sneakText());
		}).bounds(cx - 100, top + 48, 200, 20).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
				.bounds(cx - 100, top + 80, 200, 20).build());
	}

	/** Next value in CHARGES that is higher than the current one, wrapping back to the lowest. */
	private static int nextCharge(int current) {
		for (int c : CHARGES) {
			if (c > current) {
				return c;
			}
		}
		return CHARGES[0];
	}

	private static Component toggleText() {
		return Component.literal("Tap Bow: " + (TapBowConfig.isEnabled() ? "ON" : "OFF"));
	}

	private static Component chargeText() {
		int ticks = TapBowConfig.chargeTicks();
		String note = ticks >= TapBowConfig.FULL_CHARGE ? " (full draw)" : "";
		return Component.literal("Release after: " + ticks + " ticks" + note);
	}

	private static Component sneakText() {
		return Component.literal("Sneak = normal bow: " + (TapBowConfig.sneakCharges() ? "ON" : "OFF"));
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.render(graphics, mouseX, mouseY, delta);
		graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);
	}

	@Override
	public void onClose() {
		TapBowConfig.save();
		this.minecraft.setScreen(this.parent);
	}
}
