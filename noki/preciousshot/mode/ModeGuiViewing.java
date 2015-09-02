package noki.preciousshot.mode;

import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.helper.RenderHelper;
import noki.preciousshot.helper.TwitterHelper;
import noki.preciousshot.helper.LangHelper.LangKey;
import noki.preciousshot.resource.ResourceManager;
import noki.preciousshot.resource.ResourceManager.ShotResource;


/**********
 * @class ModeGuiViewing
 * @inner_class SettingButton, WithStrButton, GridSetButton, FrameSetButton
 *
 * @description 閲覧モードのGUIです。
 * @descriptoin_en 
 */
public class ModeGuiViewing extends GuiScreen {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String domain = "preciousshot";
	private static final ResourceLocation texture = new ResourceLocation(domain, "textures/gui/settings.png");
	
	private static final int charX = 160;
	private static final int charY = 8;
	
	private static final int menuWidth = 104;
	private static final int menuTop = 5;
	
	private static final int boxWidth = 100;
	private static final int boxHeight = 75;
	private static final int innerBoxWidth = 80;
	private static final int innerBoxHeight = 60;
	
	private ViewingButton openButton;
	private ViewingButton prevButton;
	private ViewingButton nextButton;
	private ViewingButton settingButton;
	
	private ViewingButton tweetButton;
	private ViewingButton returnButton;
	private ViewingButton eachPrevButton;
	private ViewingButton eachNextButton;
	
	private GuiTextField twitterText;
	private ViewingButton twitterSendButton;
	private ViewingButton twitterCancelButton;
	
	private int pageNum;
	private Phase currentPhase;
	private int currentIndex;
	private boolean twitter = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ModeGuiViewing() {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		this.buttonList.clear();
		
		int x = (this.width-menuWidth)/2;
		int y = menuTop;
		
		this.openButton		= new ViewingButton(0,	0+x,	0+y, 16, 16, 32, 64);
		this.prevButton		= new ViewingButton(1,	18+x,	0+y, 16, 16, 32, 96);
		this.nextButton		= new ViewingButton(2,	70+x,	0+y, 16, 16, 48, 96);
		this.settingButton	= new ViewingButton(3,	88+x,	0+y, 16, 16, 64, 96);
		
		this.tweetButton	= new ViewingButton(4,	160+x,	0+y, 16, 16, 80, 96);
		this.returnButton	= new ViewingButton(5,	178+x,	0+y, 16, 16, 96, 96);
		this.eachPrevButton	= new ViewingButton(6,	124+x,	0+y, 16, 16, 32, 96);
		this.eachNextButton	= new ViewingButton(7,	142+x,	0+y, 16, 16, 48, 96);
		
		this.twitterText = new GuiTextField(8, this.mc.fontRendererObj, this.width/2-100, this.height/2, 200, 20);
		this.twitterSendButton = new ViewingButton(9, this.width/2 - 18, this.height/2 + 24, 16, 16, 80, 96);
		this.twitterCancelButton = new ViewingButton(10, this.width/2, this.height/2 + 24, 16, 16, 96, 96);
		
		this.tweetButton.disable();
		this.returnButton.disable();
		this.eachPrevButton.disable();
		this.eachNextButton.disable();
		
		this.twitterSendButton.disable();
		this.twitterCancelButton.disable();
		
		Keyboard.enableRepeatEvents(true);
		this.twitterText.setVisible(false);
		this.twitterText.setEnabled(false);
		this.twitterText.setFocused(false);
		this.twitterText.setMaxStringLength(80);
		this.twitterText.setTextColor(0xffffff);
		
		this.buttonList.add(this.openButton);
		this.buttonList.add(this.prevButton);
		this.buttonList.add(this.nextButton);
		this.buttonList.add(this.settingButton);
		this.buttonList.add(this.tweetButton);
		this.buttonList.add(this.returnButton);
		this.buttonList.add(this.eachPrevButton);
		this.buttonList.add(this.eachNextButton);
		this.buttonList.add(this.twitterSendButton);
		this.buttonList.add(this.twitterCancelButton);
		
		this.pageNum = 0;
		this.currentPhase = Phase.LIST;
		this.currentIndex = 0;
		
		RenderHelper.disableCrosshairs();
		RenderHelper.disableHotbar();
		
		ResourceManager.reloadResources();
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		switch (button.id) {
			case 0:
				File directory = ResourceManager.screenshotsDirectory;
				if(directory.exists()) {
					try {
						Runtime rt = Runtime.getRuntime();
						rt.exec(String.format("cmd.exe /C start \"Open file\" \"%s\"", directory.getAbsolutePath()));
					} catch (IOException ex) {
					}
				}
				break;
			case 1:
				if(this.getMaxPage() == 0) {
					this.pageNum = 1;
				}
				else {
					this.pageNum = (this.pageNum-1);
					if(this.pageNum == -1) {
						this.pageNum = this.getMaxPage()-1;
					}
				}
				break;
			case 2:
				if(this.getMaxPage() == 0) {
					this.pageNum = 1;
				}
				else {
					this.pageNum = (this.pageNum+1) % this.getMaxPage();
				}
				break;
			case 3:
				this.mc.displayGuiScreen(new ModeGuiSetting());
				break;
			case 4:
				PreciousShotCore.log("enter button 4.");
				if(TwitterHelper.isEnable()) {
					PreciousShotCore.log("enter twitter.");
					this.twitter = true;
					this.twitterText.setVisible(true);
					this.twitterText.setEnabled(true);
					this.twitterSendButton.enable();
					this.twitterCancelButton.enable();
				}
				else {
					LangHelper.sendChat(LangKey.TWITTER_DISABLED);
				}
				break;
			case 5:
				currentPhase = Phase.LIST;
				currentIndex = 0;
				this.tweetButton.disable();
				this.returnButton.disable();
				this.eachPrevButton.disable();
				this.eachNextButton.disable();
				this.twitter = false;
				this.twitterText.setVisible(false);
				this.twitterText.setEnabled(false);
				this.twitterSendButton.disable();
				this.twitterCancelButton.disable();
				break;
			case 6:
				if(ResourceManager.getSize() == 0) {
					this.currentIndex = 0;
				}
				else {
					this.currentIndex = (this.currentIndex-1);
					if(this.currentIndex == -1) {
						this.currentIndex = ResourceManager.getSize()-1;
					}
				}
				break;
			case 7:
				if(ResourceManager.getSize() == 0) {
					this.currentIndex = 0;
				}
				else {
					this.currentIndex = (this.currentIndex+1) % ResourceManager.getSize();
				}
				break;
			case 9:
				if(TwitterHelper.isEnable()) {
					ShotResource resource = ResourceManager.getResource(currentIndex);
					if(resource != null) {
						TwitterHelper.tweetMedia(this.twitterText.getText(), resource.file);
						this.twitter = false;
						this.twitterText.setVisible(false);
						this.twitterText.setEnabled(false);
						this.twitterSendButton.disable();
						this.twitterCancelButton.disable();
					}
				}
				else {
					LangHelper.sendChat(LangKey.TWITTER_DISABLED);
				}
				break;
			case 10:
				this.twitter = false;
				this.twitterText.setVisible(false);
				this.twitterText.setEnabled(false);
				this.twitterSendButton.disable();
				this.twitterCancelButton.disable();
			default:
				break;
		}
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		drawRect(0, 0, this.width, this.height, 0x50000000);
		GlStateManager.color(255F, 255F, 255F, 255F);
		
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect((this.width-menuWidth)/2+36, 5, 0, 96, 32, 16);
		
		String str = String.valueOf(this.pageNum+1) + "/" + String.valueOf(this.getMaxPage());
		int adjustLeft = (28 - str.length()*5) / 2;
		this.drawStringFromTexture(str, (this.width-menuWidth)/2+36+adjustLeft, 11);
		
		switch(currentPhase) {
			case LIST:
				int hNum = this.getColNum();
				int vNum = this.getRowNum();
				int left = (this.width - (hNum * boxWidth)) / 2;
				int top = (this.height - (vNum * boxHeight)) / 2;
				
				int numPerPage = hNum * vNum;
				for(int i=0; i<=numPerPage-1; i++) {
					ShotResource resource = ResourceManager.getResource(i+this.pageNum*numPerPage);
					if(resource == null) {
						break;
					}
					this.mc.getTextureManager().bindTexture(resource.location);
					int scaledWidth = innerBoxWidth;
					int scaledHeight = resource.height * innerBoxWidth/ resource.width;
					if(scaledHeight > innerBoxHeight) {
						scaledWidth = resource.width * innerBoxHeight / resource.height;
						scaledHeight = innerBoxHeight;
					}
					int innerLeft = (boxWidth-scaledWidth) / 2;
					int innerTop = (boxHeight-scaledHeight) / 2;
					int col = i % hNum;
					int row = i / hNum;
					drawScaledCustomSizeModalRect(left+col*boxWidth+innerLeft, top+row*boxHeight+innerTop,
							0, 0, resource.width, resource.height, scaledWidth, scaledHeight, resource.width, resource.height);
					
					String name = resource.file.getName();
					if(this.mc.fontRendererObj.getStringWidth(name) > innerBoxWidth) {
						while(this.mc.fontRendererObj.getStringWidth(name+"...") > innerBoxWidth) {
							name = name.substring(0, name.length()-2);
						}
						name = name + "...";
					}
					int strLeft = (int)(((double)innerBoxWidth-(double)this.mc.fontRendererObj.getStringWidth(name)) / 2D);
					this.fontRendererObj.drawString(name, left+col*boxWidth+(boxWidth-innerBoxWidth)/2+strLeft,
							top+row*boxHeight+(boxHeight-innerBoxHeight)/2+innerBoxHeight+1, 0xFFFFFF);
				}
				break;
			case EACH:
				ShotResource resource = ResourceManager.getResource(currentIndex);
				if(resource == null) {
					break;
				}
				double scale = (double)this.width / (double)this.mc.displayWidth;
				double scaledWidth = scale * (double)resource.width;
				double scaledHeight = scale * (double)resource.height;
				if(scaledWidth > (double)this.width*0.8D) {
					scaledHeight = scaledHeight * (double)this.width*0.8D / scaledWidth;
					scaledWidth = (double)this.width*0.8D;
				}
				if(scaledHeight > (double)this.height*0.8D) {
					scaledWidth = scaledWidth * (double)this.height*0.8D / scaledHeight;
					scaledHeight = (double)this.height*0.8D;
				}
				int left2 = (this.width-(int)scaledWidth) / 2;
				int top2 = (this.height-(int)scaledHeight) / 2;
				this.mc.getTextureManager().bindTexture(resource.location);
				drawScaledCustomSizeModalRect(left2, top2, 0, 0,
						resource.width, resource.height, (int)scaledWidth, (int)scaledHeight, resource.width, resource.height);
				
				String name = resource.file.getName();
				if(this.mc.fontRendererObj.getStringWidth(name) > this.width) {
					while(this.mc.fontRendererObj.getStringWidth(name+"...") > this.width) {
						name = name.substring(0, name.length()-2);
					}
					name = name + "...";
				}
				int strLeft = (int)(((double)this.width-(double)this.mc.fontRendererObj.getStringWidth(name)) / 2D);
				this.fontRendererObj.drawString(name, strLeft, top2+(int)scaledHeight+1, 0xFFFFFF);
				
				break;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.twitterText.drawTextBox();
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(this.twitter == true) {
			this.twitterText.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		if(currentPhase == Phase.LIST) {
			int hNum = this.getColNum();
			int vNum = this.getRowNum();
			int left = (this.width - (hNum * boxWidth)) / 2;
			int top = (this.height - (vNum * boxHeight)) / 2;
			if(mouseX-left < 0 || mouseY-top < 0) {
				return;
			}
			
			int posX = (mouseX-left) / boxWidth;
			int posY = (mouseY-top) / boxHeight;
			if(posX > hNum-1 || posY > vNum-1) {
				return;
			}
			
			this.currentIndex = Math.min(posX, hNum) + Math.min(posY, vNum) * this.getColNum() + this.pageNum*hNum*vNum;
			if(ResourceManager.exists(this.currentIndex) == false) {
				return;
			}
			this.currentPhase = Phase.EACH;
			this.tweetButton.enable();
			this.returnButton.enable();
			this.eachPrevButton.enable();
			this.eachNextButton.enable();
		}
		
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(this.twitter == true && this.twitterText.textboxKeyTyped(typedChar, keyCode)) {
			return;
		}
		super.keyTyped(typedChar, keyCode);
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
		
	}
	
	@Override
	public void onGuiClosed() {
		
		RenderHelper.enableCrosshairs();
		RenderHelper.enableHotbar();
//		RenderHelper.recoverOriginal();
		
	}
	
	private void drawStringFromTexture(String string, int x, int y) {
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		
		for(int i=0; i<=string.length()-1; i++) {
			char token = string.charAt(i);
			if(token == "/".charAt(0)) {
				this.drawTexturedModalRect(x+i*5, y, ModeGuiViewing.charX+5*11, ModeGuiViewing.charY, 5, 5);
			}
			else {
				this.drawTexturedModalRect(x+i*5, y,
						ModeGuiViewing.charX+5*Integer.parseInt(""+token), ModeGuiViewing.charY, 5, 5);
			}
		}
		
		GlStateManager.disableBlend();
	}
	
	private int getMaxPage() {
		
		int numPerPage = this.getColNum() * this.getRowNum();
		int ceil = 1;
		int r = ResourceManager.getSize()%numPerPage;
		if(r == 0) {
			ceil = 0;
		}
		return (ResourceManager.getSize()-r)/numPerPage + ceil;
		
	}
	
	private int getColNum() {
		
		return this.width/boxWidth;
		
	}
	
	private int getRowNum() {
		
		return (this.height-boxHeight)/boxHeight;
		
	}
	
	public void setEachView(int index) {
		
		this.currentPhase = Phase.EACH;
		this.currentIndex = index;
		this.tweetButton.enable();
		this.returnButton.enable();
		this.eachPrevButton.enable();
		this.eachNextButton.enable();
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	private class ViewingButton extends GuiButton {
		//*****define member variables.*//
		protected int textureX;
		protected int textureY;
		
		
		//*****define member methods.***//
		public ViewingButton(int buttonID, int x, int y, int width, int height, int textureX, int textureY) {
			super(buttonID, x, y, width, height, "");
			this.textureX = textureX;
			this.textureY = textureY;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			if(this.visible) {
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				
				mc.getTextureManager().bindTexture(ModeGuiViewing.texture);
				this.drawTexturedModalRect(this.xPosition, this.yPosition, this.textureX, this.textureY, this.width, this.height);
				
				GlStateManager.disableBlend();
			}
		}
		
		public void enable() {
			this.enabled = true;
			this.visible = true;
		}
		
		public void disable() {
			this.enabled = false;
			this.visible = false;
		}
	}
	
	private enum Phase {
		
		LIST,
		EACH;
		
	}
	
}
