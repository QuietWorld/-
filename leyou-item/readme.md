## 商品微服务
### 明确：  
> 当我们在写一个接口之前都应该去明确：
>* 接口的请求方式
>* 接口的请求参数
>* 接口的请求url
>* 接口的返回值

### java对象和json对象的对应关系：
| java | json |
|:----:|:----:|
|List集合|数组|
|Map集合|{{name:"张三",age:22},{name:"李四",age:33}}|
|普通java对象|普通json对象| 


### leyou-item-service:  
> 提供对外的访问商品微服务的接口，完成对操作的业务功能。

**注意：**  本项目使用MySQL是8+版本，从MySQL6开始MySQL的驱动类从
原先的`com.mysql.jdbc.Driver`变成了`com.mysql.cj.jdbc.Driver`，
并且在连接MySQL服务器的时候需要指定时区，以下是新版MySQL的四大参数配置:  
```
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql:///yun6?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=CONVERT_TO_NULL
username: root
password: root
```
### leyou-item-interface:
> 放置商品微服务的通用pojo或者异常类等。将来如果有人需要调用
> 该服务就可以引入leyou-item-interface的GAV(依赖)而不必
> 自己的模块中也去创建pojo。