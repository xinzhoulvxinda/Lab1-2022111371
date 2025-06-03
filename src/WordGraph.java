import java.io.*;
import java.util.*;

/**
 * Main class implementing all required functionality(this is B2)
 */
public class WordGraph {
    private DirectedGraph graph;
    private Random random;
    private static final double DAMPING_FACTOR = 0.85; // Damping factor for PageRank algorithm
    private static final int MAX_ITERATIONS = 500; // Maximum iterations for PageRank algorithm
    
    /**
     * Constructor
     */
    public WordGraph() {
        graph = new DirectedGraph();
        random = new Random();
    }
    
    /**
     * Build a directed graph from a text file
     * @param filePath Text file path
     * @throws IOException File reading exception
     */
    public void buildGraphFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder textBuilder = new StringBuilder();
        String line;
        
        // Read all text
        while ((line = reader.readLine()) != null) {
            textBuilder.append(line).append(" "); // Replace newlines with spaces
        }
        reader.close();
        
        String text = textBuilder.toString();
        // Replace all non-alphabetic characters with spaces
        text = text.replaceAll("[^a-zA-Z]", " ");
        // Replace multiple consecutive spaces with a single space
        text = text.replaceAll("\\s+", " ").trim();
        
        // Split text by spaces to get all words
        String[] words = text.split(" ");
        
        // Build directed graph
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i + 1];
            
            // Skip empty words
            if (currentWord.isEmpty() || nextWord.isEmpty()) {
                continue;
            }
            
            // Add edge (word pair) to graph
            graph.addEdge(currentWord, nextWord);
        }
    }
    
    /**
     * Display the directed graph
     * @param G Directed graph object
     * @param scanner Scanner object for user input
     */
    public void showDirectedGraph(DirectedGraph G, Scanner scanner) {
        if (G == null || G.getNodes().isEmpty()) {
            System.out.println("Graph is empty, cannot display.");
            return;
        }
        
        // Display in command line
        System.out.println(G.toString());
        
        // Ask user whether to generate graph image
        System.out.print("Do you want to generate the graph image? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        // Generate image file if user agrees
        if (response.equals("yes")) {
            String outputPath = "graph.png";
            GraphVisualizer.visualizeGraph(G, outputPath);
            System.out.println("Graph image has been generated as 'graph.png'");
        } else {
            System.out.println("Graph image generation skipped.");
        }
    }
    
    /**
     * Query bridge words
     * @param word1 First word
     * @param word2 Second word
     * @return Query result
     */
    public String queryBridgeWords(String word1, String word2) {
        // 添加输入验证
        if (word1 == null || word2 == null) {
            return "Error: Input words cannot be null.";
        }
        if (word1.isEmpty() || word2.isEmpty()) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }
        
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        
        // Check if both words are in the graph
        if (!graph.containsNode(word1) || !graph.containsNode(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }
        
        // Find bridge words
        List<String> bridgeWords = new ArrayList<>();
        
        // Get all neighbors of word1
        Map<String, Integer> neighborsOfWord1 = graph.getEdges(word1);
        
        // For each neighbor of word1, check if it also points to word2
        for (String potentialBridge : neighborsOfWord1.keySet()) {
            if (graph.getEdgeWeight(potentialBridge, word2) > 0) {
                bridgeWords.add(potentialBridge);
            }
        }
        
        // Return appropriate message based on results
        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            StringBuilder result = new StringBuilder("The bridge words from " + word1 + " to " + word2 + " are: ");
            
            for (int i = 0; i < bridgeWords.size(); i++) {
                if (i > 0) {
                    if (i == bridgeWords.size() - 1) {
                        result.append(" and ");
                    } else {
                        result.append(", ");
                    }
                }
                result.append(bridgeWords.get(i));
            }
            result.append(".");
            
            return result.toString();
        }
    }
    
    /**
     * Generate new text based on bridge words
     * @param inputText Input text
     * @return Newly generated text
     */
    public String generateNewText(String inputText) {
        // Process input text, replace non-alphabetic characters with spaces
        String processedText = inputText.replaceAll("[^a-zA-Z]", " ");
        // Replace multiple consecutive spaces with a single space
        processedText = processedText.replaceAll("\\s+", " ").trim();
        
        // Split text by spaces to get all words
        String[] words = processedText.split(" ");
        
        // Build new text
        StringBuilder newText = new StringBuilder();
        
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i].toLowerCase();
            String nextWord = words[i + 1].toLowerCase();
            
            // Add current word to new text
            newText.append(currentWord).append(" ");
            
            // Find bridge words
            List<String> bridgeWords = new ArrayList<>();
            
            // Get all neighbors of current word
            Map<String, Integer> neighbors = graph.getEdges(currentWord);
            
            // For each neighbor of current word, check if it also points to next word
            for (String potentialBridge : neighbors.keySet()) {
                if (graph.getEdgeWeight(potentialBridge, nextWord) > 0) {
                    bridgeWords.add(potentialBridge);
                }
            }
            
            // If bridge words exist, randomly select one and add to new text
            if (!bridgeWords.isEmpty()) {
                String randomBridge = bridgeWords.get(random.nextInt(bridgeWords.size()));
                newText.append(randomBridge).append(" ");
            }
        }
        
        // Add last word
        if (words.length > 0) {
            newText.append(words[words.length - 1]);
        }
        
        return newText.toString();
    }
    
    /**
     * Calculate shortest path between two words
     * @param word1 Start word
     * @param word2 Target word (empty string for showing all paths)
     * @return Shortest path description
     */
    public String calcShortestPath(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        
        // Check if start word is in the graph
        if (!graph.containsNode(word1)) {
            return "Error: Start word '" + word1 + "' not in graph.";
        }

        // Single word mode: calculate paths to all other words
        if (word2.isEmpty()) {
            return calcShortestPathsToAll(word1);
        }
        
        // Two words mode: check if target word is in graph
        if (!graph.containsNode(word2)) {
            return "Error: Target word '" + word2 + "' not in graph.";
        }
        
        // Use modified Dijkstra's algorithm to find all shortest paths
        Map<String, Double> distances = new HashMap<>(); // Store shortest distance from start to each node
        Map<String, List<List<String>>> allPaths = new HashMap<>(); // Store all paths for each node
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparing(distances::get));
        Set<String> visited = new HashSet<>();
        
        // Initialize distances and paths
        for (String node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            allPaths.put(node, new ArrayList<>());
        }
        distances.put(word1, 0.0);
        List<String> initialPath = new ArrayList<>();
        initialPath.add(word1);
        allPaths.get(word1).add(initialPath);
        queue.add(word1);
        
        // Modified Dijkstra's algorithm to find all shortest paths
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (visited.contains(current)) {
                continue;
            }
            
            visited.add(current);
            
            // Check all neighbors
            Map<String, Integer> neighbors = graph.getEdges(current);
            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                String neighborNode = neighbor.getKey();
                double weight = 1.0 / neighbor.getValue();
                
                double newDistance = distances.get(current) + weight;
                
                // If we found a shorter or equal distance path
                if (newDistance <= distances.get(neighborNode)) {
                    if (newDistance < distances.get(neighborNode)) {
                        // Clear existing paths if we found a shorter distance
                        allPaths.get(neighborNode).clear();
                        distances.put(neighborNode, newDistance);
                        queue.add(neighborNode);
                    }
                    
                    // Add all possible paths through current node
                    for (List<String> pathToCurrent : allPaths.get(current)) {
                        List<String> newPath = new ArrayList<>(pathToCurrent);
                        newPath.add(neighborNode);
                        allPaths.get(neighborNode).add(newPath);
                    }
                }
            }
        }
        
        // If target node has no paths, there's no path
        if (allPaths.get(word2).isEmpty()) {
            return "No path exists from '" + word1 + "' to '" + word2 + "'.";
        }
        
        // Generate result string with all paths
        StringBuilder result = new StringBuilder();
        result.append("All shortest paths from '").append(word1).append("' to '").append(word2).append("':\n");
        
        List<List<String>> paths = allPaths.get(word2);
        for (int i = 0; i < paths.size(); i++) {
            result.append(i + 1).append(". ");
            result.append(String.join(" -> ", paths.get(i)));
            result.append("\n");
        }
        result.append("Path length: ").append(String.format("%.4f", distances.get(word2)));
        
        // Visualize all paths in different colors
        String outputPath = "shortest_paths.png";
        GraphVisualizer.visualizeMultiplePaths(graph, paths, outputPath);
        
        return result.toString();
    }

    /**
     * Calculate shortest paths from one word to all other words
     * @param startWord Start word
     * @return Description of all shortest paths
     */
    private String calcShortestPathsToAll(String startWord) {
        StringBuilder result = new StringBuilder();
        result.append("Shortest paths from '").append(startWord).append("' to all other words:\n\n");
        
        // Get all nodes except the start word
        Set<String> nodes = new HashSet<>(graph.getNodes());
        nodes.remove(startWord);
        
        if (nodes.isEmpty()) {
            return "No other words in the graph to find paths to.";
        }

        // Sort nodes alphabetically for better readability
        List<String> sortedNodes = new ArrayList<>(nodes);
        Collections.sort(sortedNodes);
        
        // Calculate paths to each word
        int pathCount = 0;
        for (String endWord : sortedNodes) {
            // Calculate path without showing the graph visualization
            String pathResult = findShortestPath(startWord, endWord);
            if (!pathResult.startsWith("No path")) {
                pathCount++;
                result.append(pathCount).append(". To '").append(endWord).append("':\n");
                result.append(pathResult).append("\n\n");
            }
        }
        
        if (pathCount == 0) {
            return "No paths found from '" + startWord + "' to any other word.";
        }
        
        return result.toString();
    }

    /**
     * Helper method to find shortest path between two words without visualization
     * @param word1 Start word
     * @param word2 Target word
     * @return Path description
     */
    private String findShortestPath(String word1, String word2) {
        // Use modified Dijkstra's algorithm to find shortest path
        Map<String, Double> distances = new HashMap<>();
        Map<String, List<List<String>>> allPaths = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparing(distances::get));
        Set<String> visited = new HashSet<>();
        
        // Initialize
        for (String node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            allPaths.put(node, new ArrayList<>());
        }
        distances.put(word1, 0.0);
        List<String> initialPath = new ArrayList<>();
        initialPath.add(word1);
        allPaths.get(word1).add(initialPath);
        queue.add(word1);
        
        // Dijkstra's algorithm
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            
            Map<String, Integer> neighbors = graph.getEdges(current);
            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                String neighborNode = neighbor.getKey();
                double weight = 1.0 / neighbor.getValue();
                
                double newDistance = distances.get(current) + weight;
                if (newDistance <= distances.get(neighborNode)) {
                    if (newDistance < distances.get(neighborNode)) {
                        allPaths.get(neighborNode).clear();
                        distances.put(neighborNode, newDistance);
                        queue.add(neighborNode);
                    }
                    for (List<String> pathToCurrent : allPaths.get(current)) {
                        List<String> newPath = new ArrayList<>(pathToCurrent);
                        newPath.add(neighborNode);
                        allPaths.get(neighborNode).add(newPath);
                    }
                }
            }
        }
        
        if (allPaths.get(word2).isEmpty()) {
            return "No path exists from '" + word1 + "' to '" + word2 + "'.";
        }
        
        StringBuilder result = new StringBuilder();
        List<List<String>> paths = allPaths.get(word2);
        result.append(String.join(" -> ", paths.get(0)));
        result.append("\nPath length: ").append(String.format("%.4f", distances.get(word2)));
        
        return result.toString();
    }
    
    /**
     * Calculate TF-IDF value for a word
     * @param word Word to calculate for
     * @return TF-IDF value
     */
    private double calculateTFIDF(String word) {
        // Calculate Term Frequency (TF)
        double tf = 0.0;
        Map<String, Integer> incomingEdges = graph.getIncomingEdges(word);
        Map<String, Integer> outgoingEdges = graph.getEdges(word);
        
        // Sum up all edge weights
        for (int weight : incomingEdges.values()) {
            tf += weight;
        }
        for (int weight : outgoingEdges.values()) {
            tf += weight;
        }
        
        // Calculate Inverse Document Frequency (IDF)
        double totalNodes = graph.getNodes().size();
        double nodesWithWord = 0;
        
        // Count how many nodes are connected to this word
        for (String node : graph.getNodes()) {
            if (!node.equals(word)) {
                if (graph.getEdgeWeight(node, word) > 0 || graph.getEdgeWeight(word, node) > 0) {
                    nodesWithWord++;
                }
            }
        }
        
        // Avoid division by zero
        if (nodesWithWord == 0) {
            nodesWithWord = 1;
        }
        
        double idf = Math.log(totalNodes / nodesWithWord);
        
        return tf * idf;
    }
    
    /**
     * Calculate PageRank value for a word
     * @param word Word to calculate PR value for
     * @return PR value
     */
    public Double calcPageRank(String word) {
        word = word.toLowerCase();
        
        // Check if word is in graph
        if (!graph.containsNode(word)) {
            return 0.0;
        }
        
        // Get all nodes in graph
        Set<String> nodes = graph.getNodes();
        int n = nodes.size();
        
        // Calculate TF-IDF for all nodes
        Map<String, Double> tfidf = new HashMap<>();
        double totalTFIDF = 0.0;
        for (String node : nodes) {
            double value = calculateTFIDF(node);
            tfidf.put(node, value);
            totalTFIDF += value;
        }
        
        // Initialize PR values using normalized TF-IDF
        Map<String, Double> pr = new HashMap<>();
        for (String node : nodes) {
            // If totalTFIDF is 0, use uniform distribution
            if (totalTFIDF == 0) {
                pr.put(node, 1.0 / n);
            } else {
                pr.put(node, tfidf.get(node) / totalTFIDF);
            }
        }
        
        // PageRank iteration calculation
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Map<String, Double> newPr = new HashMap<>();
            double sinkPR = 0.0; // 累积所有出度为0的节点的PR值
            
            // First pass: identify sink nodes (nodes with no outgoing edges)
            for (String node : nodes) {
                Map<String, Integer> outEdges = graph.getEdges(node);
                if (outEdges.isEmpty()) {
                    sinkPR += pr.get(node);
                }
            }
            
            // Calculate new PR value for each node
            for (String node : nodes) {
                // Get all nodes pointing to this node
                Map<String, Integer> incomingNodes = graph.getIncomingEdges(node);
                
                double sum = 0.0;
                // Add contribution from nodes with outgoing edges
                for (Map.Entry<String, Integer> entry : incomingNodes.entrySet()) {
                    String incomingNode = entry.getKey();
                    int weight = entry.getValue();
                    
                    // Skip sink nodes as they are handled separately
                    Map<String, Integer> outEdges = graph.getEdges(incomingNode);
                    if (outEdges.isEmpty()) {
                        continue;
                    }
                    
                    // Get total outgoing edge weight for incomingNode
                    int outSum = 0;
                    for (int outWeight : outEdges.values()) {
                        outSum += outWeight;
                    }
                    
                    // Calculate contribution using edge weights
                    sum += pr.get(incomingNode) * ((double) weight / outSum);
                }
                
                // Add contribution from sink nodes (distributed equally)
                sum += sinkPR / n;
                
                // New PR value = (1-d)/n + d*sum
                // For nodes with high TF-IDF, increase their random jump probability
                double randomJumpProb = (1 - DAMPING_FACTOR) * (tfidf.get(node) / totalTFIDF);
                if (totalTFIDF == 0) {
                    randomJumpProb = (1 - DAMPING_FACTOR) / n;
                }
                double newRank = randomJumpProb + DAMPING_FACTOR * sum;
                newPr.put(node, newRank);
            }
            
            // Update PR values
            pr = newPr;
        }
        
        // Return PR value for specified word
        return pr.get(word);
    }
    
    /**
     * Random walk on the graph
     * @return Walk path
     */
    public String randomWalk() {
        // Check if graph is empty
        if (graph.getNodes().isEmpty()) {
            return "Graph is empty, cannot perform random walk.";
        }
        
        // Randomly select a start node
        List<String> nodes = new ArrayList<>(graph.getNodes());
        String currentNode = nodes.get(random.nextInt(nodes.size()));
        
        List<String> path = new ArrayList<>();
        path.add(currentNode);
        
        // Set to detect repeated edges
        Set<String> visitedEdges = new HashSet<>();
        
        boolean done = false;
        while (!done) {
            // Get all outgoing edges from current node
            Map<String, Integer> neighbors = graph.getEdges(currentNode);
            
            // If no outgoing edges, end walk
            if (neighbors.isEmpty()) {
                done = true;
                continue;
            }
            
            // Randomly select an outgoing edge
            List<String> neighborNodes = new ArrayList<>(neighbors.keySet());
            String nextNode = neighborNodes.get(random.nextInt(neighborNodes.size()));
            
            // Check if edge has been visited
            String edge = currentNode + "->" + nextNode;
            if (visitedEdges.contains(edge)) {
                done = true;
                continue;
            }
            
            // Record visited edge
            visitedEdges.add(edge);
            
            // Update current node and add to path
            currentNode = nextNode;
            path.add(currentNode);
        }
        
        // Convert path to text
        String result = String.join(" ", path);
        
        // Save result to file
        try {
            PrintWriter writer = new PrintWriter("random_walk.txt");
            writer.println(result);
            writer.close();
            System.out.println("Random walk result saved to random_walk.txt");
        } catch (IOException e) {
            System.err.println("Error saving random walk result: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        WordGraph wordGraph = new WordGraph();
        Scanner scanner = new Scanner(System.in);
        String filePath = "";
        
        try {
            // Check if file path is provided as command line argument
            if (args.length > 0) {
                filePath = args[0];
            } else {
                // Otherwise ask user for file path
                System.out.println("Please enter text file path:");
                filePath = scanner.nextLine();
            }
            
            // Read file and build graph
            System.out.println("Building graph from file '" + filePath + "'...");
            wordGraph.buildGraphFromFile(filePath);
            System.out.println("Graph built successfully!");
            
            // Show directed graph
            System.out.println("\nShowing directed graph:");
            wordGraph.showDirectedGraph(wordGraph.graph, scanner);
            
            // Main menu
            boolean exit = false;
            while (!exit) {
                System.out.println("\nPlease select a function:");
                System.out.println("1. Query bridge words");
                System.out.println("2. Generate new text with bridge words");
                System.out.println("3. Calculate shortest path between words");
                System.out.println("4. Calculate PageRank value");
                System.out.println("5. Random walk");
                System.out.println("6. Show directed graph again");
                System.out.println("0. Exit program");
                
                System.out.print("Enter option (0-6): ");
                int choice;
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, please enter a number between 0-6.");
                    continue;
                }
                
                switch (choice) {
                    case 0:
                        exit = true;
                        break;
                    case 1:
                        System.out.print("Enter first word: ");
                        String word1 = scanner.nextLine();
                        System.out.print("Enter second word: ");
                        String word2 = scanner.nextLine();
                        String bridgeWords = wordGraph.queryBridgeWords(word1, word2);
                        System.out.println(bridgeWords);
                        break;
                    case 2:
                        System.out.print("Enter text: ");
                        String inputText = scanner.nextLine();
                        String newText = wordGraph.generateNewText(inputText);
                        System.out.println("Generated new text:");
                        System.out.println(newText);
                        break;
                    case 3:
                        System.out.println("Enter one word to show all paths from that word,");
                        System.out.println("or enter two words separated by space to find shortest path between them:");
                        String input = scanner.nextLine().trim().toLowerCase();
                        String[] words = input.split("\\s+");
                        
                        if (words.length == 0 || words[0].isEmpty()) {
                            System.out.println("Error: Please enter at least one word.");
                            break;
                        } else if (words.length == 1) {
                            // 单词模式：显示从该单词到所有其他单词的最短路径
                            String shortestPath = wordGraph.calcShortestPath(words[0], "");
                            System.out.println(shortestPath);
                        } else if (words.length == 2) {
                            // 两个单词模式：显示两个单词之间的最短路径
                            String shortestPath = wordGraph.calcShortestPath(words[0], words[1]);
                            System.out.println(shortestPath);
                        }
                        else {
                            System.out.println("Error: Please enter one word or two words separated by space.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter word to calculate PageRank value: ");
                        String wordForPR = scanner.nextLine();
                        Double pr = wordGraph.calcPageRank(wordForPR);
                        System.out.println("PageRank value for word '" + wordForPR + "': " + String.format("%.4f", pr));
                        break;
                    case 5:
                        System.out.println("Starting random walk...");
                        String walkResult = wordGraph.randomWalk();
                        System.out.println("Random walk result:");
                        System.out.println(walkResult);
                        break;
                    case 6:
                        wordGraph.showDirectedGraph(wordGraph.graph, scanner);
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                        break;
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
} 