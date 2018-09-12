package problem;

import java.io.IOException;

public class Solve {
    public static void main(String[] args) {
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");
    }





}