package com.taki.generator.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;


import java.sql.Connection;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName CodeGenerator
 * @Description 自动生成代码配置
 * @Author Long
 * @Date 2021/11/26 14:30
 * @Version 1.0
 */
public class CodeGenerator {

    private static final  String url = "jdbc:mysql://192.168.33.11:3306/taki-mall?useUnicode=true&characterEncoding=utf-8";


    //private static final String url2 = "jdbc:mysql://49.232.128.89:3306/sonasonic_sit?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai";

    private static final  String username = "root";
    //private static final  String username2 = "root";

   private static final  String passwrod = "Pzk2020@";
   // private static final  String passwrod2 = "M98_so892@l1UIG";

    private static final  String database = "taki-mall";
   // private static final  String database2 = "sonasonic_sit";


    private static  final DataSourceConfig.Builder DATA_SOURCE_CONFIG =
            new DataSourceConfig.Builder(url,username,passwrod)
                    .dbQuery(new MySqlQuery())
                    .schema(database)
                    .typeConvert(
                            new MySqlTypeConvert()
//                            {
//                        @Override
//                        public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
//                            if (fieldType.equals("datetime")){
//                                return DbColumnType.DATE;
//                            }
//                            return super.processTypeConvert(config, fieldType);
//                        }
//                    }
                    )
                    .keyWordsHandler(new MySqlKeyWordsHandler());

//    public void before(){
//        Connection conn = DATA_SOURCE_CONFIG.build().getConn();
//
//
//    }


    private static  String[] tables = new String[]{
            //"membership",
          //  "short_message_platform"
             //   "membership"
            //"t_rmbs_paytype"
           // "t_rule_bizpolicy","t_rule_bizpolicy_item","t_rule_bizpolicy_scope","t_rule_bizpolicy_scope_item"
            //"t_rule_bizrule"
            "order_delivery_detail"
    };


    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String path = projectPath+"/code";


        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                .globalConfig(builder -> {
                    builder.author("long") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(path); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.taki") // 设置父包名
                            .moduleName("module")
                            .entity("domain.entity")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .controller("controller") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://")); // 设置mapperXml生成路径
                })

                .strategyConfig(builder -> {
                    builder.addInclude(tables)// 设置需要生成的表名
                            .entityBuilder() // 设置 实体类
                            .superClass("com.taki.common.domin.BaseEntity")// 设置实体类父类
                            .enableColumnConstant() // 生成常量
                            .enableChainModel() // 开启链式调用
                            .enableLombok() // 开启lombok 模型
                            .enableRemoveIsPrefix() // 开启  Boolean 类型字段 is 移除
                            .enableTableFieldAnnotation()
                            //.enableActiveRecord()//开启 activeRecord 模型
                            .idType(IdType.AUTO)
                            .formatFileName("%sDO")
                            .controllerBuilder()
                            .enableHyphenStyle()
                            .enableRestStyle()
                            .formatFileName("%sController")
                            .serviceBuilder()
                            .superServiceClass(IService.class)
                            .superServiceImplClass(ServiceImpl.class)
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .mapperBuilder()
                            .superClass(BaseMapper.class)
                            .enableMapperAnnotation()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .formatMapperFileName("%sMapper")
                            .build();

                           // .versionColumnName("version") //乐观锁字段名称
                            //.versionPropertyName("version") // 乐观锁字段属性名
                            //.logicDeleteColumnName("deleted") // 逻辑删除字段(数据库)
                           // .logicDeletePropertyName("deletedFlag") // 逻辑删除字段
                           // .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();






    }


}
