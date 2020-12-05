# road_examination_manager
## jdk版本为1.8
## mysql版本为5.7

# 1、将源码下载到本地，用idea打开
推荐使用`git clone url`命令下载，(zip方式下载不能推代码到github上）
![1](https://user-images.githubusercontent.com/44233477/101240683-d3b66700-372b-11eb-9ab4-0d4352c34826.png)
需要配置好git才能使用该命令
![3](https://user-images.githubusercontent.com/44233477/101240817-97373b00-372c-11eb-8228-af342a6f5aef.png)
# 2、在idea中修改项目maven路径为本地路径
idea打开项目后，进入`file->settings->Build,Execution,Deployment->Build Tools->Maven`
检查项目的Maven路径是否与你本地路径一致
![2](https://user-images.githubusercontent.com/44233477/101240701-f2b4f900-372b-11eb-8c1d-a80c18e1db2e.png)
# 3、修改`application.yml`文件中数据库账号
修改成自己的本地mysql数据库账号（注意修改后每次提交代码时，不要提交到分支里！！！）
记得先运行群里最先的数据库sql脚本
![4](https://user-images.githubusercontent.com/44233477/101240889-1a589100-372d-11eb-9afb-29f05e35933f.png)