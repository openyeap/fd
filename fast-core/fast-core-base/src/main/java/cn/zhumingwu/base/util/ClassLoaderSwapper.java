package cn.zhumingwu.base.util;

/**
 * 为避免多个版本jar冲突，需要脱离当前classLoader去加载这些jar包，执行完成后，又退回到原来classLoader上继续执行接下来的代码
 */
public final class ClassLoaderSwapper implements AutoCloseable {
    private ClassLoader storeClassLoader = null;

    private ClassLoaderSwapper() {
    }

    public static ClassLoaderSwapper createClassLoaderSwapper() {
        return new ClassLoaderSwapper();
    }

    /**
     * 保存当前classLoader，并将当前线程的classLoader设置为所给classLoader
     *
     * @param classLoader classLoader
     * @return ClassLoaderSwapper
     */
    public ClassLoaderSwapper newClassLoader(ClassLoader classLoader) {
        this.storeClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        return this;
    }

    /**
     * 将当前线程的类加载器设置为保存的类加载
     *
     * @return ClassLoaderSwapper
     */
    public ClassLoaderSwapper restoreClassLoader() {
        Thread.currentThread().setContextClassLoader(this.storeClassLoader);
        return this;
    }

    @Override
    public void close()  {
        restoreClassLoader();
    }
}
