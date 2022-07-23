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
     * 完成订单创建发送事务消息topic
     */
    public static  final  String CREATE_ORDER_SUCCESS_TOPIC = "create_order_success_topic";

    /**
     * 完成订单创建 consumer 分组
     */
    public static  final String CREATE_ORDER_SUCCESS_CONSUMER_GROUP = "create_order_success_consumer_group";

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
     *完成订单支付 consumer topic
     */
    public static  final   String PAID_ORDER_SUCCESS_TOPIC = "paid_order_success_topic";


    /**
     * 完成订单支付 producer 分组
     */
    public static  final    String PAID_ORDER_SUCCESS_PRODUCER_GROUP = "paid_order_success_producer_group";

    /**
     * 完成订单支付 consumer 分组
     */
    public static  final    String PAID_ORDER_SUCCESS_CONSUMER_GROUP = "paid_order_success_consumer_group";

    /**
     * 触发订单履约发送事务消息topic
     */
    public static  final  String TRIGGER_ORDER_FULFILL_TOPIC = "trigger_order_fulfill_topic";


    /**
     * 触发订单履约发送事务消息 consumer 分组
     */
    public  static final String TRIGGER_ORDER_FULFILL_PRODUCER_GROUP = "trigger_order_fulfill_producer_group";




    /**
     * 触发订单履约发送事务消息 consumer 分组
     */
    public  static final String TRIGGER_ORDER_FULFILL_CONSUMER_GROUP = "trigger_order_fulfill_consumer_group";




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
     * 取消 订单 发送退款 请求 topic
     */
    public static final  String CANCEL_REFUND_REQUEST_TOPIC = "cancel_refund_request_topic";

    /**
     * 实际 退款 topic
     */
    public static  final  String ACTUAL_REFUND_TOPIC = "actual_refund_topic";

    /**
     * 监听实际 退款 producer 分组
     */
    public static final  String ACTUAL_REFUND_PRODUCER_GROUP = "actual_refund_producer_group";

    /**
     * 监听实例 退款 consumer分组
     */
    public static final String ACTUAL_REFUND_CONSUMER_GROUP = "actual_refund_consumer_group";

    /**
     *监听退款请求分组
     */
    public static  final String REQUEST_CONSUMER_GROUP = "request_consumer_group";


    /**
     * 监听释放权益资产 producer 分组
     */
    public static  final  String RELEASE_PROPERTY_PRODUCER_GROUP = "release_property_producer_Group";

    /**
     * 监听释放权益资产 consumer 分组
     */
    public static  final  String RELEASE_PROPERTY_CONSUMER_GROUP = "release_property_consumer_Group";



    /**
     * 监听释放库存 分组
     */
    public static  final  String   RELEASE_INVENTORY_CONSUMER_GROUP = "release_inventory_consumer_group";

    /**
     * 监听释放资产 PRODUCER 分组
     */
    public static  final  String    RELEASE_ASSETS_PRODUCER_GROUP = "release_assets_producer_group";


    /**
     * 监听释放资产 CONSUMER 分组
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

    /**
     * 取消履约 topic
     */
    public static  final  String CANCEL_FULFILL_TOPIC = "cancel_fulfill_topic";

    /**
     * 取消 履约 consumer 分组
     */
    public static  final    String CANCEL_FULFILL_CONSUMER_GROUP = "cancel_fulfill_consumer_group";

    /**
     *售后申请发送给客户 审核 topic
     */
    public  static  final  String AFTER_SALE_CUSTOMER_AUDIT_TOPIC = "after_sale_customer_audit_topic";

    /**
     * 监听客服审核申请分组
     */
    public static  final String AFTER_SALE_CUSTOMER_AUDIT_GROUP = "after_sale_customer_audit_group";

    /**
     *客服审核通过后发送释放资产topic
     */
    public static   final String CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_TOPIC = "customer_audit_pass_release_assets_topic";


    /**
     * 监听客服审核通过发送释放资产 producer 分组
     */
    public static  final  String CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_PRODUCER_GROUP = "customer_audit_pass_release_assets_producer_group";

    /**
     * 监听客服审核通过发送释放资产 consumer 分组
     */
    public static  final  String CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_CONSUMER_GROUP = "customer_audit_pass_release_assets_consumer_group";


    /**
     * 缺品处理 producer 分组
     */
    public  static  final  String LACK_ITEM_PRODUCER_GROUP = "lack_item_producer_group";
}

