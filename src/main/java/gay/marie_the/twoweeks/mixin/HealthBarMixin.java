package gay.marie_the.twoweeks.mixin;

import com.mojang.blaze3d.glfw.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static gay.marie_the.twoweeks.logix.HealthBarRenderer.renderFillBar;

@Mixin(InGameHud.class)
public abstract class HealthBarMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private int lastHealthValue;

	@Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
	public void HealthBarRenderer(
		GuiGraphics graphics, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

		int hunger = player.getHungerManager().getFoodLevel();
		// int width = clientWindow.getScaledWidth();


		final Identifier FILLED_BAR = new Identifier("twoweeks", "textures/full.png");
		final Identifier BLANK_BAR = new Identifier("twoweeks", "textures/empty.png");
		final Identifier TICK = new Identifier("twoweeks", "textures/tick.png");
		final Identifier SMALL_TICK = new Identifier("twoweeks", "textures/smalltick.png");

		int windowX;
		int windowY;

		if (client != null) {
			Window clientWindow = MinecraftClient.getInstance().getWindow();
			int height = clientWindow.getScaledHeight();
			int width = clientWindow.getScaledWidth();
			windowX = MathHelper.clamp(width / 10, 40, width / 2 - 128);
			windowY = height - 40;
		} else {
			throw new RuntimeException("Client not found!");
		}

		int shieldY = windowY - 10;
		int hungerY = shieldY - 4;
		// Empty health bar
		RenderSystem.setShader(GameRenderer::getPositionTexShader);

		// shadow
		// TODO : make it not use the fucking empty healthbar background you fucking idiot
		float R = 0.0f;
		float G = 0.0f;
		float B = 0.0f;
		float percent = 100 * lastHealthValue / maxHealth;
		if (percent == 0) {
			R = 0.0f;
		} else if (percent <= 10) {
			R = 1f;
		} else if (percent <= 15) {
			R = 0.75f;
		} else if (percent <= 30) {
			R = 0.5f;
		} else if (percent <= 60) {
			R = 0.25f;
		} else if (percent <= 75) {
			R = 0.1f;
		} else if (percent > 75) {
			R = 0;
			G = 0;
			B = 0;
		}

		// how the FUCK do you switch case

		/* commented out shadow

		RenderSystem.setShaderTexture(0, BLANK_BAR);
		RenderSystem.setShaderColor(R,G,B,0.5f);
		graphics.drawTexture(matrices, x+1, y+1, 0, 0, 128, 12, 128, 12);

		*/

		// empty bar

		RenderSystem.setShaderColor(R, G, B, 0.25f);
		// health bar
		graphics.drawTexture(FILLED_BAR, windowX, windowY, 0, 0, 128, 12, 128, 12);

		// hunger bar
		graphics.drawTexture(FILLED_BAR, windowX, hungerY, 0, 0, 128, 2, 128, 8);

		// todo : move back
		// health change bar
		if (blinking) {
			RenderSystem.setShaderColor(1f, 0f, 0f, 1f);
			graphics.drawTexture(FILLED_BAR, windowX, windowY, 0, 0, (int) (128 * (health / maxHealth)), 12, 128, 12);
		}
		// health fill
		RenderSystem.setShaderColor(0.0f, 0.9f, 0.0f, 1.0f);
		graphics.drawTexture(FILLED_BAR, windowX, windowY, 0, 0, (int) (128 * (lastHealthValue / maxHealth)), 12, 128, 12);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1.0f);
		graphics.drawTexture(FILLED_BAR, windowX, hungerY, 0, 0, (int) (128 * (hunger / 20f)), 2, 128, 8);

		// cap/tick
		if (lastHealthValue > 0) {
			RenderSystem.setShaderColor(0.6f, 1f, 0.6f, 0.5f);
			graphics.drawTexture(TICK, windowX + ((int) (128 * (lastHealthValue / maxHealth)) - 4), windowY, 0, 0, 4, 12, 4, 12);
		}
		// health amount (in %)
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		graphics.drawShadowedText( renderer, Integer.toString((int) lastHealthValue), windowX + 132, windowY + 2, 0xFFFFFF);
		graphics.drawShadowedText( renderer, "+", windowX - 10, windowY + 2, 0xFFFFFF);

		graphics.drawShadowedText(renderer, "\uD83D\uDEE1", windowX - 11, shieldY, 0xFFFFFF);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

		int armor = player.getArmor();
		renderFillBar(graphics, windowX, shieldY, armor, 0.6f, 0.6f, 1f, true);
		renderFillBar(graphics, windowX, windowY + 14, hunger, 1f, 0.8f, 0f, false);

		// minecraft is broken

		ci.cancel();

	}


}
