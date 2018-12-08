# Spring-Boot-Code-Generator
###uoko 代码生成器（mysql 或者 sqlserver）使用说明使用文档

## 概要

支持代码自动生成，依据数据源生成uoko架构结构代码，依于velocity模板实现数据动态添加，Beautiful 前端页面，支持数据库可选生成

## 特性

-  mysql数据源生成uoko结构代码
-  sqlserver数据生成uoko结构代码

## 配置

 * 修改generator.properties
   * moduleName = push #项目名
   * package=com.uoko #包名
   * author = shaohua # 代码注释上的作者名
   * email = shaohua@uoko.com # 开发者邮箱
   * description=星空推广平台 # 项目描述
   * version=1.0-SNAPSHOT # 项目版本号
   * serverWebApiPort=8002 #webapi端口
   * serverStarterPort=9090  #业务模块biz端口
   * sql-url=192.168.200.24  #生成代码后连接的数据源
   * sql-port=3306  # 数据库端口
   * sql-username=root # 数据库连接账号
   * sql-password=T649yrgp9a6sw2tt  # 数据库密码
   * sql-db=Star-main   # 数据库项目名
   * sql-type=mysql #sqlserver or mysql  控制生成的代码支持类型

 * 修改application.yml 数据源

```
spring:
  datasource:
    url: jdbc:mysql://192.168.200.21:3306/uk_arch_push?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useAffectedRows=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=true
    username: root
    password: T649yrgp9a6sw2t
    type: com.alibaba.druid.pool.DruidDataSource
#    url: jdbc:sqlserver://172.16.10.21:1433;DatabaseName=Star-main
#    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
#    username: user
#    password: password
```
  ## 运行项目生成文档
   * 启动项目 访问 [代码生成器地址](http://127.0.0.1:8010)
   
      ![运行效果](/image/project.jpg "运行取出来的效果")
   * 点击 remote 选择需要的表生成项目
   * 生成zip，解压导入idea或者eclipse便可以使用
   * 运行项目访问swagger文档
   
      ![运行效果](/image/menu.saveimg.savepath20180615155226.jpg "生成代码运行的效果")
   
   
   到此为止我们就可以根据业务逻辑简单修改，便可以发布项目
