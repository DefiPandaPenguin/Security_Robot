package frc.robot;

import frc.robot.proto.RobotMsgs;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;

import edu.wpi.first.wpilibj.DriverStation;

public class Communication {

    private long lastReceived;

    private ZContext context;
    private ZMQ.Socket socket;

    private Thread recvThread;

    private Callbacks.MessageCallback differentialCallback;
    private Callbacks.MessageCallback tableCallback;

    public Communication(Callbacks.MessageCallback diffDrive, Callbacks.MessageCallback table) {
        lastReceived = System.currentTimeMillis();
        differentialCallback = diffDrive;
        tableCallback = table;
        try {
            context = new ZContext();
            socket = context.createSocket(ZMQ.DEALER);
            socket.bind("tcp://*:5556");

            recvThread = new Thread() {
                public void run() {
                    while (true)
                        recv();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            DriverStation.reportError("Failed to open communication with Jetson", false);
            Robot.disable();
        }
    }

    /*private void send(byte[] bytes) {
        socket.send(bytes);
    }*/

    private void recv() {
        if (!socket.hasReceiveMore())
            return;
        try {
            Any any = Any.parseFrom(socket.recv());
            if (any.is(RobotMsgs.DifferentialDrive.class)) {
                lastReceived = System.currentTimeMillis();
                differentialCallback.run(any.unpack(RobotMsgs.DifferentialDrive.class));
            } else if (any.is(RobotMsgs.XYTable.class)) {
                lastReceived = System.currentTimeMillis();
                tableCallback.run(any.unpack(RobotMsgs.XYTable.class));
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends sensor data (XY table position, encoders, ...) to Jetson
     *
     * @param msg The message to send
     */
    public void send(AbstractMessage msg) {
        socket.send(msg.toByteArray(), 0);
    }
}
