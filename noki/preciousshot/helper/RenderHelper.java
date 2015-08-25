package noki.preciousshot.helper;


import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.client.GuiIngameForge;
import noki.preciousshot.PreciousShotData.PSOption;
import static noki.preciousshot.PreciousShotData.PSOption.*;


public class RenderHelper {
	
	public static float originalGamma;
	public static float originalFov;
	
	public static void renderMargin(int top, int right, int bottom, int left, int dispWidth, int dispHeight) {
		
		Minecraft mc = Minecraft.getMinecraft();
		
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if(PSOption.NIGHT.isEnable()) {
        	GlStateManager.color(255F, 255F, 255F, 80F/255F);
        }
        else {
        	GlStateManager.color(0F, 0F, 0F, 80F/255F);
        }
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		
		double absTop = (TOP.getDouble()/(double)mc.displayHeight) * (double)dispHeight;
		double absRight = (RIGHT.getDouble()/(double)mc.displayWidth) * (double)dispWidth;
		double absBottom = (BOTTOM.getDouble()/(double)mc.displayHeight) * (double)dispHeight;
		double absleft = (LEFT.getDouble()/(double)mc.displayWidth) * (double)dispWidth;
		
		renderer.startDrawingQuads();
		placeRect(renderer, 0, 0, dispWidth, absTop);
		placeRect(renderer, 0, (double)dispHeight-absBottom, dispWidth, dispHeight);
		placeRect(renderer, 0, absTop, absleft, (double)dispHeight-absBottom);
		placeRect(renderer, (double)dispWidth-absRight, absTop, dispWidth, (double)dispHeight-absBottom);
		tessellator.draw();
		
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
		
	}
	
	public static void placeRect(WorldRenderer renderer, double left, double top, double right, double bottom) {
		
		renderer.addVertex((double)left, (double)bottom, 0D);
		renderer.addVertex((double)right, (double)bottom, 0D);
		renderer.addVertex((double)right, (double)top, 0D);
		renderer.addVertex((double)left, (double)top, 0D);
		
	}
	
	public static void renderBorder(int top, int right, int bottom, int left, int dispWidth, int dispHeight, int type) {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		double absTop = (TOP.getDouble()/(double)mc.displayHeight) * (double)dispHeight;
		double absRight = (RIGHT.getDouble()/(double)mc.displayWidth) * (double)dispWidth;
		double absBottom = (BOTTOM.getDouble()/(double)mc.displayHeight) * (double)dispHeight;
		double absLeft = (LEFT.getDouble()/(double)mc.displayWidth) * (double)dispWidth;
		
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GL11.glEnable(GL11.GL_LINE_STIPPLE);
		if(type == 0) {
			GL11.glLineStipple(1 , (short)0xF0F0);
		}
		else {
			GL11.glLineStipple(1 , (short)0x0F0F);
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		
		renderer.startDrawing(GL11.GL_LINES);
		GL11.glLineWidth(1);
		
		placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, absTop);
		placeLine(renderer, (double)dispWidth-absRight, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
		placeLine(renderer, (double)dispWidth-absRight, (double)dispHeight-absBottom, absLeft, (double)dispHeight-absBottom);
		placeLine(renderer, absLeft, (double)dispHeight-absBottom, absLeft, absTop);
		
		tessellator.draw();
		
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
		GL11.glDisable(GL11.GL_LINE_STIPPLE);

	}
	
	public static void renderGrid(int top, int right, int bottom, int left, int dispWidth, int dispHeight, int type) {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		double absTop = (TOP.getDouble()/(double)mc.displayHeight) * (double)dispHeight;
		double absRight = (RIGHT.getDouble()/(double)mc.displayWidth) * (double)dispWidth;
		double absBottom = (BOTTOM.getDouble()/(double)mc.displayHeight) * (double)dispHeight;
		double absLeft = (LEFT.getDouble()/(double)mc.displayWidth) * (double)dispWidth;
		double absWidth = dispWidth - absRight - absLeft;
		double absHeight = dispHeight - absTop - absBottom;
		
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		
		renderer.startDrawing(GL11.GL_LINES);
		GL11.glLineWidth(1);
		
		switch(type) {
			case 1://2 by 2
				placeLine(renderer, absLeft+absWidth/2, absTop, absLeft+absWidth/2, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop+absHeight/2, (double)dispWidth-absRight, absTop+absHeight/2);
				break;
			case 2://3 by 3
				placeLine(renderer, absLeft+absWidth/3D*1D, absTop, absLeft+absWidth/3D*1D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft+absWidth/3D*2D, absTop, absLeft+absWidth/3D*2D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop+absHeight/3D*1D, (double)dispWidth-absRight, absTop+absHeight/3D*1D);
				placeLine(renderer, absLeft, absTop+absHeight/3D*2D, (double)dispWidth-absRight, absTop+absHeight/3D*2D);
				break;
			case 3://diagonal
				placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
				placeLine(renderer, (double)dispWidth-absRight, absTop, absLeft, (double)dispHeight-absBottom);
				break;
			case 4://3 by 3 & diagonal
				placeLine(renderer, absLeft+absWidth/3D*1D, absTop, absLeft+absWidth/3D*1D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft+absWidth/3D*2D, absTop, absLeft+absWidth/3D*2D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop+absHeight/3D*1D, (double)dispWidth-absRight, absTop+absHeight/3D*1D);
				placeLine(renderer, absLeft, absTop+absHeight/3D*2D, (double)dispWidth-absRight, absTop+absHeight/3D*2D);
				placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
				placeLine(renderer, (double)dispWidth-absRight, absTop, absLeft, (double)dispHeight-absBottom);
				break;
			case 5://railman
				placeLine(renderer, absLeft+absWidth/3D*1D, absTop, absLeft+absWidth/3D*1D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft+absWidth/3D*2D, absTop, absLeft+absWidth/3D*2D, (double)dispHeight-absBottom);
				placeLine(renderer, absLeft, absTop, (double)dispWidth-absRight, (double)dispHeight-absBottom);
				placeLine(renderer, (double)dispWidth-absRight, absTop, absLeft, (double)dispHeight-absBottom);
				break;
			default:
		}
		
		tessellator.draw();
		
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
		
	}
	
	public static void placeLine(WorldRenderer renderer, double x1, double y1, double x2, double y2) {
		
		renderer.addVertex((double)x1, (double)y1, 0D);
		renderer.addVertex((double)x2, (double)y2, 0D);
		
	}
	
	public static void keepOriginalEffect() {
		
		originalGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
		originalFov = Minecraft.getMinecraft().gameSettings.fovSetting;
		
	}
	
	public static void applySettingEffect() {
		
		if(GAMMA.isEnable()) {
			Minecraft.getMinecraft().gameSettings.gammaSetting = GAMMA.getFloat();
		}
		else {
			Minecraft.getMinecraft().gameSettings.gammaSetting = originalGamma;
		}
		
		if(FOV.isEnable()) {
			Minecraft.getMinecraft().gameSettings.fovSetting = FOV.getFloat();
		}
		else {
			Minecraft.getMinecraft().gameSettings.fovSetting = originalFov;
		}
		
	}
	
	public static void recoverOriginalEffect() {
		
		Minecraft.getMinecraft().gameSettings.gammaSetting = originalGamma;
		Minecraft.getMinecraft().gameSettings.fovSetting = originalFov;
		
	}
	
	public static void enableCrosshairs() {
		
		GuiIngameForge.renderCrosshairs = true;
		
	}
	
	public static void disableCrosshairs() {
		
		GuiIngameForge.renderCrosshairs = false;
		
	}
	
	public static void enableHotbar() {
		
		GuiIngameForge.renderHotbar = true;
		
	}
	
	public static void disableHotbar() {
		
		GuiIngameForge.renderHotbar = false;
		
	}
	
}
