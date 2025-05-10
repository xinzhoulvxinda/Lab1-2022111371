import java.util.*;

/**
 * Directed graph class for representing word relationships
 */
public class DirectedGraph {
    // Graph representation using adjacency map
    private Map<String, Map<String, Integer>> adjacencyMap;
    
    /**
     * Constructor
     */
    public DirectedGraph() {
        adjacencyMap = new HashMap<>();
    }
    
    /**
     * Add a node (word) to the graph
     * @param word The word to add as a node
     */
    public void addNode(String word) {
        word = word.toLowerCase(); // Convert to lowercase
        if (!adjacencyMap.containsKey(word)) {
            adjacencyMap.put(word, new HashMap<>());
        }
    }
    
    /**
     * Add an edge from source word to target word, or increase the weight of an existing edge
     * @param fromWord Source word
     * @param toWord Target word
     */
    public void addEdge(String fromWord, String toWord) {
        fromWord = fromWord.toLowerCase();
        toWord = toWord.toLowerCase();
        
        // Ensure both words are in the graph
        addNode(fromWord);
        addNode(toWord);
        
        // Get the adjacency list of fromWord
        Map<String, Integer> neighbors = adjacencyMap.get(fromWord);
        
        // Update edge weight
        int weight = neighbors.getOrDefault(toWord, 0);
        neighbors.put(toWord, weight + 1);
    }
    
    /**
     * Get all nodes (words) in the graph
     * @return Set of all nodes
     */
    public Set<String> getNodes() {
        return adjacencyMap.keySet();
    }
    
    /**
     * Check if the graph contains the specified node (word)
     * @param word The word to check
     * @return true if the graph contains the word, false otherwise
     */
    public boolean containsNode(String word) {
        return adjacencyMap.containsKey(word.toLowerCase());
    }
    
    /**
     * Get all edges from a node
     * @param fromWord Source node
     * @return Map of all edges from the node, mapped as <target node, weight>
     */
    public Map<String, Integer> getEdges(String fromWord) {
        fromWord = fromWord.toLowerCase();
        if (!adjacencyMap.containsKey(fromWord)) {
            return new HashMap<>();
        }
        return new HashMap<>(adjacencyMap.get(fromWord));
    }
    
    /**
     * Get the weight of an edge between two nodes
     * @param fromWord Source node
     * @param toWord Target node
     * @return Weight of the edge, or 0 if the edge doesn't exist
     */
    public int getEdgeWeight(String fromWord, String toWord) {
        fromWord = fromWord.toLowerCase();
        toWord = toWord.toLowerCase();
        
        if (!adjacencyMap.containsKey(fromWord)) {
            return 0;
        }
        
        Map<String, Integer> neighbors = adjacencyMap.get(fromWord);
        return neighbors.getOrDefault(toWord, 0);
    }
    
    /**
     * Get all nodes that point to a specified node
     * @param toWord Target node
     * @return Map of all nodes that point to the target node, mapped as <source node, weight>
     */
    public Map<String, Integer> getIncomingEdges(String toWord) {
        toWord = toWord.toLowerCase();
        Map<String, Integer> incomingEdges = new HashMap<>();
        
        for (String fromWord : adjacencyMap.keySet()) {
            int weight = getEdgeWeight(fromWord, toWord);
            if (weight > 0) {
                incomingEdges.put(fromWord, weight);
            }
        }
        
        return incomingEdges;
    }
    
    /**
     * Get string representation of the graph
     * @return Graph description string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Directed Graph Structure:\n");
        
        for (String fromWord : adjacencyMap.keySet()) {
            sb.append(fromWord).append(" -> ");
            
            Map<String, Integer> neighbors = adjacencyMap.get(fromWord);
            if (neighbors.isEmpty()) {
                sb.append("[No outgoing edges]");
            } else {
                boolean first = true;
                for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(entry.getKey()).append("(weight: ").append(entry.getValue()).append(")");
                    first = false;
                }
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
} 