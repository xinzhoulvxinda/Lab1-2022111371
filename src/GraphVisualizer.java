import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Utility class for visualizing directed graphs using system Graphviz(this is B2)
 */
public class GraphVisualizer {
    
    /**
     * 生成DOT语言描述
     */
    private static String generateDotString(DirectedGraph graph) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [style=filled,fillcolor=lightgray];\n");
        dot.append("    edge [fontsize=10];\n");
        
        // 添加所有节点
        for (String node : graph.getNodes()) {
            dot.append(String.format("    \"%s\";%n", node));
        }
        
        // 添加所有边
        for (String fromNode : graph.getNodes()) {
            Map<String, Integer> edges = graph.getEdges(fromNode);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String toNode = edge.getKey();
                int weight = edge.getValue();
                dot.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\"];%n",
                    fromNode, toNode, weight));
            }
        }
        
        dot.append("}");
        return dot.toString();
    }

    /**
     * 生成带高亮路径的DOT语言描述
     */
    private static String generateDotStringWithPath(DirectedGraph graph, List<String> path) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [style=filled,fillcolor=lightgray];\n");
        dot.append("    edge [fontsize=10];\n");
        
        // 添加所有节点，路径中的节点用红色填充
        for (String node : graph.getNodes()) {
            String color = path.contains(node.toLowerCase()) ? "lightpink" : "lightgray";
            dot.append(String.format("    \"%s\" [fillcolor=%s];%n", node, color));
        }
        
        // 添加所有边，路径中的边用红色
        for (String fromNode : graph.getNodes()) {
            Map<String, Integer> edges = graph.getEdges(fromNode);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String toNode = edge.getKey();
                int weight = edge.getValue();
                
                // 检查边是否在路径中
                boolean isPathEdge = false;
                for (int i = 0; i < path.size() - 1; i++) {
                    if (path.get(i).equalsIgnoreCase(fromNode) && 
                        path.get(i + 1).equalsIgnoreCase(toNode)) {
                        isPathEdge = true;
                        break;
                    }
                }
                
                String color = isPathEdge ? "red" : "black";
                String penwidth = isPathEdge ? "2.0" : "1.0";
                dot.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\",color=%s,penwidth=%s];%n",
                    fromNode, toNode, weight, color, penwidth));
            }
        }
        
        dot.append("}");
        return dot.toString();
    }

    /**
     * 生成带多条高亮路径的DOT语言描述
     */
    private static String generateDotStringWithMultiplePaths(DirectedGraph graph, List<List<String>> paths) {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [style=filled,fillcolor=lightgray];\n");
        dot.append("    edge [fontsize=10];\n");
        
        // 定义路径颜色
        String[] colors = {"red", "blue", "green", "orange", "purple", "cyan", "brown", "pink"};
        
        // 添加所有节点
        for (String node : graph.getNodes()) {
            // 检查节点是否在任何路径中
            String color = "lightgray";
            for (int i = 0; i < paths.size(); i++) {
                if (paths.get(i).contains(node.toLowerCase())) {
                    // 使用对应路径的浅色
                    color = colors[i % colors.length];
                    break;
                }
            }
            dot.append(String.format("    \"%s\" [fillcolor=%s];%n", node, color));
        }
        
        // 添加所有边
        for (String fromNode : graph.getNodes()) {
            Map<String, Integer> edges = graph.getEdges(fromNode);
            for (Map.Entry<String, Integer> edge : edges.entrySet()) {
                String toNode = edge.getKey();
                int weight = edge.getValue();
                
                // 检查边是否在任何路径中
                String color = "black";
                String penwidth = "1.0";
                for (int pathIndex = 0; pathIndex < paths.size(); pathIndex++) {
                    List<String> path = paths.get(pathIndex);
                    for (int i = 0; i < path.size() - 1; i++) {
                        if (path.get(i).equalsIgnoreCase(fromNode) && 
                            path.get(i + 1).equalsIgnoreCase(toNode)) {
                            color = colors[pathIndex % colors.length];
                            penwidth = "2.0";
                            break;
                        }
                    }
                    if (!color.equals("black")) break;
                }
                
                dot.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\",color=%s,penwidth=%s];%n",
                    fromNode, toNode, weight, color, penwidth));
            }
        }
        
        // 添加图例
        dot.append("    subgraph cluster_legend {\n");
        dot.append("        label=\"Paths\";\n");
        dot.append("        style=filled;\n");
        dot.append("        fillcolor=white;\n");
        for (int i = 0; i < paths.size(); i++) {
            String color = colors[i % colors.length];
            dot.append(String.format("        \"Path %d\" [fillcolor=%s];%n", i + 1, color));
        }
        dot.append("    }\n");
        
        dot.append("}");
        return dot.toString();
    }

    /**
     * 使用Graphviz生成图像
     */
    private static void generateImage(String dot, String outputPath) {
        try {
            // 将DOT内容写入临时文件
            File tempFile = File.createTempFile("graph_", ".dot");
            try (PrintWriter writer = new PrintWriter(tempFile, StandardCharsets.UTF_8)) {
                writer.write(dot);
            }

            // 调用dot命令生成图像
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", tempFile.getAbsolutePath(), "-o", outputPath);
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("图形已保存到: " + outputPath);
            } else {
                System.err.println("生成图形时出错，请确保已安装Graphviz并添加到系统PATH中");
            }
            
            // 删除临时文件
            if (!tempFile.delete()) {
                throw new IOException("Delete failed");
            }
            
        } catch (IOException | InterruptedException e) {
            System.err.println("生成图形时出错: " + e.getMessage());
        }
    }

    /**
     * 可视化有向图
     */
    public static void visualizeGraph(DirectedGraph graph, String outputPath) {
        String dot = generateDotString(graph);
        generateImage(dot, outputPath);
    }

    /**
     * 可视化带高亮路径的有向图
     */
    public static void visualizeGraphWithPath(DirectedGraph graph, List<String> path, String outputPath) {
        String dot = generateDotStringWithPath(graph, path);
        generateImage(dot, outputPath);
    }

    /**
     * 可视化带多条高亮路径的有向图
     */
    public static void visualizeMultiplePaths(DirectedGraph graph, List<List<String>> paths, String outputPath) {
        String dot = generateDotStringWithMultiplePaths(graph, paths);
        generateImage(dot, outputPath);
    }
} 