package com.github.zjjfly.jfi.lombok;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zjjfly
 */
public class CleanUpExamples {
    public static void main(String[] args) throws IOException {
        //@Cleanup会自动关闭打开的资源
        @Cleanup InputStream in = new FileInputStream(args[0]);
        @Cleanup OutputStream out = new FileOutputStream(args[1]);
        byte[] b = new byte[10000];
        while (true) {
            int r = in.read(b);
            if (r == -1) {
                break;
            }
            out.write(b, 0, r);
        }

        @Cleanup("destroy")  Cache cache = new Cache();
        cache.put("jjzi",28);
        System.out.println(cache.get("jjzi"));
    }

    static class Cache{
        private static final Map<String,Integer> CACHE_MAP=new ConcurrentHashMap<>();

        public void put(String name,Integer age){
            CACHE_MAP.put(name,age);
        }

        public Integer get(String name){
            return CACHE_MAP.get(name);
        }

        public void destroy(){
            System.out.println("clean cache");
            CACHE_MAP.clear();
        }
    }
}
