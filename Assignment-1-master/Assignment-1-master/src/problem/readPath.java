package problem;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class readPath {
    private static LinkedList<LinkedList>  pathList;
    private static LinkedList<String> path;
    private static String signal;
    private static Point2D BoxPos;
    private static double width;
    private static Point2D initialPos;

    public static void writeTXT(LinkedList<LinkedList> pathList){
        while(!pathList.isEmpty()){
            path=new LinkedList<>();
            path=pathList.poll();
            signal=path.poll();
            if(signal.equals("R")){signalR(path);}
            else if(signal.equals("A")){signalA(path,initialPos);}
            else if(signal.equals("B")){signalB(path);}
        }
    }

    private static void signalR(LinkedList<String>pa){}

    private static void signalA(LinkedList<String>pa,Point2D initialPos){
        double armX=initialPos.getX();
        double armY=initialPos.getY();
        while(!pa.isEmpty()){
            if (pa.poll().equals("N")){
                System.out.println(armX);
            }
            else if(pa.poll().equals("S")){}
            else if(pa.poll().equals("W")){}
            else if(pa.poll().equals("E")){}
        }
    }

    private static void signalB(LinkedList<String>pa){
        String step;
        String nextStep;
        double boxX;
        double boxY;
        double armX;
        double armY;
        double angle;
        while (pa.size()>=1){
            step=pa.poll();
            if(pa.size()==0){nextStep=step;}
            else{nextStep=pa.getFirst();}

            if (nextStep.equals(step)){//Same direction.
                if (step.equals("N")){
                    armX=BoxPos.getX()+1/2*width;
                    armY=BoxPos.getY()+0.001;
                    System.out.println(armX+" "+armY+" "+0);
                }
                else if(step.equals("S")){
                    armX=BoxPos.getX()+1/2*width;
                    armY=BoxPos.getY()+2*width-0.001;
                    System.out.println(armX+" "+armY+" "+0);

                }
                else if(step.equals("W")){
                    armX=BoxPos.getX()+2*width-0.001;
                    armY=BoxPos.getY()+1/2*width;
                    angle=1/2*Math.PI;
                    System.out.println(armX+" "+armY+" "+angle);
                }
                else if (step.equals("E")){
                    armX=BoxPos.getX()+0.001;
                    armY=BoxPos.getY()+1/2*width;
                    angle=1/2*Math.PI;
                    System.out.println(armX+" "+armY+" "+angle);
                }
            }

            else {
                if(step.equals("E")&&nextStep.equals("N")){
                    armX=BoxPos.getX();
                    armY=BoxPos.getY()+1/2*width;
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    System.out.println(armX+" "+armY+" "+0);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                }
                else if(step.equals("E")&&nextStep.equals("S")){
                    armX=BoxPos.getX();
                    armY=BoxPos.getY()+1/2*width;
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    System.out.println(armX+" "+armY+" "+0);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                }
                else if(step.equals("W")&&step.equals("N")){
                    armX=BoxPos.getX()+width;
                    armY=BoxPos.getY()+1/2*width;
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    System.out.println(armX+" "+armY+" "+0);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                }
                else if(step.equals("W")&&nextStep.equals("S")){
                    armX=BoxPos.getX()+width;
                    armY=BoxPos.getY()+1/2*width;
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    System.out.println(armX+" "+armY+" "+0);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                }
                else if(step.equals("S")&&nextStep.equals("W")){
                    armX=BoxPos.getX()+1/2*width;
                    armY=BoxPos.getY()+width;
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                }
                else if(step.equals("S")&&nextStep.equals("E")){
                    armX=BoxPos.getX()+1/2*width;
                    armY=BoxPos.getY()+width;
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                }
                else if(step.equals("N")&&nextStep.equals("W")){
                    armX=BoxPos.getX()+1/2*width;
                    armY=BoxPos.getY();
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                }
                else if(step.equals("N")&&nextStep.equals("E")){
                    armX=BoxPos.getX()+1/2*width;
                    armY=BoxPos.getY();
                    double i=0;
                    while(i<1/2*width){
                        i=i+0.001;
                        armY=armY-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armX=armX-0.001;
                        System.out.println(armX+" "+armY+" "+0);
                    }
                    System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    i=0;
                    while(i<width){
                        i=i+0.001;
                        armY=armY+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                    i=0;
                    while (i<1/2*width){
                        i=i+0.001;
                        armX=armX+0.001;
                        System.out.println(armX+" "+armY+" "+1/2*Math.PI);
                    }
                }
            }
        }
    }
}
