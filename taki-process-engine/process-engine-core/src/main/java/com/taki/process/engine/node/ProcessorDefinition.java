package com.taki.process.engine.node;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProcessorDefinition
 * @Description TODO
 * @Author Long
 * @Date 2023/4/10 22:50
 * @Version 1.0
 */
@Getter
@NoArgsConstructor
public class ProcessorDefinition {

    private String name;
    /**
     * 初始节点
     */
    private ProcessorNode fist;


    public ProcessorDefinition(ProcessorNode fist) {
        this.fist = fist;
    }


    public void  setName(String name) {
        this.name= name;
    }

    public void setFist(ProcessorNode node){
        this.fist = node;
    }


    public String toStr(){
        StringBuilder sb = new StringBuilder();
        buildStr(sb,fist);
        return sb.toString();
    }

    private void buildStr (StringBuilder sb,ProcessorNode node) {

        node.getNextNodes().values().forEach(processorNode -> {
            sb.append(node.getName()).append("->").append(processorNode.getName()).append("\n");
            buildStr(sb,processorNode);
        });


    }
}
