/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package create.code.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Henry
 */
public class CreateCodeGenerator {

    /**
     * @param args the command line arguments
     */
    public static final Map<String, Integer> BYTE_CODES;
    public static final Map<String, Integer> VAL_CODES;

    static {
        BYTE_CODES = new HashMap<String, Integer>();
        BYTE_CODES.put("STR", 128);
        BYTE_CODES.put("FUL", 132);
        BYTE_CODES.put("DRI", 137);
        BYTE_CODES.put("DRD", 145);
        BYTE_CODES.put("SCR", 152);
        BYTE_CODES.put("PSC", 153);

        BYTE_CODES.put("WAT", 155);
        BYTE_CODES.put("WAD", 156);
        BYTE_CODES.put("WAA", 157);
        BYTE_CODES.put("WAE", 158);

        VAL_CODES = new HashMap<String, Integer>();
        VAL_CODES.put("_NUL", 0);
        VAL_CODES.put("_MX1", 1);
        VAL_CODES.put("_MX2", 244);
        VAL_CODES.put("_WHD", 1);
        VAL_CODES.put("_FWD", 2);
        VAL_CODES.put("_LWD", 3);
        VAL_CODES.put("_RWD", 4);
        VAL_CODES.put("_BMP", 5);
        VAL_CODES.put("_LBP", 6);
        VAL_CODES.put("_RBP", 7);

        VAL_CODES.put("_DI0", 18);
        VAL_CODES.put("_DI1", 19);
        VAL_CODES.put("_DI2", 20);
        VAL_CODES.put("_DI3", 21);

    }

    public static Map<String, Integer> labelDict = new HashMap<String, Integer>();
    public static Map<String, Integer> tmpLabelDict = new HashMap<String, Integer>();

    public static void main(String[] args) throws IOException {
        String[] casm = new String(Files.readAllBytes(Paths.get(args[0]))).replace("\r", "").split("\n");
        List<NumOrLabel> result = new ArrayList<NumOrLabel>();
        int lineNum = 0;
        for (int i = 0; i < casm.length; i++) {
            lineNum++;
            if (casm[i].startsWith("!")) {

                String label = casm[i].substring(1, casm[i].length());
                if (tmpLabelDict.containsKey(label)) {
                    labelDict.put(label, lineNum - tmpLabelDict.get(label));
                    tmpLabelDict.remove(label);
                } else {
                    tmpLabelDict.put(label, lineNum);
                }
                lineNum--;
                continue;
            }
            String[] cmdParts = casm[i].split("#")[0].trim().split(" ");
            if (cmdParts.length == 0) {
                lineNum--;
                continue;
            }
            if (cmdParts[0].length() == 0) {
                lineNum--;
                continue;
            }
            if (BYTE_CODES.containsKey(cmdParts[0].toUpperCase())) {
                result.add(new NumOrLabel(BYTE_CODES.get(cmdParts[0].toUpperCase())));
            } else {
                result.add(parseNum(cmdParts[0]));
            }

            for (int j = 1; j < cmdParts.length; j++) {
                result.add(parseNum(cmdParts[j]));
            }

        }

        System.out.println("int length = " + result.size() + ";");
        System.out.print("char byte-codes[" + result.size() + "] = {");
        for (int i = 0; i < result.size() - 1; i++) {
            if (result.get(i).num == false) {
                System.out.print(labelDict.get(result.get(i).label) + ", ");
            } else {
                System.out.print(result.get(i) + ", ");
            }
        }
        System.out.print(result.get(result.size() - 1) + "};\n");
    }

    public static NumOrLabel parseNum(String num) {
        if (VAL_CODES.containsKey(num.toUpperCase())) {
            return new NumOrLabel(VAL_CODES.get(num.toUpperCase()));
        }
        int numVal;
        try {
            numVal = Integer.parseInt(num);
            return new NumOrLabel(numVal);
        } catch (Exception ex) {
            return new NumOrLabel(num);
        }
    }

}
