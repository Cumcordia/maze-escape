import java.util.Arrays;

/**
 * Mission 3. Escape: DFS from cell 0 to the last cell (rows * cols - 1) in the maze of Mission 1.
 * Two neighbouring cells are connected when the wall between them is broken.
 * The neighbours are tried in this order: up, right, down, left (cell - cols, cell + 1, cell + cols, cell - 1).
 * The ready method Graphs.open(w, broken) gives you the open neighbours of every cell, in no particular order:
 * to visit them in the required order, go through the four cells above one by one and take those that are in the list.
 * The program must not overflow the stack on a maze of 1000 by 1000 cells.
 */
public class Escape {

    /**
     * path: the cells of the way out, from 0 to the last cell.
     * visited: how many cells are marked as visited at the moment the exit is marked (the start and the exit are counted).
     * deadEnds: how many cells of the maze have exactly one open neighbour.
     */
    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.rows() * w.cols();
        int[][] adj = Graphs.open(w, broken);
        int[] offset = {-w.cols(), 1, w.cols(), -1};
        int[] stack = new int[n];
        boolean[] seen = new boolean[n];
        int[] dir = new int[n];
        int top = 0;
        int visited = 1;
        int deadEnds = 0;
        stack[0] = 0;
        seen[0] = true;

        for (int v = 0; v < n; v++) {
            if (adj[v].length == 1) deadEnds++;
        }

        while(stack[top] != n -1){
            int v = stack[top];
            if(dir[v] < 4){
                int u = v + offset[dir[v]];
                dir[v]++;

                if(contains(adj[v], u) && !seen[u]){
                    seen[u] = true;
                    visited++;
                    stack[++top] = u;
                }
            } else{
                top--;
            }
        }

        int[] path = Arrays.copyOf(stack, top + 1);
        return new Result(path, visited, deadEnds);
    }

    public static boolean contains(int[] list, int x){
        for (int y : list){
            if(x == y){
                return true;
            }
        }
        return false;
    }
}
