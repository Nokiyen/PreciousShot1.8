package noki.preciousshot.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;


/**********
 * @class ScreenShotsResourcePack
 *
 * @description screenshotsフォルダを、リソースパックとして扱うためのクラスです。
 * これにより、screenshotsフォルダ内の画像に対してResourceLocationを使うことができます。
 * @descriptoin_en 
 */
public class ScreenShotsResourcePack implements IResourcePack {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final Set<String> resourceDomains = ImmutableSet.of("ps_screenshots");
	private File screenshotsDirectory;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public ScreenShotsResourcePack(File directory) {
		
		this.screenshotsDirectory = directory;
		
	}
	
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		
		return new FileInputStream(new File(this.screenshotsDirectory, location.getResourcePath()));
		
	}
	
	@Override
	public boolean resourceExists(ResourceLocation location) {
		
		return new File(this.screenshotsDirectory, location.getResourcePath()).exists();
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Set getResourceDomains() {
		
		return resourceDomains;
		
	}
	
	@Override
	public IMetadataSection getPackMetadata(IMetadataSerializer serializer, String type) throws IOException {
		
		return null;
		
	}

	@Override
	public BufferedImage getPackImage() throws IOException {
		
		return null;
		
	}

	@Override
	public String getPackName() {
		
		return (String)resourceDomains.toArray()[0];
		
	}

}
