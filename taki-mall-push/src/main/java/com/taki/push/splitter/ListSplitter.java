package com.taki.push.splitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName ListSplitter
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 20:17
 * @Version 1.0
 */
public class ListSplitter implements Iterator<List<String>> {

    /**
     * 设置每一个batch最多不超过800k，因为rocketmq官方推荐，不建议长度超过1MB，
     * 而封装一个rocketmq的message，包括了messagebody,topic，addr等数据，所以我们这边儿设置的小一点儿
     */
    private int sizeLimit = 800 * 1024;

    private final List<String> messages;

    private int currIndex;

    private int batchSize = 100;

    public ListSplitter(List<String> messages) {
        this.messages = messages;
    }

    public ListSplitter(List<String> messages, int batchSize) {
        this.messages = messages;
        this.batchSize = batchSize;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<String> next() {
        int nextIndex = currIndex;
        int totalSize = 0;

        for (; nextIndex < messages.size(); nextIndex++) {
            String message = messages.get(nextIndex);
            //获取每条message的长度
            int tmpSize = message.length();

            if (tmpSize  > sizeLimit){
                if (nextIndex - currIndex == 0){
                    nextIndex ++ ;
                }
            break;
            }

            if (tmpSize +totalSize > sizeLimit || (nextIndex - currIndex) == batchSize){
                break;
            }else{
                totalSize += tmpSize;
            }
        }
            List<String> sublist = messages.subList(currIndex,nextIndex);
            currIndex = nextIndex;

            return  sublist;
            


    }

}
