package model;

public class TestPro {
    public static void main(String arg[]) throws Exception {
        PortFinder pf = new PortFinder("model.PortFinder");
        pf.start();
        StudentDB.setConnection();
        Menu me = new Menu();
        me.menu();
        pf.join();
    }

}
