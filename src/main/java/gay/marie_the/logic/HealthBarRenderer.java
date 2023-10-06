package gay.marie_the.logic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class HealthBarRenderer {
	private static final Identifier FILLED_BAR = new Identifier("two-weeks", "textures/full.png");
	public static void renderFillBar(DrawContext graphics,        // Screen to render on
									 int x,                       // X position on screen (left, right)
									 int y,                       // Y position on screen (up, down)
									 float armor,                 // out of 100, usually for health, shields, etc.
									 float r,                     // red
									 float g,                     // green
									 float b,                     // blue
									 boolean smallTick,
									 float max,
									 float lastValue,              // cool fx
									 int statusEffect,  // status effect
									 int type                      // type of bar (1 is health (advanced), 2 is hunger (simple), 3 is armor (basic))
									 // 1 has a more advanced colored background and is made to track changes in health
									 // 2 has a red glow when under 30% (when you are unable to sprint)
									 // 3 has none of the above. Useful for adding extra bars where the amount does not affect
									 // 	health or hunger. I don't know how to explain this well lmao
						  ) {
		// ONLY FOR NON-HEALTH. health rendering will be done in another method
		int barHeight = 12;
		int textY = y + 2;
		String symbol;


		if (smallTick) {
			barHeight = 8;
			textY -= 2;
		}

		// Text

		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		graphics.drawTextWithShadow(renderer, Integer.toString((int) armor), x + 132, textY, 0xFFFFFF);


		float alpha = 0.25f;
		// Bar Background, the bar will fill up this spot. (NOT A SHADOW) (also is dynamic)
		switch(type){
			case 1:	// health
				graphics.setShaderColor(1f - (armor/max), 0, 0, alpha);
				symbol = "+";
				break;
			case 2:	// armor
				graphics.setShaderColor(0, 0, 0, alpha);
				symbol = "\uD83D\uDEE1";
				break;
			case 3: // hunger
				graphics.setShaderColor(armor/max < 0.3 ? 0.5f : 0,0,0, alpha);
				symbol = "";
				break;
			default:
				graphics.setShaderColor(0, 0, 0, 0.25f);
				symbol = "";
		}

		graphics.drawTexture(FILLED_BAR, x, y, 0, 0, 128, barHeight);
		graphics.setShaderColor(1,1,1,1); // there has GOT to be a better way than this jfc
		graphics.drawCenteredTextWithShadow(renderer, symbol, x - 5, textY, 0xFFFFFF);

		if (type == 1) {
			graphics.setShaderColor(1, 1, 1, 1);
			graphics.drawTexture(FILLED_BAR, x, y, 0, 0, (int)(128 * (lastValue / max)), barHeight);

		}

		switch(statusEffect) {
			case 1:
				graphics.setShaderColor((float) 128 /255, (float) 120 /255, (float) 24 /255, 1);
				break;
			case 2:
				graphics.setShaderColor((float) 43 /255, (float) 43 /255, (float) 43 /255, 1);
				break;
			case 3:
				graphics.setShaderColor(1f,1f,1f,1f);
				break;
			case 4:
				graphics.setShaderColor(0.83f, 0.68f, 0.21f, 1);
				break;
			case 5:
				graphics.setShaderColor((float) 148 /255, (float) 110 /255, (float) 50 /255, 1);
				break;
			default:
				graphics.setShaderColor(r, g, b, 1);
		}
		// Bar Fill

		graphics.drawTexture(FILLED_BAR, x, y, 0, 0, (int)(128 * (armor / max)), barHeight);

		// final Identifier TICK = new Identifier("two-weeks", "textures/bar_cap.png");

		// Bar Tick
		if (armor > 0) {

			graphics.setShaderColor(
				MathHelper.clamp(r + 0.2f, 0, 1),
				MathHelper.clamp(g + 0.2f, 0, 1),
				MathHelper.clamp(b + 0.2f, 0, 1),
				1f);

			graphics.drawTexture(FILLED_BAR, (x + ((int) (128 * (armor / max)))) -2 , y, 0, 0, 2, barHeight);
		}

		graphics.setShaderColor(1, 1, 1, 1);
		// minecraft is broken
	}

	public static void TinyBar(
			DrawContext graphics

	){

	}
}