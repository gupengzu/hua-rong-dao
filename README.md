# hua-rong-dao
本项目使用SpringBoot框架、MyBatis等实现后端；使用vue实现前端。
本项目还有两个.mp4文件未上传，一个是"16mins超清水墨视频素材，无水印.mp4",另一个是"水墨长画卷大屏背景.mp4"，两个视频均可从B站获得，获取后将其放到前端src/assets目录里。
本项目所有视频与图像资源来自B站等平台。

使用方法：

1.建立数据库，3个建表语句是

CREATE TABLE layout (
    username VARCHAR(255) NOT NULL,
    count INT,
    layout VARCHAR(255),
    PRIMARY KEY (username)
);

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    email VARCHAR(255),
    award_count INT,
    image VARCHAR(255)
);

CREATE TABLE useringame (
    username VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    layout VARCHAR(255),
    PRIMARY KEY (username)
);

建表完成后修改后端application.yml中的datasource，按照你自己建立的数据库进行修改。（如果没有修改好，mapper包中的类会报错。）

2.运行后端项目（运行HuaRongDaoApplication这个文件）

3.在前端\hua-rong-dao_vue\hua_rong_dao_vue文件中终端输入npm run dev 

4.打开nginx-1.22.0-web，然后运行nginx.exe。然后在浏览器中http://localhost:5173/login  即可使用。

5.本项目暂时未用Linux中的Docker进行部署，后续会更新
