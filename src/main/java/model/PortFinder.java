package model;

import jssc.SerialPortList;

public class PortFinder extends Thread {
    private static String port = ""; // Порт к которому подключен считыватель
    private static volatile boolean status = false; //Подключен ли считыватель к порту
    private static volatile boolean stop = true;
    private HardReader hr = null;

    public PortFinder(String nameOfThread) {
        super(nameOfThread);
    }

    public static boolean getStatus() {
        return status;
    }

    public static void setStop() {
        stop = false;
    }

    public void run() {
        while (stop) {
            QueryProcessor queryProcess = new QueryProcessor();

            String currentPort = findPort();
            if (currentPort != null) {
                status = true;
                if (!port.equals(currentPort)) {
                    closePort();
                    port = currentPort;
                    hr = new HardReader(port, queryProcess);
                }

            } else {
                status = false;
                System.out.println("Считыватель не найден!!! Проверьте подключение.");
            }
            try {
                sleep(5000);
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
        }
        closePort();
    }

    private void closePort() {
        if (hr != null) {
            HardReader.closePort();
        }
    }

    private String findPort() {
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length > 0) {
            return portNames[0];
        } else {
            return null;
        }
    }


}
