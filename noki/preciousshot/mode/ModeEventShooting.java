package noki.preciousshot.mode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import noki.preciousshot.asm.F2PressedEvent;
import noki.preciousshot.helper.RenderHelper;
import noki.preciousshot.helper.ScreenShotHelper;
import static noki.preciousshot.PreciousShotData.PSOption.*;


public class ModeEventShooting {
	
	protected boolean bookScreenShot = false;
	protected boolean confirmScreenShot = false;
	protected boolean hideGuiKeep = false;
	protected int counter = 0;
	
	protected boolean enable = false;
	
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		
		if(enable == false) {
			return;
		}
		
		switch (event.phase) {
		case START:		
			break;
		case END:
			if(CONT.isEnable() && ModeManager.keyF2.isKeyDown()) {
				counter = (counter+1) % (CONT.value()-1);
				if(counter == 0) {
					bookScreenShotOnNextTick();
				}
			}
			else {
				counter = 0;
			}
			break;
		}
		
	}
	
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		
		if(this.enable == false) {
			return;
		}
		
		switch(event.phase) {
		case START:
			if(this.bookScreenShot) {
				this.confirmScreenShot = true;
				if(HIDE.isEnable()) {
					Minecraft.getMinecraft().gameSettings.hideGUI = true;
				}
			}
			break;
		case END:
			if(Minecraft.getMinecraft().currentScreen == null && !this.confirmScreenShot) {
				this.render();
			}
			
			if(this.confirmScreenShot) {
				this.saveScreenshot(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value());
				this.bookScreenShot = false;
				this.confirmScreenShot = false;
				Minecraft.getMinecraft().gameSettings.hideGUI = hideGuiKeep;
			}
			break;
		}
				
	}
	
	@SubscribeEvent
	public void onKeyTyped(F2PressedEvent event) {
		
		if(enable == false) {
			return;
		}
		
		if(SHOT.isEnable() == true) {
			if(Minecraft.getMinecraft().gameSettings.hideGUI == true && GRID.value() == 0) {
				this.saveScreenshot(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value());
			}
			else {
				this.bookScreenShotOnNextTick();
			}
		}
		else {
			Minecraft mc = Minecraft.getMinecraft();
			IChatComponent res = net.minecraft.util.ScreenShotHelper.saveScreenshot(
					mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
			mc.ingameGUI.getChatGUI().printChatMessage(res);
		}
		
	}
	
	public void render() {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		mc.entityRenderer.setupOverlayRendering();
		ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		if(MARGIN.isEnable()) {
			RenderHelper.renderMargin(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(),
						resolution.getScaledWidth(), resolution.getScaledHeight());
		}
		if(GRID.value() != 0) {
			RenderHelper.renderGrid(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), 
					resolution.getScaledWidth(), resolution.getScaledHeight(), GRID.value());
		}
		
	}
	
	public void bookScreenShotOnNextTick() {
		
		if(this.enable == false) {
			return;
		}
		
		this.bookScreenShot = true;
		this.hideGuiKeep = Minecraft.getMinecraft().gameSettings.hideGUI;
		
	}
	
	public String saveScreenshot(int top, int right, int bottom, int left) {
		
		return ScreenShotHelper.saveScreenshot(top, right, bottom, left);
		
	}
	
	public void openMode() {
		
		this.enable = true;
		RenderHelper.keepOriginalEffect();
		RenderHelper.applySettingEffect();
		
	}
	
	public void closeMode() {
		
		this.enable = false;
		RenderHelper.recoverOriginalEffect();
		
	}
	
	public boolean isOpen() {
		
		return this.enable;
		
	}
	
}
