package barcode;

import java.io.Serializable;

public class Settings implements Serializable {

    private String suffix;
    private String path;
    private String format;
    private String fillSign;
    private String outputName;
    private int id;
    private int lengthCode;
    private float height;
    private float width;
    private float lMargin;
    private float rMargin;

    //default serialVersion id
    private static final long serialVersionUID = 1L;

    public Settings(String suffix, String path, String format, String fillSign,
                    String outputName, int id, int lengthCode, float height,
                    float width, float lMargin, float rMargin) {
        this.suffix = suffix;
        this.path = path;
        this.format = format;
        this.fillSign = fillSign;
        this.outputName = outputName;
        this.id = id;
        this.lengthCode = lengthCode;
        this.height = height;
        this.width = width;
        this.lMargin = lMargin;
        this.rMargin = rMargin;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPath() {
        return path;
    }

    public String getFormat() {
        return format;
    }

    public String getFillSign() {
        return fillSign;
    }

    public String getOutputName() {
        return outputName;
    }

    public int getId() {
        return id;
    }

    public int getLengthCode() {
        return lengthCode;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getlMargin() {
        return lMargin;
    }

    public float getrMargin() {
        return rMargin;
    }


}
