package noki.preciousshot.mode;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import noki.preciousshot.PreciousShotData.PSOption;
import noki.preciousshot.asm.F2PressedEvent;
import noki.preciousshot.helper.LangHelper.LangKey;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.helper.RenderHelper;
import noki.preciousshot.helper.ScreenShotHelper;
import noki.preciousshot.helper.RenderHelper.FadeStringRender;
import static noki.preciousshot.PreciousShotData.PSOption.*;


/**********
 * @class ModeEventShooting
 *
 * @description 撮影モードをコントロールするクラスです。各種イベントにより疑似的なGUIになっています。
 * @descriptoin_en 
 */
public class ModeEventShooting {
	
	//******************************//
	// define member variables.
	//******************************//
	protected boolean bookScreenShot = false;
	protected boolean confirmScreenShot = false;
	protected boolean hideGuiKeep = false;
	protected int counter = 0;
	protected ArrayList<FadeStringRender> fadeStrings = new ArrayList<FadeStringRender>();
	protected FadeStringRender fovRender;
	
	protected boolean enable = false;
	
	protected boolean isRightClicked = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	//START: スクショが予約されていた場合、スクショを確約し、描画を消す。
	//END: 各種描画を行う。スクショが確約されていた場合撮影。
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
	
	protected void render() {
		
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
		
		for(FadeStringRender each: this.fadeStrings) {
			each.tick();
/*			if(each.isFinished()) {
				this.fadeStrings.remove(each);
			}*/
		}
		
	}
	
	//START: 右クリック状態を初期化。
	//END: 連写のタイミングに達したら撮影。
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		
		if(enable == false) {
			return;
		}
		
		switch (event.phase) {
			case START:
				this.isRightClicked = false;
				break;
			case END:
				if(CONT.isEnable() && (ModeManager.keyF2.isKeyDown() || this.isRightClicked)) {
					counter = (counter+1) % (CONT.value()-1);
					if(counter == 0) {
						dealScreenshot();
					}
				}
				else {
					counter = 0;
				}
				break;
		}
		
	}
	
	//F2キーを押されたら撮影。
	@SubscribeEvent
	public void onKeyTyped(F2PressedEvent event) {
		
		if(enable == false) {
			return;
		}
		
		this.dealScreenshot();
		
	}
	
	@SubscribeEvent
	public void onMouseInput(MouseEvent event) {
		
		if(this.enable == false) {
			return;
		}
		
		if(!PSOption.CLICK.isEnable()) {
			return;
		}
		
		//右クリックで撮影。
		if(event.button == 1 && event.buttonstate == true) {
			event.setCanceled(true);
			this.isRightClicked = true;
			this.dealScreenshot();
		}
		
		//ホイールでズーム(fov)
		int wheel = Mouse.getEventDWheel();
		if(wheel != 0) {
			if(wheel > 0) {
				FOV.add();
			}
			else if(wheel < 0) {
				FOV.dif();
			}
			RenderHelper.applySettingEffect();
			event.setCanceled(true);
			
			if(fovRender == null) {
				Minecraft mc = Minecraft.getMinecraft();
				ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
				this.fovRender = new FadeStringRender("FOV: "+String.valueOf(FOV.value()), 0xffffff,
						resolution.getScaledWidth()-50, 5, Minecraft.getMinecraft().fontRendererObj, 0, 0, 100, 20);
				this.fadeStrings.add(this.fovRender);
			}
			this.fovRender.text = "FOV: "+String.valueOf(FOV.value());
			this.fovRender.resetPhase();
		}
		
	}
	
	protected void dealScreenshot() {
		
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
	
	public void bookScreenShotOnNextTick() {
		
		if(this.enable == false) {
			return;
		}
		
		this.bookScreenShot = true;
		this.hideGuiKeep = Minecraft.getMinecraft().gameSettings.hideGUI;
		
	}
	
	public String saveScreenshot(int top, int right, int bottom, int left) {
		
		String fileName =  ScreenShotHelper.saveScreenshot(top, right, bottom, left);
		if(PSOption.CHAT.isEnable()) {
			if(fileName != null) {
				LangHelper.sendChatWithViewOpen(LangKey.SHOOTING_DONE, LangKey.SHOOTING_URL, fileName);
			}
			else {
				LangHelper.sendChat(LangKey.SHOOTING_FAILED);
			}
		}
		return fileName;
		
	}
	
	public void openMode() {
		
		this.enable = true;
		RenderHelper.keepOriginalEffect();
		RenderHelper.applySettingEffect();
		this.setFadeStrings();
		
	}
	
	public void setFadeStrings() {
		
		this.fadeStrings.clear();
		
		this.fadeStrings.add(new FadeStringRender(LangKey.SHOOTING_MODE.translated(), 0xffffff, 5, 5,
				Minecraft.getMinecraft().fontRendererObj, 0, 0, 100, 20));
		if(this.fovRender != null) {
			this.fadeStrings.add(this.fovRender);
		}
		
	}
	
	public void closeMode() {
		
		this.enable = false;
		RenderHelper.recoverOriginalEffect();
		
	}
	
	public boolean isOpen() {
		
		return this.enable;
		
	}
	
}
