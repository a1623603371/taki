package com.taki.process.engine.util;

import com.taki.process.engine.node.ProcessorNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ProcessorUtils
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 16:46
 * @Version 1.0
 */
public class ProcessorUtils {

    private static ExecutorService DEFAULT_POOL = new ThreadPoolExecutor(0,Integer.MAX_VALUE,60, TimeUnit.MICROSECONDS,new SynchronousQueue<>());

    public static boolean hasRing(ProcessorNode  node){
        return hasRing(node,new HashSet<>());
    }


    private static boolean hasRing(ProcessorNode processorNode, Set<String>idSet){
        Map<String, ProcessorNode> nextNodes = processorNode.getNextNodes();

        if (nextNodes == null || nextNodes.isEmpty()){
            return  false;
        }else {
            idSet.add(processorNode.getName());
            boolean ref = false;
            for (Map.Entry<String, ProcessorNode> entry : nextNodes.entrySet()) {
                ProcessorNode value = entry.getValue();

                if (idSet.contains(value.getName())){
                    return true;
                }else {
                    idSet.add(value.getName());
                    ref = ref || hasRing(value,new HashSet<>(idSet));
                }
            }
            return ref;
        }

    }
}
