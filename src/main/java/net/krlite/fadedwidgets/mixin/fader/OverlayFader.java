package net.krlite.fadedwidgets.mixin.fader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.krlite.fadedwidgets.FadedWidgets;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class OverlayFader {
	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	private void tiltHand(MatrixStack matrixStack, Camera camera, float tickDelta, CallbackInfo ci) {
		matrixStack.translate(0, -0.7 * FadedWidgets.fading(), 0);
	}

	@Redirect(
			method = "render",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/toast/ToastManager;draw(Lnet/minecraft/client/gui/DrawContext;)V"
			)
	)
	private void tiltToast(ToastManager toastManager, DrawContext context) {
		MatrixStack modelViewStack = RenderSystem.getModelViewStack();

		modelViewStack.push();
		modelViewStack.translate(160 * FadedWidgets.fading(), 0, 0);
		RenderSystem.applyModelViewMatrix();

		toastManager.draw(context);

		modelViewStack.pop();
		RenderSystem.applyModelViewMatrix();
	}
}

@Mixin(ToastManager.class)
class ToastFader {
	@Inject(method = "draw", at = @At("HEAD"))
	private void setOpacity(DrawContext context, CallbackInfo ci) {
		FadedWidgets.setShaderAlpha(context);
	}
}
