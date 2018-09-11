package problem;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //output file



        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
            //ps.loadSolution("output1.txt");
            /** The number of each type of obstacle **/
            int numMovingBoxes;
            int numMovingObstacles;
            int numStaticObstacles;
            double robotWidth = ps.getRobotWidth();

            List<StaticObstacle>staticObstacles = ps.getStaticObstacles();
            List<Box>movingBoxes = ps.getMovingBoxes();
            List<Box>movingObstacles = ps.getMovingObstacles();
            List<RobotConfig>robotPath = ps.getRobotPath();
            List<List<Box>>movingBoxPath = ps.getMovingBoxPath();
            List<List<Box>>movingObstaclePath = ps.getMovingObstaclePath();
            List<Point2D>movingBoxEndPositions = ps.getMovingBoxEndPositions();

        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");

    }

    public void findClosestBox(List<Box>movingBoxes) {


    }
}

