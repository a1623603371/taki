package com.taki.process.engine.process;

/**
 * @ClassName AbstractProcessor
 * @Description 抽象流程，提供了流程包装功能
 * template method
 * 通过定义一个process方法，定义了各个子方法的运行流程h额顺序
 *各个子方法都提供了一个空实现，但具体的实现给你的子类覆盖实现
 *对于子类来说，自把需要执行的子方法覆盖，具体方法运行流程h额调用顺序，都交给抽象类父类
 * @Author Long
 * @Date 2023/4/10 22:41
 * @Version 1.0
 */

public abstract class AbstractProcessor implements Processor {


}
