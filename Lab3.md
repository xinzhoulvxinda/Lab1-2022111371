## 实验目标

- 针对Lab1完成的程序，设计黑盒测试用例和白盒测试用例。
- JUnit环境下撰写测试代码并执行测试。
- 使用EclEmma或IDE自带工具统计测试的覆盖度。



## Step 1：设计黑盒测试用例

- 利用黑盒测试的等价类和边界值方法，为Lab1待测程序设计一组测试用例。
- 测试对象(五选一)：
  - String queryBridgeWords(String word1, String word2)：查询桥接 词
  -  String generateNewText(String inputText)：根据bridge word生成 新文本
  -  String calcShortestPath(String word1, String word2)：计算两个 单词之间的最短路径
  - Double calPageRank(String word) ：计算单词的PR值
  - String randomWalk()：随机游走
- 根据Lab1的要求，给出所选被测函数的需求规约描述；
- 每个测试用例由输入数据和期望输出两部分组成。



## Step 2：使用JUnit编写黑盒测试用例并执行

- 在Lab1的Git仓库里，建立新的Git分支，命名为Lab3b (b代表black- box testing)；
- 针对每个测试用例撰写testcase；
- 执行测试用例：
  - 执行，产生结果，记录实际输出；
  - 记录、分析结果；
  - 针对失败的测试用例，发现代码的问题，并修改代码；
- 重复上一步，直到所有的测试用例都完全通过为止。
- 将Lab3b合并到master分支，并推送至GitHub。



## Step 3：设计白盒测试用例

- 针对Lab3b分支的当前代码，对以下函数(三选一)使用基本路径法设计白盒测试用例：
  - String queryBridgeWords(type G, String word1, String word2)查询桥接词
  - String calcShortestPath(type G, String word1, String word2)： 计算两个单词之间的最短路径
  - String randomWalk(type G)：随机游走
- 每个测试用例由输入数据和期望输出两部分组成。



## Step 4：使用JUnit编写并执行白盒测试代码

- Lab1的Git仓库里，建立新的Git分支，命名为Lab3w (w代表white-box testing)；
- 针对每个测试用例撰写testcase；
-  执行测试用例：
  - 执行，产生结果，记录实际输出；
  - 记录、分析结果；
  - 针对失败的测试用例，发现代码的问题，并修改代码；

- 重复上一步，直到所有的测试用例都完全通过为止。

- 将Lab3w合并到master分支，并推送至GitHub。



## Step 5：统计测试覆盖度

- 测试的覆盖率是测试质量的一个重要指标。
  -  可使用EclEmma工具 https://www.eclemma.org
  - 或Eclipse自带Coverage分析工具
  - 或IDEA自带Coverage分析工具
- 在测试过程中，当运行测试程序，覆盖度分析工具可自动分析出被测程 序的各行代码被覆盖的情况；
- 代码被覆盖得越全面，测试质量就越好。
- 从工具导出覆盖度分析报告，观察语句覆盖度。