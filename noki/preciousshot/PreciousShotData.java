package noki.preciousshot;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;


/**********
 * @class PreciousShotData
 * @inner_class PSOption, FrameSet
 *
 * @description このModの各種データを保存するクラスです。
 * @descriptoin_en Class to store all of the data of this mod.
 */
public class PreciousShotData {
	
	//******************************//
	// define member variables.
	//******************************//
	public static File configFile;
	public static Configuration config;
	
	public static int keyNum;
	public static FrameSet frameSet1;
	public static FrameSet frameSet2;
	public static FrameSet frameSet3;
	public static FrameSet frameSet4;
	
	public static String[] twitterKeys = new String[4];
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static void setConf(File file) {
		
		configFile = file;
		
		config = new Configuration(configFile);
		config.load();
		
		//set configuration values.
		keyNum = config.getInt("keyNum", "Settings", 38, 1, 300, "");
		frameSet1 = new FrameSet(1, config, 640, 480);
		frameSet2 = new FrameSet(2, config, 640, 360);
		frameSet3 = new FrameSet(3, config, 640, 384);
		frameSet4 = new FrameSet(4, config, 854, 480);
		
		PSOption.setConfig(config);
		
		twitterKeys[0] = config.getString("consumerKey", "twitter", "", "");
		twitterKeys[1] = config.getString("consumerSecret", "twitter", "", "");
		twitterKeys[2] = config.getString("accessToken", "twitter", "", "");
		twitterKeys[3] = config.getString("accessTokenSecret", "twitter", "", "");
		//end.
		
		config.save();
		
	}
	
	//--------------------
	// Inner Class.
	//--------------------
	public enum PSOption {
		//*****define enums.************//
		TOP("top", true, false, 0, 0, Integer.MAX_VALUE){
			@Override public void set(int value) {
				this.property.set(MathHelper.clamp_int(value, this.minValue, Minecraft.getMinecraft().displayHeight));
				config.save();
			}
		},
		RIGHT("right", true, false, 0, 0, Integer.MAX_VALUE){
			@Override public void set(int value) {
				this.property.set(MathHelper.clamp_int(value, this.minValue, Minecraft.getMinecraft().displayWidth));
				config.save();
			}
		},
		BOTTOM("bottom", true, false, 0, 0, Integer.MAX_VALUE){
			@Override public void set(int value) {
				this.property.set(MathHelper.clamp_int(value, this.minValue, Minecraft.getMinecraft().displayHeight));
				config.save();
			}
		},
		LEFT("left", true, false, 0, 0, Integer.MAX_VALUE){
			@Override public void set(int value) {
				this.property.set(MathHelper.clamp_int(value, this.minValue, Minecraft.getMinecraft().displayWidth));
				config.save();
			}
		},
		GAMMA("gamma", false, false, 0, 0, 200){
			@Override public float getFloat() {
				return (float)this.value() / 100.0F;
			}
		},
		FOV("fov", false, false, 70, 10, 170),
		CONT("cont", false, false, 5, 1, 20),
		PANORAMA("panorama", false, false, 6, 1, 20),
		GRID("grid", true, true, 0, 0, 5),
		HIDE("hide", true, true, 1, 0, 1),
		SHOT("shot", true, true, 1, 0, 1),
		MARGIN("margin", true, true, 1, 0, 1),
		CLICK("click", false, true, 0, 0, 1),
		NIGHT("night", false, true, 0, 0, 1),
		CHAT("chat", true, true, 1, 0, 1),
		OTHER("other", true, true, 1, 0, 1);
		
		
		//*****define member variables.*//
		protected static Configuration config;
		
		protected String name;
		protected boolean enable;
		protected boolean cycle;
		protected int defaultValue;
		protected int maxValue;
		protected int minValue;
		protected Property property;
		protected Property flagProperty;
		
		
		//*****define member methods.***//
		private PSOption(String name, boolean enable, boolean cycle, int defaultValue, int minValue, int maxValue) {
			this.name = name;
			this.enable = enable;
			this.cycle = cycle;
			this.defaultValue = defaultValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
		
		public int value() {
			return this.property.getInt();
		}
		
		public double getDouble() {
			return (double)this.value();
		}
		
		public float getFloat() {
			return (float)this.value();
		}
		
		public void set(int value) {
			this.property.set(MathHelper.clamp_int(value, this.minValue, this.maxValue));
			config.save();
		}
		
		public void add() {
			this.add(1);
		}
		
		public void add(int step) {
			int newValue = this.property.getInt() + step;
			if(newValue <= this.maxValue) {
				this.property.set(newValue);
				config.save();
			}
			else if(this.cycle == true) {
				this.property.set(newValue%(this.maxValue+1));
				config.save();
			}
		}
		
		public void dif() {
			this.dif(1);
		}
		
		public void dif(int step) {
			int newValue = this.property.getInt() - step;
			if(newValue >= this.minValue) {
				this.property.set(newValue);
				config.save();
			}
			else if(this.cycle == true) {
				this.property.set(newValue%(this.maxValue+1));
				config.save();
			}
		}
		
		public void enable() {
			this.flagProperty.set(true);
			config.save();
		}
		
		public void disable() {
			this.flagProperty.set(false);
			config.save();
		}
		
		public boolean isEnable() {
			return this.flagProperty.getBoolean();			
		}
		
		public void switchEnable() {
			if(this.isEnable()) {
				this.disable();
			}
			else {
				this.enable();
			}
		}
		
		
		//-----static methods.----------//
		public static void setConfig(Configuration configuration) {			
			config = configuration;
			
			for(PSOption each: PSOption.values()) {
				each.property = config.get("Saved Option", each.name, each.defaultValue);
				each.flagProperty = config.get("Saved Option", each.name+"Flag", each.enable);				
			}			
		}
	}
	
	public static class FrameSet {
		//*****define member variables.*//
		public int id;
		public Configuration config;
		
		public int defaultWidth;
		public int defaultHeight;
		public int width;
		public int height;
		public boolean forceDisplayWidth = false;	//currently not used.
		public boolean forceDisplayHeight = false;	//currently not used.
		
		
		//*****define member methods.***//
		public FrameSet(int id, Configuration config, int defaultWidth, int defaultHeight) {
			this.id = id;
			this.config = config;
			this.defaultWidth= defaultWidth;
			this.defaultHeight = defaultHeight;
			
			this.width = config.getInt("frameWidth"+this.id, "Settings", this.defaultWidth, 10, Integer.MAX_VALUE, "");
			this.height = config.getInt("frameHeight"+this.id, "Settings", this.defaultHeight, 10, Integer.MAX_VALUE, "");
			
			if(this.width < 10 || Minecraft.getMinecraft().displayWidth < this.width) {
				this.width = Minecraft.getMinecraft().displayWidth;
				forceDisplayWidth = true;
			}
			if(this.height < 10 || Minecraft.getMinecraft().displayHeight < this.height) {
				this.height = Minecraft.getMinecraft().displayHeight;
				forceDisplayHeight = true;
			}
		}
	}
	
}
