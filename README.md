![Markdown](http://i1.fuimg.com/604599/002c225fec754412.png)
# BookManager - 图书管理系统
UI基于Dandelion编写的图书管理系统，包含登陆、图书管理、图书借阅归还、用户管理等功能。

所用框架技术：
* Dandelion (UI框架)
* FastJSON (JSON配置文件解析)
* Mybatis+JDBC (持久层框架)
* Log4j (日志管理)
* 
* Lombok (快速创建POJO)
* Reflect (利用反射处理跨平台Icon兼容)

除了图书管理系统最基本的功能外，它还支持：浅色模式和深色模式切换、多语言、本地配置文件。
得益于Dandelion的高效性，整个项目从建立到完成只花费了2天时间，首先来看看它的样子吧：
![Markdown](http://i1.fuimg.com/604599/999a1ae5845c87ba.png)

## 🛠 整体架构图
项目采用MVC+三层架构模式，包括视图层、业务层、持久层。其中Dandelion得益于Swing的设计模式，
自带MVC结构。
![Markdown](http://i1.fuimg.com/604599/c8e3174d2e04952b.png)

视图层包含显示界面和接收用户操作，业务层负责处理数据以及为Dandelion的Model提供数据。
持久层则是为了获取数据库的数据并转交给业务层进行处理。

## 🔗 使用流程图
管理员使用流程大致为：登陆 > 管理 > 退出，读者使用流程大致为：登陆 > 借阅/还书 > 退出。
![Markdown](http://i1.fuimg.com/604599/f7b3038caddc0591.png)
用户可以任意使用对应角色的功能模块，读者可以借书和还书，管理员可以进行图书管理、用户管理、
借阅管理。通用功能是所有角色都能使用的功能，包括首页和设置功能。

## 🇨🇳 颜色和多语言切换
得益于Dandelion的颜色和多语言支持，目前内置中文和英文以及浅色和深色模式，只需在设置界
面直接选择需要的颜色样式和语言即可立即切换项目的样式。
![Markdown](http://i2.tiimg.com/604599/f23c80a0af5bcfe4.png)

## 类UML图
利用idea直接生成的类UML图，其中Mapper是数据库操作接口，Tip和Gui是视图类，Panel包含
数据和组件，其他为实体类。
![Markdown](http://i1.fuimg.com/604599/a233b98de618853f.jpg)

## 数据库设计
目前设计的数据库表如下：
* DB_ACCOUNT: 用户（包含管理员和读者）
* DB_BOOK: 图书（所有的图书信息）
* DB_BOOK_TYPE: 图书分类
* DB_BOOK_LEND: 图书借阅
设计的ER图如下：
![Markdown](http://i1.fuimg.com/604599/546fcd95f6a5fdd5.png)
