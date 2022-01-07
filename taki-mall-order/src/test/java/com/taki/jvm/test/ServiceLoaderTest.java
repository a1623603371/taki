package com.taki.jvm.test;

import com.alibaba.druid.proxy.DruidDriver;

import java.sql.Driver;
import java.util.ServiceLoader;

/**
 * @ClassName ServiceLoaderTest
 * @Description TODO
 * @Author Long
 * @Date 2022/1/6 17:22
 * @Version 1.0
 */
public class ServiceLoaderTest {


    public static void main(String[] args) throws ClassNotFoundException {

        ServiceLoader<Driver> serviceLoader = ServiceLoader.load(Driver.class);

        while (serviceLoader.iterator().hasNext()){
            Driver driver = serviceLoader.iterator().next();

            System.out.println("driver class:" +driver.getClass() + "driver class loader"+ driver.getClass().getClassLoader());

        }

//        Driver driver = Class.forName("");
//
//        System.out.println("driver class loader :" + driver.getClass().getClassLoader());
    }
}
