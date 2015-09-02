/**********
 * NOTICE
 * 
 *
 * This work uses the Java library, "Twitter4J (http://twitter4j.org)", licensed under Apache License 2.0.
 * 
 * For Apache License 2.0, see here.
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Especially the class, noki.preciousshot.helper.TwitterHelper, directly uses classes supplied by the library.
 * 
 **********/


package noki.preciousshot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import noki.preciousshot.mode.ModeManager;
import noki.preciousshot.resource.ResourceManager;


/**********
 * @Mod PreciousShot
 *
 * @author Nokiyen
 * 
 * @description スクリーンショットを強化するmodです。
 * @description_en Mod for adding variable functions for screenshot.
 */
@Mod(modid = ModInfo.ID, version = ModInfo.VERSION, name = ModInfo.NAME, clientSideOnly = true)
public class PreciousShotCore {
	
	//******************************//
	// define member variables.
	//******************************//
	@Instance(value = ModInfo.ID)
	public static PreciousShotCore instance;
	@Metadata
	public static ModMetadata metadata;	//	extract from mcmod.info file, not java internal coding.
	
	public static VersionInfo versionInfo;
	
	public static boolean DEBUG = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	//----------
	//Core Event Methods.
	//----------
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		PreciousShotData.setConf(event.getSuggestedConfigurationFile());
		
		//for version notification.
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		
		ModeManager.init();
		
		//for version notification.
		MinecraftForge.EVENT_BUS.register(versionInfo);
				
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		ResourceManager.init();
		
	}
	
	
	//----------
	//Static Method.
	//----------
	public static void log(String message, Object... data) {
		
		if(DEBUG == true) {
			FMLLog.fine("[PreciousShot:LOG] "+message, data);
		}
		
	}
	
}
