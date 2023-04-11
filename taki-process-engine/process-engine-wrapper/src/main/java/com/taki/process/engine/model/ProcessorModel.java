package com.taki.process.engine.model;

import com.taki.process.engine.instance.ProcessorInstanceCreator;
import com.taki.process.engine.node.ProcessorDefinition;
import com.taki.process.engine.node.ProcessorNode;
import com.taki.process.engine.process.Processor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ProcessorModel
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 20:32
 * @Version 1.0
 */
@Data
public class ProcessorModel {
    private String name;

    private Map<String,ProcessorNodeModel> nodes = new HashMap<>();

    /***
     * @description: 添加一个节点
     * @param processorNodeModel node节点信息
     * @return  void
     * @author Long
     * @date: 2023/4/11 20:39
     */
    public void addNode(ProcessorNodeModel processorNodeModel){
        if (nodes.containsKey(processorNodeModel.getName())){
            throw new IllegalArgumentException("同一个流程不能定义多个相同的id节点");
        }
        nodes.put(processorNodeModel.getName(), processorNodeModel);
    }


    /*** 
     * @description: 检查配置是否合法
     * 1.classname是否合法
     * 2.上下节点依赖是否正确
     * 3.是否仅一个开始节点
     *
     *
     * @param
     * @return  void
     * @author Long
     * @date: 2023/4/11 20:39
     */ 
    public void check(){
        int startNode = 0;
        for (ProcessorNodeModel processorNodeModel : nodes.values()) {

            String className = processorNodeModel.getClassName();
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("无法加载节点【" + processorNodeModel.getName() + "]的类:" + className);
            }

            String nextNode = processorNodeModel.getNextNode();

            if (nextNode != null){
            String[] nextNoes = nextNode.split(",");

                for (String nodeName : nextNoes) { // next的node节点names，必须在你的流程定义里定义其他的bean name
                    if (!nodes.containsKey(nextNode)){
                        throw new IllegalArgumentException("节点【name= "+ nodeName + "]不存在" );
                    }
                    
                }
            }

            //如果你的process node 设置了begin=true ，酒吧startNode数量++
            if (processorNodeModel.isBegin()){
                startNode ++;
            }

        }

        // 这里设置begin= true的节点数量大于1，此时不等于1，或者小于1，没有设置begin节点
        // 这2中情况都是不对的，有且只有一个开始节点
        if (startNode != 1){
            throw new IllegalArgumentException("不合法的流程，每个流程只能有一个开始节点");
        }

    }


    public ProcessorDefinition build (ProcessorInstanceCreator processorInstanceCreator) throws Exception {
            Map<String,ProcessorNode> processorNodeMap = new HashMap<>();

            ProcessorDefinition processorDefinition = new ProcessorDefinition();

            processorDefinition.setName(processorDefinition.getName());

        for (ProcessorNodeModel processorNodeModel : nodes.values()) {
                String className = processorNodeModel.getClassName();

            // 根据我们的流程节点的class，以及流程节点的name，在这里使用流程节点构造器，去构造流程节点对应的Processor
            // 没有spring容器，就可以把每个节点的class通过反射构建为一个实例对象，然后把实例对象转换为接口类型就可以了
            // 如果有spring容器，一般来说我们都是基于spring环境去跑的，就是直接根据你的class类型，去spring容器里获取这个class类型对应的bean实例
            // 这个bean实例，他就是你的业务代码里自己定义的spring bean，强转为接口类型
                Processor processor = processorInstanceCreator.newInstance(className,name);
                ProcessorNode processorNode  = new ProcessorNode();//ProcessorNode代表一个可以运行的流程节点
                processorNode.setProcessor(processor);
                processorNode.setName(processorNode.getName());

                if (processorNodeModel.isBegin()){//如果说你的流程节点数据模型里的begin属性他是true，此时就把你的ProcessorNode设置为
                 // 设置为第一个流程节点
                processorDefinition.setFist(processorNode);
                }
            // 还会把你的方法调用模式，sync还是async，设置到你的ProcessorNode里去
                processorNode.setInvokeMethod(processorNodeModel.getInvokeMethod());
                processorNodeMap.put(processorNodeModel.getName(), processorNode);
            }

        processorNodeMap.values().forEach(processorNode -> {

            String nextNodeStr = nodes.get(processorNode.getName()).getNextNode();

            if (nextNodeStr == null){
                return;
            }

            String [] nextNodes = nextNodeStr.split(",");

            for (String nextNode : nextNodes) {
                processorNode.addNextNode(processorNodeMap.get(nextNode));
            }
        });

        // 可运行的一个流程
        return processorDefinition;

    }

}
