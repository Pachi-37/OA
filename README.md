# OA系统

办公自动化系统



### 需求

- 采用多用户的 `B/S` 架构
- 主用户分配系统账户，下级用户使用该账户登录系统
- 采用分级定岗，一共分为八级



### 具体要求

- `6` 级含以下为 `业务岗`
- `7` 级 `部门经理`
- `8` 级 `总经理`



![image-20220223143043463](imgs/image-20220223143043463.png)



### 框架及组件

![image-20220223143409856](imgs/image-20220223143409856.png)



### `RBAC`

基于角色的权限控制是面向企业安全策略的访问控制方式

核心思想：将控制访问的资源和角色绑定



### `md5` 摘要算法

`MD5` 可以产生一个 128 位的散列值用于唯一标识原数据



- 压缩性，`MD5` 生成的摘要长度固定
- 抗修改
- 不可逆，无法通过反向推算源数据

> `Commons-Codec`
> `Apache` 提供的编码 / 解码组件

```java
String md5 = DigestUtils.md5Hex(String source);
```

##### 敏感数据加盐处理
在散列中任意固定的位置插入特定的字符串，其作用让加盐之后的散列结果和加盐前不同，这个处理可以增加额外的安全性
大部分情况，盐是不需要保密的。盐可以是随机的字符串，插入位置也可以是随意的。如果将来这个散列的结果需要验证(验证用户输入的密码)，则需要将已使用的盐记录下来

### 工作流程表设计
* 请假单表
* 审批任务流程表
* 消息通知表

##### 设计表单
* 每一个请假单对应一个审批流程
* 请假单创建之后，按照业务规则生成部门经理、总经理审批任务
* 审批任务只能审批自己辖区的请假申请
* 所有的审批任务通过，代表请假批准
* 任意审批任务驳回，其余任务取消，请假申请驳回
* 请假流程中的任意操作都要生成对应的系统通知

### 请假单流程服务
1. 持久化 form 表单数据，8级以下员工表状态为 processing，8级(总经理) 状态为 approved
2. 增加第一条流程数据，说明表单已经被提交，状态 complete
3. 分级判断以下条件
   1. 7 级以下员工，生成部门经理审批任务，请假时间如果大于36小时，生成总经理审批任务
   2. 7 级员工，生成总经理审批任务
   3. 8 级员工，生成总经理审批任务，系统自动通过

##### `sweetalter`
类似于 `layui`，用来替代 `javaScript` 弹出框

### 审批程序
1. 部门经理审批通过
   * 时间超过规定审批时间，更改总经理审批状态
   * 未超过规定的审批时间，完成审批任务
2. 部门经理审批未通过
   * 未超过规定时间，申请驳回
   * 超过规定时间，总经理审批任务取消
> 按中间节点审批状态和最后节点审批状态分类



### 项目部署

安装两台 `Centos7` `Centos-DB` `Centos-WEB`



##### 安装 `mysql 8`

- 使用 `wget` 下载 `mysql` 安装包
- 本地安装源 `yum localinstall -y *.rpm`
- 安装 `Mysql` `yum install -y mysql-community-server`



##### 初始化 `mysql`

- 设置密码

  - `vi /var/log/mysqld.log`
    - 查看默认生成密码
  - `alter user 'root'@'localhost' identified with mysql_native_password by '[password]'`

  > `with mysql_native_password` 考虑到兼容早期版本

- 设置允许远程登录

  - `update user set host='%' where user='root'`
  - `flush priviledges`

- 放行防火墙

  - `firewall-cmd --zone=public --permanent --add-port=3306/tcp`
  - `firewall-cmd --zone=public --permanent --add-rich-rule="rule family="ipv4" source address="[ip]" port protocol="tcp" port="3306" accept "`
    - 设置防火墙对指定 `ip` 指定端口放行



##### 配置 `centos-web`

- 配置 `java` 环境
- 安装 `Tomcat`
- 解压项目 `.war`
- 更改项目配置文件 `Mybatis-config.xml`
- 配置 `Tomcat/conf/server.xml`
  - 更改端口 `80`
  - 配置默认的项目
    - `<Context path="/" docBase="[项目地址]">`
  - 启动 `Tomcat`
  - 暴露 `80` 端口

#更新部分
1. 行政管理
   1. 员工入职
   

### 行政管理

##### 员工入职
- 总经理有权给任意部门新添员工
- 部门经理只有权为自己部门添加员工
