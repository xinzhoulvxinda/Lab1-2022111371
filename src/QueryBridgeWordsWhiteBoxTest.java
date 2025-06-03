import java.io.IOException;

/**
 * 白盒测试类 - queryBridgeWords函数
 * 使用基本路径法设计测试用例，确保所有路径覆盖
 */
public class QueryBridgeWordsWhiteBoxTest {
    
    private WordGraph wordGraph;
    
    public void setUp() throws IOException {
        // 创建专门的测试图，确保能覆盖所有路径
        wordGraph = new WordGraph();
        createTestGraph();
    }
    
    /**
     * 构造特定的测试图，支持所有白盒测试路径
     */
    private void createTestGraph() {
        // 手动构建测试图
        DirectedGraph graph = new DirectedGraph();
        
        // 构造有桥接词的情况：a -> bridge -> b
        graph.addEdge("a", "bridge");
        graph.addEdge("bridge", "b");
        
        // 构造多个桥接词的情况：a -> bridge1,bridge2,bridge3 -> c
        graph.addEdge("a", "bridge1");
        graph.addEdge("bridge1", "c");
        graph.addEdge("a", "bridge2");
        graph.addEdge("bridge2", "c");
        graph.addEdge("a", "bridge3");
        graph.addEdge("bridge3", "c");
        
        // 构造无桥接词但都在图中的情况
        graph.addEdge("scientist", "analyzed");
        graph.addEdge("team", "requested");
        
        // 构造孤立节点（在图中但无出边）
        graph.addNode("isolated");
        graph.addNode("target");
        
        // 通过反射设置内部图对象
        try {
            java.lang.reflect.Field graphField = wordGraph.getClass().getDeclaredField("graph");
            graphField.setAccessible(true);
            graphField.set(wordGraph, graph);
        } catch (Exception e) {
            System.err.println("Error setting up test graph: " + e.getMessage());
        }
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
    
    // TC-WB01: null输入测试 - 路径1
    public void testNullInput() {
        System.out.println("执行测试: TC-WB01 - null输入测试");
        String result = wordGraph.queryBridgeWords(null, "test");
        assertEquals("Error: Input words cannot be null.", result, 
                    "null输入应该返回错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB01 通过 - 覆盖路径1\n");
    }
    
    // TC-WB02: 空字符串输入测试 - 路径2
    public void testEmptyStringInput() {
        System.out.println("执行测试: TC-WB02 - 空字符串输入测试");
        String result = wordGraph.queryBridgeWords("", "test");
        assertTrue(result.contains("No  or test in the graph!"), 
                  "空字符串输入应该返回错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB02 通过 - 覆盖路径2\n");
    }
    
    // TC-WB03: 单词不在图中测试 - 路径3
    public void testWordsNotInGraph() {
        System.out.println("执行测试: TC-WB03 - 单词不在图中测试");
        String result = wordGraph.queryBridgeWords("notexist", "also_notexist");
        assertEquals("No notexist or also_notexist in the graph!", result,
                    "不存在的单词应该返回错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB03 通过 - 覆盖路径3\n");
    }
    
    // TC-WB04: 无桥接词测试 - 路径4
    public void testNoBridgeWords() {
        System.out.println("执行测试: TC-WB04 - 无桥接词测试");
        String result = wordGraph.queryBridgeWords("scientist", "team");
        assertEquals("No bridge words from scientist to team!", result,
                    "无桥接词情况应该返回相应消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB04 通过 - 覆盖路径4\n");
    }
    
    // TC-WB05: 单个桥接词测试 - 路径5a
    public void testSingleBridgeWord() {
        System.out.println("执行测试: TC-WB05 - 单个桥接词测试");
        String result = wordGraph.queryBridgeWords("a", "b");
        assertEquals("The bridge words from a to b are: bridge.", result,
                    "单个桥接词应该正确显示");
        System.out.println("结果: " + result);
        System.out.println("TC-WB05 通过 - 覆盖路径5a\n");
    }
    
    // TC-WB06: 多个桥接词测试 - 路径5b,5c,5d
    public void testMultipleBridgeWords() {
        System.out.println("执行测试: TC-WB06 - 多个桥接词测试");
        String result = wordGraph.queryBridgeWords("a", "c");
        assertTrue(result.startsWith("The bridge words from a to c are:"), 
                  "多个桥接词应该正确显示");
        assertTrue(result.contains("bridge1") && result.contains("bridge2") && result.contains("bridge3"),
                  "应该包含所有桥接词");
        assertTrue(result.contains(" and "), "最后一个桥接词前应该有'and'");
        System.out.println("结果: " + result);
        System.out.println("TC-WB06 通过 - 覆盖路径5b,5c,5d\n");
    }
    
    // TC-WB07: word1无邻居测试 - 路径6
    public void testWord1NoNeighbors() {
        System.out.println("执行测试: TC-WB07 - word1无邻居测试");
        String result = wordGraph.queryBridgeWords("isolated", "target");
        assertEquals("No bridge words from isolated to target!", result,
                    "无邻居的单词应该返回无桥接词消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB07 通过 - 覆盖路径6\n");
    }
    
    // TC-WB08: 大小写转换测试
    public void testCaseConversion() {
        System.out.println("执行测试: TC-WB08 - 大小写转换测试");
        String result1 = wordGraph.queryBridgeWords("A", "B");
        String result2 = wordGraph.queryBridgeWords("a", "b");
        assertEquals(result1, result2, "大小写不同的输入应该产生相同结果");
        System.out.println("大写结果: " + result1);
        System.out.println("小写结果: " + result2);
        System.out.println("TC-WB08 通过 - 验证大小写转换\n");
    }
    
    // 边界测试：第二个参数为null
    public void testSecondParameterNull() {
        System.out.println("执行测试: TC-WB09 - 第二个参数null测试");
        String result = wordGraph.queryBridgeWords("test", null);
        assertEquals("Error: Input words cannot be null.", result,
                    "第二个参数为null应该返回错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB09 通过\n");
    }
    
    // 边界测试：两个参数都为空字符串
    public void testBothParametersEmpty() {
        System.out.println("执行测试: TC-WB10 - 两个参数都为空字符串测试");
        String result = wordGraph.queryBridgeWords("", "");
        assertTrue(result.contains("No  or  in the graph!"),
                  "两个空字符串应该返回错误消息");
        System.out.println("结果: " + result);
        System.out.println("TC-WB10 通过\n");
    }
    
    public void runAllTests() {
        try {
            setUp();
            System.out.println("开始执行白盒测试用例（基本路径法）...\n");
            
            testNullInput();
            testEmptyStringInput();
            testWordsNotInGraph();
            testNoBridgeWords();
            testSingleBridgeWord();
            testMultipleBridgeWords();
            testWord1NoNeighbors();
            testCaseConversion();
            testSecondParameterNull();
            testBothParametersEmpty();
            
            System.out.println("所有白盒测试用例执行完成！");
            System.out.println("路径覆盖情况：");
            System.out.println("✅ 路径1: null输入");
            System.out.println("✅ 路径2: 空字符串输入");
            System.out.println("✅ 路径3: 单词不在图中");
            System.out.println("✅ 路径4: 无桥接词");
            System.out.println("✅ 路径5a: 单个桥接词");
            System.out.println("✅ 路径5b,5c,5d: 多个桥接词的所有分支");
            System.out.println("✅ 路径6: word1无邻居");
            System.out.println("✅ 额外边界情况覆盖");
            
        } catch (Exception e) {
            System.err.println("测试执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        QueryBridgeWordsWhiteBoxTest test = new QueryBridgeWordsWhiteBoxTest();
        test.runAllTests();
    }
} 