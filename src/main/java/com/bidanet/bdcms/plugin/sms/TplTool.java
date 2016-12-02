package com.bidanet.bdcms.plugin.sms;

/**
 * Created by xuejike on 2016/12/1.
 */
public class TplTool {
    public static String build(String msgTpl,String... params){
        if (params!=null){
            for (int i = 0; i < params.length; i++) {
                msgTpl=msgTpl.replaceAll("{"+i+"}",params[i]);
            }
        }
        return msgTpl;
    }
}
