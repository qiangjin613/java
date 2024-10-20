package org.example.classloader;

import sun.misc.Launcher;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class SimpleDemo {

    public static void main(String[] args) throws Exception {
        demo1();
        demo2();
//        demo3();
        demo4();
    }

    /**
     * 如何获取 ClassLoader？
     */
    private static void demo1() {
        // 获取当前系统的 ClassLoader -> 默认：AppClassLoader
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
        // output: sun.misc.Launcher$AppClassLoader@18b4aac2

        // 获取当前线程上下文的 ClassLoader -> 默认：AppClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        // output: sun.misc.Launcher$AppClassLoader@18b4aac2

        // 获取当前类的 ClassLoader
        System.out.println(SimpleDemo.class.getClassLoader());
        // 用户自定义类，output: sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(String.class.getClassLoader());
        // Java 核心类库 rt.jar 中的 String 类，output: null
    }

    /**
     * 查看 ClassLoader 的层级
     */
    private static void demo2() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);
        while (classLoader != null) {
            classLoader = classLoader.getParent();
            System.out.println(classLoader);
        }
        // output:
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        // sun.misc.Launcher$ExtClassLoader@1b6d3586
        // null
    }

    /**
     * 类与类加载器
     */
//    private static void demo3() throws Exception {
//        ClassLoader myClassLoader = new ClassLoader() {
//            @Override
//            public Class<?> loadClass(String name) throws ClassNotFoundException {
//                System.out.println("使用自定义类加载器加载 " + name);
//                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
//                InputStream resource = getClass().getResourceAsStream(fileName);
//                if (resource == null) {
//                    return super.loadClass(name);
//                }
//                byte[] b;
//                try {
//                    b = new byte[resource.available()];
//                    resource.read(b);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                return defineClass(name, b, 0, b.length);
//            }
//        };
//
//
//        Object obj = myClassLoader.loadClass("org.example.classloader.SimpleDemo").newInstance();
//        System.out.println(obj.getClass());
//        System.out.println(obj instanceof SimpleDemo);
//        // output: false
//
//
//        Object s = myClassLoader.loadClass("java.lang.String").newInstance();
//        System.out.println(s.getClass());
//        System.out.println(s instanceof String);
//        // output: true
//    }

    /**
     * 查看类加载器都加载了哪些资源
     */
    private static void demo4() {
        // AppClassLoader
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        System.out.printf("\n --- AppClassLoader(%s) 加载的资源 ---\n", appClassLoader);
        URLClassLoader urlClassLoader = (URLClassLoader) appClassLoader;
        printUrl(urlClassLoader.getURLs());

        // ExtClassLoader
        ClassLoader extClassLoader = appClassLoader.getParent();
        System.out.printf("\n --- ExtClassLoader(%s) 加载的资源 ---\n", extClassLoader);
        urlClassLoader = (URLClassLoader) extClassLoader;
        printUrl(urlClassLoader.getURLs());

        // 获取 Bootstrap ClassLoader 加载的类路径
        System.out.printf("\n --- Bootstrap ClassLoader(%s) 加载的资源 ---\n", extClassLoader.getParent());
        URLClassPath bootstrapClassPath = Launcher.getBootstrapClassPath();
        printUrl(bootstrapClassPath.getURLs());
    }

    private static void printUrl(URL[] urls) {
        for (URL url : urls) {
            System.out.println(url);
        }
    }
}
