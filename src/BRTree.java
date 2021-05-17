/**
 * Created by IntelliJ IDEA
 * Date: 16.05.2021
 * Time: 7:31 AM
 *
 * @author lordvidex
 * Name: Овамойо Олувадамилола Эванс
 * <p>
 * Desc:
 */
public class BRTree {
    // number of items in the tree
    private int count = 0;
    public Node root = Node.TERMINAL;

    // used for counting iterations
    private int iter = 0;
    private long startTime;

    // prettifiers
    private static final String PIPE = "│  ";
    private static final String SPACE = "   ";
    private static final String CONNECTOR = "├──";
    private static final String PIPE_END = "└──";
    private static final String COLOR_RED = "\033[0;31m";
    private static final String COLOR_RESET = "\033[0m";


    public int size() {
        return this.count;
    }

    public SpeedAnalysis deleteItemWithAnalysis(int item) {
        iter = 1;
        int itemSize = count;
        startTime = System.nanoTime();
        boolean isDeleted = deleteItem(item);
        long time = System.nanoTime() - startTime;
        return new SpeedAnalysis(isDeleted, item, time, iter, itemSize);
    }

    public boolean deleteItem(int item) {
        //z is the node to be deleted
        Node x, y, z = root;

        // find node z
        while (z != Node.TERMINAL && z.value != item) {
            iter++;
            if (item < z.value) {
                z = z.left;
            } else {
                z = z.right;
            }
        }
        if (z == Node.TERMINAL) {
            // item was not found
            return false;
        }
        y = z;
        boolean affectingColorIsRed = y.isRed;
        if (z.left == Node.TERMINAL) {
            x = z.right;
            transplant(z,z.right);
        } else if (z.right == Node.TERMINAL) {
            x = z.left;
            transplant(z,z.left);
        } else {
            y = minimumNode(z.right);
            affectingColorIsRed = y.isRed;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                // moving y.right to old position of y
                transplant(y, y.right);
                y.addRightChild(z.right);
            }
            transplant(z, y);
            y.addLeftChild(z.left);
            y.isRed = z.isRed;
        }
        if (!affectingColorIsRed) {
            fixDelete(x);
        }
        count--;
        return true;
    }

//    private void fixDelete(Node x) {
//        while (x != root && !x.isRed) {
//            iter++;
//            Node s = x.getSibling();
//            if (s.isRed) {
//                // case 3.1
//                s.isRed = false;
//                x.parent.isRed = true;
//                if (x.isLeftChild())
//                    leftRotate(x.parent);
//                else
//                    rightRotate(x.parent);
//                s = x.getSibling();
//            }
//
//            if (!s.left.isRed && !s.right.isRed) {
//                // case 3.2
//                s.isRed = true;
//                x = x.parent;
//            } else {
//                Node lineNephew = x.getLineNephew();
//                if (!lineNephew.isRed) {
//                    // case 3.3
//                    lineNephew.getSibling().isRed = false;
//                    s.isRed = true;
//                    if (x.isLeftChild()) {
//                        rightRotate(s);
//                    } else {
//                        leftRotate(s);
//                    }
//                    s = x.getSibling();
//                }
//                // case 3.4
//                s.isRed = x.parent.isRed;
//                x.parent.isRed = false;
//                x.getLineNephew().isRed = false;
//                if (x.isLeftChild()) {
//                    leftRotate(x.parent);
//                } else {
//                    rightRotate(x.parent);
//                }
//                x = root;
//            }
//        }
//        x.isRed = false;
//
//    }
private void fixDelete(Node x) {
    Node s;
    while (x != root && !x.isRed) {
        if (x == x.parent.left) {
            s = x.parent.right;
            if (s.isRed) {
                // case 3.1
                s.isRed = false;
                x.parent.isRed = true;
                leftRotate(x.parent);
                s = x.parent.right;
            }

            if (!s.left.isRed && !s.right.isRed) {
                // case 3.2
                s.isRed = true;
                x = x.parent;
            } else {
                if (!s.right.isRed) {
                    // case 3.3
                    s.left.isRed = false;
                    s.isRed = true;
                    rightRotate(s);
                    s = x.parent.right;
                }

                // case 3.4
                System.out.println(s.right.isRed);
                s.isRed = x.parent.isRed;
                x.parent.isRed = false;
                s.right.isRed = false;
                leftRotate(x.parent);
                x = root;
            }
        } else {
            s = x.parent.left;
            if (s.isRed) {
                // case 3.1
                s.isRed = false;
                x.parent.isRed = true;
                rightRotate(x.parent);
                s = x.parent.left;
            }

            if (!s.right.isRed) {
                // case 3.2
                s.isRed = true;
                x = x.parent;
            } else {
                if (!s.left.isRed) {
                    // case 3.3
                    s.right.isRed = false;
                    s.isRed = true;
                    leftRotate(s);
                    s = x.parent.left;
                }

                // case 3.4
                s.isRed = x.parent.isRed;
                x.parent.isRed = false;
                s.left.isRed = false;
                rightRotate(x.parent);
                x = root;
            }
        }
    }
    x.isRed = false;
}

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    public SpeedAnalysis insertItemWithAnalysis(int item) {
        iter = 1;
        int itemSize = count;
        startTime = System.nanoTime();
        boolean isInserted = insertItem(item);
        long time = System.nanoTime() - startTime;
        return new SpeedAnalysis(isInserted, item, time, iter, itemSize);
    }

    private boolean insertItem(int item) {
        Node node = new Node(item);
        // Case 0
        if (root == Node.TERMINAL) {
            root = node;
        } else {

            // find a nice position for it at the leaves of the tree
            Node x = null;
            Node y = root;
            while (y != Node.TERMINAL) {
                iter++;
                x = y;
                if (node.value < y.value) {
                    y = y.left;
                } else if (node.value > y.value) {
                    y = y.right;
                } else {
                    // item already exists in the tree
                    return false;
                }
            }
            x.addChild(node, node.value > x.value);
            if (node.getGrandParent() != null) {
                fixInsert(node);
            }
        }
        count++;
        return true;
    }

    // Balance the node after insertion
    private void fixInsert(Node node) {
        while (node.getGrandParent() != null && node.parent.isRed) {
            iter++;
            Node uncle = node.getUncle();
            if (uncle != Node.TERMINAL && uncle.isRed) {
                // case 1 - repaint parent, grandparent and uncle
                node.parent.isRed = false;
                uncle.isRed = false;
                node.getGrandParent().isRed = true;
                node = node.getGrandParent();
            } else {
                /*
                     *                 *
                      \               /
                       \             /
                        *           *
                       /             \
                      /               \
                     *                 *
                 */
                if (isAngleRelationship(node)) {
                    // case 2
                    if (isLeftAngleRelationship(node)) {
                        node = node.parent;
                        leftRotate(node);
                    } else {
                        node = node.parent;
                        rightRotate(node);
                    }
                }

                // case 3
                /*
                     *                 *
                      \               /
                       \             /
                        *           *
                         \         /
                          \       /
                           *     *
                 */
                // repaint grand parent and parent
                node.parent.isRed = false;
                node.getGrandParent().isRed = true;
                if (isLeftLineRelationship(node)) {
                    rightRotate(node.getGrandParent());
                } else {
                    leftRotate(node.getGrandParent());
                }
            }
        }

        // case 0
        root.isRed = false;
    }

    public SpeedAnalysis findItemWithAnalysis(int item) {
        iter = 1;
        int itemSize = count;
        startTime = System.nanoTime();
        boolean found = findItem(item);
        long time = System.nanoTime() - startTime;
        return new SpeedAnalysis(found, item, time, iter, itemSize);
    }

    public boolean findItem(int item) {
        Node x = root;
        while (x != Node.TERMINAL) {
            iter++;
            if (item > x.value) {
                x = x.right;
            } else if (item < x.value) {
                x = x.left;
            } else {
                return true;
            }
        }
        return false;
    }

    private Node minimumNode(Node node) {
        while (node.left != null && node.left != Node.TERMINAL) {
            node = node.left;
        }
        return node;
    }

    private boolean isAngleRelationship(Node node) {
        return isLeftAngleRelationship(node)
                || isRightAngleRelationship(node);
    }

    private boolean isRightAngleRelationship(Node node) {
        return node.parent.left == node
                && node.getGrandParent().right == node.parent;
    }

    private boolean isLeftAngleRelationship(Node node) {
        return (node.parent.right == node)
                && (node.getGrandParent().left == node.parent);
    }

    private boolean isLeftLineRelationship(Node node) {
        return node.parent.left == node
                && node.getGrandParent().left == node.parent;
    }

    private boolean isRightLineRelationship(Node node) {
        return node.parent.right == node
                && node.getGrandParent().right == node.parent;
    }

    private void leftRotate(Node node) {
        Node oldRightChild = node.right;
        Node oldParent = node.parent;
        node.addRightChild(node.right.left);
        oldRightChild.addLeftChild(node);
        if (oldParent == null) {
            root = oldRightChild;
            root.parent = null;
        } else {
            oldParent.addChild(oldRightChild, oldRightChild.value > oldParent.value);
        }
    }

    private void rightRotate(Node node) {
        Node oldLeftChild = node.left;
        Node oldParent = node.parent;
        node.addLeftChild(node.left.right);
        oldLeftChild.addRightChild(node);
        if (oldParent == null) {
            root = oldLeftChild;
            root.parent = null;
        } else {
            oldParent.addChild(oldLeftChild, oldLeftChild.value > oldParent.value);
        }

    }

    /*
     * SECTION for printing data
     */
    public String printTree() {
        if (root == Node.TERMINAL) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(root.value).append("\n");

        // children
        String pointerForLeft = (root.right == Node.TERMINAL) ? PIPE_END : CONNECTOR;

        // left
        traverseNodes(sb, "", pointerForLeft, root.left, root.right != Node.TERMINAL);
        // right
        traverseNodes(sb, "", PIPE_END, root.right, false);

        return sb.toString();
    }

    private void traverseNodes(
            StringBuilder sb,
            String padding,
            String pointer,
            Node node,
            boolean hasRightSiblings
    ) {
        if (node != Node.TERMINAL) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.isRed ? COLOR_RED : "")
                    .append(node.value)
                    .append(node.isRed ? COLOR_RESET : "");
            sb.append("\n");
            String paddingForBoth = padding + (hasRightSiblings ? PIPE : SPACE);

            String pointerForLeft = (node.right == Node.TERMINAL) ? PIPE_END : CONNECTOR;

            traverseNodes(sb, paddingForBoth, pointerForLeft, node.left, node.right != Node.TERMINAL);
            traverseNodes(sb, paddingForBoth, PIPE_END, node.right, false);
        }
    }
}
