package com.taki.careerplan.cookbook.hotkey;

import com.jd.platform.hotkey.client.ClientStarter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName HotKeyConfig
 * @Description TODO
 * @Author Long
 * @Date 2023/3/3 20:49
 * @Version 1.0
 */

@Component
@Data
@Slf4j
public class HotKeyConfig {

    @Autowired
    private HotKeyProperties hotKeyProperties;

    @PostConstruct
    public void initHotKey(){
    log.info("init hotkey appName:{},etcdSever:{},caffeineSize:{},pushPeriod:{}",hotKeyProperties.getAppName(),hotKeyProperties.getEtcdServer(),
            hotKeyProperties.getCaffeineSize(),hotKeyProperties.getPushPeriod());

        ClientStarter.Builder builder =new ClientStarter.Builder();
       ClientStarter starter =   builder.setAppName(hotKeyProperties.getAppName())
        .setEtcdServer(hotKeyProperties.getEtcdServer())
        .setCaffeineSize(hotKeyProperties.getCaffeineSize())
        .setPushPeriod(hotKeyProperties.getPushPeriod()).build();
        starter.startPipeline();
    }

}
