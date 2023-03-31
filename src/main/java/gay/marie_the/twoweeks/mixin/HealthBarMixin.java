package gay.marie_the.twoweeks.mixin;

import com.mojang.blaze3d.glfw.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class HealthBarMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private int lastHealthValue;

	@Shadow
	public abstract void tick(boolean paused);

	@Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
	public void HealthBarRenderer(
		MatrixStack matrices,
		PlayerEntity player,
		int x,
		int y,
		int lines,
		int regeneratingHeartIndex,
		float maxHealth,
		int lastHealth,
		int health,
		int absorption,
		boolean blinking,
		CallbackInfo ci) {

		int hunger = player.getHungerManager().getFoodLevel();
		// int width = clientWindow.getScaledWidth();

		// health percent
		double hpercent = 100 * (lastHealthValue / maxHealth);
		// health number
		double displayNum = MathHelper.ceil(hpercent);

		final Identifier FILLED_BAR = new Identifier("twoweeks", "textures/full.png");
		final Identifier BLANK_BAR = new Identifier("twoweeks", "textures/empty.png");
		final Identifier TICK = new Identifier("twoweeks", "textures/tick.png");
		final Identifier SMALL_TICK = new Identifier("twoweeks", "textures/smalltick.png");

		if (client != null){
			Window clientWindow = MinecraftClient.getInstance().getWindow();
			int height = clientWindow.getScaledHeight();
			x = 30;
			y = height - 30;
		}

		int shieldY = y - 10;
		int hungerY = shieldY - 4;
		// Empty health bar
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, FILLED_BAR);

		// shadow
		// TODO : make it not use the fucking empty healthbar background you fucking idiot
		float R = 0.0f;
		float G = 0.0f;
		float B = 0.0f;
		float percent = 100 * lastHealthValue / maxHealth;
		if (percent == 0) {
			R = 0.0f;
		} else if (percent <= 10){
			R = 1f;
		} else if (percent <= 15) {
			R = 0.75f;
		} else if (percent <= 30) {
			R = 0.5f;
		} else if (percent <= 60) {
			R = 0.25f;
		} else if (percent <= 75) {
			R = 0.1f;
		} else if (percent > 75){
			R = 0;
			G = 0;
			B = 0;
		}

		// how the FUCK do you switch case

		/* commented out shadow

		RenderSystem.setShaderTexture(0, BLANK_BAR);
		RenderSystem.setShaderColor(R,G,B,0.5f);
		DrawableHelper.drawTexture(matrices, x+1, y+1, 0, 0, 128, 12, 128, 12);

		*/

		// empty bar

		RenderSystem.setShaderTexture(0, FILLED_BAR);
		RenderSystem.setShaderColor(R,G,B,0.25f);
		// health bar
		DrawableHelper.drawTexture(matrices, x, y, 0, 0, 128, 12, 128, 12);
		RenderSystem.setShaderColor(0f,0f,0f,0.25f);
		// shield bar
		DrawableHelper.drawTexture(matrices, x, shieldY, 0, 0, 128, 8, 128, 8);

		// hunger bar
		DrawableHelper.drawTexture(matrices, x, hungerY, 0, 0, 128, 2, 128, 8);

		// todo : move back
		// health change bar
		if (blinking) {
			RenderSystem.setShaderColor(1f, 0f, 0f, 1f);
			DrawableHelper.drawTexture(matrices, x, y, 0, 0, (int) (128 * (health / maxHealth)), 12, 128, 12);
		}
		// health fill
		RenderSystem.setShaderColor(0.0f,0.9f,0.0f, 1.0f);
		DrawableHelper.drawTexture(matrices, x, y,0,0, (int) (128 * (lastHealthValue / maxHealth)),12,128,12);
		RenderSystem.setShaderColor(1f,1f,1f, 1.0f);
		DrawableHelper.drawTexture(matrices, x, hungerY, 0, 0, (int) (128 * (hunger / 20f)), 2, 128, 8);

		// cap/tick
		if (lastHealthValue > 0) {
			RenderSystem.setShaderTexture(0, TICK);
			RenderSystem.setShaderColor(0.6f, 1f, 0.6f, 0.5f);
			DrawableHelper.drawTexture(matrices, x + ((int) (128 * (lastHealthValue / maxHealth)) - 4), y, 0, 0, 4, 12, 4, 12);
		}
		// health amount (in %)
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		DrawableHelper.drawStringWithShadow(matrices, renderer, Integer.toString((int) lastHealthValue), x + 132, y + 2, 0xFFFFFF);
		DrawableHelper.drawStringWithShadow(matrices, renderer, "+", x - 10, y + 2, 0xFFFFFF);

		int armor = player.getArmor();



		RenderSystem.setShaderTexture(0, FILLED_BAR);

		RenderSystem.setShaderColor( 0.0f, 0.0f, 0.0f, 1.0f );



		RenderSystem.setShaderColor(0.7f , 0.7f, 1.0f, 0.5f);
		DrawableHelper.drawTexture(matrices, x, shieldY, 0,0, (int) (128 * (armor / 20f)),8,128,12);

		DrawableHelper.drawStringWithShadow(matrices, renderer, Integer.toString((int) armor), x + 132, shieldY, 0xFFFFFF);
		DrawableHelper.drawStringWithShadow(matrices, renderer, "\uD83D\uDEE1", x - 11, shieldY, 0xFFFFFF);
		/*		   _____________________
				+ |___________________\| 100
		 */
		if (armor > 0) {
			RenderSystem.setShaderTexture(0, SMALL_TICK);
			RenderSystem.setShaderColor(0.9f, 0.9f, 1f, 1f);
			DrawableHelper.drawTexture(matrices, x + ((int) (128 * (armor / 20f)) - 4), shieldY, 0, 0, 4, 8, 4, 8);
		}
		ci.cancel();
	}


}
