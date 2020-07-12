package org.syq.core;

import org.syq.annotation.ActionAnnotation;
import org.syq.annotation.DriverAnnotation;
import org.syq.annotation.RuleAnnotation;
import org.syq.bean.Tuple;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by cage on 2020-07-12
 */
public class RuleDriver {

    private Class clazztWithDriverAnnotation;

    public RuleDriver(Class classWithDriverAnnotation) {
        this.clazztWithDriverAnnotation = classWithDriverAnnotation;
    }


    private Object getInstanceByClazz(Class classWithDriverAnnotation) {
        assert classWithDriverAnnotation != null;
        Objects.requireNonNull("invalid parameter: RuleDriver needs a Class with a Driver Annotation, Not null!");
        Constructor constructor = null;
        try {
            constructor = clazztWithDriverAnnotation.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert constructor != null;
        constructor.setAccessible(true);
        Object instance = null;
        try {
            instance = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }


    private Map<Integer, Map<Integer, Tuple<Object, Method>>> allMethodRuleMap(Object driverInstance) {
        DriverAnnotation annotation = driverInstance.getClass().getAnnotation(DriverAnnotation.class);
        String[] pkgs = {};
        // 沒有指定rule的package，则使用传入的类所在的包作为扫描的范围
        if (annotation == null) {
            String pkgName = driverInstance.getClass().getPackage().getName();
            System.out.println("pkgName:");
            System.out.println(pkgName);
            pkgs = new String[]{pkgName};
        } else {
            pkgs = annotation.basePackages();
        }
        for (String pkg : pkgs) {
            System.out.println("pkg: " + pkg);
        }
        System.out.println("--------------------------------------");
        Map<Integer, Map<Integer, Tuple<Object, Method>>> allRuleMethodMap = new TreeMap<>();
        for (String pkg : pkgs) {
            System.out.println("pkg:" + pkg);
            Set<Class<?>> classes1 = getClasses(pkg);
            for (Class<?> aClass : classes1) {
                boolean annotationPresent = aClass.isAnnotationPresent(RuleAnnotation.class);
                System.out.println("class name: " + aClass.getName());
                System.out.println("ruleAnnotation Present: " + annotationPresent);
                if (annotationPresent) {
                    Object object = null;
                    try {
                        object = aClass.newInstance(); // todo: 对象池
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    RuleAnnotation ruleannotation = aClass.getAnnotation(RuleAnnotation.class);
                    int ruleOrder = ruleannotation.order();
                    Method[] methods = aClass.getMethods();
                    Map<Integer, Tuple<Object, Method>> methodMap = new TreeMap<>();
                    for (Method method : methods) {
                        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                        for (Annotation declaredAnnotation : declaredAnnotations) {
                            if ((declaredAnnotation instanceof ActionAnnotation)) {
                                System.out.println("rule name: " + ruleannotation.name() + ", action order: " + ((ActionAnnotation) declaredAnnotation).order());
                                int actionOrder = ((ActionAnnotation) declaredAnnotation).order();
                                methodMap.put(actionOrder, new Tuple<>(object, method));
                            }
                        }
                        System.out.println("methodMap.size: " + methodMap.size());
                    }
                    System.out.println("ruleOrder: " + ruleOrder);
                    allRuleMethodMap.put(ruleOrder, methodMap);
                }
            }
        }
        return allRuleMethodMap;
    }



    private Map<String, Map<Integer, Map<Integer, Tuple<Object, Method>>>> allGroupRuleMethodMap(Object driverInstance) {
        Map<String, Map<Integer, Map<Integer, Tuple<Object, Method>>>> groupRuleMethodMap = new HashMap<>();
        DriverAnnotation annotation = driverInstance.getClass().getAnnotation(DriverAnnotation.class);
        String[] pkgs = {};
        // 沒有指定rule的package，则使用传入的类所在的包作为扫描的范围
        if (annotation == null) {
            String pkgName = driverInstance.getClass().getPackage().getName();
            System.out.println("pkgName: " + pkgName);
            System.out.println();
            pkgs = new String[]{pkgName};
        } else {
            pkgs = annotation.basePackages();
        }
        System.out.println("--------------------------------------");


        Map<Integer, Map<Integer, Tuple<Object, Method>>> allRuleMethodMap = new TreeMap<>();
        for (String pkg : pkgs) {
            System.out.println("pkg:" + pkg);
            Set<Class<?>> classes1 = getClasses(pkg);
            for (Class<?> aClass : classes1) {

                boolean annotationPresent = aClass.isAnnotationPresent(RuleAnnotation.class);
                System.out.println("class name: " + aClass.getName());
                System.out.println("ruleAnnotation Present: " + annotationPresent);
                if (annotationPresent) {
                    Object object = null;
                    try {
                        object = aClass.newInstance(); // todo: 对象池
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    RuleAnnotation ruleannotation = aClass.getAnnotation(RuleAnnotation.class);
                    int ruleOrder = ruleannotation.order();
                    Method[] methods = aClass.getMethods();
                    Map<Integer, Tuple<Object, Method>> methodMap = new TreeMap<>();
                    for (Method method : methods) {
                        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                        for (Annotation declaredAnnotation : declaredAnnotations) {
                            if ((declaredAnnotation instanceof ActionAnnotation)) {
                                System.out.println("rule name: " + ruleannotation.name() + ", action order: " + ((ActionAnnotation) declaredAnnotation).order());
                                int actionOrder = ((ActionAnnotation) declaredAnnotation).order();
                                methodMap.put(actionOrder, new Tuple<>(object, method));
                            }
                        }
                        System.out.println("methodMap.size: " + methodMap.size());
                    }
                    System.out.println("ruleOrder: " + ruleOrder);
                    allRuleMethodMap.put(ruleOrder, methodMap);
                }
            }
        }
        return groupRuleMethodMap;
    }

    private void runMethod(Map<Integer, Map<Integer, Tuple<Object, Method>>> allRuleMap) {
        Iterator<Map.Entry<Integer, Map<Integer, Tuple<Object, Method>>>> iterator = allRuleMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Map<Integer, Tuple<Object, Method>>> methodInRule = iterator.next();
            Iterator<Map.Entry<Integer, Tuple<Object, Method>>> method = methodInRule.getValue().entrySet().iterator();
            while (method.hasNext()) {
                Map.Entry<Integer, Tuple<Object, Method>> next = method.next();
                Object object = next.getValue().k;
                Method v = next.getValue().v;
                try {
                    v.invoke(object);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() {
        Object instance = getInstanceByClazz(clazztWithDriverAnnotation);
        Map<Integer, Map<Integer, Tuple<Object, Method>>> allRuleMap = allMethodRuleMap(instance);
        runMethod(allRuleMap);
    }



    /**
     * 从包package中获取所有的Class
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(String pack) {

        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(
                    packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    System.err.println("file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, true, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class.forName(packageName + '.' + className));
                                    } catch (ClassNotFoundException e) {
                                        // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath,
                                                        final boolean recursive,
                                                        Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        assert dirfiles != null;
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
