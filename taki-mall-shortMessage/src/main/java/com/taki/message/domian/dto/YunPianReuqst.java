package com.taki.message.domian.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName YunPianReuqst
 * @Description 云片 发送短信 请求
 * @Author Long
 * @Date 2021/12/6 15:45
 * @Version 1.0
 */
@Data
@Builder
public class YunPianReuqst {
    //用户唯一标识，在"账号设置"-"子帐号管理"中查看 必填: 是
    private  String apikey;
    //接收的手机号，仅支持单号码发送，不需要带+86 前缀 必填: 是
    private String mobile;
    //接收的手机号，仅支持单号码发送，不需要带+86 前缀  必填: 是
    private String text;
    //下发号码扩展号，纯数字 必填: 否
    private  String extend;
    //该条短信在您业务系统内的 ID，如订单号或者短信发送记录流水号。 必填: 否
    private String uid;
    // 短信发送后将向这个地址推送(运营商返回的)发送报告。 如推送地址固定，建议在"数据推送与获取”做批量设置。 如后台已设置地址，且请求内也包含此参数，将以请求内地址为准 必填: 否
    private String callback_url;
    // 	是否为注册验证码短信，如果传入 true，则该条短信作为注册验证码短信统计注册成功率，需联系客服开通。 必填: 否
    private Boolean register;
    // 若短信中包含云片短链接，此参数传入 true 将会把短链接替换为目标手机号的专属链接，用于统计哪些号码的机主点击了短信中的链接，可在云片后台查看。详情参考短信点击统计。 必填: 否
    private Boolean mobile_stat;


}
