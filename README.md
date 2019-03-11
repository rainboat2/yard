# 料场运输调度

## 运行环境

JDK10，基于Intellij IDEA开发

## 结构说明

项目中包含以下三个包：

1. ga。遗传算法，用于规划皮带路径。
2. plan。仅包含一个TransportPlanGenerator类，用于根据混料计划生成运输计划，使用回溯法实现。
3. transport_info。使用List保存传输带和原料的信息。
