/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package create.code.generator;

/**
 *
 * @author Henry
 */
public class NumOrLabel {

    boolean num;
    int number;
    String label;

    @Override
    public String toString() {
        if (num) {
            return number + ""; //To change body of generated methods, choose Tools | Templates.
        } else {
            return "Non-evalulated label";
        }
    }

    public NumOrLabel(Integer num) {
        number = num;
        this.num = true;
    }

    public NumOrLabel(String label) {
        this.label = label;
        this.num = false;
    }
}
