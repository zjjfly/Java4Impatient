package chp01;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ConcurrentGreeter {
    class CcGreeter extends Greeter {
        @Override
        public void greet() {
            //引用包裹类型的实例方法
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-thread-%d").build();
                    ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                                                                                 new LinkedBlockingDeque<>(1024), threadFactory,
                                                                                 new ThreadPoolExecutor.AbortPolicy());
            singleThreadPool.execute(ConcurrentGreeter.this::greet);
            singleThreadPool.shutdown();
        }
    }
    private void greet(){
        System.out.println("Out Hello World");
    }
}
