package noki.preciousshot.mode;

import static noki.preciousshot.PreciousShotData.PSOption.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.helper.ScreenShotHelper;
import noki.preciousshot.resource.ResourceManager;
import noki.preciousshot.resource.ResourceManager.ShotResource;


public class ModeEventPanorama extends ModeEventShooting {
	
	private int panoramaTimes;
	private String currentFileName;
	private ArrayList<int[]> pixels = new ArrayList<int[]>();
	
	
	@Override
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		
		if(this.enable == false) {
			return;
		}
		super.onRenderTick(event);
		
		switch(event.phase) {
			case START:
				break;
			case END:
				if(this.panoramaTimes == PANORAMA.value()) {
					Minecraft mc = Minecraft.getMinecraft();
					
					Framebuffer buffer = mc.getFramebuffer();
					
					int width = mc.displayWidth;
					int height = mc.displayHeight;
					if(OpenGlHelper.isFramebufferEnabled()) {
						width = buffer.framebufferTextureWidth;
						height = buffer.framebufferTextureHeight;
					}
					int eachWidth = width-LEFT.value()-RIGHT.value();
					int outputWidth = eachWidth*PANORAMA.value();
					int outputHeight = height-TOP.value()-BOTTOM.value();
					
					if(outputWidth < 1 || outputHeight < 1) {
						PreciousShotCore.log("invalid width & height: width/%s, height/%s", width, height); 
						return;
					}
					
					BufferedImage bufferedimage = null;
					if(OpenGlHelper.isFramebufferEnabled()) {
						bufferedimage = new BufferedImage(outputWidth, outputHeight, 1);
						int times = 0;
						for(int[] each: this.pixels) {
							for(int i = TOP.value(); i < height - BOTTOM.value(); i++) {
								for(int j = LEFT.value(); j < width - RIGHT.value(); j++) {
									bufferedimage.setRGB(j-LEFT.value()+times*eachWidth, i-TOP.value(), each[i * width + j]);
								}
							}
							times++;
						}
					}
					
					this.panoramaTimes = 0;
					this.currentFileName = null;
					this.pixels.clear();
					
					File file = null;
					try {
						File directory = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
						directory.mkdir();
						file = ScreenShotHelper.getTimestampedPNGFileForDirectory(directory);
						PreciousShotCore.log("file name is %s.", file.getPath());
						boolean res = ImageIO.write(bufferedimage, "png", file);
						PreciousShotCore.log("res is %s.", String.valueOf(res));
					}
					catch(Exception exception) {
						PreciousShotCore.log("exception: %s", exception.toString());
					}
				}
				break;
		}
		
	}
	
	@Override
	public void render() {
		
		super.render();
		
		if(this.panoramaTimes == 0 || this.currentFileName == null) {
			return;
		}
		
		ShotResource resource = ResourceManager.getResource(this.currentFileName);
		if(resource == null) {
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		double absTop = (TOP.getDouble()/(double)mc.displayHeight) * (double)resolution.getScaledHeight();
		double absBottom = (BOTTOM.getDouble()/(double)mc.displayHeight) * (double)resolution.getScaledHeight();
		double absleft = (LEFT.getDouble()/(double)mc.displayWidth) * (double)resolution.getScaledWidth();
		
		mc.getTextureManager().bindTexture(resource.location);
		Gui.drawScaledCustomSizeModalRect(0, (int)absTop, resource.width-LEFT.value(), 0, LEFT.value(), resource.height,
				(int)absleft, (int)(resolution.getScaledHeight()-absTop-absBottom), resource.width, resource.height);
		
	}
	
	@Override
	public String saveScreenshot(int top, int right, int bottom, int left) {
		
		this.currentFileName = super.saveScreenshot(top, right, bottom, left);
		this.panoramaTimes++;
		int[] target = ScreenShotHelper.getPixels();
		this.pixels.add(Arrays.copyOf(target, target.length));
		ResourceManager.reloadResources();
		return this.currentFileName;
		
	}
	
	@Override
	public void closeMode() {
		
		super.closeMode();
		this.panoramaTimes = 0;
		this.currentFileName = null;
		this.pixels.clear();
		
	}

}
