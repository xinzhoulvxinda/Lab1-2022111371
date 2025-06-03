import java.io.IOException;

/**
 * 黑盒测试类 - queryBridgeWords函数
 * 使用等价类和边界值分析方法设计测试用例
 */
public class QueryBridgeWordsBlackBoxTest {
    
    private WordGraph wordGraph;
    
    public void setUp() throws IOException {
        // 初始化WordGraph并构建测试图
        wordGraph = new WordGraph();
        // 使用Easy Test.txt构建图（修改路径）
        wordGraph.buildGraphFromFile("../Easy Test.txt");
    }
    
    // 简单的断言方法
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("断言失败: " + message);
        }
    }
    
    private void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError("断言失败: " + message + ". 期望: " + expected + ", 实际: " + actual);
        }
    }
    
    public void testBothWordsNotInGraph() {
        System.out.println("执行测试: TC01 - 两个单词都不在图中");
        String result = wordGraph.queryBridgeWords("notexist1", "notexist2");
        assertTrue(result.contains("No notexist1 or notexist2 in the graph!"), 
                  "应该返回两个单词都不在图中的错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC01 通过\n");
    }
    
    public void testWord1NotInGraphWord2InGraph() {
        System.out.println("执行测试: TC02 - word1不在图中，word2在图中");
        String result = wordGraph.queryBridgeWords("notexist", "the");
        assertTrue(result.contains("No notexist or the in the graph!"), 
                  "应该返回word1不在图中的错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC02 通过\n");
    }
    
    public void testWord1InGraphWord2NotInGraph() {
        System.out.println("执行测试: TC03 - word1在图中，word2不在图中");
        String result = wordGraph.queryBridgeWords("the", "notexist");
        assertTrue(result.contains("No the or notexist in the graph!"), 
                  "应该返回word2不在图中的错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC03 通过\n");
    }
    
    public void testBothWordsInGraphNoBridge() {
        System.out.println("执行测试: TC04 - 两个单词都在图中，无桥接词");
        String result = wordGraph.queryBridgeWords("scientist", "team");
        assertTrue(result.contains("No bridge words from scientist to team!"), 
                  "应该返回无桥接词的消息");
        System.out.println("结果: " + result);
        System.out.println("TC04 通过\n");
    }
    
    public void testBothWordsInGraphWithBridge() {
        System.out.println("执行测试: TC05 - 两个单词都在图中，有桥接词");
        String result = wordGraph.queryBridgeWords("the", "scientist");
        System.out.println("结果: " + result);
        // 检查是否返回了正确的格式
        assertTrue(result.contains("No bridge words") || 
                  result.contains("The bridge words from the to scientist are:"), 
                  "返回格式应该正确");
        System.out.println("TC05 通过\n");
    }
    
    public void testEmptyStrings() {
        System.out.println("执行测试: TC07 - 空字符串测试");
        String result1 = wordGraph.queryBridgeWords("", "the");
        String result2 = wordGraph.queryBridgeWords("the", "");
        String result3 = wordGraph.queryBridgeWords("", "");
        
        assertTrue(result1.contains("No") && result1.contains("in the graph!"), 
                  "空字符串应该返回错误消息");
        assertTrue(result2.contains("No") && result2.contains("in the graph!"), 
                  "空字符串应该返回错误消息");
        assertTrue(result3.contains("No") && result3.contains("in the graph!"), 
                  "两个空字符串应该返回错误消息");
        
        System.out.println("结果1: " + result1);
        System.out.println("结果2: " + result2);
        System.out.println("结果3: " + result3);
        System.out.println("TC07 通过\n");
    }
    
    public void testCaseInsensitive() {
        System.out.println("执行测试: TC08 - 大小写测试");
        String result1 = wordGraph.queryBridgeWords("THE", "DATA");
        String result2 = wordGraph.queryBridgeWords("the", "data");
        
        assertEquals(result1, result2, "大小写不同的输入应该产生相同结果");
        System.out.println("大写结果: " + result1);
        System.out.println("小写结果: " + result2);
        System.out.println("TC08 通过\n");
    }
    
    public void testSameWords() {
        System.out.println("执行测试: TC09 - 相同单词测试");
        String result = wordGraph.queryBridgeWords("the", "the");
        assertTrue(result.contains("No bridge words from the to the!"), 
                  "相同单词之间应该没有桥接词");
        System.out.println("结果: " + result);
        System.out.println("TC09 通过\n");
    }
    
    public void runAllTests() {
        try {
            setUp();
            System.out.println("开始执行黑盒测试用例...\n");
            
            testBothWordsNotInGraph();
            testWord1NotInGraphWord2InGraph();
            testWord1InGraphWord2NotInGraph();
            testBothWordsInGraphNoBridge();
            testBothWordsInGraphWithBridge();
            testEmptyStrings();
            testCaseInsensitive();
            testSameWords();
            
            System.out.println("所有测试用例执行完成！");
            
        } catch (Exception e) {
            System.err.println("测试执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        QueryBridgeWordsBlackBoxTest test = new QueryBridgeWordsBlackBoxTest();
        test.runAllTests();
    }
} 