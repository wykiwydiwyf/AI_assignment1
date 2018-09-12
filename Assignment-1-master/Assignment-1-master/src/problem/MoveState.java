package problem;

import java.awt.geom.Point2D;

public class MoveState {
    private Point2D ArmLocation;
    private String MoveDirection;//上下左右四个方向
    private MoveState preMoveState;

    public Point2D getArmLocation(){
        return ArmLocation;
    }

    public String getMoveDirection(){
        return MoveDirection;
    }

    public void setArmLocation(Point2D ArmLocation){
        this.ArmLocation=ArmLocation;
    }

    public MoveState getPreMoveState(){
        return preMoveState;
    }

    public void setPreMoveState(MoveState preMoveState){
        this.preMoveState=preMoveState;
    }

    public void setMoveDirection(String moveDirection) {
        this.MoveDirection = moveDirection;
    }
}
