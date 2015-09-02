package noki.preciousshot.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;


/**********
 * @class ASMClassTransformer
 * @inner_class CustomClassVisitor1, CustomMethodVisitor1, CustomClassVisitor2, CustomMethodVisitor2
 *
 * @description 実際にASMによるバイトコード改変を行うクラスです。
 */
public class ASMClassTransformer implements IClassTransformer, Opcodes {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String TARGET_CLASS_NAME1 = "net.minecraft.client.Minecraft";
	private static final String TARGET_CLASS_NAME2 = "net.minecraft.client.gui.GuiScreen";
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		
		ASMLoadingPlugin.LOGGER.fine("enter ASMClassTransformer.");
		
		if (!transformedName.equals(TARGET_CLASS_NAME1) && !transformedName.equals(TARGET_CLASS_NAME2)) {
			return basicClass;
		}
/*		if(!transformedName.equals(TARGET_CLASS_NAME1)) {
			return basicClass;
		}*/
		try {
			ASMLoadingPlugin.LOGGER.fine("enter transforming.");
			
			//**この辺りから、ASMの書き方ができます。**//
			
			//ClassReader, ClassWriter, ClassVisitorで3すくみになるように引数を与えることで処理を早める。
			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(classReader, 0);
			ClassVisitor customVisitor = null;
			if(transformedName.equals(TARGET_CLASS_NAME1)) {
				customVisitor = new CustomClassVisitor1(name, classWriter);
			}
			else if(transformedName.equals(TARGET_CLASS_NAME2)) {
				customVisitor = new CustomClassVisitor2(name, classWriter);
			}
			classReader.accept(customVisitor, ClassReader.EXPAND_FRAMES);
			return classWriter.toByteArray();
			
			//**ここまで**//
		} catch (Exception e) {
			throw new RuntimeException("asm, class transforming failed.", e);
		}
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	private class CustomClassVisitor1 extends ClassVisitor {
		//*****define member variables.*//
		private String owner;
		
		private static final String TARGET_METHOD_NAME_OBF = "func_152348_aa";
		private static final String TARGET_METHOD_DESC = "()V";
		@SuppressWarnings("unused")
		private static final String TARGET_METHOD_NAME = "dispatchKeypresses";
		
		
		//*****define member methods.***//
		public CustomClassVisitor1(String owner, ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
			this.owner = owner;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			boolean flag1 = TARGET_METHOD_NAME_OBF.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(this.owner, name, desc));
			boolean flag2 = TARGET_METHOD_DESC.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc));
			if(flag1 && flag2) {
				ASMLoadingPlugin.LOGGER.fine("enter method.");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor1(this.api, this.owner, access, name, desc, mv);
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}
	
	//MethodVisitorの代わりにAnalyzerAdapterを使うことで、
	//visitMax()やvisitFrame()の処理をしなくてよくなる。
	//その代わり、各種superメソッドを呼び出す必要がある。
	//COMPUTE_MAXでもそんなに速度落ちないという噂も。
	private class CustomMethodVisitor1 extends AnalyzerAdapter {
		//*****define member variables.*//
		private boolean flag = false;
		
		private static final String NAME1_OBF = "func_148260_a";
		private static final String DESC1 = "(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;";
		private static final String NAME2_OBF = "func_146227_a";
		private static final String DESC2 = "(Lnet/minecraft/util/IChatComponent;)V";
		
		
		//*****define member methods.***//
		protected CustomMethodVisitor1(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, owner, access, name, desc, mv);
		}
		
		//Minecraft.dispatchKeypresses()内の、F2キーの処理をスキップ。
		@Override
		public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
			//ScreenShotHelper.saveScreenshot()をスキップ。
			if(opcode == INVOKESTATIC && NAME1_OBF.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))
					&& DESC1.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc))) {
				this.flag = true;
				return;
			}
			//GuiNewChat.printChatMessage()をスキップ。
			if(opcode == INVOKEVIRTUAL && NAME2_OBF.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))
					&& DESC2.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc)) && flag == true) {
				this.flag = false;
				//メソッドコール用にスタックに積まれた引数は消去。
				for(int i=1; i<=5; i++) {
					super.visitInsn(POP);
				}
				//加えてイベント発生。
				super.visitMethodInsn(INVOKESTATIC, "noki/preciousshot/asm/F2PressedEvent",
						"postEvent", "()V", false);
				return;
			}
			super.visitMethodInsn(opcode, owner, name, desc, itf);
	    }
	}
	
	private class CustomClassVisitor2 extends ClassVisitor {
		//*****define member variables.*//
		private String owner;
		
		private static final String TARGET_METHOD_NAME_OBF = "func_175276_a";
		private static final String TARGET_METHOD_DESC = "(Lnet/minecraft/util/IChatComponent;)Z";
		
		
		//*****define member methods.***//
		public CustomClassVisitor2(String owner, ClassVisitor cv) {
			super(Opcodes.ASM4, cv);
			this.owner = owner;
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			boolean flag1 = TARGET_METHOD_NAME_OBF.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(this.owner, name, desc));
			boolean flag2 = TARGET_METHOD_DESC.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc));
			if(flag1 && flag2) {
				ASMLoadingPlugin.LOGGER.fine("enter method.");
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				return new CustomMethodVisitor2(this.api, this.owner, access, name, desc, mv);
			}
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}
	
	private class CustomMethodVisitor2 extends AnalyzerAdapter {
		//*****define member methods.***//
		protected CustomMethodVisitor2(int api, String owner, int access, String name, String desc, MethodVisitor mv) {
			super(api, owner, access, name, desc, mv);
		}
		
		//chatクリック時のイベントを発生。
		@Override
		public void visitCode() {
			/*
			 * if(ChatClickEvent.post(chatComponent) == true) {
			 * 		return false;
			 * }
			 */
			ASMLoadingPlugin.LOGGER.fine("enter visitCode().");
			super.visitCode();
			super.visitVarInsn(ALOAD, 1);
			super.visitMethodInsn(INVOKESTATIC, "noki/preciousshot/asm/ChatClickEvent",
					"postEvent", "(Lnet/minecraft/util/IChatComponent;)Z", false);
			Label label = new Label();
			super.visitJumpInsn(IFEQ, label);
			super.visitLdcInsn(0);
			super.visitInsn(IRETURN);
			super.visitLabel(label);
		}
	}
	
}
