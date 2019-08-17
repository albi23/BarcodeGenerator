package control;

import barcode.Colors;

public interface IControl {

    void printDoneWork();

    void printOptionMessage();

    String makeColor(String data, Colors color);

}
