package com.taki.common.constants;

/**
 * @ClassName RocketMQConstant
 * @Description RocketMQ 常量
 * @Author Long
 * @Date 2022/1/12 10:40
 * @Version 1.0
 */
public class RocketMQConstant {

    /**
     * 默认的producer 分组
     */
    public static final String ORDER_DEFAULT_PRODUCER_GROUP = "order_default_producer_group";

    /**
     * 支付订单超时自动关单发送延时消息 topic
     */
    public static final  String PAY_ORDER_TIMEOUT_DELAY_TOPIC = "pay_order_timeout_delay_topic";

    /**
     * 支付订单超时 自动关单 consumer 分组
     */
    public static  final String PAY_ORDER_TIME_DELAY_CONSUMER_GROUP = "pay_order_time_delay_consumer_group";

    /**
     *完成订单支付 consumer 分组
     */
    public static  final   String PAID_ORDER_SUCCESS_TOPIC = "paid_order_success_topic";

    /**
     * 完成订单支付 consumer 分组
     */
    public static  final    String PAID_ORDER_SUCCESS_CONSUMER_GROUP = "paid_order_success_consumer_group";

    /**
     *取消订单 发送释放 权益 topic
     */
    public static final  String RELEASE_ASSETS_TOPIC = "release_assets_topic";

    /**
     * 取消订单 发送释放 库存 topic
     */
    public  static final  String CANCEL_RELEASE_INVENTORY_TOPIC = "cancel_release_inventory_topic";

    /**
     * 取消订单 发哦送那个释放权益资产 topic
     */
    public  static final  String CANCEL_RELEASE_PROPERTY_TOPIC = "release_property_topic";

    /**
     * 实际 退款 topic
     */
    public static  final  String ACTUAL_REFUND_TOPIC = "actual_refund_topic";

    /**
     * 监听实例 退款分组
     */
    public static final String ACTUAL_REFUND_CONSUMER_GROUP = "actual_refund_consumer_group";

    /**
     *监听退款请求分组
     */
    public static  final String REQUEST_CONSUMER_GROUP = "request_consumer_group";

    /**
     * 监听释放权益资产分组
     */
    public static  final  String RELEASE_PROPERTY_CONSUMER_GROUP = "release_property_consumer_Group";

    /**
     * 监听释放库存 分组
     */
    public static  final  String   RELEASE_INVENTORY_CONSUMER_GROUP = "release_inventory_consumer_group";

    /**
     * 监听释放资产分组
     */
    public static  final  String    RELEASE_ASSETS_CONSUMER_GROUP = "release_assets_consumer_group";

    /**
     * 正向订单物流配送结果相关topic 信息
     */
    public static  final  String    ORDER_WMS_SHIP_RESULT_TOPIC = "order_wms_ship_result_topic";

    /**
     *正向订单物流配送结果 consumer 分组
     */
    public static  final  String      ORDER_WMS_SHIP_RESULT_CONSUMER_GROUP = "order_wms_ship_result_consumer_group";
}
