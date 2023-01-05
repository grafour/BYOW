package byow.Core;
import edu.princeton.cs.algs4.Graph;

public class DFSConnected {

    private Graph g;
    private boolean connected;

    /**
     * Performs a DFS on a given graph to see if the graph is fully connected.
     * NOTE: No longer needed.
     */
    public DFSConnected(Graph g) {
        this.g = g;
        isConnected(g);
    }

    private void isConnected(Graph g) {

        int numOfVertices = g.V();
        boolean[] visited = new boolean[numOfVertices];

        DFS(0, g.adj(0), visited);
        for (boolean b : visited) {
            if (!b) {
                connected = false;
                break;
            } else {
                connected = true;
            }
        }
    }

    private void DFS(int v, Iterable<Integer> adj, boolean[] visited) {
        visited[v] = true;

        for (Integer e : adj) {
            Iterable<Integer> nodeNeighbors = g.adj(e);
            if (visited[e] == false) {
                DFS(e, nodeNeighbors, visited);
            }
        }
    }

    public boolean connected() {
        return connected;
    }
}
