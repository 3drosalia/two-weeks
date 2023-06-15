package gay.marie_the.twoweeks.logix;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class HealthBarRenderer {

	public static void renderFillBar(GuiGraphics graphics,		// Screen to render on
							  int x,					// X position on screen (left, right)
							  int y,					// Y position on screen (up, down)
						  	  float armor,		// out of 100, usually for health, shields, etc.
							  float r,					// red
							  float g,					// green
							  float b,					// blue
							  boolean smallTick
						  ) {
		// ONLY FOR NON-HEALTH. health rendering will be done in another method
		Identifier TICK = new Identifier("twoweeks", "textures/tick.png");
		int barHeight;
		final Identifier FILLED_BAR = new Identifier("twoweeks", "textures/full.png");
		final Identifier EMPTY_BAR = new Identifier("twoweeks", "textures/barbackground.png");
		int textY;


		if (smallTick) {
			//TICK = new Identifier("twoweeks", "textures/smalltick.png");
			barHeight = 8;
			textY = y;
		} else {
			//TICK = new Identifier("twoweeks", "textures/tick.png");
			barHeight = 12;
			textY = y + 2;
		}

		// Text
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		graphics.drawShadowedText(renderer, Integer.toString((int) armor), x + 132, textY, 0xFFFFFF);

		// Bar Background, the bar will fill up this spot. (NOT A SHADOW)
		graphics.drawTexture(EMPTY_BAR, x, y, 0, 0, 128, barHeight);

		// Bar Fill
		RenderSystem.setShaderColor(r, g, b, 1);
		graphics.drawTexture(FILLED_BAR, x, y, 0, 0, (int) (128 * (armor / 20f)), barHeight);
		// Bar Tick
		if (armor > 0) {
			RenderSystem.setShaderColor(
				MathHelper.clamp(r + 0.2f, 0, 1),
				MathHelper.clamp(g + 0.2f, 0, 1),
				MathHelper.clamp(b + 0.2f, 0, 1),
				1f);
			graphics.drawTexture(TICK, x + ((int) (128 * (armor / 20f)) - 4), y, 0, 0, 4, barHeight);
		}

		RenderSystem.setShaderColor(1, 1, 1, 1);
		// minecraft is broken
	}
}
