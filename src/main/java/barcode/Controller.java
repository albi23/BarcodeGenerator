package barcode;

import com.onbarcode.barcode.AbstractBarcode;

import java.io.IOException;
import java.util.Scanner;

public class Controller {

    private static Scanner scanner;

    public static void main(String[] args) {

        Controller controller = new Controller();
        scanner = new Scanner(System.in);

        while (true) {
            System.out.println(controller.makeColor("Wat do you want to do: \n 1 : Print Barcode \n 2 : Merge barcode \n 3 : Exit",Colors.WHITE));
            controller.printOptionMessage();
            switch (controller.inputTextValidation(scanner.next())) {
                case 1:
                    System.out.println(controller.makeColor("\nChoose needed type of code",Colors.WHITE));
                    System.out.print(controller.makeColor(" 1 : Code 128 \n 2 : Code 93 \n 3 : Code 39 \n 4 : Code 25 \n 5 : Code 11 \n 6 : QRCode \n 7 : PDF417 \n 8 : DataMatrix \n 9 : Back \n",Colors.WHITE));
                    controller.printOptionMessage();
                    BarcodeVisualisation barcodeVisualisation = controller.controlCodeFactory(controller.inputTextValidation(scanner.next()));
                    if (barcodeVisualisation == null) break;
                    controller.manageBarcodeVisualisation(barcodeVisualisation);
                    break;
                case 2:
                    System.out.print("to Implement");
                    break;
                case 3:
                    System.out.println(controller.makeColor("Bye!",Colors.BLUE));
                    return;
                default:
                    System.out.println(controller.makeColor("Choose correct answer",Colors.RED));
                    break;
            }
        }

    }

    private int inputTextValidation(String inputText) {

        int result = 0;
        try {
            result = Integer.parseInt(inputText);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    private int inputTextValidation(String inputText, boolean mustBePositive){
        int result = inputTextValidation(inputText);
        if (result <= 0) System.out.println(makeColor("Number must me positive!",Colors.RED));
        return result;
    }

    private BarcodeVisualisation controlCodeFactory(int option) {
        new BarcodeGeneratorFactory();
        AbstractBarcode barcodeType;

        if ((barcodeType = BarcodeGeneratorFactory.codeType(option)) != null)
            return new BarcodeVisualisation(barcodeType);

        return null;
    }

    private void manageBarcodeVisualisation(BarcodeVisualisation barcodeVisualisation) {

        while (true) {
            System.out.print(makeColor("\n 1 : Print with actual settings\n" +
                    " 2 : Show settings\n 3 : Change settings\n 4 : Save settings\n 5 : Back\n",Colors.WHITE));
            printOptionMessage();
            switch (inputTextValidation(scanner.next())) {
                case 1:
                    printingCode(barcodeVisualisation);
                    break;
                case 2:
                    System.out.println("\n"+makeReadable(barcodeVisualisation.getActualSettings()));
                    break;
                case 3:
                    changeSettings(barcodeVisualisation);
                    break;
                case 4:
                    saveSettings(barcodeVisualisation); break;
                case 5:
                    return;
                default:
                    System.out.println("Choose correct answer");
                    break;
            }
        }
    }

    private void printingCode(BarcodeVisualisation barcodeVisualisation) {

        System.out.print(makeColor("Numbers of barcode : ", Colors.PINK));
        int numbers = inputTextValidation(scanner.next(), true);
        if (numbers <= 0 ) return;
        System.out.print(makeColor("Random data (Y/N): ", Colors.PINK));
        boolean isRandom = false;
        String option;

        while (true) {
            option = scanner.next();
            if (option.toUpperCase().equals("Y")) {
                isRandom = true;
                break;
            } else if (option.toUpperCase().equals("N")) {
                break;
            } else {
                System.out.print(makeColor("\nChoose Y or N : ",Colors.PINK));
            }
        }
        printingCode(barcodeVisualisation, numbers, isRandom);
    }

    private void printingCode(BarcodeVisualisation barcodeVisualisation, int numbers, boolean isRandomData) {

        if (!isRandomData){
            printCodeIncresing(barcodeVisualisation, numbers);
        }else {
            printCodeRandom(barcodeVisualisation,numbers);
        }
    }

    private void printCodeIncresing(BarcodeVisualisation barcodeVisualisation, int number){

        String outputName = barcodeVisualisation.getOutputName();
        for (int i = 1; i <= number; i++) {
            printing(barcodeVisualisation, i,false);
            percentageVisualization(i, number);
            barcodeVisualisation.setId(barcodeVisualisation.getId()+1);
            barcodeVisualisation.setOutputName(outputName+i);
        }
        System.out.println();
        printDoneWork();
    }

    private void printCodeRandom(BarcodeVisualisation barcodeVisualisation, int number){

        String outputName = barcodeVisualisation.getOutputName();
        for (int i = 1; i <= number; i++) {
            barcodeVisualisation.setData(barcodeVisualisation.getRandomData().toString());
            printing(barcodeVisualisation, i, true);
            barcodeVisualisation.setOutputName(outputName+i);
            percentageVisualization(i, number);
        }
        System.out.println();
        printDoneWork();
    }



    private void printing(BarcodeVisualisation barcodeVisualisation, int id, boolean randomData){
        try {
            barcodeVisualisation.printCode(randomData);
        } catch (Exception e) {
            System.out.println(makeColor("Something gone wrong at printing "+id+" code", Colors.RED));
            e.printStackTrace();
        }
    }

    private void percentageVisualization(int currentValue, int expectedValue){
        System.out.print("\r"+makeColor(String.format("Progress : %3.2f",((double)currentValue/(double)expectedValue)*100)+"%",Colors.BLUE));

    }
    private void changeSettings(BarcodeVisualisation barcodeVisualisation) {
        while (true) {
            System.out.println(makeColor("\n 1  : Change Width \n 2  : Change Height \n 3  : Change Left Margin \n " +
                            "4  : Change Right Margin \n 5  : Change Saving Format \n 6  : Change Initial Number \n" +
                            " 7  : Change Output Name \n 8  : Change Filling Sign \n 9  : Change Length Of Number Code \n " +
                            "10 : Change Save Path \n 11 : Change Suffix\n 12 : Back\n",Colors.WHITE));
            printOptionMessage();
            int option = inputTextValidation(scanner.next());
            if (option != 8 && option !=9 && option != 12)printNewValueMessage();
            switch (option) {
                case 1:
                    barcodeVisualisation.setWidth(scanner.nextFloat());
                    break;
                case 2:
                    barcodeVisualisation.setHeight(scanner.nextFloat());
                    break;
                case 3:
                    barcodeVisualisation.setLeftMargin(scanner.nextFloat());
                    break;
                case 4:
                    barcodeVisualisation.setRightMargin(scanner.nextFloat());
                    break;
                case 5:
                    availableFormatInfo(barcodeVisualisation);
                    break;
                case 6:
                    barcodeVisualisation.setId(scanner.nextInt());
                    break;
                case 7:
                    barcodeVisualisation.setOutputName(scanner.next());
                    break;
                case 8:
                    printExampleFormat();
                    printNewValueMessage();
                    barcodeVisualisation.setFillSign(scanner.next());
                    break;
                case 9:
                    printExampleFormat();
                    printNewValueMessage();
                    barcodeVisualisation.setLengthCode(inputTextValidation(scanner.next()));
                    break;
                case 10:
                    barcodeVisualisation.setPath(scanner.next());
                    break;
                case 11:
                    barcodeVisualisation.setSuffix(scanner.next());break;
                case 12:
                    return;
                default:
                    System.out.println(makeColor("Choose correct answer",Colors.RED));
                    break;
            }
            if (option < 12 && option > 0) printDoneWork();

        }
    }


    private String makeColor(String data, Colors color) {
        return color.makeColor(color, data, true);
    }

    private void availableFormatInfo(BarcodeVisualisation barcodeVisualisation) {

        System.out.println(makeColor("\n Choose saving format:  1 : .GIF \n 2 : .JPEG \n 3 : .PNG \n 4 : .EPS \n",Colors.WHITE));
        switch (inputTextValidation(scanner.next())) {
            case 1:
                barcodeVisualisation.setFormat(".gif");
                printDoneWork();
                break;
            case 2:
                barcodeVisualisation.setFormat(".jpeg");
                printDoneWork();
                break;
            case 3:
                barcodeVisualisation.setFormat(".png");
                printDoneWork();
                break;
            case 4:
                barcodeVisualisation.setFormat(".eps");
                printDoneWork();
                break;
            default:
                System.out.println("Choose correct answer");
        }
    }

    private void saveSettings(BarcodeVisualisation barcodeVisualisation) {

        try {
            barcodeVisualisation.saveSettings("target/", "settings");
            printDoneWork();
        } catch (IOException ex) {
            System.out.println(makeColor("Setting can't be save correct", Colors.RED));
            ex.printStackTrace();
        }
    }


    private void printDoneWork() {
        System.out.println(makeColor("Done\n", Colors.GREEN));
    }

    private void printOptionMessage(){
        String message = "Your choose : ";
        System.out.print(makeColor(message,Colors.PINK));
    }

    private void printNewValueMessage(){
        System.out.print(makeColor("New Value : ", Colors.BLUE));
    }

    private void printExampleFormat() {
        System.out.println(makeColor("Format depends of : filling Sign, length of number code, and data in code \n" +
                "eg. when filling sign = \'-\' , Length of code = \'6\' and Code number = \'123\' give result : \'---123\'", Colors.YELLOW));
    }

    private String makeReadable(String settings) {
        String[] arr = settings.split("\n");
        StringBuilder readableString = new StringBuilder();
        for (String s : arr) {
            String[] temp = s.split(":");
            readableString.append(makeColor(temp[0],Colors.WHITE)).append(":").append(makeColor(temp[1],Colors.RED)).append("\n");
        }
        return readableString.toString();
    }
}



/*
        try {
            Code aa = new Cod
            Code128 barcode = new Code128();
            barcode.setData(suffix + data);
            barcode.setX(2);
            barcode.setY(60);
            barcode.setBarcodeWidth(290);
            barcode.setBarcodeHeight(100);

            barcode.setLeftMargin(0);
            barcode.setRightMargin(0);
            barcode.drawBarcode("target/" + suffix + data + ".png");

            barcode.setShowText(true);
            barcode.setTextFont(new Font("Arial", Font.BOLD, 18));
            barcode.setTextMargin(8);
        } catch (Exception e) {
            e.printStackTrace();
        }
 */