package com.bidanet.bdcms.plugin.sms;

import java.util.List;

/**
 * Created by xuejike on 2016/12/1.
 */
public interface Sms {
    /**
     * 发送单人短信
     * @param tel 接收手机号
     * @param msgTpl 短信模板（参数用{0}、{1}作为占位符）
     * @param params 参数
     */
    void send(String tel,String msgTpl,String... params);

    /**
     * 群发短信
     * @param tel 手机号
     * @param msgTpl 信息模板（参数用{0}、{1}作为占位符）
     * @param params 参数
     */
    void sendAll(List<String> tel,String msgTpl,String... params);
}
