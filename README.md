![Markdown](https://i0.hdslb.com/bfs/album/eda5cb8be0a4198226f9cdd1e5b1878f37769ec6.png)
# BookManager - 图书管理系统
UI基于Dandelion编写的图书管理系统，包含登陆、图书管理、图书借阅归还、用户管理等功能。

所用框架技术：
* Dandelion (UI框架)
* FastJSON (JSON配置文件解析)
* Mybatis+JDBC (持久层框架)
* Log4j (日志管理)
* Lombok (快速创建POJO)
* Reflect (利用反射处理跨平台Icon兼容)


除了图书管理系统最基本的功能外，它还支持：浅色模式和深色模式切换、多语言、本地配置文件。
得益于Dandelion的高效性，整个项目从建立到完成只花费了2天时间，首先来看看它的样子吧：
![Markdown](https://i0.hdslb.com/bfs/album/86850addc4d080b7fc9592aa8344f0c801a2f779.png)

## 🛠 整体架构图
项目采用MVC+三层架构模式，包括视图层、业务层、持久层。其中Dandelion得益于Swing的设计模式，
自带MVC结构。
![Markdown](https://i0.hdslb.com/bfs/album/e4f22ae4ef70b10ba96fc468ce8163fc2bf9ce83.png)

视图层包含显示界面和接收用户操作，业务层负责处理数据以及为Dandelion的Model提供数据。
持久层则是为了获取数据库的数据并转交给业务层进行处理。

## 🔗 使用流程图
管理员使用流程大致为：登陆 > 管理 > 退出，读者使用流程大致为：登陆 > 借阅/还书 > 退出。
![Markdown](https://i0.hdslb.com/bfs/album/d7b788c4d98a3f33da5f2a2fcafd4bfde1dae53c.png)
用户可以任意使用对应角色的功能模块，读者可以借书和还书，管理员可以进行图书管理、用户管理、
借阅管理。通用功能是所有角色都能使用的功能，包括首页和设置功能。

## 🇨🇳 颜色和多语言切换
得益于Dandelion的颜色和多语言支持，目前内置中文和英文以及浅色和深色模式，只需在设置界
面直接选择需要的颜色样式和语言即可立即切换项目的样式。
![Markdown](https://i0.hdslb.com/bfs/album/ae778fe5a73f8ee8c30b31c3dd51f20c792b3074.png)

## 数据库设计
目前设计的数据库表如下：
* DB_ACCOUNT: 用户（包含管理员和读者）
* DB_BOOK: 图书（所有的图书信息）
* DB_BOOK_TYPE: 图书分类
* DB_BOOK_LEND: 图书借阅
设计的ER图如下：
![Markdown](https://i0.hdslb.com/bfs/album/b8f79845c2cc0f360b68bc8f82bb35323ffe6966.png)
