package com.code.webflux.utils;

import com.code.webflux.exceptions.CheckException;

import java.util.stream.Stream;

/**
 * Created by yankefei on 2018/12/2.
 */
public class CheckUtil {

    private final static String[] INVALID_NAME = {"admin", "root"};

    /**
     * 校验名字，不成功抛出校验异常
     * @param name
     */
    public static void checkName(String name) {
        Stream.of(INVALID_NAME).filter(value -> value.equalsIgnoreCase(name))
                .findAny().ifPresent(value -> {
                    throw new CheckException("name", value);
        });
    }

}
