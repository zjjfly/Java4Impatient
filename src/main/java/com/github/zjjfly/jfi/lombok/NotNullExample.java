package com.github.zjjfly.jfi.lombok;


/**
 * @author zjjfly
 */
public class NotNullExample {
    public static void main(String[] args) {
        try {
            Person1 p1=new Person1(null);
        }catch (NullPointerException e){
            System.out.println(e);
        }
        try {
            Person1 p1=new Person1("jjzi");
            p1.setName(null);
        }catch (NullPointerException e){
            System.out.println(e);
        }
        try {
            Person2 p2=new Person2(null);
        }catch (NullPointerException e){
            System.out.println(e);
        }
        try {
            Person2 p2=new Person2("jjzi");
            p2.setName(null);
        }catch (NullPointerException e){
            System.out.println(e);
        }
    }

    @Data
    static class Person1{
        @NonNull
        private String name;
    }

    static class Person2{
        private String name;

        Person2(@NonNull String name) {
            this.name = name;
        }

        public void setName(@NonNull String name) {
            this.name = name;
        }
    }
}
