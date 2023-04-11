package com.taki.process.engine.process;

/**
 * @ClassName DynamicProcessor
 * @Description TODO
 * @Author Long
 * @Date 2023/4/10 23:03
 * @Version 1.0
 */
public abstract class DynamicProcessor extends AbstractProcessor{

 /*** 
  * @description:  获取下一个节点
  * @param context 上下文
  * @return  java.lang.String
  * @author Long
  * @date: 2023/4/11 15:25
  */    
 protected abstract String nextNodeId(ProcessContext context);

}
