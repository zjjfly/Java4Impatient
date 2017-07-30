package chp01;

class ConcurrentGreeter {
    class CGreeter extends Greeter {
        @Override
        public void greet() {
            //引用包裹类型的实例方法
            new Thread(ConcurrentGreeter.this::greet).start();
        }
    }
    private void greet(){
        System.out.println("Out Hello World");
    }
}
