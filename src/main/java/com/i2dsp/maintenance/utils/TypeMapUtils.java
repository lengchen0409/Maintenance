package com.i2dsp.maintenance.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 保养类型常量封装类
 * @author : 梁海聪
 * @since : 2021/07/05 10:41
 */

public class TypeMapUtils {

    public final static Map<String, Integer> typeConstants = new HashMap<String, Integer>() {
        {
            put("d", 1);
            put("w", 7);
            put("m", 30);
            put("y", 365);
        }
    };
}
