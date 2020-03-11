package com.github.zjjfly.jfi.common;

import com.github.zjjfly.jfi.lombok.AllArgsConstructor;
import com.github.zjjfly.jfi.lombok.Data;

/**
 * @author zjjfly
 */
@Data
@AllArgsConstructor
public class City {
    private String name;
    private String state;
    private int population;
}
