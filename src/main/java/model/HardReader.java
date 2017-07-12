package model;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class HardReader {

    private static SerialPort serialPort;
    QueryProcessor processor;

    HardReader(String port, QueryProcessor processor) {
        this.processor = processor;
        serialPort = new SerialPort(port);
        //Открываем порт
        try {
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
        } catch (SerialPortException ex) {
            ex.getMessage();
        }
    }

    public static void startReading() {
        try {
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            //Извлечение мусора
            serialPort.readString();
            //Отправляем запрос устройству
            //serialPort.writeString("Clean data");
            serialPort.writeString("Get data");
        } catch (SerialPortException ex) {
            ex.getMessage();
        }
    }

    static void startReporting() {
        try {
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReaderReport(), SerialPort.MASK_RXCHAR);
            //Извлечение мусора
            serialPort.readString();
            //Отправляем запрос устройству
            serialPort.writeString("Get data");
        } catch (SerialPortException ex) {
            ex.getMessage();
        }
    }

    static void endReporting() {
        try {
            serialPort.removeEventListener();
        } catch (SerialPortException se) {
            se.printStackTrace();
        }
    }

    static void closePort() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            System.out.println(e.getMessage());
        }
    }


    //Считывание карты для регистрации и проверки
    private static class PortReader implements SerialPortEventListener {

        synchronized public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    String cardId = serialPort.readString(8);
                    //System.out.println(cardId);
                    serialPort.removeEventListener();
                    if (cardId.length() == 8) {
                        QueryProcessor q = new QueryProcessor();
                        //q.process(cardId);
                        q.process("sss");
                        //processor.process(cardId);
                    } else {
                        System.out.println("Карта считана неправильно, попробуйте еще раз");
                    }
                } catch (SerialPortException ex) {
                    ex.getMessage();
                }
            }

        }
    }

    //Считывание карты для отчета
        private static class PortReaderReport implements SerialPortEventListener {

            synchronized public void serialEvent(SerialPortEvent event) {
                if (event.isRXCHAR() && event.getEventValue() > 0) {
                    try {
                        //Получаем ответ от устройства, обрабатываем данные и т.д.
                        String data = serialPort.readString(8);
                        serialPort.readString();

                        if (data.length() == 8) {

                            System.out.println("72");
                            ReportProcess.writeRecord(data);
                            //model.ReportProcess.close();
                        } else {
                            System.out.println("Карта считана неправильно, попробуйте еще раз");
                        }
                        //model.Menu.menu();
                    } catch (SerialPortException ex) {
                        ex.getMessage();
                    } catch (Exception e) {

                        System.out.println(e.getMessage());
                    }
                }
            }
        }


}