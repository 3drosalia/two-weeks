package gay.marie_the.twoweeks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.analysis.Value;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class HotBarMixin {

	@ModifyVariable(method = "renderHotbar", at = @At("STORE"), ordinal = 0)
	private int injected(int i) {
		return MinecraftClient.getInstance().getWindow().getScaledWidth() - 116;
	}


	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 1), index = 6)
	private int inject(int value) {
		// selection
		return value + 2;
	}
	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), index = 2)
	private int hyperinjectsupercoolpissbaby2000(int value) {
		// hotbar
		return value - 18;
	}

	@ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 1)
	private int iteminject(int value) {
		// items
		return value - 18;
	}
	@ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"), index = 3)
	private float fuckthisxptext(float value) {
		return value + 23;
	}
	@ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), index = 2)
	private int injectxp(int value) {
		// xp bar
		return value + 23;
	}

	@ModifyVariable(method = "renderStatusBars", at = @At(value = "STORE"), ordinal = 11)
	private int fuck_the_u(int u) {
		// xp bar
		return u - 20;
	}

}
