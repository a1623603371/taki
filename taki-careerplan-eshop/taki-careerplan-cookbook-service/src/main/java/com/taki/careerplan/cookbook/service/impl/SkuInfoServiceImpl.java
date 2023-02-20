package com.taki.careerplan.cookbook.service.impl;

import com.google.common.collect.Lists;
import com.taki.careerplan.cookbook.service.SkuInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
public class SkuInfoServiceImpl  implements SkuInfoService {


    /**
     * 商品中心，标签对应的商品
     * 模拟商品中心
     */
    private static Map<String, List<Long>> tagGoodsSkuIdMap = new HashMap<String, List<Long>>() {{
        put("酱油", Lists.newArrayList(10001L, 10002L));
        put("盐", Lists.newArrayList(20001L, 20002L));
        put("油", Lists.newArrayList(30001L, 30002L));
        put("菜", Lists.newArrayList(40001L));
    }};

    @Override
    public List<Long> getSkuIdsByTags(List<String> tags) {

        List<Long> skuList = new ArrayList<>();

        tags.forEach(tag ->{
            List<Long> tagsSkuIds = tagGoodsSkuIdMap.get(tag);
            skuList.addAll(tagsSkuIds);
        });

        return skuList;
    }
}
