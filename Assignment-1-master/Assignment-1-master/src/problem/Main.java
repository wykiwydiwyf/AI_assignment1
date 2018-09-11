package problem;

import javax.print.attribute.standard.MediaSize;
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

    /** 距离arm最近的box **/
    public Box closestBox;
    public int NumclosestBox;

    //更新最近的Box
    public void findClosestBox(List<Box> Box,RobotConfig config) {
        double dist;
        double closestDist=1;
        for(int i=0;i<Box.size();i++){
            Box tempBox;
            tempBox=Box.get(i);
            //manhattan distance
            dist=Math.abs(config.getPos().getX()-tempBox.getPos().getX())+Math.abs(config.getPos().getY()-tempBox.getPos().getY());
            if(dist<closestDist){
                closestDist=dist;
                closestBox=tempBox;
                NumclosestBox = i ;
            }
        }
    }

    public void boxNavi(List<Box>movingBoxes, List<StaticObstacle>staticObstacles,  List<Point2D>movingBoxEndPositions, List<Box>movingObstacles){
        Boolean coincide = false;

        while(movingBoxes.size() != 0){

            while(closestBox.getPos().getX()!= movingBoxEndPositions.get(NumclosestBox).getX()&&closestBox.getPos().getY() != movingBoxEndPositions.get(NumclosestBox).getY()) {

                for (StaticObstacle obs:staticObstacles) {
                    for (Box mobs:movingObstacles) {

                        double boxMaxX = closestBox.getPos().getX()+0.05;
                        double boxMinX = closestBox.getPos().getX()-0.05;
                        double boxMaxY = closestBox.getPos().getY()+0.05;
                        double boxMinY = closestBox.getPos().getY()-0.05;

                        double obsMaxX = obs.getRect().getMaxX();
                        double obsMinX = obs.getRect().getMinX();
                        double obsMaxY = obs.getRect().getMaxY();
                        double obsMinY = obs.getRect().getMinY();

                        double mobsMaxX = mobs.getRect().getMaxX();
                        double mobsMinX = mobs.getRect().getMinX();
                        double mobsMaxY = mobs.getRect().getMaxY();
                        double mobsMinY = mobs.getRect().getMinY();
                        //Test to Right
                        if (((boxMaxX > obsMinX && boxMaxY >obsMinY && boxMaxY < obsMaxY)||(boxMaxX > obsMinX &&  boxMinY >obsMinY && boxMinY < obsMaxY))
                        ||((boxMaxX > mobsMinX && boxMaxY >mobsMinY && boxMaxY < mobsMaxY)||(boxMaxX > mobsMinX &&  boxMinY >mobsMinY && boxMinY < mobsMaxY))){
                            coincide = true; continue;
                        }
                        //Test to Up
                        else if (((boxMaxY > obsMinY && boxMaxX >obsMinX && boxMaxX < obsMaxX)||(boxMaxY > obsMinY &&  boxMinX >obsMinX && boxMinX < obsMaxX))
                                ||((boxMaxY > mobsMinY && boxMaxX >mobsMinX && boxMaxX < mobsMaxX)||(boxMaxY > mobsMinY &&  boxMinX >mobsMinX && boxMinX < mobsMaxX))){
                            coincide = true; continue;
                        }
                        //Test to Left
                        else if (((boxMinX < obsMaxX && boxMaxY >obsMinY && boxMaxY < obsMaxY)||(boxMinX < obsMaxX &&  boxMinY >obsMinY && boxMinY < obsMaxY))
                                ||((boxMinX < obsMaxX && boxMaxY >mobsMinY && boxMaxY < mobsMaxY)||(boxMinX < obsMaxX &&  boxMinY >mobsMinY && boxMinY < mobsMaxY))){
                            coincide = true; continue;
                        }
                        //Test to Down
                        else if (((boxMinY < obsMaxY && boxMaxX >obsMinX && boxMaxX < obsMaxX)||(boxMinY < obsMaxY &&  boxMinX >obsMinX && boxMinX < obsMaxX))
                                ||((boxMinY < obsMaxY && boxMaxX >mobsMinX && boxMaxX < mobsMaxX)||(boxMinY < obsMaxY &&  boxMinX >mobsMinX && boxMinX < mobsMaxX))){
                            coincide = true; continue;
                        }
                        }
                    }
                }
            }
        }
    }

}

