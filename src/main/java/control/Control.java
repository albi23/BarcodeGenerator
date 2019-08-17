package control;

import barcode.Colors;

public class Control implements IControl {


    public Control() {
    }

    public void printDoneWork() {
        System.out.println(makeColor("Done\n", Colors.GREEN));
    }

    public String makeColor(String data, Colors color) {
        return color.makeColor(color, data, true);
    }
    public void printOptionMessage(){
        String message = "Your choose : ";
        System.out.print(makeColor(message,Colors.PINK));
    }
}
