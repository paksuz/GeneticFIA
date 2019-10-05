import java.util.List;

public class MainGenetic {

    public static void main(String[] args) {

        try {
            List<SetCoveringInstanceFile> scps = SetCoveringLoadData.start();

            scps.stream().forEach((scp) -> {
                SetCoveringInstanceFile.getInstance().setRows(scp.getRows());
                SetCoveringInstanceFile.getInstance().setCols(scp.getCols());
                SetCoveringInstanceFile.getInstance().setCosts(scp.getCosts());
                SetCoveringInstanceFile.getInstance().setConstraints(scp.getConstraints());
                
                (new Genetic()).execute();
            });
            
        } catch (Exception e) {
            System.err.println(String.format("%s \n%s", e.getMessage(), e.getCause()));
        }
    }
}