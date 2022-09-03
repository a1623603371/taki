package com.taki.consistency.exception;

/**
 * @ClassName ConsistencyException
 * @Description TODO
 * @Author Long
 * @Date 2022/8/31 23:37
 * @Version 1.0
 */
public class ConsistencyException  extends RuntimeException{

    public ConsistencyException() {
    }

    public ConsistencyException(String message) {
        super(message);
    }

    public ConsistencyException(Exception e) {
        super(e);
    }
}
