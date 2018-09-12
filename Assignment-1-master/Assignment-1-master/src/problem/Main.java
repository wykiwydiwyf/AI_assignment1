package problem;

import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Main {

    static List<StaticObstacle>staticObstacles;

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

            staticObstacles = ps.getStaticObstacles();
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

    public List<Box> unmovedBox;
    public List<Point2D>visited;
    public Queue<MoveState> RobotQueue;

    public void BoxBFS(MoveState State,List<Box>movingBoxes, List<StaticObstacle>staticObstacles,  List<Point2D>movingBoxEndPositions, List<Box>movingObstacles){
        Boolean coincide = false;
        double width = closestBox.getWidth();
        double boxMaxX = closestBox.getPos().getX()+width;
        double boxMinX = closestBox.getPos().getX();
        double boxMaxY = closestBox.getPos().getY()+width;
        double boxMinY = closestBox.getPos().getY();
        while(closestBox.getPos().getX()!= movingBoxEndPositions.get(NumclosestBox).getX()&&closestBox.getPos().getY() != movingBoxEndPositions.get(NumclosestBox).getY()) {

            for (Box box:unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();

                //Test to Right
                if (((boxMaxX + 0.001 == obsMinX && boxMaxY > obsMinY && boxMaxY < obsMaxY)
                        || (boxMaxX + 0.001 == obsMinX && boxMinY > obsMinY && boxMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box:unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Up
                if (((boxMaxY + 0.001 == obsMinY && boxMaxX > obsMinX && boxMaxX < obsMaxX)
                        || (boxMaxY + 0.001 == obsMinY && boxMinX > obsMinX && boxMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box:unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Left
                if (((boxMinX - 0.001 == obsMaxX && boxMaxY > obsMinY && boxMaxY < obsMaxY)
                        || (boxMinX - 0.001 == obsMaxX && boxMinY > obsMinY && boxMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box:unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Down
                if (((boxMinY - 0.001 == obsMaxY && boxMaxX > obsMinX && boxMaxX < obsMaxX)
                        || (boxMinY - 0.001 == obsMaxY && boxMinX > obsMinX && boxMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }
            }


            for (StaticObstacle obs:staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();

                //Test to Right
                if (((boxMaxX + 0.001 == obsMinX && boxMaxY > obsMinY && boxMaxY < obsMaxY) 
                        || (boxMaxX + 0.001 == obsMinX && boxMinY > obsMinY && boxMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs:staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Up
                if (((boxMaxY +0.001 == obsMinY && boxMaxX >obsMinX && boxMaxX < obsMaxX)
                        ||(boxMaxY +0.001 == obsMinY &&  boxMinX >obsMinX && boxMinX < obsMaxX))){
                    coincide = true; continue;
                }
            }
            for (StaticObstacle obs:staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Left
                if (((boxMinX - 0.001 == obsMaxX && boxMaxY > obsMinY && boxMaxY < obsMaxY) 
                        || (boxMinX - 0.001 == obsMaxX && boxMinY > obsMinY && boxMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs:staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Down
                if (((boxMinY -0.001 == obsMaxY && boxMaxX >obsMinX && boxMaxX < obsMaxX)
                        ||(boxMinY -0.001 == obsMaxY &&  boxMinX >obsMinX && boxMinX < obsMaxX))){
                    coincide = true; continue;
                }
            }
            if(coincide==false){
                if (x+0.001<1) {
                    MoveState newState = new MoveState();
                    Point2D tempP=new Point2D.Double(x+0.001,y);
                    newState.setArmLocation(tempP);
                    newState.setMoveDirection("E");
                    newState.setPreMoveState(State);
                    boolean v=false;
                    for(Point2D point:visited){
                        if(isSamePoint(point,tempP)){v=true;}
                    }
                    if (v==false){
                        RobotQueue.add(newState);
                        visited.add(tempP);
                    }
                }
            }
        }
    }



    public void ArmBFS(MoveState State, int angle,List<Box>movingBoxes, List<StaticObstacle>staticObstacles,  List<Point2D>movingBoxEndPositions, List<Box>movingObstacles){
        double XCenter=State.getArmLocation().getX();
        double YCenter=State.getArmLocation().getY();
        double width= closestBox.getWidth();

        boolean coincide = false;

        if (angle == 0) {//垂直的时候

            double ArmMaxX = XCenter;
            double ArmMaxY = YCenter + 1 / 2 * width;
            double ArmMinX = XCenter;
            double ArmMinY = YCenter - 1 / 2 * width;

            coincide = false;
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Right
                if (((ArmMaxX + 0.001 == obsMinX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMaxX + 0.001 == obsMinX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Up
                if (((ArmMaxY + 0.001 == obsMinY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMaxY + 0.001 == obsMinY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Left
                if (((ArmMinX - 0.001 == obsMaxX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMinX - 0.001 == obsMaxX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Down
                if (((ArmMinY - 0.001 == obsMaxY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMinY - 0.001 == obsMaxY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }


            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();


                //Test to Right
                if (((ArmMaxX + 0.001 == obsMinX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMaxX + 0.001 == obsMinX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Up
                if (((ArmMaxY + 0.001 == obsMinY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMaxY + 0.001 == obsMinY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Left
                if (((ArmMinX - 0.001 == obsMaxX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMinX - 0.001 == obsMaxX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Down
                if (((ArmMinY - 0.001 == obsMaxY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMinY - 0.001 == obsMaxY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }

            }
            if (coincide == false) {
                if (XCenter + 0.001 < 1) {
                    MoveState newState = new MoveState();
                    Point2D tempP = new Point2D.Double(XCenter + 0.001, YCenter);
                    newState.setArmLocation(tempP);
                    newState.setMoveDirection("E");
                    newState.setPreMoveState(State);
                    boolean v = false;
                    for (Point2D point : visited) {
                        if (isSamePoint(point, tempP)) {
                            v = true;
                        }
                    }
                    if (v == false) {
                        RobotQueue.add(newState);
                        visited.add(tempP);
                    }
                }
            }
        }
        if (angle == 0) {//平行的时候

            double ArmMaxX = XCenter + 1 / 2 * width;
            double ArmMaxY = YCenter;
            double ArmMinX = XCenter - 1 / 2 * width;
            double ArmMinY = YCenter;

            coincide = false;
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Right
                if (((ArmMaxX + 0.001 == obsMinX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMaxX + 0.001 == obsMinX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Up
                if (((ArmMaxY + 0.001 == obsMinY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMaxY + 0.001 == obsMinY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Left
                if (((ArmMinX - 0.001 == obsMaxX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMinX - 0.001 == obsMaxX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (Box box : unmovedBox) {

                double obsMaxX = box.getPos().getX() + width;
                double obsMinX = box.getPos().getX();
                double obsMaxY = box.getPos().getY() + width;
                double obsMinY = box.getPos().getY();
                //Test to Down
                if (((ArmMinY - 0.001 == obsMaxY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMinY - 0.001 == obsMaxY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }


            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();


                //Test to Right
                if (((ArmMaxX + 0.001 == obsMinX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMaxX + 0.001 == obsMinX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Up
                if (((ArmMaxY + 0.001 == obsMinY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMaxY + 0.001 == obsMinY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Left
                if (((ArmMinX - 0.001 == obsMaxX && ArmMaxY > obsMinY && ArmMaxY < obsMaxY)
                        || (ArmMinX - 0.001 == obsMaxX && ArmMinY > obsMinY && ArmMinY < obsMaxY))) {
                    coincide = true;
                    continue;
                }
            }
            for (StaticObstacle obs : staticObstacles) {

                double obsMaxX = obs.getRect().getMaxX();
                double obsMinX = obs.getRect().getMinX();
                double obsMaxY = obs.getRect().getMaxY();
                double obsMinY = obs.getRect().getMinY();
                //Test to Down
                if (((ArmMinY - 0.001 != obsMaxY && ArmMaxX > obsMinX && ArmMaxX < obsMaxX)
                        || (ArmMinY - 0.001 != obsMaxY && ArmMinX > obsMinX && ArmMinX < obsMaxX))) {
                    coincide = false;
                    if (ArmMinY - 0.001 > 0) {
                        MoveState newState = new MoveState();
                        Point2D tempP = new Point2D.Double(XCenter , YCenter-0.001);
                        newState.setArmLocation(tempP);
                        newState.setMoveDirection("S");
                        newState.setPreMoveState(State);
                        boolean v = false;
                        for (Point2D point : visited) {
                            if (isSamePoint(point, tempP)) {
                                v = true;
                            }
                        }
                        if (v == false) {
                            RobotQueue.add(newState);
                            visited.add(tempP);
                        }
                    }
                }
            }
        }
    }

    
    
    
    public static boolean isSamePoint(Point2D point1,Point2D point2){
        if((point1.getX()==point2.getX())&& (point1.getY()==point2.getY())){return true;}
        return false;
    }
    
    public static boolean isGoal(MoveState state,Point2D point){
        if(isSamePoint(state.getArmLocation(),point)){return true;}
        return false;
    }

    public void mergeList(List<Box> list1,List<Box> list2){
        list1.addAll(list2);
    }

    //Queue
    public static Stack<String> path;
    public static void output(MoveState state){
        while(state!=null){
            if (state.getMoveDirection()!=null){path.push(state.getMoveDirection());}
            state=state.getPreMoveState();
        }
        //输出路径：
    }
}

