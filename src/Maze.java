import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k] and costs w.cost()[k]. Indexes start from 0,
 * the line number in walls.txt is the index + 1. The cells are connected by DisjointSet.
 */
public class Maze {

    /**
     * Mission 1. Take the walls in the order of the file. Break a wall when its two cells are in different sets.
     * Returns the indexes of the broken walls, in the order of breaking.
     */
    public static int[] breakWalls(Input.Walls w) {
        int[] result = new int[w.rows() * w.cols() - 1];
        int breakCount = 0;
        int[] a = w.a();
        int[] b = w.b();

        DisjointSet disjointSet = new DisjointSet(w.rows() * w.cols());

        for(int i = 0; i < w.count(); i++){
            if(disjointSet.union(a[i], b[i])){
                result[breakCount] = i;
                breakCount++;
            }
        }
        
        return result;
    }

    /**
     * Mission 2. Kruskal: the same, but take the walls from the cheapest to the most expensive.
     * Returns the indexes of the broken walls.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int[] result = new int[w.rows() * w.cols() - 1];
        Integer[] order = new Integer[w.count()];
        int breakCount = 0;
        int[] a = w.a();
        int[] b = w.b();
        DisjointSet disjointSet = new DisjointSet(w.rows() * w.cols());

        for(int i = 0; i < w.count(); i++){
            order[i] = i;
        }

        int[] cost = w.cost();
        Arrays.sort(order, (x, y) -> Integer.compare(cost[x], cost[y]));

        for(int i = 0; i < w.count(); i++){
            if(disjointSet.union(a[order[i]], b[order[i]])){
                result[breakCount] = order[i];
                breakCount++;
            }
        }
        return result;
    }

    /**
     * Mission 2. Prim with a priority queue. Returns the total cost of the cheapest maze.
     * It must be equal to the total cost of cheapestMaze.
     */
    public static long primCost(Input.Walls w) {
        int V = w.rows() * w.cols();
        int count = w.count();
        int[] cost = w.cost();
        int[] a = w.a();
        int[] b = w.b();
        int[] head = new int[V];
        Arrays.fill(head, -1);
        int[] next = new int[2 * count];
        int[] to = new int[2 * count];

        for(int i = 0; i < count; i++){
            next[2 * i] = head[a[i]];
            head[a[i]] = 2 * i;
            to[2 * i] = b[i];
            
            next[2 * i + 1] = head[b[i]];
            head[b[i]] = 2 * i + 1;
            to[2 * i + 1] = a[i];
        }

        boolean[] inTree = new boolean[V];
        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> Integer.compare(cost[x / 2], cost[y / 2]));
        long total = 0;
        int taken = 0;

        inTree[0] = true;
        for(int e = head[0]; e != -1; e = next[e]){
            pq.add(e);
        }

        while (!pq.isEmpty() && taken < V - 1){
            int e = pq.poll();
            int u = to[e];
            if(inTree[u]){
                continue;
            }

            total += cost[e/2];
            taken++;
            inTree[u] = true;
            for (int f = head[u]; f != -1; f = next[f]){
                pq.add(f);
            }
        }
        return total;
    }
}
