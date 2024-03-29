package frc.robot.modules;

import com.google.protobuf.Message;
import frc.robot.proto.RobotMsgs;
import frc.robot.proto.RobotMsgs.EncoderPair.Builder;

import edu.wpi.first.wpilibj.Spark;

public class Differential {

    private Spark frontLeft;
    private Spark frontRight;

    private Spark backLeft;
    private Spark backRight;

    private String frame;

    /**
     * Initializes the speed controllers
     *
     * @param fl PWM ID of the front left motor
     * @param fr PWM ID of the front right motor
     * @param bl PWM ID of the back left motor
     * @param br PWM ID of the back right motor
     */
    public Differential(int fl, int fr, int bl, int br, String frame) {
        frontLeft = new Spark(fl);
        frontRight = new Spark(fr);
        backLeft = new Spark(bl);
        backRight = new Spark(br);
        this.frame = frame;
    }

    public void drive(double left, double right) {
        frontLeft.set(left);
        backLeft.set(left);

        frontRight.set(right);
        backRight.set(right);
    }

    /**
     * @return Current encoder readings for this drive train
     */
    public Message getEncoders() {
        return RobotMsgs.EncoderPair.newBuilder()
                .setLeft(0)
                .setRight(0)
                .setName(frame)
                .build();
    }
}
