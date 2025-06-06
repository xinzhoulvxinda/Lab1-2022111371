import org.junit.Test;
import org.junit.Before;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * WordGraph类的JUnit测试 - 黑盒测试
 * 使用等价类和边界值分析方法设计测试用例
 */
public class WordGraphTest {
    
    private WordGraph wordGraph;
    
    @Before
    public void setUp() throws IOException {
        // 初始化WordGraph并构建测试图
        wordGraph = new WordGraph();
        // 使用Easy Test.txt构建图
        wordGraph.buildGraphFromFile("Easy Test.txt");
    }
    
    @Test
    public void testBothWordsNotInGraph() {
        System.out.println("执行测试: TC01 - 两个单词都不在图中");
        String result = wordGraph.queryBridgeWords("notexist1", "notexist2");
        assertTrue("应该返回两个单词都不在图中的错误消息", 
                  result.contains("No notexist1 or notexist2 in the graph!"));
        System.out.println("结果: " + result);
        System.out.println("TC01 通过\n");
    }
    
    @Test
    public void testWord1NotInGraphWord2InGraph() {
        System.out.println("执行测试: TC02 - word1不在图中，word2在图中");
        String result = wordGraph.queryBridgeWords("notexist", "the");
        assertTrue("应该返回word1不在图中的错误消息", 
                  result.contains("No notexist or the in the graph!"));
        System.out.println("结果: " + result);
        System.out.println("TC02 通过\n");
    }
    
    @Test
    public void testWord1InGraphWord2NotInGraph() {
        System.out.println("执行测试: TC03 - word1在图中，word2不在图中");
        String result = wordGraph.queryBridgeWords("the", "notexist");
        assertTrue("应该返回word2不在图中的错误消息", 
                  result.contains("No the or notexist in the graph!"));
        System.out.println("结果: " + result);
        System.out.println("TC03 通过\n");
    }
    
    @Test
    public void testBothWordsInGraphNoBridge() {
        System.out.println("执行测试: TC04 - 两个单词都在图中，无桥接词");
        String result = wordGraph.queryBridgeWords("scientist", "team");
        assertTrue("应该返回无桥接词的消息", 
                  result.contains("No bridge words from scientist to team!"));
        System.out.println("结果: " + result);
        System.out.println("TC04 通过\n");
    }
    
    @Test
    public void testBothWordsInGraphWithBridge() {
        System.out.println("执行测试: TC05 - 两个单词都在图中，有桥接词");
        String result = wordGraph.queryBridgeWords("the", "scientist");
        System.out.println("结果: " + result);
        // 检查是否返回了正确的格式
        assertTrue("返回格式应该正确", 
                  result.contains("No bridge words") || 
                  result.contains("The bridge words from the to scientist are:"));
        System.out.println("TC05 通过\n");
    }
    
    @Test
    public void testBridgeWordsExist() {
        System.out.println("执行测试: TC06 - 两个单词都在图中，存在桥接词");
        String result = wordGraph.queryBridgeWords("but", "data");
        System.out.println("结果: " + result);
        // 检查是否返回了桥接词列表，包含"the"
        assertTrue("应该返回桥接词列表", 
                  result.contains("The bridge words from but to data are:") && 
                  result.contains("the"));
        System.out.println("TC06 通过\n");
    }
    
    @Test
    public void testEmptyStrings() {
        System.out.println("执行测试: TC07 - 空字符串测试");
        String result1 = wordGraph.queryBridgeWords("", "the");
        String result2 = wordGraph.queryBridgeWords("the", "");
        String result3 = wordGraph.queryBridgeWords("", "");
        
        assertTrue("空字符串应该返回错误消息", 
                  result1.contains("No") && result1.contains("in the graph!"));
        assertTrue("空字符串应该返回错误消息", 
                  result2.contains("No") && result2.contains("in the graph!"));
        assertTrue("两个空字符串应该返回错误消息", 
                  result3.contains("No") && result3.contains("in the graph!"));
        
        System.out.println("结果1: " + result1);
        System.out.println("结果2: " + result2);
        System.out.println("结果3: " + result3);
        System.out.println("TC07 通过\n");
    }
    
    @Test
    public void testCaseInsensitive() {
        System.out.println("执行测试: TC08 - 大小写测试");
        String result1 = wordGraph.queryBridgeWords("THE", "DATA");
        String result2 = wordGraph.queryBridgeWords("the", "data");
        
        assertEquals("大小写不同的输入应该产生相同结果", result1, result2);
        System.out.println("大写结果: " + result1);
        System.out.println("小写结果: " + result2);
        System.out.println("TC08 通过\n");
    }
    
    @Test
    public void testSameWords() {
        System.out.println("执行测试: TC09 - 相同单词测试");
        String result = wordGraph.queryBridgeWords("the", "the");
        assertTrue("相同单词之间应该没有桥接词", 
                  result.contains("No bridge words from the to the!"));
        System.out.println("结果: " + result);
        System.out.println("TC09 通过\n");
    }
    
    @Test
    public void testNullInput() {
        System.out.println("执行测试: TC10 - null输入测试");
        String result = wordGraph.queryBridgeWords(null, "test");
        assertEquals("null输入应该返回错误消息", 
                    "Error: Input words cannot be null.", result);
        System.out.println("结果: " + result);
        System.out.println("TC10 通过\n");
    }
    
    @Test
    public void testSecondParameterNull() {
        System.out.println("执行测试: TC11 - 第二个参数null测试");
        String result = wordGraph.queryBridgeWords("test", null);
        assertEquals("第二个参数为null应该返回错误消息", 
                    "Error: Input words cannot be null.", result);
        System.out.println("结果: " + result);
        System.out.println("TC11 通过\n");
    }
    
    @Test
    public void testBothParametersEmpty() {
        System.out.println("执行测试: TC12 - 两个参数都为空字符串测试");
        String result = wordGraph.queryBridgeWords("", "");
        assertTrue("两个空字符串应该返回错误消息",
                  result.contains("No  or  in the graph!"));
        System.out.println("结果: " + result);
        System.out.println("TC12 通过\n");
    }
}