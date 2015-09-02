package noki.preciousshot.mode;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import noki.preciousshot.PreciousShotData;
import noki.preciousshot.asm.ChatClickEvent;
import noki.preciousshot.asm.F2PressedEvent;
import noki.preciousshot.helper.LangHelper;
import noki.preciousshot.resource.ResourceManager;
import static noki.preciousshot.PreciousShotData.PSOption.*;


/**********
 * @class ModeManager
 *
 * @description 画面切り替え等をコントロールするクラスです。
 * @description_en
 */
public class ModeManager {
	
	//******************************//
	// define member variables.
	//******************************//
	public static ModeManager instance;
	public static ModeEventShooting modeShooting;
	public static ModeEventShooting modePanorama;
	
	public static KeyBinding keyF2 = new KeyBinding("chunkvisualizer.key.f2", Keyboard.KEY_F2, "System");
	public static KeyBinding key = new KeyBinding("chunkvisualizer.key.on", PreciousShotData.keyNum, "System");
	
	
	
	//******************************//
	// define member methods.
	//******************************//
	//F2キーを押されたとき、撮影モード・パノラマモードでなければ通常のスクリーンショットを行う。
	@SubscribeEvent
	public void onF2Pressed(F2PressedEvent event) {
		
		if(modeShooting.isOpen() == false && modePanorama.isOpen() == false) {
			// default screen shot.
			Minecraft mc = Minecraft.getMinecraft();
			IChatComponent res = net.minecraft.util.ScreenShotHelper.saveScreenshot(
					mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
			mc.ingameGUI.getChatGUI().printChatMessage(res);
		}
		
	}
	
	//Lキーを押されたとき、通常画面からなら撮影・パノラマモードへ、撮影・パノラマモードなら設定モードへ移行する。
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		
		if(key.isPressed() == false) {
			return;
		}
		
		ModeEventShooting currentMode = modeShooting;
		if(PANORAMA.isEnable()) {
			currentMode = modePanorama;
		}
		if(currentMode.isOpen()) {
			currentMode.closeMode();
			Minecraft.getMinecraft().displayGuiScreen(new ModeGuiSetting());
		}
		else {
			currentMode.openMode();
		}
		
	}
	
	//チャットがクリックされたとき、閲覧モードを開く設定のチャット文なら、閲覧モードの個別表示に直接移行する。
	@SubscribeEvent
	public void onChatClick(ChatClickEvent event) {
		
		String fileName = LangHelper.getViewOpenString(event.clickEvent);
		if(fileName == null) {
			return;
		}
		
		int index = ResourceManager.getResourceIndex(fileName);
		if(index == -1) {
			index = 0;
		}
		ModeGuiViewing gui = new ModeGuiViewing();
		Minecraft.getMinecraft().displayGuiScreen(gui);
		gui.setEachView(index);
		
		event.setCanceled(true);
		
	}
	
	
	//----------
	//Static Method.
	//----------
	public static void init() {
		
		keyF2 = new KeyBinding("chunkvisualizer.key.f2", Keyboard.KEY_F2, "System");
		key = new KeyBinding("chunkvisualizer.key.on", PreciousShotData.keyNum, "System");
		
		ClientRegistry.registerKeyBinding(ModeManager.keyF2);
		ClientRegistry.registerKeyBinding(ModeManager.key);
		
		instance = new ModeManager();
		modeShooting = new ModeEventShooting();
		modePanorama = new ModeEventPanorama();
		
		FMLCommonHandler.instance().bus().register(instance);
		FMLCommonHandler.instance().bus().register(modeShooting);
		FMLCommonHandler.instance().bus().register(modePanorama);
		MinecraftForge.EVENT_BUS.register(instance);
		MinecraftForge.EVENT_BUS.register(modeShooting);
		MinecraftForge.EVENT_BUS.register(modePanorama);
		
	}
	
}
