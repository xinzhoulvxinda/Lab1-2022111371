import org.junit.Test;

import static org.junit.Assert.*;
import java.io.IOException;

public class WordGraphWhiteTest {

    @Test
    public void calcShortestPath() {
        try {
            // 初始化测试环境
            WordGraph wordGraph = new WordGraph();
            wordGraph.buildGraphFromFile("Easy Test.txt");
            
            System.out.println("=== calcShortestPath方法白盒测试 ===");
            System.out.println("使用基本路径法设计测试用例\n");
            
            // 基本路径1: 起始词不在图中(包含边界条件) (覆盖路径: 1→2→3→4→18)
            // 对应代码行: 第209-213行 (word1预处理、containsNode检查、返回错误信息)
            System.out.println("测试用例1: 起始词不在图中(包含空字符串等边界条件)");
            String result1 = wordGraph.calcShortestPath("nonexistent", "target");
            assertTrue("应该返回起始词不在图中的错误信息", 
                       result1.contains("Error: Start word 'nonexistent' not in graph."));
            // 测试边界条件：空字符串
            String result1b = wordGraph.calcShortestPath("", "test");
            assertTrue("空字符串应该被视为不存在的词", 
                       result1b.contains("Error: Start word '' not in graph."));
            System.out.println("✓ 路径1测试通过: " + result1);
            System.out.println(result1b);
            
            // 基本路径2: 单词模式(word2为空) (覆盖路径: 1→2→3→5→6→18)  
            // 对应代码行: 第209-217行 (预处理、word1检查、word2.isEmpty()检查、调用calcShortestPathsToAll)
            System.out.println("\n测试用例2: 单词模式(word2为空)");
            String result2 = wordGraph.calcShortestPath("the", "");
            assertTrue("应该返回到所有词的路径", 
                       result2.contains("Shortest paths from 'the' to all other words:"));
            System.out.println("✓ 路径2测试通过: " + result2.substring(0, Math.min(50, result2.length())) + "...");
            
            // 基本路径3: 目标词不在图中 (覆盖路径: 1→2→3→5→7→8→18)
            // 对应代码行: 第209-222行 (预处理、word1检查、word2非空检查、word2.containsNode检查、返回目标词错误)
            System.out.println("\n测试用例3: 目标词不在图中");
            String result3 = wordGraph.calcShortestPath("the", "nonexistent");
            assertTrue("应该返回目标词不在图中的错误信息", 
                       result3.contains("Error: Target word 'nonexistent' not in graph."));
            System.out.println("✓ 路径3测试通过: " + result3);
            
            // 基本路径4: 无路径存在 (覆盖路径: 1→2→3→5→7→9→10→15→16→18)
            // 对应代码行: 第209-279行 (完整流程到第278行路径检查、第279行返回无路径信息)
            System.out.println("\n测试用例4: 无路径存在");
            // 创建一个包含孤立节点的图来测试无路径情况
            WordGraph isolatedGraph = new WordGraph();
            java.lang.reflect.Field graphField = WordGraph.class.getDeclaredField("graph");
            graphField.setAccessible(true);
            DirectedGraph graph = (DirectedGraph) graphField.get(isolatedGraph);
            
            // 添加两个不相连的节点
            graph.addNode("isolated1");
            graph.addNode("isolated2");
            
            String result4 = isolatedGraph.calcShortestPath("isolated1", "isolated2");
            assertTrue("应该返回无路径信息", 
                       result4.contains("No path exists"));
            System.out.println("✓ 路径4测试通过: " + result4);
            
            // 基本路径5: 正常路径存在 (覆盖路径: 1→2→3→5→7→9→10→11→12→13→14→13→15→17→18)
            // 对应代码行: 第209-297行 (完整的正常执行路径，包含while循环第242行、邻居遍历第253行、路径更新第269行、结果生成第282-297行)
            System.out.println("\n测试用例5: 正常路径存在");
            String result5 = wordGraph.calcShortestPath("the", "data");
            assertTrue("应该找到从'the'到'data'的路径", 
                       result5.contains("All shortest paths from 'the' to 'data':"));
            assertTrue("应该包含路径长度信息", 
                       result5.contains("Path length:"));
            System.out.println("✓ 路径5测试通过: " + result5.substring(0, Math.min(50, result5.length())) + "...");
            
            // 基本路径6: 相同词路径 (覆盖路径: 1→2→3→5→7→9→10→11→12→10→15→17→18)
            // 对应代码行: 第209-297行 (特殊情况：访问过的节点跳过，第245-246行continue分支)
            System.out.println("\n测试用例6: 相同词路径");
            String result6 = wordGraph.calcShortestPath("the", "the");
            assertTrue("从相同词到自身应该有路径", 
                       result6.contains("All shortest paths from 'the' to 'the':"));
            System.out.println("✓ 路径6测试通过: " + result6.substring(0, Math.min(50, result6.length())) + "...");
            
            // 基本路径7: 大小写转换和复杂情况测试 (覆盖路径: 1→2→3→5→7→9→10→11→12→13→15→17→18)
            // 对应代码行: 第209-210行输入预处理 + 第252行无邻居节点情况
            System.out.println("\n测试用例7: 大小写转换和无邻居测试");
            String result7 = wordGraph.calcShortestPath("THE", "DATA");
            assertTrue("应该不区分大小写并正确处理", 
                       result7.contains("All shortest paths from 'the' to 'data':"));
            System.out.println("✓ 路径7测试通过: " + result7.substring(0, Math.min(50, result7.length())) + "...");
            
            // 基本路径8: 算法主循环复杂情况 (覆盖路径: 1→2→3→5→7→9→10→11→12→13→14→...→10→15→17→18)
            // 对应代码行: 第257-272行距离计算和路径更新的多种分支
            System.out.println("\n测试用例8: 算法主循环测试");
            String result8 = wordGraph.calcShortestPath("scientist", "team");
            // 验证结果格式和内容
            assertTrue("应该返回路径信息或无路径信息", 
                       result8.contains("All shortest paths") || result8.contains("No path exists"));
            if (result8.contains("Path length:")) {
                // 验证路径长度格式
                String[] lines = result8.split("\n");
                for (String line : lines) {
                    if (line.contains("Path length:")) {
                        String lengthStr = line.substring(line.indexOf(":") + 1).trim();
                        try {
                            double length = Double.parseDouble(lengthStr);
                            assertTrue("路径长度应该是正数", length > 0);
                        } catch (NumberFormatException e) {
                            fail("路径长度格式不正确: " + lengthStr);
                        }
                        break;
                    }
                }
            }
            System.out.println("✓ 路径8测试通过: " + result8.substring(0, Math.min(50, result8.length())) + "...");
            
            System.out.println("\n=== 所有基本路径测试完成 ===");
            System.out.println("共测试了8个基本路径，覆盖了calcShortestPath方法的所有主要执行分支");
            
        } catch (Exception e) {
            fail("测试执行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}