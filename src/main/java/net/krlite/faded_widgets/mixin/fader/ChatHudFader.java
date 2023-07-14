package net.krlite.faded_widgets.mixin.fader;

import net.krlite.faded_widgets.FadedWidgets;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChatHud.class)
public class ChatHudFader {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
	private void setOpacity(MatrixStack matrixStack, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
		DrawableHelper.fill(matrixStack, xBegin, yBegin, xEnd, yEnd, FadedWidgets.getColor(color));
	}

	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"
			)
	)
	private int setTextOpacity(TextRenderer textRenderer, MatrixStack matrixStack, OrderedText text, float x, float y, int color) {
		return textRenderer.drawWithShadow(matrixStack, text, x, y, FadedWidgets.getTextColor(color));
	}

	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
			)
	)
	private int setTextOpacityOrdered(TextRenderer textRenderer, MatrixStack matrixStack, Text text, float x, float y, int color) {
		return textRenderer.drawWithShadow(matrixStack, text, x, y, FadedWidgets.getTextColor(color));
	}
}
