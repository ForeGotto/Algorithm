package exp1;

/**
 * Created by deded on 2017/3/30.
 */
public class Test1 {

    static Graph.UnDigraph unDigraph;
    static Graph.Digraph digraph;

    static void testBfs() {
        System.out.println("bfs begin:");
        System.out.println("unDigraph:");
        unDigraph.bfs(0);
        System.out.println("digraph:");
        digraph.bfs(0);
        System.out.println("bfs end");
    }

    static void testDfs() {
        System.out.println("dfs begin:");
        System.out.println("unDigraph:");
        unDigraph.dfs(0);
        System.out.println("digraph:");
        digraph.dfs(0);
        System.out.println("dfs end");
    }

    static void testDirectComponent() {
        System.out.println("strong connected component begin:");
        System.out.println("digraph:");
        digraph.getDirectComponent();
        System.out.println("strong connected component end");
    }

    static void testKruskalMst() {
        System.out.println("Kruskal Mst begin:");
        System.out.println("digraph:");
        unDigraph.kruscalMst();
        System.out.println("Kruskal Mst end");
    }

    static void testPrimMst() {
        System.out.println("Prim Mst begin:");
        System.out.println("digraph:");
        unDigraph.primMst(0);
        System.out.println("Prim Mst end");
    }

    static void testBellmanShortestPath() {
        System.out.println("Bellman shortest path begin:");
        System.out.println("unDigraph:");
        unDigraph.bellmanShortestPath(0);
        System.out.println("digraph:");
        digraph.bellmanShortestPath(0);
        System.out.println("Bellman shortest path end");
    }

    static void testDijkstraShortestPath() {
        System.out.println("Dijkstra shortest path begin:");
        System.out.println("unDigraph:");
        unDigraph.dijkstraShortestPath(0);
        System.out.println("digraph:");
        digraph.dijkstraShortestPath(0);
        System.out.println("Dijkstra shortest path end");
    }

    static void testAll() {
        testBfs();
        testDfs();
        testDirectComponent();
        testKruskalMst();
        testPrimMst();
        testBellmanShortestPath();
        testDijkstraShortestPath();
    }

    public static void main(String[] args) {
//
        unDigraph =
                Graph.getRandomUnDigraph(20,100,100, false);
        digraph = Graph.getRandomDigraph(20, 100, 100, false);

        testAll();  
//        TreeSet<Integer> treeSet = new TreeSet();
//        treeSet.add(3);
//        treeSet.add(2);
//        treeSet.add(1);
//        System.out.println(treeSet.pollFirst());
//        treeSet.add(0);
//        System.out.println(treeSet.first());
    }
}
