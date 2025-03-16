/**
 * A convenient class that stores a pair of integers.
 * DO NOT MODIFY THIS CLASS.
 */

class IntPair {
    // Make the fields final to ensure they cannot be changed after initialization
    public final int first;
    public final int second;

    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "(" + first + "," + second + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        IntPair other = (IntPair) obj;
        return first == other.first && second == other.second;
    }

    @Override
    public int hashCode() {
        return 31 * first + second;
    }
}

class Node {
    public Node left;
    public Node right;
    public int id;
    public int h;
    public int value;
    public int depth;

    public Node(int height, int value, int id) {
        this.h = height;
        this.value = value;
        this.id = id;
        this.depth = 0;
    }
}

class Valley {
    public int depth;
    public Valley left;
    public Valley right;
    public int value;
    public int h;
    public int id;

    public Valley(Node node) {
        this.depth = node.depth;
        this.value = node.value;
        this.id = node.id;
        this.h = node.h;
        this.left = null;
        this.right = null;
    }
}

/**
 * TreasureValleyExplorer class operates on a landscape of Numerica,
 * selectively modifying the most and least valuable valleys of a specified
 * depth.
 * 
 * DO NOT MODIFY THE SIGNATURE OF THE METHODS PROVIDED IN THIS CLASS.
 * You are encouraged to add methods and variables in the class as needed.
 *
 * @author Brandon Newman
 */
public class TreasureValleyExplorer2 {

    // Create instance variables here.
    private Node landscape = null;
    private Valley valleys = null; // Valleys of the same depth in a sorted structure
    private int id = 0;

    /**
     * Constructor to initialize the TreasureValleyExplorer with the given heights
     * and values
     * of points in Numerica.
     *
     * @param heights An array of distinct integers representing the heights of
     *                points in the landscape.
     * @param values  An array of distinct integers representing the treasure value
     *                of points in the landscape.
     */
    public TreasureValleyExplorer2(int[] heights, int[] values) {
        // TODO: Implement the constructor.

        // Get depth: if descending, depth = depth(prev) + 1, else 0
        // Store landscape:
        // Store valley: BST w/ value priority

        // isValley if prev and next > curr
        // Depth = curr < prev ? prev + 1 else 0
        // value = value

        Node prev = null;

        for (int i = 0; i < heights.length; i++) {
            Node curr = new Node(heights[i], values[i], id++);

            if (prev == null) {
                curr.left = null;
                curr.depth = 0;
                landscape = curr;
            } else {
                curr.left = prev;
                prev.right = curr;
                curr.depth = curr.value < prev.value ? prev.depth + 1 : 0;
                if (curr.h < prev.h && curr.h < heights[i + 1]) {
                    Valley curr_val = new Valley(curr);
                    if (valleys == null) {
                        valleys = curr_val;
                    } else {
                        insertValley(curr_val);
                    }
                }
            }
        }

    }

    public void insertValley(Valley valley) {
        Valley curr = valleys;

        while (curr != null) {
            if (curr.depth < valley.depth) {
                if (curr.right == null) {
                    curr.right = valley;
                    break;
                }
                curr = curr.right;
            } else if (curr.depth > valley.depth) {
                if (curr.left == null) {
                    curr.left = valley;
                    break;
                }
                curr = curr.left;
            } else {
                if (curr.value < valley.value) {
                    if (curr.right == null || curr.right.depth != valley.depth) {
                        valley.right = curr.right;
                        curr.right = valley;
                        break;
                    }
                    curr = curr.right;
                } else if (curr.value > valley.value) {
                    if (curr.left == null || curr.left.depth != valley.depth) {
                        valley.left = curr.left;
                        curr.left = valley;
                        break;
                    }
                    curr = curr.left;
                }
            }
        }
    }

    public void insertChildren(Valley valley) {
        if (valley == null) {
            return;
        }

        insertChildren(valley.left);
        insertValley(valley);
        insertChildren(valley.right);
    }

    /**
     * Checks if the entire landscape is excavated (i.e., there are no points
     * left).
     *
     * @return true if the landscape is empty, false otherwise.
     */
    public boolean isEmpty() {
        // TODO: Implement the isEmpty method.
        return landscape == null;
    }

    /**
     * A method to insert a new landform prior to the most valuable valley of the
     * specified depth
     *
     * @param height The height of the new landform
     * @param value  The treasure value of the new landform
     * @param depth  The depth of the valley we wish to insert at
     *
     * @return true if the insertion is successful, false otherwise
     */
    public boolean insertAtMostValuableValley(int height, int value, int depth) {
        // TODO: Implement the insertAtMostValuableValley method
        Valley valley = valleys;
        Valley prev = null;
        Node curr = landscape;
        int idx = -1;

        // FIND VALLEY
        while (valley != null && valley.depth != depth) {
            prev = valley;
            valley = valley.depth > depth ? valley.left : valley.right;
        }

        if (valley == null) {
            return false;
        }

        while (valley.right.depth == valley.depth) {
            prev = valley;
            valley = valley.right;
        }

        // MAKE NEW NODE TO INSERT
        Node newNode = new Node(height, value, id++);
        idx = valley.id;

        // UPDATE OLD VALLEY IF NOT VALLEY ANYMORE
        if (height < valley.h) {
            if (prev.right == valley) {
                prev.right = null;
            } else {
                prev.left = null;
            }
            insertChildren(valley);
        }

        // FIND IN LANDSCAPE
        while (curr.id != idx) {
            curr = curr.right;
        }

        // UPDATE NEW NODE
        newNode.left = curr.left;
        newNode.right = curr;
        if (curr.left != null) {
            curr.left.right = newNode;
            newNode.depth = newNode.value < newNode.left.value ? newNode.left.depth + 1 : 0;
            if (newNode.h < curr.h && newNode.h < curr.left.h) {
                Valley newVal = new Valley(newNode);
                if (valleys == null) {
                    valleys = newVal;
                } else {
                    insertValley(newVal);
                }
            } else {
                if (curr.left.depth != 0 && curr.left.h < newNode.h) {
                    Valley newVal = new Valley(curr.left);
                    if (valleys == null) {
                        valleys = newVal;
                    } else {
                        insertValley(newVal);
                    }
                }
            }
        } else {
            if (newNode.h < curr.h) {
                Valley newVal = new Valley(newNode);
                if (valleys == null) {
                    valleys = newVal;
                } else {
                    insertValley(newVal);
                }
            }
        }
        curr.left = newNode;

        return true;
    }

    /**
     * A method to insert a new landform prior to the least valuable valley of the
     * specified depth
     *
     * @param height The height of the new landform
     * @param value  The treasure value of the new landform
     * @param depth  The depth of the valley we wish to insert at
     *
     * @return true if the insertion is successful, false otherwise
     */
    public boolean insertAtLeastValuableValley(int height, int value, int depth) {
        // TODO: Implement the insertAtLeastValuableValley method
        Valley valley = valleys;
        Valley prev = null;
        Node curr = landscape;
        int idx = -1;

        // FIND VALLEY
        while (valley != null && valley.depth != depth) {
            prev = valley;
            valley = valley.depth > depth ? valley.left : valley.right;
        }

        if (valley == null) {
            return false;
        }

        while (valley.left.depth == valley.depth) {
            prev = valley;
            valley = valley.left;
        }

        // MAKE NEW NODE TO INSERT
        Node newNode = new Node(height, value, id++);
        idx = valley.id;

        // UPDATE OLD VALLEY IF NOT VALLEY ANYMORE
        if (height < valley.h) {
            if (prev.right == valley) {
                prev.right = null;
            } else {
                prev.left = null;
            }
            insertChildren(valley);
        }

        // FIND IN LANDSCAPE
        while (curr.id != idx) {
            curr = curr.right;
        }

        // UPDATE NEW NODE
        newNode.left = curr.left;
        newNode.right = curr;
        if (curr.left != null) {
            curr.left.right = newNode;
            newNode.depth = newNode.value < newNode.left.value ? newNode.left.depth + 1 : 0;
            if (newNode.h < curr.h && newNode.h < curr.left.h) {
                Valley newVal = new Valley(newNode);
                if (valleys == null) {
                    valleys = newVal;
                } else {
                    insertValley(newVal);
                }
            } else {
                if (curr.left.depth != 0 && curr.left.h < newNode.h) {
                    Valley newVal = new Valley(curr.left);
                    if (valleys == null) {
                        valleys = newVal;
                    } else {
                        insertValley(newVal);
                    }
                }
            }
        } else {
            if (newNode.h < curr.h) {
                Valley newVal = new Valley(newNode);
                if (valleys == null) {
                    valleys = newVal;
                } else {
                    insertValley(newVal);
                }
            }
        }
        curr.left = newNode;

        return true;
    }

    /**
     * A method to remove the most valuable valley of the specified depth
     *
     * @param depth The depth of the valley we wish to remove
     *
     * @return An IntPair where the first field is the height and the second field
     *         is the treasure value of the removed valley
     * @return null if no valleys of the specified depth exist
     */
    public IntPair removeMostValuableValley(int depth) {
        // TODO: Implement the removeMostValuableValley method
        Valley valley = valleys;
        Valley prev = null;
        Node curr = landscape;
        int idx = -1;

        // FIND VALLEY
        while (valley != null && valley.depth != depth) {
            prev = valley;
            valley = valley.depth > depth ? valley.left : valley.right;
        }

        if (valley == null) {
            return null;
        }

        while (valley.right.depth == valley.depth) {
            prev = valley;
            valley = valley.right;
        }

        if (prev.right == valley) {
            prev.right = null;
        } else {
            prev.left = null;
        }
        insertChildren(valley);

        IntPair fin = new IntPair(valley.h, valley.value);
        idx = valley.id;

        // FIND IN LANDSCAPE
        while (curr.id != idx) {
            curr = curr.right;
        }

        if (curr.left != null) {
            curr.left.right = curr.right;
        }
        if (curr.right != null) {
            curr.right.left = curr.left;
        }

        return fin;
    }

    /**
     * A method to remove the least valuable valley of the specified depth
     *
     * @param depth The depth of the valley we wish to remove
     *
     * @return An IntPair where the first field is the height and the second field
     *         is the treasure value of the removed valley
     * @return null if no valleys of the specified depth exist
     */
    public IntPair removeLeastValuableValley(int depth) {
        // TODO: Implement the removeLeastValuableValley method
        // Need: Search for that depth, find least valuable node, remove
        // Remove: Update valley ds, update landscape ds.
        Valley valley = valleys;
        Valley prev = null;
        Node curr = landscape;
        int idx = -1;

        // FIND VALLEY
        while (valley != null && valley.depth != depth) {
            prev = valley;
            valley = valley.depth > depth ? valley.left : valley.right;
        }

        if (valley == null) {
            return null;
        }

        while (valley.left.depth == valley.depth) {
            prev = valley;
            valley = valley.left;
        }

        if (prev.right == valley) {
            prev.right = null;
        } else {
            prev.left = null;
        }
        insertChildren(valley);

        IntPair fin = new IntPair(valley.h, valley.value);
        idx = valley.id;

        // FIND IN LANDSCAPE
        while (curr.id != idx) {
            curr = curr.right;
        }

        if (curr.left != null) {
            curr.left.right = curr.right;
        }
        if (curr.right != null) {
            curr.right.left = curr.left;
        }

        return fin;
    }

    /**
     * A method to get the treasure value of the most valuable valley of the
     * specified depth
     *
     * @param depth The depth of the valley we wish to find the treasure value of
     *
     * @return An IntPair where the first field is the height and the second field
     *         is the treasure value of the found valley
     * @return null if no valleys of the specified depth exist
     */
    public IntPair getMostValuableValley(int depth) {
        // TODO: Implement the getMostValuableValleyValue method
        // Need: search for valleys of depth, find most valuable node, return height and
        // value
        Valley valley = valleys;

        while (valley != null && valley.depth != depth) {
            valley = valley.depth > depth ? valley.left : valley.right;
        }

        if (valley == null) {
            return null;
        }

        while (valley.right.depth == valley.depth) {
            valley = valley.right;
        }

        return new IntPair(valley.h, valley.value);
    }

    /**
     * A method to get the treasure value of the least valuable valley of the
     * specified depth
     *
     * @param depth The depth of the valley we wish to find the treasure value of
     *
     * @return An IntPair where the first field is the height and the second field
     *         is the treasure value of the found valley
     * @return null if no valleys of the specified depth exist
     */
    public IntPair getLeastValuableValley(int depth) {
        // TODO: Implement the getLeastValuableValleyValue method

        // Need: efficiently find depth, efficiently find value at that depth
        Valley valley = valleys;

        while (valley != null && valley.depth != depth) {
            valley = valley.depth > depth ? valley.left : valley.right;
        }

        if (valley == null) {
            return null;
        }

        while (valley.left.depth == valley.depth) {
            valley = valley.left;
        }

        return new IntPair(valley.h, valley.value);
    }

    /**
     * A method to get the number of valleys of a given depth
     *
     * @param depth The depth that we want to count valleys for
     *
     * @return The number of valleys of the specified depth
     */
    public int getValleyCount(int depth) {
        // TODO: Implement the getValleyCount method

        // Need: efficiently search for depth.
        return countHelper(valleys, depth);
    }

    public int countHelper(Valley root, int depth) {
        if (root == null) {
            return 0;
        }
        int count = 0;
        if (root.depth > depth) {
            count += countHelper(root.left, depth);
        } else if (root.depth < depth) {
            count += countHelper(root.right, depth);
        } else {
            count += 1 + countHelper(root.left, depth) + countHelper(root.right, depth);
        }
        return count;
    }
}