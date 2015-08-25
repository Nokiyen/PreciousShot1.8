package noki.preciousshot.asm;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;

public class F2PressedEvent extends Event {
	
	public static void postEvent() {
		
		FMLCommonHandler.instance().bus().post(new F2PressedEvent());
	}

}
