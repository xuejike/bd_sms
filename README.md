#短信发送插件，
目前支持：1.企信通
##1.引入插件
```xml

```
##2.配置短信业务
```xml
    <bean class="com.bidanet.bdcms.plugin.sms.QixintongSmsImpl">
        <property name="userId" value="用户ID"/>
        <property name="pwd" value="用户密码"/>
        <property name="username" value="用户名"/>
    </bean>
```
##3.发送短信
```java
 sms.send("15501****","【***】您的验证码是：{0}","155");

```


