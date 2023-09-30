package gay.marie_the.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static gay.marie_the.logic.HealthBarRenderer.renderFillBar;

@Mixin(InGameHud.class)
public abstract class HealthBarMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private int lastHealthValue;

	@Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
	public void HealthBarRenderer(
			DrawContext graphics, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

		int windowX;
		int windowY;
		int hunger = player.getHungerManager().getFoodLevel();
		int armor = player.getArmor();

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

		// 1 is health, 2 is armor, 3 is hunger

		// Health
		renderFillBar(graphics, windowX, windowY, lastHealth, 0.3f, 0.9f, 0.3f, false, maxHealth, health, 1);

		// Armor
		renderFillBar(graphics, windowX, shieldY, armor, 0.6f, 0.6f, 1f, true, 20, 0,2);

		// Hunger
		renderFillBar(graphics, windowX, windowY + 14, hunger, 1f, 0.8f, 0f, false, 20,0,3);

		ci.cancel();
	}
}