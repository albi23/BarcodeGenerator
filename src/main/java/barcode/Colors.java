package barcode;

public enum Colors {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    WHITE,
    PINK;

    private String getEscape(Colors color) {
        switch (color) {
            case RED:
                return "\u001b[38;5;160m";
            case GREEN:
                return "\u001b[38;5;82m";
            case YELLOW:
                return "\u001b[33m";
            case BLUE:
                return "\u001b[38;5;26m";
            case PINK:
                return "\u001b[38;5;205m";
            default:
                return "\u001b[38;5;255m"; //WHITE
        }
    }

    public String makeColor(Colors color, String data, boolean bold) {
        String result = getEscape(color) + data + "\u001b[0m";
        if (!bold) return result;
        return "\u001b[1m" + result;
    }
}
