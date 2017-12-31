package exp1;

import java.util.*;

/**
 * Created by deded on 2017/3/30.
 */
abstract class Graph {
    //用于遍历过程中节点颜色变化
    static final int WHITE = 0;
    static final int GRAY = 1;
    static final int BLACK = 2;
    //存储图的边
    ArrayList<ArrayList<Edge>> edges;

    protected int time;
    static int maxWeight;

    private Graph(int vertexAmount) {
        edges = new ArrayList<>(vertexAmount);
        for (int i = 0; i < vertexAmount; i++) {
            edges.add(new ArrayList<>());
        }
    }

    abstract void addEdge(int from, int to, int weight);

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("graph:\n");
        for (int i = 0; i < edges.size(); i++) {
            stringBuilder.append("from " + i + ":");
            ArrayList<Edge> cur = edges.get(i);
            if (!cur.isEmpty()) {
                stringBuilder.append('\n');
                for (Edge edge : cur) {
                    stringBuilder.append(edge);
                }
                stringBuilder.append("\b;\n");
            } else {
                stringBuilder.append(" no edge;\n");
            }

        }
        stringBuilder.append("end\n");
        return stringBuilder.toString();
    }

    public boolean contains(int from, int to) {
        if (from == to) {
            return true;
        }

        ArrayList<Edge> curEdgeList = edges.get(from);

        return curEdgeList.stream().anyMatch(cur -> cur.to == to);

//        for (Edge edge : curEdgeList) {
//            if (edge.to == to) {
//                return true;
//            }
//        }
//        return false;
    }

    public abstract void primMst(int r);

    public abstract void kruscalMst();

    public void bfs(int u) {
        if (u < 0 || u >= edges.size()) {
            System.out.println("illegal start point for bfs, and start point will be 0");
            u = 0;
        }
        int vertexAmount = edges.size();
        BfsVertices[] vertices = new BfsVertices[vertexAmount];
        for (int i = 0; i < vertexAmount; i++) {
            vertices[i] = new BfsVertices(i);
        }

        StringBuilder result = new StringBuilder("bfs:\n");
        vertices[u].pi = -1;
        vertices[u].d = 0;
        vertices[u].color = GRAY;
        result.append(vertices[u] + " ");
        ArrayList<BfsVertices> queue = new ArrayList<>();
        queue.add(vertices[u]);

        while (!queue.isEmpty()) {
            BfsVertices curVertices = queue.get(0);
            queue.remove(0);
            ArrayList<Edge> curEdges = edges.get(curVertices.id);
            for (Edge edge : curEdges) {
                BfsVertices destVertices = vertices[edge.to];
                if (destVertices.color == WHITE) {
                    destVertices.d = curVertices.d + 1;
                    destVertices.pi = curVertices.id;
                    destVertices.color = GRAY;
                    queue.add(destVertices);
                    result.append(destVertices);
                }
            }
            curVertices.color = BLACK;
        }
        System.out.println(result);
    }

    public void dfs(int u) {
        int vertexAmount = edges.size();
        if (u < 0 || u >= vertexAmount) {
            System.out.println("illegal start point for dfs, and start point will be 0");
            u = 0;
        }

        DfsVertices[] vertex = new DfsVertices[vertexAmount];
        for (int i = 0; i < vertexAmount; i++) {
            vertex[i] = new DfsVertices(i);
        }

        time = 0;
        ArrayList<DfsVertices> queue = new ArrayList<>(vertexAmount);

        dfsVisitWithoutRecursion(u, vertex, queue);

        for (int i = edges.size() - 1; i >= 0; i--) {
            dfsVisitWithoutRecursion(i, vertex, queue);
        }
    }

    public void bellmanShortestPath(int s) {
        int vertexAmount = edges.size();
        if (s < 0 || s >= vertexAmount) {
            s = 0;
        }

        ArrayList<ShortestPathVertices> vertex = new ArrayList<>(vertexAmount);
        for (int i = 0; i < vertexAmount; i++) {
            vertex.add(new ShortestPathVertices(i, maxWeight));
        }
        vertex.get(s).d = 0;

        ArrayList<Edge> allEdges = new ArrayList<>();
        for (ArrayList<Edge> edgeArrayList : edges) {
            allEdges.addAll(edgeArrayList);
        }

        for (int i = 0; i < vertexAmount; i++) {
            for (Edge edge : allEdges) {
                ShortestPathVertices u = vertex.get(edge.from);
                ShortestPathVertices v = vertex.get(edge.to);
//                if (v.id != s && v.pi == -1) {
//                    v.d = u.d + edge.weight;
//                    v.pi = u.id;
//                } else if (v.d > u.d + edge.weight) {
//                    v.d = u.d + edge.weight;
//                    v.pi = u.id;
//                }
                if (v.d > u.d + edge.weight) {
                    v.d = u.d + edge.weight;
                    v.pi = u.id;
                }
            }
        }

        for (Edge edge : allEdges) {
            ShortestPathVertices u = vertex.get(edge.from);
            ShortestPathVertices v = vertex.get(edge.to);
            if (v.d > u.d + edge.weight) {
                System.out.println("has minor circle");
                return;
            }
        }

        vertex.sort(null);
        for (ShortestPathVertices vertices : vertex) {
            System.out.print(vertices);
        }
        System.out.println();
    }

    public void dijkstraShortestPath(int s) {
        int vertexAmount = edges.size();
        if (s < 0 || s >= vertexAmount) {
            s = 0;
        }

        ArrayList<ShortestPathVertices> vertex = new ArrayList<>(vertexAmount);
        for (int i = 0; i < vertexAmount; i++) {
            vertex.add(new ShortestPathVertices(i, maxWeight));
        }
        vertex.get(s).d = 0;

        TreeSet<ShortestPathVertices> queue = new TreeSet<>();
        queue.add(vertex.get(s));
        while (!queue.isEmpty()) {
            ShortestPathVertices u = queue.pollFirst();
            for (Edge edge : edges.get(u.id)) {
                ShortestPathVertices v = vertex.get(edge.to);
                if (v.d > u.d + edge.weight) {
                    v.d = u.d + edge.weight;
                    v.pi = u.id;
                    queue.add(v);
                }
            }
        }

        vertex.sort(null);
        for (ShortestPathVertices vertices : vertex) {
            System.out.print(vertices);
        }
        System.out.println();
    }

    public void dfsVisitWithoutRecursion(int u, DfsVertices[] vertex, ArrayList<DfsVertices> queue) {
        if (vertex[u].color != WHITE) {
            return;
        }
        ArrayList<Integer> sequence = new ArrayList<>();
        sequence.add(u);
        vertex[u].pi = -1;
        vertex[u].d = ++time;
        vertex[u].color = GRAY;

        queue.add(vertex[u]);

        while (!queue.isEmpty()) {
            DfsVertices curVertices = queue.get(0);
            ArrayList<Edge> curEdges = edges.get(curVertices.id);
            boolean noMoreSon = true;
            for (Edge edge : curEdges) {
                DfsVertices destVertices = vertex[edge.to];
                if (destVertices.color == WHITE) {
                    noMoreSon = false;
                    destVertices.pi = curVertices.id;
                    destVertices.d = ++time;
                    destVertices.color = GRAY;
                    queue.add(0, destVertices);
                    sequence.add(destVertices.id);
                    break;
                }
            }
            if (noMoreSon) {
                curVertices.color = BLACK;
                curVertices.f = ++time;
                queue.remove(0);
            }
        }


        System.out.println("dfs from " + u + " : ");
        for (int i : sequence) {
            System.out.print(vertex[i]);
        }


    }

    public abstract void getDirectComponent();

    private static void fillGraph(Graph graph, int vertexAmount, int edgeAmount, int weightBound,
                                  boolean hasMinorWeight) {
        Random random = new Random();
        int edgesBuilt = 0;

        maxWeight = weightBound;

        int maxEdgeAmount = vertexAmount * (vertexAmount - 1) / 2;
        if (edgeAmount > maxEdgeAmount || edgeAmount < 0) {
            edgeAmount = maxEdgeAmount;
        }

        while (edgesBuilt < edgeAmount) {
            int from = random.nextInt(vertexAmount);
            int to = random.nextInt(vertexAmount);
            if (!graph.contains(from, to)) {
                int weight = random.nextInt(weightBound);
                if (hasMinorWeight) {
                    weight = weight * 2 - weightBound;
                }
                graph.addEdge(from, to, weight);
                edgesBuilt++;
            }
        }

        for (ArrayList arrayList : graph.edges) {
            arrayList.sort(null);
        }
    }

    public static UnDigraph getRandomUnDigraph(int vertexAmount, int edgeAmount, int weightBound,
                                               boolean hasMinorWeight) {
        UnDigraph toReturn = new UnDigraph(vertexAmount);

        fillGraph(toReturn, vertexAmount, edgeAmount, weightBound, hasMinorWeight);

        return toReturn;
    }

    public static Digraph getRandomDigraph(int vertexAmount, int edgeAmount, int weightBound,
                                           boolean hasMinorWeight) {
        Digraph toReturn = new Digraph(vertexAmount);

        fillGraph(toReturn, vertexAmount, edgeAmount, weightBound, hasMinorWeight);

        return toReturn;
    }

    public static class Digraph extends Graph {

        public Digraph(int vertexAmount) {
            super(vertexAmount);
        }

        @Override
        void addEdge(int from, int to, int weight) {
            edges.get(from).add(new Edge(from, to, weight));
        }

        @Override
        public void getDirectComponent() {
            int vertexAmount = edges.size();
            DfsVertices[] vertex = new DfsVertices[vertexAmount];
            for (int i = 0; i < vertexAmount; i++) {
                vertex[i] = new DfsVertices(i);
            }

            dfsForDirectComponent(0, vertex);
            DfsVertices[] vertexOrder = Arrays.copyOf(vertex, vertexAmount);
            Arrays.sort(vertexOrder);
            for (DfsVertices vertices : vertex) {
                vertices.color = WHITE;
            }
            time = 0;

            Digraph transposedGraph = getTransposedGraph();

            ArrayList<DfsVertices> queue = new ArrayList<>();

            ArrayList<ArrayList<DfsVertices>> dfsTrees = new ArrayList<>();
            for (int i = vertexAmount - 1; i >= 0; i--) {
                ArrayList<DfsVertices> tree =
                        transposedGraph.dfsVisitForDirectComponent(vertexOrder[i].id, vertex, queue);
                if (tree != null) {
                    dfsTrees.add(tree);
                }
            }
            System.out.println("direct component:");
            for (ArrayList<DfsVertices> tree : dfsTrees) {
                System.out.println(tree);
            }
        }

        private void dfsForDirectComponent(int u, DfsVertices[] vertex) {
            int vertexAmount = edges.size();
            if (u < 0 || u >= vertexAmount) {
                System.out.println("illegal start point for dfs, and start point will be 0");
                u = 0;
            }
            ArrayList<DfsVertices> queue = new ArrayList<>();
            dfsVisitForDirectComponent(u, vertex, queue);
            for (int i = 0; i < vertexAmount; i++) {
                dfsVisitForDirectComponent(i, vertex, queue);
            }
        }

        Digraph getTransposedGraph() {
            Digraph transposedGraph;

            transposedGraph = new Digraph(this.edges.size());

            for (int i = edges.size() - 1; i >= 0; i--) {
                ArrayList<Edge> edgeArrayList = edges.get(i);
                for (Edge edge : edgeArrayList) {
                    transposedGraph.addEdge(edge.to, i, edge.weight);
                }
            }
            return transposedGraph;
        }

        private ArrayList<DfsVertices> dfsVisitForDirectComponent(int u, DfsVertices[] vertex, ArrayList<DfsVertices> queue) {
            if (vertex[u].color != WHITE) {
                return null;
            }
            ArrayList<DfsVertices> tree = new ArrayList<>();
            tree.add(vertex[u]);
            vertex[u].pi = -1;
            vertex[u].d = ++time;
            vertex[u].color = GRAY;
            queue.add(vertex[u]);

            while (!queue.isEmpty()) {
                DfsVertices curVertices = queue.get(0);
                ArrayList<Edge> curEdges = edges.get(curVertices.id);
                boolean noMoreSon = true;

                for (Edge edge : curEdges) {
                    DfsVertices destVertices = vertex[edge.to];
                    if (destVertices.color == WHITE) {
                        noMoreSon = false;
                        destVertices.d = ++time;
                        destVertices.color = GRAY;
                        destVertices.pi = curVertices.id;
                        queue.add(0, destVertices);
                        tree.add(destVertices);
                        break;
                    }
                }
                if (noMoreSon) {
                    curVertices.f = ++time;
                    curVertices.color = BLACK;
                    queue.remove(0);
                }
            }
            return tree;
        }

        @Override
        public void primMst(int r) {
            System.out.println("unsupported method for digraph");
        }

        @Override
        public void kruscalMst() {
            System.out.println("unsupported method for digraph");
        }
    }

    public static class UnDigraph extends Graph {

        public UnDigraph(int vertexAmount) {
            super(vertexAmount);
        }

        @Override
        void addEdge(int from, int to, int weight) {
            edges.get(from).add(new Edge(from, to, weight));
            edges.get(to).add(new Edge(to, from, weight));
        }

        @Override
        public void primMst(int r) {
            int verticesAmount = edges.size();
            if (r < 0 || r >= verticesAmount) {
                r = 0;
            }

            PrimVertices[] vertex = new PrimVertices[verticesAmount];
            for (int i = 0; i < verticesAmount; i++) {
                vertex[i] = new PrimVertices(i, verticesAmount);
            }

            ArrayList<PrimVertices> verticesArrayList = new ArrayList<>(verticesAmount);
            verticesArrayList.addAll(Arrays.asList(vertex));

            vertex[r].key = 0;
            verticesArrayList.sort(null);

            while (!verticesArrayList.isEmpty()) {
                int u = verticesArrayList.get(0).id;
                verticesArrayList.remove(0);
                for (Edge edge : edges.get(u)) {
                    int v = edge.to;
                    if (verticesArrayList.contains(vertex[v]) && edge.weight < vertex[v].key) {
                        vertex[v].pi = u;
                        vertex[v].key = edge.weight;
                    }
                }
                verticesArrayList.sort(null);
            }
            int weightSum = 0;
            for (PrimVertices vertices : vertex) {
                weightSum += vertices.key;
            }
            System.out.println("primMst\n" + Arrays.toString(vertex) + "\nweight sum : " + weightSum);
        }

        @Override
        public void kruscalMst() {
            Comparator comparator = (o1, o2) -> {
                Edge edge1 = (Edge) o1;
                Edge edge2 = (Edge) o2;
                return Integer.compare(edge1.weight, edge2.weight);
//                if (edge1.weight < edge2.weight) {
//                    return -1;
//                }
//                if (edge1.weight > edge2.weight) {
//                    return 1;
//                }
//                return 0;
            };
            int edgesNum = 0;
            for (ArrayList<Edge> edgeArrayList : edges) {
                edgesNum += edgeArrayList.size();
            }
            ArrayList<Edge> allEdges = new ArrayList<>(edgesNum);
            for (ArrayList<Edge> edgeArrayList : edges) {
                allEdges.addAll(edgeArrayList);
            }
            allEdges.sort(comparator);

            int vertexAmount = edges.size();

            int[] set = new int[vertexAmount];
            for (int i = 0; i < vertexAmount; i++) {
                set[i] = i;
            }

            ArrayList<Edge> result = new ArrayList<>(vertexAmount);

            for (Edge edge : allEdges) {
                if (set[edge.from] != set[edge.to]) {
                    int newValue = set[edge.from];
                    int oldValue = set[edge.to];
                    for (int i = 0; i < vertexAmount; i++) {
                        if (set[i] == oldValue) {
                            set[i] = newValue;
                        }
                    }
                    result.add(edge);
                }
            }

            int weightSum = 0;
            for (Edge edge : result) {
                weightSum += edge.weight;
            }
            System.out.println("kruskalMst:\n" + result + "\nweight sum: " + weightSum);

        }

        @Override
        public void getDirectComponent() {
            System.out.println("unsupported method for unDigraph");
        }
    }

    static class Edge implements Comparable {
        int from;
        int to;
        int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            Edge oEdge = (Edge) o;
            return from == oEdge.from && to == oEdge.to;
        }

        @Override
        public String toString() {
            return "( " + from + "->" + to + ", " + weight + " )\n";
        }

        @Override
        public int compareTo(Object o) {
            Edge eo = (Edge) o;
            return Integer.compare(to, eo.to);
//            if (to < eo.to) {
//                return -1;
//            }
//            if (to > eo.to) {
//                return 1;
//            }
//            return 0;
        }

    }

    static class Vertices implements Comparable {
        int id;

        Vertices(int id) {
            this.id = id;
        }

        @Override
        public int compareTo(Object o) {
            Vertices oVertices = (Vertices) o;
            return Integer.compare(id, oVertices.id);
//            if (oVertices.id < id) {
//                return 1;
//            }
//            if (oVertices.id > id) {
//                return -1;
//            }
//            return 0;
        }

        @Override
        public String toString() {
            return "(" + id + ") ";
        }
    }

    static class BfsVertices extends Vertices {
        int pi;
        int d;
        int color;

        BfsVertices(int id) {
            super(id);
            pi = -1;
            d = -1;
            color = WHITE;
        }

        @Override
        public String toString() {
            return "(" + pi + "->" + id + ", " + d + ") ";
        }
    }

    static class DfsVertices extends Vertices {
        int pi;
        int d;
        int color;
        int f;

        DfsVertices(int id) {
            super(id);
            pi = -1;
            d = -1;
            color = WHITE;
            f = -1;
        }

        @Override
        public int compareTo(Object o) {
            DfsVertices oVertices = (DfsVertices) o;
            return Integer.compare(f, oVertices.f);
//            if (oVertices.f < f) {
//                return 1;
//            }
//            if (oVertices.f > f) {
//                return -1;
//            }
//            return 0;
        }

        @Override
        public String toString() {
            return "(" + pi + "->" + id + ", " + d + ", " + f + ") ";
        }

    }

    static class PrimVertices extends Vertices {
        int key;
        int pi;

        PrimVertices(int id, int maxKey) {
            super(id);
            key = maxKey;
            pi = -1;
        }

        @Override
        public int compareTo(Object o) {
            PrimVertices oVertices = (PrimVertices) o;
            return Integer.compare(key, oVertices.key);
//            if (oVertices.key < key) {
//                return 1;
//            }
//            if (oVertices.key > key) {
//                return -1;
//            }
//            return 0;
        }

        @Override
        public String toString() {
            return "( " + pi + "->" + id + " ," + key + " ) ";
        }

    }

    static class ShortestPathVertices extends Vertices implements Comparable {
        int d;
        int pi;

        ShortestPathVertices(int id, int maxWeight) {
            super(id);
            d = maxWeight;
            pi = -1;
        }

        @Override
        public int compareTo(Object o) {
            ShortestPathVertices os = (ShortestPathVertices) o;
            return Integer.compare(d, os.d);
//            if (os.d < d) {
//                return 1;
//            }
//            if (os.d > d) {
//                return -1;
//            }
//            return 0;
        }

        @Override
        public String toString() {
            return "(" + pi + "->" + id + "," + d + ") ";
        }
    }

}
