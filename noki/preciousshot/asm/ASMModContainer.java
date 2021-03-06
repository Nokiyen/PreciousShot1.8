package noki.preciousshot.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;


/**********
 * @class ASMTransformerCore
 *
 * @description CoreModとしての情報を格納するクラスです。通常のModのように各種Mod情報を定義します。
 * ほぼおまじない。
 */
public class ASMModContainer extends DummyModContainer {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	public ASMModContainer() {
		
		super(new ModMetadata());
		ModMetadata meta = super.getMetadata();
		meta.modId = "preciousshottransform";
		meta.name = "PreciousShotTransform";
		meta.version = "1.0";
		meta.authorList = Arrays.asList(new String[] { "Nokiyen" });
		meta.description = "A transformer class of PreciousShot.";
		meta.url = "";
		
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController lc) {
		
		bus.register(this);
		return true;
		
	}
	
}
