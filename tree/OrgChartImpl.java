package tree;

import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class OrgChartImpl implements OrgChart {

    private List<GenericTreeNode<Employee>> nodes = new ArrayList<>();
    private GenericTreeNode<Employee> root = null;

    @Override
    public void addRoot(Employee e) {
        if (root == null) {
            root = new GenericTreeNode<>(e);
            nodes.add(root);
        } else {
            System.out.println("Root already exists!");
        }
    }

    @Override
    public void clear() {
        nodes.clear();
        root = null;
    }

    @Override
    public void addDirectReport(Employee manager, Employee newPerson) {
        GenericTreeNode<Employee> managerNode = findNode(manager);
        if (managerNode != null) {
            GenericTreeNode<Employee> newNode = new GenericTreeNode<>(newPerson);
            managerNode.addChild(newNode);
            nodes.add(newNode);
        } else {
            System.out.println("Manager not found!");
        }
    }

    @Override
    public void removeEmployee(Employee firedPerson) {
        GenericTreeNode<Employee> firedNode = findNode(firedPerson);
        if (firedNode != null && firedNode != root) {
            GenericTreeNode<Employee> managerNode = firedNode.getParent();
            if (managerNode != null) {
                managerNode.removeChild(firedNode);
            }
            nodes.remove(firedNode);
        } else if (firedNode == root) {
            System.out.println("Cannot remove the root employee directly.");
        } else {
            System.out.println("Employee not found!");
        }
    }

    @Override
    public void showOrgChartDepthFirst() {
        if (root != null) {
            depthFirstTraversal(root, 0);
        } else {
            System.out.println("Org chart is empty.");
        }
    }

    private void depthFirstTraversal(GenericTreeNode<Employee> node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("--");
        }
        System.out.println(node.getData());
        for (GenericTreeNode<Employee> child : node.getChildren()) {
            depthFirstTraversal(child, level + 1);
        }
    }

    @Override
    public void showOrgChartBreadthFirst() {
        if (root != null) {
            Queue<GenericTreeNode<Employee>> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                GenericTreeNode<Employee> current = queue.poll();
                System.out.println(current.getData());
                queue.addAll(current.getChildren());
            }
        } else {
            System.out.println("Org chart is empty.");
        }
    }

    private GenericTreeNode<Employee> findNode(Employee e) {
        for (GenericTreeNode<Employee> node : nodes) {
            if (node.getData().equals(e)) {
                return node;
            }
        }
        return null;
    }
}