package com.github.zjjfly.jfi.proxy;

/**
 * @author zjjfly
 */
public class Hello implements HelloInterface {
    @Override
    public void hello() {
        System.out.println("hello");
    }
}
