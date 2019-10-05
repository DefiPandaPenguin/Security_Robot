package frc.robot;

import com.google.protobuf.Any;
import frc.robot.modules.Differential;
import frc.robot.modules.IMU;
import frc.robot.modules.Joy;
import frc.robot.modules.XYTable;

public class Control {

    private Differential frontDrive;
    private Differential rearDrive;
    private XYTable table;

    private IMU rearImu;
    private Joy stick;

    public Control() {
        //  0, 1, 4, and 5 were all inverted, but I don't know which motors they referred to
        frontDrive = new Differential(0, 1, 2, 3, "front");
        rearDrive = new Differential(4, 5, 6, 7, "rear");

        table = new XYTable();

        rearImu = new IMU("rear");

        stick = new Joy(0);
    }

    public void sendMessages(Communication comms) {
        comms.send(Any.pack(frontDrive.getEncoders()));

        comms.send(Any.pack(rearDrive.getEncoders()));

        comms.send(Any.pack(rearImu.getMessage()));

        comms.send(Any.pack(stick.getMessage()));

        comms.send(Any.pack(
                table.getPosition()
        ));
    }

    public void driveFront(double left, double right) {
        frontDrive.drive(left, right);
    }

    public void driveRear(double left, double right) {
        rearDrive.drive(left, right);
    }

    public void setTableSetpoint(int x, int y) {
        table.set(x, y);
    }
}
