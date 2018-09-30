package chp06;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Java8对ConcurrentHashMap做了很多增强.比如,把底层的数据结构换成了红黑树
 * @author zjjfly
 */
public class EnhancedConcurrentHashMap {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(100);
        //如果包含的键值对太多,int可能会不足以表达,Java8加入了mappingCount用与此(size方法返回的是int)
        System.out.println(map.mappingCount());
    }
}
