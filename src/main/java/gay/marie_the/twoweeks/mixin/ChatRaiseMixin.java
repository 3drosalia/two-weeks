package gay.marie_the.twoweeks.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatHud.class)
public class ChatRaiseMixin {
	@ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 6)
	private int chatraisin(int value) {
		return value - 10;
	}
}
