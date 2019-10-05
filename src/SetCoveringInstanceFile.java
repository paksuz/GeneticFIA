import java.util.Random;

public class SetCoveringInstanceFile {

    private static SetCoveringInstanceFile INSTANCE;

    protected final long seed = System.currentTimeMillis();
    
    protected final Random rnd = new Random(seed);

    private int rows;
    private int cols;
    private int[] costs;
    private int[][] constraints;

    protected static SetCoveringInstanceFile getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    private static void createInstance() {
        synchronized (SetCoveringInstanceFile.class) {
            if (INSTANCE == null) {
                INSTANCE = new SetCoveringInstanceFile();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int[] getCosts() {
        return costs;
    }

    public void setCosts(int[] costs) {
        this.costs = costs;
    }

    public int[][] getConstraints() {
        return constraints;
    }

    public void setConstraints(int[][] constraints) {
        this.constraints = constraints;
    }
}
