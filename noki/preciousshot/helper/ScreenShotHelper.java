package noki.preciousshot.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import noki.preciousshot.PreciousShotCore;


/**********
 * @class ScreenShotHelper
 *
 * @description スクリーンショットを保存するためのクラスです。
 * @descriptoin_en 
 */
public class ScreenShotHelper {
	
	//******************************//
	// define member variables.
	//******************************//
	private static IntBuffer pixelBuffer;
	private static int[] pixelValues;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static String saveScreenshot(int top, int right, int bottom, int left) {
		
		PreciousShotCore.log("enter screenshot.");
		
		Minecraft mc = Minecraft.getMinecraft();
		
		Framebuffer buffer = mc.getFramebuffer();
		
		int width = mc.displayWidth;
		int height = mc.displayHeight;
		if(OpenGlHelper.isFramebufferEnabled()) {
			width = buffer.framebufferTextureWidth;
			height = buffer.framebufferTextureHeight;
		}
		int outputWidth = width - left - right;
		int outputHeight = height - top - bottom;
		
		if(outputWidth < 1 || outputHeight < 1) {
			PreciousShotCore.log("invalid width & height: width/%s, height/%s", width, height); 
			return null;
		}
		
		int k = width * height;
		if(pixelBuffer == null || pixelBuffer.capacity() < k) {
			pixelBuffer = BufferUtils.createIntBuffer(k);
			pixelValues = new int[k];
		}
		
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		pixelBuffer.clear();
		
		if(OpenGlHelper.isFramebufferEnabled()) {
			GlStateManager.bindTexture(buffer.framebufferTexture);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
		}
		else{
			GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
		}
		
		pixelBuffer.get(pixelValues);
		TextureUtil.processPixelValues(pixelValues, width, height);
		BufferedImage bufferedimage = null;
		
		if(OpenGlHelper.isFramebufferEnabled()) {
			bufferedimage = new BufferedImage(outputWidth, outputHeight, 1);
			for(int i = top; i < height - bottom; i++) {
				for(int j = left; j < width - right; j++) {
					bufferedimage.setRGB(j-left, i-top, pixelValues[i * width + j]);
				}
			}
		}
		else {
			bufferedimage = new BufferedImage(width, height, 1);
			bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
		}
		
		File file = null;
		try {
			File directory = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
			directory.mkdir();
			file = getTimestampedPNGFileForDirectory(directory);
			PreciousShotCore.log("file name is %s.", file.getPath());
			boolean res = ImageIO.write(bufferedimage, "png", file);
			PreciousShotCore.log("res is %s.", String.valueOf(res));
		}
		catch(Exception exception) {
			PreciousShotCore.log("exception: %s", exception.toString());
		}
		
		/*SSSCore.log("finish getting pixels.");
		
		Thread thread = new ScreenShotThread(bufferedimage);
		thread.start();*/
		
		return  file != null ? file.getName() : null;
		
	}
	
	public static File getTimestampedPNGFileForDirectory(File gameDirectory) {
		
		String s = dateFormat.format(new Date()).toString();
		int i = 1;

		while(true) {
			File file = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

			if(!file.exists()) {
				return file;
			}

			++i;
		}
		
	}
	
	public static int[] getPixels() {
		
		return pixelValues;
		
	}

}
