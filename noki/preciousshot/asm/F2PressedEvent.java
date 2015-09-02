package noki.preciousshot.asm;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;


/**********
 * @class F2PressedEvent
 *
 * @description F2キーが押されたときに発生するイベントです。
 * @descriptoin_en 
 */
public class F2PressedEvent extends Event {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	public static void postEvent() {
		
		FMLCommonHandler.instance().bus().post(new F2PressedEvent());
	}

}
