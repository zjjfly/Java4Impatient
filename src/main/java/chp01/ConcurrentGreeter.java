package chp01;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

class ConcurrentGreeter {
    class CcGreeter extends Greeter {
        @Override
        public void greet() {
            //引用包裹类型的实例方法
            ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(
                    1,
                    new BasicThreadFactory.Builder()
                            .namingPattern(
                                    "my-thread-%d")
                            .daemon(true)
                            .build());
            poolExecutor.execute(ConcurrentGreeter.this::greet);
            poolExecutor.shutdown();
        }
    }

    private void greet() {
        System.out.println("Out Hello World");
    }
}
