
package frc.robot;

import com.google.protobuf.Message;

import frc.robot.proto.RobotMsgs;

public class Callbacks {

    /**
     * Callback template for when a message is received
     *
     * @author aramist
     */
    public interface MessageCallback {
        void run(Message command);
    }

    private Control control;

    public Callbacks(Control control) {
        this.control = control;
    }


    public Callbacks.MessageCallback driveCallback = new MessageCallback() {
        @Override
        public void run(Message command) {
            RobotMsgs.DifferentialDrive drive = (RobotMsgs.DifferentialDrive) command;
            if (drive.getName().equalsIgnoreCase("front")) {
                control.driveFront(drive.getLeft(), drive.getRight());
            } else if (drive.getName().equalsIgnoreCase("rear")) {
                control.driveRear(drive.getLeft(), drive.getRight());
            }
        }
    };

    public Callbacks.MessageCallback tableCallback = new MessageCallback() {
        @Override
        public void run(Message command) {
            RobotMsgs.XYTable table = (RobotMsgs.XYTable) command;
            control.setTableSetpoint(table.getX(), table.getY());
        }
    };
}
