package noki.preciousshot.asm;

import java.util.Map;
import java.util.logging.Logger;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;


/**********
 * @class ASMLoadingPlugin
 *
 * @description CoreModの本体です。
 * このクラスで、CoreModとしての情報を格納するクラスと、実際に改変を行うクラスを指定します。
 * ほぼおまじない。
 * 
 * このクラスを、MANIFEST.MFファイル内で指定する必要があります。
 * 通常のModとCoreModをひとつのMod(ひとつのjar)として配布するならば、
 * あわせて"FMLCorePluginContainsFMLMod: true"を追記する必要があります。
 * 
 * ~~~.jar/META-INF/MANIFEST.MF
 * 		Manifest-Version: 1.0
 * 		FMLCorePlugin: noki.preciousshot.asm.ASMLoadingPlugin
 * 		FMLCorePluginContainsFMLMod: true
 * 
 * @see ASMModContainer, ASMClassTransformer
 */
public class ASMLoadingPlugin implements IFMLLoadingPlugin {
	
	//******************************//
	// define member variables.
	//******************************//
	public static final Logger LOGGER = Logger.getLogger("preciousshot");

	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public String[] getASMTransformerClass() {
		
		return new String[] {"noki.preciousshot.asm.ASMClassTransformer"};
		
	}
	
	@Override
	public String getModContainerClass() {
		
		return "noki.preciousshot.asm.ASMModContainer";
		
	}
	
	@Override
	public String getSetupClass() {
		
		return null;
		
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		
	}

	@Override
	public String getAccessTransformerClass() {
		
		return null;
		
	}
	
}
