package com.github.zjjfly.jfi.lombok;


import lombok.SneakyThrows;

import java.io.UnsupportedEncodingException;

/**
 * SneakyThrowsExample类似Scala的@Throw,谨慎使用
 * @author zjjfly
 */
public class SneakyThrowsExample implements Runnable {
    @SneakyThrows(UnsupportedEncodingException.class)
    public String utf8ToString(byte[] bytes) {
        return new String(bytes, "UTF-8");
    }

    @Override
    @SneakyThrows
    public void run() {
        throw new Throwable();
    }
}
