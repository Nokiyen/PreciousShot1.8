package noki.preciousshot.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;


public class ResourceManager {
	
	public static File screenshotsDirectory;
	public static IResourcePack screenshotsResourcePack;
	public static ArrayList<ShotResource> resources;
	
	
	public static void init() {
		
		screenshotsDirectory = new File(Minecraft.getMinecraft().mcDataDir, "screenshots");
		screenshotsResourcePack = new ScreenShotsResourcePack(screenshotsDirectory);
		resources = new ArrayList<ShotResource>();
		reloadResources();
		
	}
	
	public static void reloadResources() {
		
		((SimpleReloadableResourceManager)Minecraft.getMinecraft().getResourceManager())
			.reloadResourcePack(screenshotsResourcePack);
		
		resources.clear();
		File[] files = screenshotsDirectory.listFiles();
		Arrays.sort(files, new DateDescComparator());
		for(File each: files) {
			if(!isImage(each)) {
				continue;
			}
			ResourceLocation location = new ResourceLocation("ps_screenshots", each.getName());
			try {
				InputStream stream = screenshotsResourcePack.getInputStream(location);
				BufferedImage image = TextureUtil.readBufferedImage(stream);
				resources.add(new ShotResource(each, location, image.getWidth(), image.getHeight()));
			} catch (Exception e) {
			}
		}
		
	}
	
	public static int getSize() {
		
		return resources.size();
		
	}
	
	public static ShotResource getResource(int index) {
		
		if(0<=index && index<=resources.size()-1) {
			return resources.get(index);
		}
		return null;
		
	}
	
	public static ShotResource getResource(String fileName) {
		
		for(ShotResource each: resources) {
			if(each.file.getName().equals(fileName)) {
				return each;
			}
		}
		return null;
		
	}
	
	public static boolean isImage(File file) {
		
		if(!file.isFile()) {
			return false;
		}
		
		int position = file.getName().lastIndexOf(".");
		if(position == -1) {
			return false;
		}
		
		String ext = file.getName().substring(position + 1).toLowerCase();
		if(!ext.equals("png") && !ext.equals("jpg") && !ext.equals("bmp")) {
			return false;
		}
		
		return true;
		
	}
	
	public static class ShotResource {
		
		public File file;
		public ResourceLocation location;
		public int width;
		public int height;
		
		public ShotResource(File file, ResourceLocation location, int width, int height) {
			this.file = file;
			this.location = location;
			this.width = width;
			this.height = height;
		}
		
	}
	
	public static class DateDescComparator implements Comparator<File> {
		
		@Override
		public final int compare(final File file1, final File file2) {
			if((file1 != null) && (file2 != null)) {
				return (int)Math.signum(file2.lastModified() - file1.lastModified());
			}
			return 0;
		}
		
	}

}
