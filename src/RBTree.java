public class RBTree {
    // root node
    private Node root;

    // a leaf node
    private final Node NULL;

    // number of iterations used to measure
    // operation performance
    private int iter;

    // number of items in tree
    private int count;

    // start time of operations when measuring performance
    private long startTime;

    // prettifiers
    private static final String PIPE = "│  ";
    private static final String SPACE = "   ";
    private static final String CONNECTOR = "├──";
    private static final String PIPE_END = "└──";
    private static final String COLOR_RED = "\033[0;31m";
    private static final String COLOR_RESET = "\033[0m";


    public RBTree() {
        count = 0;
        NULL = new Node();
        NULL.color = 0;
        NULL.left = null;
        NULL.right = null;
        root = NULL;
    }

    /*
     * THIS SECTION CONTAINS MAIN OPERATIONS
     * FIND, INSERT AND DELETE
     * and it's basic implementations
     */
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

    // insert the item to the tree in its appropriate position
    // and fix the tree
    public boolean insertItem(int item) {

        // Ordinary Binary Search Insertion
        Node node = new Node();
        node.parent = null;
        node.value = item;
        node.left = NULL;
        node.right = NULL;
        node.color = 1; // new node must be red

        Node y = null;
        Node x = this.root;

        while (x != NULL) {
            iter++;
            y = x;
            if (node.value < x.value) {
                x = x.left;
            } else if (node.value > x.value) {
                x = x.right;
            } else {
                // item already exist
                return false;
            }
        }

        // y is parent of x
        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.value < y.value) {
            y.left = node;
        } else {
            y.right = node;
        }

        // if new node is a root node, simply return
        if (node.parent == null) {
            node.color = 0;
            count++;
            return true;
        }

        // if it has a grandparent fix the tree else return
        if (node.parent.parent != null) {
            // Fix the tree
            fixInsert(node);
        }
        count++;
        return true;
    }


    // delete the node from the tree
    public boolean deleteItem(int item) {
        // find the node containing key
        Node z = root;
        Node x, y;
        while (z != NULL && z.value != item) {
            iter++;
            if (z.value < item) {
                z = z.right;
            } else {
                z = z.left;
            }
        }
        if (z == NULL) {
            return false;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == NULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == NULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0) {
            fixDelete(x);
        }
        count--;
        return true;
    }


    /**
     * THIS SECTION CONTAINS FIX OPERATIONS
     * POST- OPERATIONS FOR BALANCING THE RED-BLACK TREE
     *
     * <b>fixDelete, fixInsert</b>
     */

    // fix the rb tree modified by the delete operation
    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == 0) {
            iter++;
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.parent.color = 1;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }

                if (s.left.color == 0 && s.right.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.right.color == 0) {
                        // case 3.3
                        s.left.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.parent.right;
                    }

                    // case 3.4
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.right.color = 0;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.parent.color = 1;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }

                if (s.right.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.left.color == 0) {
                        // case 3.3
                        s.right.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.parent.left;
                    }

                    // case 3.4
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.left.color = 0;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }


    // fix the red-black tree after inserting
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == 1) {
            iter++;
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left; // uncle
                if (u.color == 1) {
                    // case 3.1
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        // case 3.2.2
                            /*

                     *
                      \
                       \
                        *
                       /
                      /
                     *

                             */
                        k = k.parent;
                        rightRotate(k);
                    }
                    // case 3.2.1
                      /*
                     *
                      \
                       \
                        *
                         \
                          \
                           *
                 */
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right; // uncle

                if (u.color == 1) {
                    // mirror case 3.1
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        /*
                                   *
                                  /
                                 /
                                *
                                 \
                                  \
                                   *
                        */
                        // mirror case 3.2.2
                        k = k.parent;
                        leftRotate(k);
                    }
                    // mirror case 3.2.1
                       /*
                                  *
                                 /
                                /
                               *
                              /
                             /
                            *
                 */
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }

        root.color = 0;
    }

    /*
     * HELPER FUNCTIONS
     */
    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    // find the node with the minimum key
    private Node minimum(Node node) {
        while (node.left != NULL) {
            node = node.left;
        }
        return node;
    }

    // rotate left at node x
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // rotate right at node x
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != NULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // getter that returns the number of items
    // currently in the tree
    public int size() {
        return count;
    }

    /**
     * ANALYSIS METHODS
     * methods that insert,finds, deletes and also returns
     * the time and number of iterations to perform this operations
     * <b> insert, delete and find </b>
     */
    public SpeedAnalysis insertItemWithAnalysis(int item) {
        iter = 1;
        int itemSize = count;
        startTime = System.nanoTime();
        boolean isInserted = insertItem(item);
        long time = System.nanoTime() - startTime;
        return new SpeedAnalysis(isInserted, item, time, iter, itemSize);
    }

    public SpeedAnalysis deleteItemWithAnalysis(int item) {
        iter = 1;
        int itemSize = count;
        startTime = System.nanoTime();
        boolean deleted = deleteItem(item);
        long time = System.nanoTime() - startTime;
        return new SpeedAnalysis(deleted, item, time, iter, itemSize);
    }

    public SpeedAnalysis findItemWithAnalysis(int item) {
        iter = 1;
        int itemSize = count;
        startTime = System.nanoTime();
        boolean found = findItem(item);
        long time = System.nanoTime() - startTime;
        return new SpeedAnalysis(found, item, time, iter, itemSize);
    }

    /*
     * SECTION for printing data
     */
    @Override
    public String toString() {
        return treeString(root);
    }

    public String treeString(Node root) {
        if (root == NULL) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(root.value).append("\n");

        // children
        String pointerForLeft = (root.right == NULL) ? PIPE_END : CONNECTOR;

        // left
        traverseNodes(sb, "", pointerForLeft, root.left, root.right != NULL);
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
        if (node != NULL) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.color == 1 ? COLOR_RED : "")
                    .append(node.value)
                    .append(node.color == 1 ? COLOR_RESET : "");
            sb.append("\n");
            String paddingForBoth = padding + (hasRightSiblings ? PIPE : SPACE);

            String pointerForLeft = (node.right == NULL) ? PIPE_END : CONNECTOR;

            traverseNodes(sb, paddingForBoth, pointerForLeft, node.left, node.right != NULL);
            traverseNodes(sb, paddingForBoth, PIPE_END, node.right, false);
        }
    }

}