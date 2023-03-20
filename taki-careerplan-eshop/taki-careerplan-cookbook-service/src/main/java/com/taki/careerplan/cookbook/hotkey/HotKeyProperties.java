package com.taki.careerplan.cookbook.hotkey;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName HotKeyProperties
 * @Description TODO
 * @Author Long
 * @Date 2023/3/3 20:54
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "hotkey")
public class HotKeyProperties {

    private String appName;

    private String etcdServer;

    private Integer caffeineSize;

    private Long pushPeriod;
}
