package noki.preciousshot.mode;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noki.preciousshot.PreciousShotData;
import noki.preciousshot.PreciousShotData.FrameSet;
import noki.preciousshot.PreciousShotData.PSOption;
import noki.preciousshot.helper.RenderHelper;
import static noki.preciousshot.PreciousShotData.PSOption.*;


/**********
 * @class ModeGuiSetting
 * @inner_class SettingButton, WithStrButton, GridSetButton, FrameSetButton
 *
 * @description 設定モードのGUIです。
 * @descriptoin_en 
 */
public class ModeGuiSetting extends GuiScreen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String domain = "preciousshot";
	private static final ResourceLocation texture = new ResourceLocation(domain, "textures/gui/settings.png");
	private static final int charX = 160;
	private static final int charY = 8;
	
	private static final int menuWidth = 304;
	private static final int menuTop = 5;
	
	private int counter;
	private boolean topMoving = false;
	private boolean rightMoving = false;
	private boolean bottomMoving = false;
	private boolean leftMoving = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ModeGuiSetting() {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		this.buttonList.clear();
		int x = (this.width-menuWidth)/2;
		int y = menuTop;
		
		this.buttonList.add(new SettingButton(0,	18+x,	8+y,	16,	8,	128,8,	GAMMA));
		this.buttonList.add(new SettingButton(1,	18+x,	0+y,	16,	8,	128,0,	GAMMA));
		this.buttonList.add(new WithStrButton(2,	34+x,	0+y,	38,	16,	16,	32,	GAMMA,	16, 2, "", ""));
		this.buttonList.add(new SettingButton(3,	72+x,	8+y,	16,	8,	144,8,	GAMMA));
		this.buttonList.add(new SettingButton(4,	72+x,	0+y,	16,	8,	144,0,	GAMMA));
		
		this.buttonList.add(new SettingButton(5,	90+x,	8+y,	16,	8,	128,8,	FOV));
		this.buttonList.add(new SettingButton(6,	90+x,	0+y,	16,	8,	128,0,	FOV));
		this.buttonList.add(new WithStrButton(7,	106+x,	0+y,	38,	16,	54,	32,	FOV,	16, 2, "", ""));
		this.buttonList.add(new SettingButton(8,	144+x,	8+y,	16,	8,	144,8,	FOV));
		this.buttonList.add(new SettingButton(9,	144+x,	0+y,	16,	8,	144,0,	FOV));
		
		this.buttonList.add(new SettingButton(10,	162+x,	8+y,	16,	8,	128,8,	CONT));
		this.buttonList.add(new SettingButton(11,	162+x,	0+y,	16,	8,	128,0,	CONT));
		this.buttonList.add(new WithStrButton(12,	178+x,	0+y,	38,	16,	92,	32,	CONT,	16, 2, "/", ""));
		this.buttonList.add(new SettingButton(13,	216+x,	8+y,	16,	8,	144,8,	CONT));
		this.buttonList.add(new SettingButton(14,	216+x,	0+y,	16,	8,	144,0,	CONT));
		
		this.buttonList.add(new SettingButton(15,	234+x,	8+y,	16,	8,	128,8,	PANORAMA));
		this.buttonList.add(new SettingButton(16,	234+x,	0+y,	16,	8,	128,0,	PANORAMA));
		this.buttonList.add(new WithStrButton(17,	250+x,	0+y,	38,	16,	130,32,	PANORAMA, 16, 2, "x", ""));
		this.buttonList.add(new SettingButton(18,	288+x,	8+y,	16,	8,	144,8,	PANORAMA));
		this.buttonList.add(new SettingButton(19,	288+x,	0+y,	16,	8,	144,0,	PANORAMA));
		
		this.buttonList.add(new GridSetButton(20,	0+x,	18+y,	16,	16,	0,	0,	GRID));
		
		this.buttonList.add(new SettingButton(21,	18+x,	18+y,	16,	16,	16,	0,	HIDE));
		this.buttonList.add(new SettingButton(22,	36+x,	18+y,	16,	16,	32,	0,	SHOT));
		this.buttonList.add(new SettingButton(23,	54+x,	18+y,	16,	16,	48,	0,	MARGIN));
		this.buttonList.add(new SettingButton(24,	72+x,	18+y,	16,	16,	48,	64,	CLICK));
		this.buttonList.add(new SettingButton(25,	90+x,	18+y,	16,	16,	16,	64,	NIGHT));
		this.buttonList.add(new SettingButton(26,	108+x,	18+y,	16,	16,	64,	64,	CHAT));
		
		this.buttonList.add(new SettingButton(27,	126+x,	18+y,	16,	16,	64,	0,	OTHER));
		this.buttonList.add(new SettingButton(28,	144+x,	18+y,	16,	16,	80,	0,	OTHER));
		this.buttonList.add(new SettingButton(29,	162+x,	18+y,	16,	16,	96,	0,	OTHER));
		this.buttonList.add(new SettingButton(30,	180+x,	18+y,	16,	16,	112,0,	OTHER));
		
		this.buttonList.add(new FrameSetButton(31,198+x,	18+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotData.frameSet1));
		this.buttonList.add(new FrameSetButton(32,198+x,	27+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotData.frameSet2));
		this.buttonList.add(new FrameSetButton(33,252+x,	18+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotData.frameSet3));
		this.buttonList.add(new FrameSetButton(34,252+x,	27+y,	52,	7,	160,0,	OTHER,	0, 0, "", "", PreciousShotData.frameSet4));
		
		this.buttonList.add(new SettingButton(35,	0+x,	0+y,	16,	16,	32,	64,	OTHER));
		
		RenderHelper.keepOriginalEffect();
		RenderHelper.applySettingEffect();
		
		RenderHelper.disableCrosshairs();
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		SettingButton target = (SettingButton)button;
		switch(button.id) {
			case 0: case 5: case 10: case 15:
				if(target.option.isEnable()) target.option.dif();
				break;
			case 1: case 6: case 11: case 16:
				if(target.option.isEnable()) target.option.dif(10);
				break;
			case 3: case 8: case 13: case 18: case 20:
				if(target.option.isEnable()) target.option.add();
				break;
			case 4: case 9: case 14: case 19:
				if(target.option.isEnable()) target.option.add(10);
				break;
			case 2: case 7: case 12: case 17: case 21: case 22: case 23: case 24: case 25: case 26:
				target.option.switchEnable();
				break;
			case 27:
				int margin = LEFT.value() + RIGHT.value();
				int newLeft = margin - margin/2;
				int newRight = margin - newLeft;
				LEFT.set(newLeft);
				RIGHT.set(newRight);
				break;
			case 28:
				int margin2 = TOP.value() + BOTTOM.value();
				int newTop = margin2 - margin2/2;
				int newBottom = margin2 - newTop;
				TOP.set(newTop);
				BOTTOM.set(newBottom);
				break;
			case 29:
				int newWidth = Math.min(this.mc.displayHeight - TOP.value() - BOTTOM.value(), this.mc.displayWidth);
				int newHeight = Math.min(this.mc.displayWidth - LEFT.value() - RIGHT.value(), this.mc.displayHeight);
				int newLeft2 = (this.mc.displayWidth-newWidth) / 2;
				int newRight2 =(this.mc.displayWidth-newWidth) - newLeft2;
				int newTop2 = (this.mc.displayHeight-newHeight) / 2;
				int newBottom2 = (this.mc.displayHeight-newHeight) - newTop2;
				LEFT.set(newLeft2);
				RIGHT.set(newRight2);
				TOP.set(newTop2);
				BOTTOM.set(newBottom2);
				break;
			case 30:
				LEFT.set(0);
				RIGHT.set(0);
				TOP.set(0);
				BOTTOM.set(0);
				break;
			case 31: case 32: case 33: case 34:
				FrameSetButton setButton = (FrameSetButton)target;
				int newLeft3 = (this.mc.displayWidth-setButton.frameSet.width) / 2;
				int newRight3 =(this.mc.displayWidth-setButton.frameSet.width) - newLeft3;
				int newTop3 = (this.mc.displayHeight-setButton.frameSet.height) / 2;
				int newBottom3 = (this.mc.displayHeight-setButton.frameSet.height) - newTop3;
				LEFT.set(newLeft3);
				RIGHT.set(newRight3);
				TOP.set(newTop3);
				BOTTOM.set(newBottom3);
				break;
			case 35:
				this.mc.displayGuiScreen(new ModeGuiViewing());
				break;
			default:
		}
		
		if(0 <= button.id && button.id <= 9) {
			RenderHelper.applySettingEffect();
		}
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		RenderHelper.renderMargin(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), this.width, this.height);
		
		this.counter = (this.counter+1)%20;
		RenderHelper.renderBorder(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), this.width, this.height, this.counter<=10 ? 0 : 1);
		
		if(GRID.value() != 0) {
			RenderHelper.renderGrid(TOP.value(), RIGHT.value(), BOTTOM.value(), LEFT.value(), this.width, this.height, GRID.value());
		}
		
		int centerX = this.width/2;
		int centerY = this.height/2;
		String strT = "T: "+String.valueOf(TOP.value());
		String strL = "L: "+String.valueOf(RIGHT.value());
		String strB = "B: "+String.valueOf(BOTTOM.value());
		String strR = "R: "+String.valueOf(LEFT.value());
		String strW = "W: "+String.valueOf(this.mc.displayWidth-LEFT.value()-RIGHT.value());
		String strH = "H: "+String.valueOf(this.mc.displayHeight-TOP.value()-BOTTOM.value());
		int adjustLeft = Math.max(this.fontRendererObj.getStringWidth(strL), this.fontRendererObj.getStringWidth(strR));
		adjustLeft = Math.max(adjustLeft, this.fontRendererObj.getStringWidth(strW));
		this.fontRendererObj.drawString(strL, centerX-adjustLeft-2, centerY-10, 0xFFFFFF);
		this.fontRendererObj.drawString(strR, centerX-adjustLeft-2, centerY, 0xFFFFFF);
		this.fontRendererObj.drawString(strW, centerX-adjustLeft-2, centerY+10, 0xFFFFFF);
		this.fontRendererObj.drawString(strT, centerX+1, centerY-10, 0xFFFFFF);
		this.fontRendererObj.drawString(strB, centerX+1, centerY, 0xFFFFFF);
		this.fontRendererObj.drawString(strH, centerX+1, centerY+10, 0xFFFFFF);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		int absX = (int)Math.round((double)mouseX/(double)this.width * (double)this.mc.displayWidth);
		int absY = (int)Math.round((double)mouseY/(double)this.height * (double)this.mc.displayHeight);
		
		if((LEFT.value() <= absX && absX <= LEFT.value()+4)
				&& (TOP.value() <= absY && absY <= this.mc.displayHeight-BOTTOM.value())) {
			this.leftMoving = true;
		}
		if((LEFT.value() <= absX && absX <= this.mc.displayWidth-RIGHT.value())
				&& (TOP.value() <= absY && absY <= TOP.value()+4)) {
			this.topMoving = true;
		}
		if((this.mc.displayWidth-RIGHT.value()-4 <= absX && absX <= this.mc.displayWidth-RIGHT.value())
				&& (TOP.value() <= absY && absY <= this.mc.displayHeight-BOTTOM.value())) {
			this.rightMoving = true;
		}
		if((LEFT.value() <= absX && absX <= this.mc.displayWidth-RIGHT.value())
				&& (this.mc.displayHeight-BOTTOM.value()-4 <= absY && absY <= this.mc.displayHeight-BOTTOM.value())) {
			this.bottomMoving = true;
		}
		
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
		this.leftMoving = false;
		this.topMoving = false;
		this.rightMoving = false;
		this.bottomMoving = false;
		
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		
		int absX = (int)Math.round((double)mouseX/(double)this.width * (double)this.mc.displayWidth);
		int absY = (int)Math.round((double)mouseY/(double)this.height * (double)this.mc.displayHeight);
		
		if(this.leftMoving == true) {
			LEFT.set(Math.min(absX, this.mc.displayWidth-RIGHT.value()-10));
		}
		if(this.topMoving == true) {
			TOP.set(Math.min(absY, this.mc.displayHeight-BOTTOM.value()-10));
		}
		if(this.rightMoving == true) {
			RIGHT.set(Math.min(this.mc.displayWidth-absX, this.mc.displayWidth-LEFT.value()-10));
		}
		if(this.bottomMoving == true) {
			BOTTOM.set(Math.min(this.mc.displayHeight-absY, this.mc.displayHeight-TOP.value()-10));
		}
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(keyCode == 1 || keyCode == PreciousShotData.keyNum) {
			this.mc.displayGuiScreen((GuiScreen)null);
			if(this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
			return;
		}
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	@Override
	public void onGuiClosed() {
		
		RenderHelper.recoverOriginalEffect();
		RenderHelper.enableCrosshairs();
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	private class SettingButton extends GuiButton {
		//*****define member variables.*//
		protected int textureX;
		protected int textureY;
		public PSOption option;
		
		
		//*****define member methods.***//
		public SettingButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY, PSOption option) {
			super(buttonID, x, y, width, height, "");
			this.textureX = textureX;
			this.textureY = textureY;
			this.option = option;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			this.drawButtonBasic(mc, mouseX, mouseY);
		}
		
		protected void drawButtonBasic(Minecraft mc, int mouseX, int mouseY) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			
			mc.getTextureManager().bindTexture(ModeGuiSetting.texture);
			int y = this.textureY;
			if(this.option.isEnable() == false) {
				y += 16;
			}
			this.drawTexturedModalRect(this.xPosition, this.yPosition,
					this.textureX, y, this.width, this.height);
			
			GlStateManager.disableBlend();
		}
	}
	
	private class WithStrButton extends SettingButton {
		//*****define member variables.*//
		private int adjustLeft;
		private int adjustRight;
		private String prefix;
		private String suffix;
		
		
		//*****define member methods.***//
		public WithStrButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY,
				PSOption option, int adjustLeft, int adjustRight, String prefix, String suffix) {
			super(buttonID, x, y, width, height, textureX, textureY, option);
			this.adjustLeft = adjustLeft;
			this.prefix = prefix;
			this.suffix = suffix;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			this.drawButtonBasic(mc, mouseX, mouseY);
			this.drawStringFromTexture(this.prefix+this.option.value()+this.suffix);
		}
		
		protected void drawStringFromTexture(String string) {			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			
			int strWidth = string.length()*5;
			int left = (this.width - this.adjustLeft - this.adjustRight- strWidth)/2 + this.adjustLeft;
			int top = (this.height - 5)/2;
			for(int i=0; i<=string.length()-1; i++) {
				char token = string.charAt(i);
				int disable = 0;
				if(this.option.isEnable() == false) {
					disable = 5;
				}
				if(token == "x".charAt(0)) {
					this.drawTexturedModalRect(this.xPosition+left+i*5, this.yPosition+top,
							ModeGuiSetting.charX+5*10, ModeGuiSetting.charY+disable, 5, 5);
				}
				else if(token == "/".charAt(0)) {
					this.drawTexturedModalRect(this.xPosition+left+i*5, this.yPosition+top,
							ModeGuiSetting.charX+5*11, ModeGuiSetting.charY+disable, 5, 5);
				}
				else {
					this.drawTexturedModalRect(this.xPosition+left+i*5, this.yPosition+top,
							ModeGuiSetting.charX+5*Integer.parseInt(""+token), ModeGuiSetting.charY+disable, 5, 5);
				}
			}
			
			GlStateManager.disableBlend();
		}
	}
	
	private class GridSetButton extends SettingButton {
		//*****define member methods.***//
		public GridSetButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY, PSOption option) {
			super(buttonID, x, y, width, height, textureX, textureY, option);
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			
			mc.getTextureManager().bindTexture(ModeGuiSetting.texture);
			this.drawTexturedModalRect(this.xPosition, this.yPosition,
					this.textureX, this.option.value()*16 + this.textureY, this.width, this.height);
			
			GlStateManager.disableBlend();
		}
	}
	
	private class FrameSetButton extends WithStrButton {
		//*****define member variables.*//
		public FrameSet frameSet;
		
		
		//*****define member methods.***//
		public FrameSetButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY, PSOption option,
				int adjustLeft, int adjustRight, String prefix, String suffix, FrameSet frameSet) {
			super(buttonID, x, y, width, height, textureX, textureY, option, adjustLeft, adjustRight, prefix, suffix);
			this.frameSet = frameSet;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			this.drawButtonBasic(mc, mouseX, mouseY);
			this.drawStringFromTexture(this.frameSet.width+"x"+this.frameSet.height);
		}
	}
	
}
