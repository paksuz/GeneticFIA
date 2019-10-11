import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetCoveringLoadData {

    private static List<SetCoveringInstanceFile> SCPs = null;
    private static SetCoveringInstanceFile SCP = null;

    public static List<SetCoveringInstanceFile> start() {
        SCPs = new ArrayList<>();

        for (String file : SCP_FILES) {
            fs = true;
            ss = true;
            rs = 0;
            rc = 0;
            cc = 0;
            cr = 0;
            SCP = SetCoveringInstanceFile.getInstance();
            read(file);
            SCPs.add(SCP);
        }

        return SCPs;
    }

    private static void load(String line) {
        String[] values = line.trim().split(" ");
        if (fs) {
            SCP.setRows(Integer.parseInt(values[0]));
            SCP.setCols(Integer.parseInt(values[1]));
            SCP.setCosts(new int[SCP.getCols()]);
            SCP.setConstraints(new int[SCP.getRows()][SCP.getCols()]);
            fs = false;
        } else {
            if (ss) {
                for (String value : values) {
                    SCP.getCosts()[cc++] = Integer.parseInt(value);
                }
                ss = cc < SCP.getCols();
            } else {
                if (rc == 0) {
                    rc = Integer.parseInt(values[0]);
                } else {
                    for (String value : values) {
                        SCP.getConstraints()[rs][Integer.parseInt(value) - 1] = 1;
                        cr++;
                    }
                    if (rc <= cr) {
                        rs++;
                        rc = 0;
                        cr = 0;
                    }
                }
            }
        }
    }

    private static void read(String inputDataFile) {
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader(PATH + inputDataFile));
            while ((line = br.readLine()) != null) {
                load(line);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    private static boolean fs = true, ss = true;
    private static int rs, rc, cc, cr;
    private static final Logger LOGGER = Logger.getLogger(SetCoveringLoadData.class.getName());

    private static final String PATH = "resources/input/";

    private static final String[] SCP_FILES = {
       /* "scp41.txt",
        "scp42.txt",
        "scp43.txt",
        "scp44.txt",
        "scp45.txt",
        "scp46.txt",
        "scp47.txt",
        "scp48.txt",
        "scp49.txt",
        "scp410.txt"/*,
        "scp51.txt",
        "scp52.txt",
        "scp53.txt",
        "scp54.txt",
        "scp55.txt",
        "scp56.txt",
        "scp57.txt",
        "scp58.txt",
        "scp59.txt",
        */"scp510.txt",/*
        "scp61.txt",
        "scp62.txt",
        "scp63.txt",
        "scp64.txt",
        "scp65.txt",
        "scpa1.txt",
        "scpa2.txt",
        "scpa3.txt",
        "scpa4.txt",
        "scpa5.txt",
        "scpb1.txt",
        "scpb2.txt",
        "scpb3.txt",
        "scpb4.txt",
        "scpb5.txt",
        "scpc1.txt",
        "scpc2.txt",
        "scpc3.txt",
        "scpc4.txt",
        "scpc5.txt",
        "scpd1.txt",
        "scpd2.txt",
        "scpd3.txt",
        "scpd4.txt",
        "scpd5.txt",
        "scpnre1.txt",
        "scpnre2.txt",
        "scpnre3.txt",
        "scpnre4.txt",
        "scpnre5.txt",
        "scpnrf1.txt",
        "scpnrf2.txt",
        "scpnrf3.txt",
        "scpnrf4.txt",
        "scpnrf5.txt",
        "scpnrg1.txt",
        "scpnrg2.txt",
        "scpnrg3.txt",
        "scpnrg4.txt",
        "scpnrg5.txt",
        "scpnrh1.txt",
        "scpnrh2.txt",
        "scpnrh3.txt",
        "scpnrh4.txt",
        "scpnrh5.txt"*/
    };
}
