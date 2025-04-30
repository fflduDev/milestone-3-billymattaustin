package graph;

import java.util.*;

public class DiGraphImpl implements DiGraph {
    private List<GraphNode> nodes = new ArrayList<>();
    @Override
    public Boolean addNode(GraphNode n) {
        if (getNode(n.getValue()) != null) return false;
        nodes.add(n);
        return true;
    }
    @Override
    public Boolean removeNode(GraphNode n) {
        GraphNode found =getNode(n.getValue());
        if (found ==null) return false;
        nodes.remove(found);
        for (GraphNode other : nodes) {
            other.removeNeighbor(found);
        }
        return true;
	}
    @Override
    public Boolean addEdge(GraphNode src, GraphNode dst, Integer weight) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        if (s ==null ||d == null) return false;
        return s.addNeighbor(d, weight);
    }
    @Override
    public Boolean removeEdge(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        if (s == null || d == null) return false;
        return s.removeNeighbor(d);
    }
    @Override
    public Boolean setEdgeValue(GraphNode src, GraphNode dst, Integer newWeight) {
        GraphNode s =getNode(src.getValue()), d = getNode(dst.getValue());
        if (s == null || d == null || !s.getNeighbors().contains(d)) return false;
        s.removeNeighbor(d);
        s.addNeighbor(d, newWeight);
        return true;
    }
    @Override
    public Integer getEdgeValue(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        if (s == null || d == null) return null;
        return s.getDistanceToNeighbor(d);
    }

    @Override
    public String getNodeValue(GraphNode node) {
        GraphNode n = getNode(node.getValue());
        return (n == null ? null : n.getValue());
    }

    @Override
    public Boolean setNodeValue(GraphNode node, String newValue) {
        GraphNode n = getNode(node.getValue());
        if (n == null) return false;
        n.setValue(newValue);
        return true;
    }
    @Override
    public List<GraphNode> getAdjacentNodes(GraphNode n) {
        GraphNode x = getNode(n.getValue());
        return (x == null ? null : x.getNeighbors());
    }
    @Override
    public Boolean nodesAreAdjacent(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        return (s != null && d != null && s.getNeighbors().contains(d));
    }
    @Override
    public Boolean nodeIsReachable(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d =getNode(dst.getValue());
        if (s == null || d == null) return false;
        Set<GraphNode> seen = new HashSet<>();
        Queue<GraphNode> q = new LinkedList<>();
        q.add(s); seen.add(s);

        while (!q.isEmpty()) {
            GraphNode cur = q.poll();
            if (cur.equals(d)) return true;
            for (GraphNode nbr : cur.getNeighbors()) {
                if (!seen.contains(nbr)) {
                    seen.add(nbr);
                    q.add(nbr);
                }
            }
        }
        return false;
    }
    @Override
    public Boolean hasCycles() {
        Set<GraphNode> white = new HashSet<>(nodes),
                       gray  = new HashSet<>(),
                       black = new HashSet<>();
        for (GraphNode n : nodes) {
            if (white.contains(n) && dfsCycle(n, white, gray, black)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    private boolean dfsCycle(GraphNode n,
                             Set<GraphNode> white,
                             Set<GraphNode> gray,
                             Set<GraphNode> black) {
        white.remove(n);
        gray.add(n);
        for (GraphNode nbr : n.getNeighbors()) {
            if (black.contains(nbr)) continue;
            if (gray.contains(nbr) || dfsCycle(nbr, white, gray, black)) {
                return true;
            }
        }
        gray.remove(n);
        black.add(n);
        return false;
    }
    @Override
    public List<GraphNode> getNodes() {
        return new ArrayList<>(nodes);
    }
    @Override
    public GraphNode getNode(String nodeValue) {
        for (GraphNode n : nodes) {
            if (n.getValue().equals(nodeValue)) return n;
        }
        return null;
    }
    @Override
    public int fewestHops(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        if (s == null || d == null) return -1;
        Map<GraphNode, Integer> dist = new HashMap<>();
        Queue<GraphNode> q = new LinkedList<>();
        dist.put(s, 0);
        q.add(s);
        while (!q.isEmpty()) {
            GraphNode cur = q.poll();
            int steps = dist.get(cur);
            if (cur.equals(d)) return steps;
            for (GraphNode nbr : cur.getNeighbors()) {
                if (!dist.containsKey(nbr)) {
                    dist.put(nbr, steps + 1);
                    q.add(nbr);
                }
            }
        }
        return -1;
    }
    @Override
    public List<GraphNode> getFewestHopsPath(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        List<GraphNode> path = new ArrayList<>();
        if (s == null || d == null) return path;
        Map<GraphNode, GraphNode> parent = new HashMap<>();
        Queue<GraphNode> q = new LinkedList<>();
        q.add(s);
        parent.put(s, null);
        boolean found = false;
        while (!q.isEmpty() && !found) {
            GraphNode cur = q.poll();
            for (GraphNode nbr : cur.getNeighbors()) {
                if (!parent.containsKey(nbr)) {
                    parent.put(nbr, cur);
                    if (nbr.equals(d)) {
                        found = true;
                        break;
                    }
                    q.add(nbr);
                }
            }
        }
        if (!found) return path;
        for (GraphNode step = d;step != null; step = parent.get(step)) {
            path.add(0, step);
        }
        return path;
    }
    @Override
    public int shortestPath(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        if (s == null || d == null) return -1;
        Map<GraphNode, Integer> dist = new HashMap<>();
        for (GraphNode n : nodes) dist.put(n, Integer.MAX_VALUE);
        dist.put(s, 0);
        PriorityQueue<GraphNode> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(s);
        while (!pq.isEmpty()) {
            GraphNode cur = pq.poll();
            int best = dist.get(cur);
            if (cur.equals(d)) return best;
            for (GraphNode nbr : cur.getNeighbors()) {
                Integer w = cur.getDistanceToNeighbor(nbr);
                if (w == null) continue;
                int nd = best + w;
                if (nd < dist.get(nbr)) {
                    dist.put(nbr, nd);
                    pq.remove(nbr);
                    pq.add(nbr);
                }
            }
        }
        return -1;
    }
    @Override
    public List<GraphNode> getShortestPath(GraphNode src, GraphNode dst) {
        GraphNode s = getNode(src.getValue()), d = getNode(dst.getValue());
        List<GraphNode> path = new ArrayList<>();
        if (s == null || d == null) return path;
        Map<GraphNode, Integer> dist   = new HashMap<>();
        Map<GraphNode, GraphNode> parent = new HashMap<>();
        for (GraphNode n : nodes) {
            dist.put(n, Integer.MAX_VALUE);
            parent.put(n, null);
        }
        dist.put(s, 0);
        PriorityQueue<GraphNode> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(s);
        while (!pq.isEmpty()) {
            GraphNode cur = pq.poll();
            if (cur.equals(d)) break;
            int best = dist.get(cur);
            for (GraphNode nbr : cur.getNeighbors()) {
                Integer w = cur.getDistanceToNeighbor(nbr);
                if (w == null) continue;
                int nd = best + w;
                if (nd < dist.get(nbr)) {
                    dist.put(nbr, nd);
                    parent.put(nbr, cur);
                    pq.remove(nbr);
                    pq.add(nbr);
                }
            }
        }
        if (!parent.containsKey(d) || (parent.get(d) == null && !s.equals(d))) {
            return path;
        }
        for (GraphNode step = d; step != null; step = parent.get(step)) {
            path.add(0, step);
        }
        return path;
    }
    @Override
    public Boolean addEdgeStr(String fromValue, String toValue, Integer weight) {
        GraphNode s = getNode(fromValue), d = getNode(toValue);
        if (s == null || d == null) return false;
        return s.addNeighbor(d, weight);
    }
}

