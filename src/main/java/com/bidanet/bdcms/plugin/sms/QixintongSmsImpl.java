package com.bidanet.bdcms.plugin.sms;

import com.bidanet.bdcms.plugin.sms.exception.SmsException;
import com.bidanet.bdcms.plugin.sms.exception.SysSmsException;
import com.bidanet.bdcms.plugin.sms.exception.UserSmsException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.net.URLCodec;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

/**
 * 企信通 短信发送接口
 */
public class QixintongSmsImpl implements Sms {
    protected String sendUrl="http://42.96.248.183:8080/sendsms.php";

    protected static HashMap<String,String> errorMsgMap;
    static {
        errorMsgMap=new HashMap<>();
        errorMsgMap.put("-1","账号/密码或企业ID错误");
        errorMsgMap.put("-2","缺少企业账号");
        errorMsgMap.put("-3","缺少密码");
        errorMsgMap.put("-4","缺少短信内容");
        errorMsgMap.put("-5","缺少目标号码");
        errorMsgMap.put("-7","短信内容过长(小灵通最大56个字)");
        errorMsgMap.put("-8","含有非法字符，第二行返回非法关键词（换行使用\\r\\n）");
        errorMsgMap.put("-9","目标号码格式错误，或者包含错误的手机号码");
        errorMsgMap.put("-10","超过规定发送时间，禁止提交发送");
        errorMsgMap.put("-12","余额不足");
        errorMsgMap.put("-14","号码超过发送数量限制");
        errorMsgMap.put("-15","发送内容前面需加签名，例如【XXX公司】，签名必须放在最前面，如果内容的编码错误，出现乱码，识别不出签名【XXX公司】，也会返回-15");
        errorMsgMap.put("-16","提交号码数量小于最小提交量限制");
        errorMsgMap.put("-20","未开通接口");
        errorMsgMap.put("-22","短信内容签名不正确");
        errorMsgMap.put("-23","IP鉴权失败");
        errorMsgMap.put("-24","缺少企业ID");
        errorMsgMap.put("-99","连接失败");
        errorMsgMap.put("-100","系统内部错误");
    }
    /**
     * 企业ID
     */
    protected String userId;
    /**
     * 企业账号
     */
    protected String username;
    /**
     * 密码
     */
    protected String pwd;
    protected String md5Pwd;


    @Override
    public void send(String tel, String msgTpl, String... params) {
        String message = TplTool.build(msgTpl, params);
        sendSms(tel,message);
    }

    @Override
    public void sendAll(List<String> tel, String msgTpl, String... params) {
        String joinTel = String.join(",", tel);
        sendSms(joinTel,TplTool.build(msgTpl,params));
    }
    protected void sendSms(String mobile,String msg)  {
        URLCodec urlCodec = new URLCodec("GBK");
        if (md5Pwd==null){
            //16位md5
            md5Pwd= DigestUtils.md5Hex(pwd).substring(8,24);
        }
        try {
            String encode = urlCodec.encode(msg);
            HttpResponse<String> response = Unirest.get(
                    "http://42.96.248.183:8080/sendsms.php?" +
                            "userid="+userId +
                            "&username="+username +
                            "&passwordMd5="+md5Pwd +
                            "&mobile="+mobile +
                            "&message="+encode
            ).asString();
            String body = response.getBody();
            long result = Long.parseLong(body);
            if(result>0){

            }else{
                if (body.equals("-14")||body.equals("-9")){
                    String s = errorMsgMap.get(body);
                    throw new UserSmsException(body+"=>"+s);
                }else{
                    String s = errorMsgMap.get(body);
                    throw new SysSmsException(body+"=>"+s);
                }

            }



        } catch (UnirestException e) {
            e.printStackTrace();
            throw new SmsException("网络异常");

        } catch (EncoderException e) {
            e.printStackTrace();
            throw new SmsException("短信内容编码异常");
        }
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
