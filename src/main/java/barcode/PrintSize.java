package barcode;

public enum PrintSize {
    A3,A4,A5;

    private static final int A3_WIDTH = 842;
    private static final int A3_HEIGHT = 1191;
    private static final int A4_WIDTH = 842;
    private static final int A4_HEIGHT = 595;
    private static final int A5_WIDTH = 420;
    private static final int A5_HEIGHT = 595;

    public int getWidth(PrintSize size){
        switch (size){
            case A3:
                return A3_WIDTH;
            case A4:
                return A4_WIDTH;
            case A5:
                return A5_WIDTH;
        }
        return 0;
    }

    public int getHeight(PrintSize size){
        switch (size){
            case A3:
                return A3_HEIGHT;
            case A4:
                return A4_HEIGHT;
            case A5:
                return A5_HEIGHT;
        }
        return 0;
    }
}
