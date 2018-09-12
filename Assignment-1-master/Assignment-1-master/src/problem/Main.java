package problem;

import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {

    public static List<StaticObstacle>staticObstacles;
    public static List<Box>movingBoxes;
    public static List<Box>movingObstacles;
    public static List<RobotConfig>robotPath;
    public static List<List<Box>>movingBoxPath;
    public static List<List<Box>>movingObstaclePath;
    public static List<Point2D>movingBoxEndPositions;
    public static RobotConfig InitialRobotConfig;
    public static LinkedList<LinkedList> PathList;
    public static Stack<String> reversedPath;
    public static LinkedList<String> path;
    public static RobotConfig RobotConfigPath;
    public static double width;
    public static Point2D ArmNewEndPos;
    public static List<Box>movedBoxes;

    public static void main(String[] args) {
        //output file
        ProblemSpec ps = new ProblemSpec();
        try {
            ps.loadProblem("input1.txt");
            //output path into output1.tx
            //ps.loadSolution("output1.txt");
            staticObstacles = ps.getStaticObstacles();
            movingBoxes = ps.getMovingBoxes();
            movingObstacles = ps.getMovingObstacles();
            robotPath = ps.getRobotPath();
            movingBoxPath = ps.getMovingBoxPath();
            movingObstaclePath = ps.getMovingObstaclePath();
            movingBoxEndPositions = ps.getMovingBoxEndPositions();
            InitialRobotConfig = ps.getInitialRobotConfig();
            width = ps.getRobotWidth();

        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        System.out.println("Finished loading!");


        /* While非空
            Find the closest moving box
            Find the goal position
            While box-goal queue is not empty
                new state = 1st state
                if new state = goal
                    output(state)
                    store box direction
                    calculate moving box path (x,y)
                    calculate arm path (x,y)**
                    store both calculation results
                    break
                else Do BoxBFS
                Calculate Arm final position
                While arm queue is not empty
                    new state = 1st state
                    if new state = goal
                        output(state)
                        store arm direction
                        calculate path (x,y)
                        store path result
                        break
                    else Do ArmBFS
                 endWhile
             EndWhile
             Output into text file
        */

        BoxQueue = new LinkedList<MoveState>();
        RobotQueue = new LinkedList<MoveState>();

        movedBoxes = new LinkedList<Box>();
        findClosestBox(movingBoxes,InitialRobotConfig);
        Point2D goal_pos = movingBoxEndPositions.get(NumClosestBox);
        movingBoxes.remove(NumClosestBox);
        movedBoxes.add(closestBox);
        movingBoxEndPositions.remove(NumClosestBox);
        MoveState IniBox=new MoveState();
        IniBox.setArmLocation(closestBox.getPos());
        IniBox.setMoveDirection(null);
        IniBox.setPreMoveState(null);
        BoxQueue.add(IniBox);


        while (movingBoxes.size()>0){

            mergeList(unmovedBox,movingBoxes,movingObstacles,movedBoxes);
            if (!unmovedBox.isEmpty()){
                unmovedBox.clear();}
            while (!BoxQueue.isEmpty()) {

                MoveState state = BoxQueue.poll();
                if (isSamePoint(state.getArmLocation(), goal_pos)) {
                    LinkedList<String> path = new LinkedList<String>();
                    output(state, path);
                    PathList.add(path);
                    break;
                } else {
                    BoxBFS(state);
                }
            }
            if (path.getLast().equals("N")){
                Point2D NewRobotPos = new Point2D.Double(goal_pos.getX()+width/2,goal_pos.getY());
                int NewAngle = 0;
            }
            else if (path.getLast().equals("E")){
                Point2D NewRobotPos = new Point2D.Double(goal_pos.getX(),goal_pos.getY()+width/2);
                int NewAngle = 1;
            }
            else if (path.getLast().equals("S")){
                Point2D NewRobotPos = new Point2D.Double(goal_pos.getX()+width/2,goal_pos.getY()+width);
                int NewAngle = 0;
            }
            else if (path.getLast().equals("W")){
                Point2D NewRobotPos = new Point2D.Double(goal_pos.getX()+width,goal_pos.getY()+width/2);
                int NewAngle = 1;
            }

            if (Math.abs(InitialRobotConfig.getOrientation())-1/2*Math.PI < 0.1) {
                MoveState ArmState = new MoveState();
                ArmState.setArmLocation(InitialRobotConfig.getPos());
                if (path.getFirst().equals("W") || path.getFirst().equals("E")) {
                    while (!RobotQueue.isEmpty()) {
                        MoveState state = RobotQueue.poll();
                        if (path.getFirst().equals("N")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY());
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        } else if (path.getFirst().equals("E")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX(), closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        } else if (path.getFirst().equals("S")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY() + width);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        } else if (path.getFirst().equals("W")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width, closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        }
                    }

                }
                else{ rotateRobot();//如果臂垂直并且初始运动方向为东西
                    while (!RobotQueue.isEmpty()) {
                        MoveState state = RobotQueue.poll();
                        if (path.getFirst().equals("N")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY());
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        } else if (path.getFirst().equals("E")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX(), closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        } else if (path.getFirst().equals("S")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY() + width);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        } else if (path.getFirst().equals("W")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width, closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        }
                    }


                }
            }
            if (Math.abs(InitialRobotConfig.getOrientation())< 0.1) {
                MoveState ArmState = new MoveState();
                ArmState.setArmLocation(InitialRobotConfig.getPos());
                if (path.getFirst().equals("N") || path.getFirst().equals("S")) {
                    while (!RobotQueue.isEmpty()) {
                        MoveState state = RobotQueue.poll();
                        if (path.getFirst().equals("N")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY());
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        } else if (path.getFirst().equals("E")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX(), closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        } else if (path.getFirst().equals("S")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY() + width);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        } else if (path.getFirst().equals("W")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width, closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 0);
                            }
                        }
                    }
                }
                else {
                    while (!RobotQueue.isEmpty()) {
                        MoveState state = RobotQueue.poll();
                        if (path.getFirst().equals("N")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY());
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        } else if (path.getFirst().equals("E")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX(), closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        } else if (path.getFirst().equals("S")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width / 2, closestBox.getPos().getY() + width);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        } else if (path.getFirst().equals("W")) {
                            Point2D ArmEndPos = new Point2D.Double(closestBox.getPos().getX() + width, closestBox.getPos().getY() + width / 2);
                            if (isSamePoint(state.getArmLocation(), ArmEndPos)) {
                                LinkedList<String> path = new LinkedList<String>();
                                output(state, path);
                                PathList.add(path);
                                break;
                            } else {
                                ArmBFS(state, 1);
                            }
                        }
                    }

                }
            }

            if (path.getFirst().equals("N")) {
                ArmNewEndPos = new Point2D.Double(goal_pos.getX() + width / 2, goal_pos.getY());
            } else if (path.getFirst().equals("E")) {
                ArmNewEndPos = new Point2D.Double(goal_pos.getX(), goal_pos.getY() + width / 2);
            } else if (path.getFirst().equals("S")) {
                ArmNewEndPos = new Point2D.Double(goal_pos.getX() + width / 2, goal_pos.getY() + width);
            } else if (path.getFirst().equals("W")) {
                ArmNewEndPos = new Point2D.Double(goal_pos.getX() + width, goal_pos.getY() + width / 2);
            }
            findClosestBox2(movingBoxes,ArmNewEndPos);
            goal_pos = movingBoxEndPositions.get(NumClosestBox);
            movingObstacles.add(movingBoxes.get(NumClosestBox));
            movingBoxes.remove(NumClosestBox);
            movingBoxEndPositions.remove(NumClosestBox);
            movedBoxes.add(closestBox);
            RobotQueue.clear();
            BoxQueue.clear();
            IniBox.setArmLocation(closestBox.getPos());
            IniBox.setMoveDirection(null);
            IniBox.setPreMoveState(null);
            BoxQueue.add(IniBox);
        }
    }



    /** 距离arm最近的box **/
    public static Box closestBox;
    public static int NumClosestBox;

    //更新最近的Box
    public static void findClosestBox(List<Box> Box,RobotConfig config) {
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
                NumClosestBox = i ;
            }
        }
    }

    public static void findClosestBox2(List<Box> Box,Point2D Position) {
        double dist;
        double closestDist=1;
        for(int i=0;i<Box.size();i++){
            Box tempBox;
            tempBox=Box.get(i);
            //manhattan distance
            dist=Math.abs(Position.getX()-tempBox.getPos().getX())+Math.abs(Position.getY()-tempBox.getPos().getY());
            if(dist<closestDist){
                closestDist=dist;
                closestBox=tempBox;
                NumClosestBox = i ;
            }
        }
    }

    public static List<Box> unmovedBox;
    public static List<Point2D>visited;
    public static LinkedList<MoveState> RobotQueue;
    public static LinkedList<MoveState> BoxQueue;

    public static void BoxBFS(MoveState State){
        Boolean coincide = false;
        double boxMaxX = closestBox.getPos().getX()+width;
        double boxMinX = closestBox.getPos().getX();
        double boxMaxY = closestBox.getPos().getY()+width;
        double boxMinY = closestBox.getPos().getY();
        for (Box box:unmovedBox) {

            double obsMaxX = box.getPos().getX() + width;
            double obsMinX = box.getPos().getX();
            double obsMaxY = box.getPos().getY() + width;
            double obsMinY = box.getPos().getY();

            //Test to Right
            if (((boxMaxX + 0.001 == obsMinX && boxMaxY > obsMinY && boxMaxY < obsMaxY)
                    || (boxMaxX + 0.001 == obsMinX && boxMinY > obsMinY && boxMinY < obsMaxY))) {
                coincide = true;
                break;
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
                break;
            }
        }
        if (boxMaxX + 0.001 < 1&& coincide==false) {
            MoveState newState = new MoveState();
            Point2D tempP = new Point2D.Double(closestBox.getPos().getX()+0.001 , closestBox.getPos().getY());
            newState.setArmLocation(tempP);
            newState.setMoveDirection("E");
            newState.setPreMoveState(State);
            boolean v = false;
            for (Point2D point : visited) {
                if (isSamePoint(point, tempP)) {
                    v = true;
                    break;
                }
            }
            if (v == false) {
                BoxQueue.add(newState);
                visited.add(tempP);
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
                break;
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
                coincide = true;
                break;
            }
        }
        if (boxMaxY + 0.001 < 1&& coincide==false) {
            MoveState newState = new MoveState();
            Point2D tempP = new Point2D.Double(closestBox.getPos().getX() , closestBox.getPos().getY()+0.001);
            newState.setArmLocation(tempP);
            newState.setMoveDirection("N");
            newState.setPreMoveState(State);
            boolean v = false;
            for (Point2D point : visited) {
                if (isSamePoint(point, tempP)) {
                    v = true;
                    break;
                }
            }
            if (v == false) {
                BoxQueue.add(newState);
                visited.add(tempP);
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
                break;
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
                break;
            }
        }
        if (boxMaxX - 0.001 > 0&& coincide==false) {
            MoveState newState = new MoveState();
            Point2D tempP = new Point2D.Double(closestBox.getPos().getX()-0.001 , closestBox.getPos().getY());
            newState.setArmLocation(tempP);
            newState.setMoveDirection("W");
            newState.setPreMoveState(State);
            boolean v = false;
            for (Point2D point : visited) {
                if (isSamePoint(point, tempP)) {
                    v = true;
                    break;
                }
            }
            if (v == false) {
                BoxQueue.add(newState);
                visited.add(tempP);
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
                break;
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
                coincide = true; break;
            }
        }
        if (boxMaxY - 0.001 > 0&& coincide==false) {
            MoveState newState = new MoveState();
            Point2D tempP = new Point2D.Double(closestBox.getPos().getX() , closestBox.getPos().getY()-0.001);
            newState.setArmLocation(tempP);
            newState.setMoveDirection("S");
            newState.setPreMoveState(State);
            boolean v = false;
            for (Point2D point : visited) {
                if (isSamePoint(point, tempP)) {
                    v = true;
                    break;
                }
            }
            if (v == false) {
                BoxQueue.add(newState);
                visited.add(tempP);
            }
        }

    }



    public static void ArmBFS(MoveState State, int angle){
        double XCenter=State.getArmLocation().getX();
        double YCenter=State.getArmLocation().getY();

        boolean coincide = false;

        if (angle == 1) {//垂直的时候

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
                    break;
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
                    break;
                }
            }
            if (ArmMaxX + 0.001 < 1&& coincide == false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter +0.001, YCenter);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("E");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    break;
                }
            }
            if (ArmMaxY + 0.001 < 1&& coincide==false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter, YCenter+0.001);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("N");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    break;
                }
            }
            if (ArmMinX - 0.001 > 0&& coincide==false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter-0.001, YCenter);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("W");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    coincide = true;
                    break;
                }
            }
            if (ArmMinY - 0.001 > 0&& coincide==false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter , YCenter-0.001);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("S");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    break;
                }
            }
            if (ArmMaxX + 0.001 < 1&& coincide == false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter +0.001, YCenter);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("E");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    break;
                }
            }
            if (ArmMaxY + 0.001 < 1&& coincide==false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter, YCenter+0.001);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("N");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    break;
                }
            }
            if (ArmMinX - 0.001 > 0&& coincide==false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter-0.001, YCenter);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("W");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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
                    break;
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
                    coincide = true;
                    break;
                }
            }
            if (ArmMinY - 0.001 > 0&& coincide==false) {
                MoveState newState = new MoveState();
                Point2D tempP = new Point2D.Double(XCenter , YCenter-0.001);
                newState.setArmLocation(tempP);
                newState.setMoveDirection("S");
                newState.setPreMoveState(State);
                boolean v = false;
                for (Point2D point : visited) {
                    if (isSamePoint(point, tempP)) {
                        v = true;
                        break;
                    }
                }
                if (v == false) {
                    RobotQueue.add(newState);
                    visited.add(tempP);
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

    public static void mergeList(List<Box>unmovedBox,List<Box> list1,List<Box> list2,List<Box> list3){
        unmovedBox.addAll(list1);
        unmovedBox.addAll(list2);
        unmovedBox.addAll(list3);

    }

    //Queue
    public static void output(MoveState state,LinkedList<String> path){
        Stack<String> reversedPath=new Stack<>();
        while(state.getPreMoveState()!=null){
            if (state.getMoveDirection()!=null){reversedPath.push(state.getMoveDirection());}
            state=state.getPreMoveState();
        }
        //输出路径：
        while(!reversedPath.isEmpty()){path.add(reversedPath.pop());}
    }

    public static void rotateRobot(){
        LinkedList<String> R=new LinkedList<>();
        R.add("R");
        PathList.add(R);
    }
}

