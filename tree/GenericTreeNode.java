package tree;
import java.util.ArrayList;
import java.util.*;

public class GenericTreeNode<E> {
    private E data;
    private GenericTreeNode<E> parent;
    private List<GenericTreeNode<E>> children;

    public GenericTreeNode(E theItem) {
        data = theItem;
        children = new ArrayList<>();
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public GenericTreeNode<E> getParent() {
        return parent;
    }

    public void setParent(GenericTreeNode<E> parent) {
        this.parent = parent;
    }

    public List<GenericTreeNode<E>> getChildren() {
        return children;
    }

    public void addChild(GenericTreeNode<E> child) {
        child.setParent(this);
        children.add(child);
    }

    public void removeChild(GenericTreeNode<E> child) {
        if (children.contains(child)) {
            // Reassign grandkids to this node
            for (GenericTreeNode<E> grandChild : child.getChildren()) {
                addChild(grandChild);
                grandChild.setParent(this);
            }
            children.remove(child);
        }
    }
}