// package ltd.fdsa.core;

// import javassist.ClassPool;
// import javassist.CtClass;
// import javassist.CtMethod;
// import jdk.internal.org.objectweb.asm.*;
// import lombok.extern.slf4j.Slf4j;
// import lombok.var;
// import ltd.fdsa.core.config.ProjectAutoConfiguration;
// import ltd.fdsa.core.support.MyClassLoader;
// import ltd.fdsa.core.support.TestClass;
// import ltd.fdsa.core.util.NamingUtils;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.ApplicationContext;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.junit4.SpringRunner;

// import java.io.File;
// import java.io.FileOutputStream;

// @Slf4j
// @RunWith(SpringRunner.class)
// @SpringBootTest
// @ContextConfiguration(classes = ProjectAutoConfiguration.class)
// public class ASMTests {

//     @Autowired
//     ApplicationContext applicationContext;

//     @Test
//     public void Test_HumpToLine() throws Exception {
//         var className = TestClass.class.getCanonicalName().replace(".", "/");
//         NamingUtils.formatLog(log,"{}", className);
//         //读取
//         ClassReader classReader = new ClassReader(className);
//         ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//         //处理
//         ClassVisitor classVisitor = new MyClassVisitor(classWriter);
//         classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
//         byte[] data = classWriter.toByteArray();
//         //输出
//         String projectPath = System.getProperty("user.dir") + "/target/classes/" + className + ".class";
//         File file = new File(projectPath);
//         FileOutputStream fileOutputStream = new FileOutputStream(file);
//         fileOutputStream.write(data);
//         fileOutputStream.close();
//         MyClassLoader classLoader = new MyClassLoader(this.getClass().getClassLoader());
//         var clazz = classLoader.loadClass(TestClass.class.getCanonicalName());
//         var obj = clazz.newInstance();
//         clazz.getMethod("process").invoke(obj);
//     }

//     @Test
//     public void Test_ClassLoader() throws Exception {

//         MyClassLoader classLoader = new MyClassLoader(this.getClass().getClassLoader());
//         Thread.currentThread().setContextClassLoader(classLoader);
//         var clazz = classLoader.loadClass(TestClass.class.getCanonicalName());
//         var obj = clazz.newInstance();
//         clazz.getMethod("process").invoke(obj);
//     }

//     @Test
//     public void Test_CC() throws Exception {
//         MyClassLoader classLoader = new MyClassLoader(this.getClass().getClassLoader());
//         var className = TestClass.class.getCanonicalName();
//         NamingUtils.formatLog(log,"TestClass: {}", className);
//         classLoader.addClassName(className);

//         //处理
//         ClassPool cp = ClassPool.getDefault();
//         CtClass cc = cp.get(className);
//         CtMethod m = cc.getDeclaredMethod("process");
//         m.insertBefore("{ System.out.println(\"start\"); }");
//         m.insertAfter("{ System.out.println(\"end\"); }");
//         //输出
//         byte[] data = cc.toBytecode();

//         String path = System.getProperty("user.dir").replace("\\", "/") + "/target/classes/" + className.replace(".", "/") + ".class";
//         File file = new File(path);
//         var fileOutputStream = new FileOutputStream(file);
//         fileOutputStream.write(data);
//         fileOutputStream.close();

//         var clazz = classLoader.loadClass(className);
//         var obj = clazz.newInstance();
//         clazz.getMethod("process").invoke(obj);
//     }

//     class MyClassVisitor extends ClassVisitor implements Opcodes {

//         public MyClassVisitor(ClassVisitor cv) {
//             super(ASM5, cv);
//         }

//         @Override
//         public void visit(int version, int access, String name, String signature,
//                           String superName, String[] interfaces) {
//             cv.visit(version, access, name, signature, superName, interfaces);
//         }

//         @Override
//         public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//             MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
//             // Base类中有两个方法：无参构造以及process方法，这里不增强构造方法
//             if (!name.equals("<init>") && mv != null) {
//                 mv = new MyMethodVisitor(mv);
//             }
//             return mv;
//         }

//         class MyMethodVisitor extends MethodVisitor implements Opcodes {
//             public MyMethodVisitor(MethodVisitor mv) {
//                 super(Opcodes.ASM5, mv);
//             }

//             /**
//              * 在ASM在开始访问某一个方法的Code区时被调用，所以可以将AOP的前置逻辑放到这里
//              */
//             @Override
//             public void visitCode() {
//                 super.visitCode();
//                 mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                 mv.visitLdcInsn("start");
//                 mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//             }

//             /**
//              * 每当ASM读取到无参指令时调用此方法
//              * 这里判断了当前指令是不是无参的return指令，如果是则将AOP的后置逻辑放到这里
//              *
//              * @param opcode
//              */
//             @Override
//             public void visitInsn(int opcode) {
//                 if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)
//                         || opcode == Opcodes.ATHROW) {
//                     //方 法在返回之前，打印"end"
//                     mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                     mv.visitLdcInsn("end");
//                     mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//                 }
//                 mv.visitInsn(opcode);
//             }
//         }
//     }

// }
