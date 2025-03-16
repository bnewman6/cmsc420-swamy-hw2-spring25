import java.util.*;

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
public class TreasureValleyExplorer {

    // Create instance variables here.
    private List<Integer> heights;
    private List<Integer> values;
    private Map<Integer, TreeSet<IntPair>> valleysByDepth; // depth → sorted (value, index)
    private Map<Integer, Integer> depths; // index → depth

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
    public TreasureValleyExplorer(int[] heights, int[] values) {
        // TODO: Implement the constructor.
        this.heights = new ArrayList<>();
        this.values = new ArrayList<>();
        this.valleysByDepth = new HashMap<>();
        this.depths = new HashMap<>();

        for (int i = 0; i < heights.length; i++) {
            this.heights.add(heights[i]);
            this.values.add(values[i]);
        }

        computeDepths();
    }

    private void computeDepths() {
        depths.clear();
        valleysByDepth.clear();

        if (heights.isEmpty())
            return;

        int depth = 0;
        for (int i = 0; i < heights.size(); i++) {
            if ((i == 0 || heights.get(i) > heights.get(i - 1)) &&
                    (i == heights.size() - 1 || heights.get(i) > heights.get(i + 1))) {
                depth = 0;
            } else if (i > 0 && heights.get(i) < heights.get(i - 1)) {
                depth = depths.getOrDefault(i - 1, 0) + 1;
            } else {
                depth = 0;
            }

            depths.put(i, depth);
            if (isValley(i)) {
                valleysByDepth.putIfAbsent(depth, new TreeSet<>(Comparator.comparingInt(a -> a.second)));
                valleysByDepth.get(depth).add(new IntPair(heights.get(i), values.get(i)));
            }
        }
    }

    private boolean isValley(int i) {
        return (i == 0 || heights.get(i) < heights.get(i - 1))
                && (i == heights.size() - 1 || heights.get(i) < heights.get(i + 1));
    }

    /**
     * Checks if the entire landscape is excavated (i.e., there are no points
     * left).
     *
     * @return true if the landscape is empty, false otherwise.
     */
    public boolean isEmpty() {
        // TODO: Implement the isEmpty method.
        return heights.isEmpty();
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
        if (!valleysByDepth.containsKey(depth) || valleysByDepth.get(depth).isEmpty()) {
            return false;
        }

        int insertIdx = heights.indexOf(valleysByDepth.get(depth).last().first);
        heights.add(insertIdx, height);
        values.add(insertIdx, value);
        computeDepths();
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
        if (!valleysByDepth.containsKey(depth) || valleysByDepth.get(depth).isEmpty()) {
            return false;
        }

        int insertIdx = heights.indexOf(valleysByDepth.get(depth).first().first);
        heights.add(insertIdx, height);
        values.add(insertIdx, value);
        computeDepths();
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
        if (!valleysByDepth.containsKey(depth) || valleysByDepth.get(depth).isEmpty()) {
            return null;
        }

        IntPair mostValuable = valleysByDepth.get(depth).pollLast();
        heights.remove((Integer) mostValuable.first);
        values.remove((Integer) mostValuable.second);
        computeDepths();
        return mostValuable;
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
        if (!valleysByDepth.containsKey(depth) || valleysByDepth.get(depth).isEmpty()) {
            return null;
        }

        IntPair leastValuable = valleysByDepth.get(depth).pollFirst();
        heights.remove((Integer) leastValuable.first);
        values.remove((Integer) leastValuable.second);
        computeDepths();
        return leastValuable;
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
        if (!valleysByDepth.containsKey(depth) || valleysByDepth.get(depth).isEmpty()) {
            return null;
        }
        return valleysByDepth.get(depth).last();
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
        if (!valleysByDepth.containsKey(depth) || valleysByDepth.get(depth).isEmpty()) {
            return null;
        }
        return valleysByDepth.get(depth).first();
    }

    /**
     * A method to get the number of valleys of a given depth
     *
     * @param depth The depth that we want to count valleys for
     *
     * @return The number of valleys of the specified depth
     */
    public int getValleyCount(int depth) {
        return valleysByDepth.getOrDefault(depth, new TreeSet<>()).size();
    }
}