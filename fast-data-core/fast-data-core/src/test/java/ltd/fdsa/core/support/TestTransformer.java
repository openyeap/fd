package ltd.fdsa.core.support;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.util.NamingUtils;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

@Slf4j
public class TestTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        NamingUtils.formatLog(log,"Transforming {}", className);
        try {
            ClassPool cp = ClassPool.getDefault();
            CtClass cc = cp.get(className);
            for (CtMethod m : cc.getMethods()) {
                NamingUtils.formatLog(log,"CtMethod: {}", m);
                m.insertBefore("{ System.out.println(\"start\"); }");
                m.insertAfter("{ System.out.println(\"end\"); }");
            }
            return cc.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
