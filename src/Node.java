import java.util.Optional;

/**
 * Created by IntelliJ IDEA
 * Date: 16.05.2021
 * Time: 7:13 AM
 *
 * @author lordvidex
 * Name: Овамойо Олувадамилола Эванс
 * <p>
 * Desc:
 */
public class Node {
    public int value;
    public Node left;
    public Node right;
    public Node parent;
    // 1 . Red, 0 . Black
    public int color;

    public Node(int value) {
        this(value, Node.TERMINAL, Node.TERMINAL, 1);
    }
    public Node(){}

    public Node(int value, Node left, Node right, int color) {
        this.value = value;
        this.left = left;
        this.right = right;
        this.color = color;
    }

    public static final Node TERMINAL = new Node(0, null, null, 0);

    public Node getGrandParent() {
        return Optional.of(this)
                .map(node -> node.parent)
                .map(node -> node.parent)
                .orElse(null);
    }

    public Node getUncle() {
        // if the node does not have parent or grandparents, then no uncle
        if (this.parent == null || this.getGrandParent() == null) {
            return null;
        }

        // uncle is my grand father's other child :))
        if (this.getGrandParent().right == this.parent) {
            return this.getGrandParent().left;
        } else {
            return this.getGrandParent().right;
        }
    }

    public Node getSibling() {
        if (parent == null) {
            return null;
        } else if (isLeftChild()) {
            return parent.right;
        } else {
            return parent.left;
        }
    }

    public Node getLineNephew() {
        return isLeftChild() ? getSibling().right : getSibling().left;
    }

    public boolean isLeftChild() {
        assert parent != null;
        return parent.left == this;
    }

    public void addRightChild(Node child) {
        addChild(child, true);
    }

    public void addLeftChild(Node child) {
        addChild(child, false);
    }

    /**
     * this function must be called to add attach a child node to it's parent
     * so that the parent can also be recognized from the child, it also removes
     * the old child from the position specified and attaches the new child
     *
     * @param child node to be added
     */
    public void addChild(Node child, boolean childToRight) {
        if (childToRight) {
            this.right = child;
        } else {
            this.left = child;
        }
        if (child != null) child.parent = this;

    }
}
