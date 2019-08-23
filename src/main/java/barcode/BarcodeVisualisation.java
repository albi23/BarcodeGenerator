package barcode;

import com.onbarcode.barcode.AbstractBarcode;

import java.io.*;
import java.util.UUID;

public class BarcodeVisualisation  {

    private AbstractBarcode abstractBarcode;
    private String suffix = "Test-";
    private String path;
    private String format;
    private String fillSign;
    private String outputName;
    private int id;
    private int lengthCode;

    BarcodeVisualisation(AbstractBarcode abstractBarcode) {
        this.abstractBarcode = abstractBarcode;
        this.abstractBarcode.setAutoResize(true);
        if (!loadPreviousSettings()){
            initData();
        }
    }

    private void initData(){
        this.format = ".png";
        this.path = "target/";
        this.fillSign = "0";
        this.lengthCode = 5;
        this.id = 0;
        this.outputName = this.getRandomData().toString();
        this.setHeight(100f);
        this.setWidth(200f);
        this.setLeftMargin(0f);
        this.setRightMargin(0f);
    }


    public void setWidth(float width) {
        this.abstractBarcode.setBarcodeWidth(width);
    }

    public void setHeight(float height) {
        this.abstractBarcode.setBarcodeHeight(height);
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public UUID getRandomData() {
        return UUID.randomUUID();
    }

    public void setLeftMargin(float margin) {
        this.abstractBarcode.setRightMargin(margin);
    }

    public void setRightMargin(float margin) {
        this.abstractBarcode.setLeftMargin(margin);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setOutputName(String name) {
        this.outputName = name;
    }

    public String getOutputName() { return outputName; }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() { return this.id; }

    public void setFillSign(String fillSign) {
        this.fillSign = fillSign;
    }

    public void setLengthCode(int lengthCode) {
        this.lengthCode = lengthCode;
    }

    public void setData(String data){
        this.abstractBarcode.setData(data);
    }

    public void printCode(boolean randomData) throws Exception {
        if (!randomData)this.abstractBarcode.setData(this.suffix + this.getFormatOfPrint());
        this.abstractBarcode.drawBarcode(this.path + this.outputName + this.format);
    }

    public String getActualSettings() {
        StringBuilder setting = new StringBuilder();
        setting.append(" Height         : ").append(this.abstractBarcode.getBarcodeHeight()).append("\n")
                .append(" Width          : ").append(this.abstractBarcode.getBarcodeWidth()).append("\n")
                .append(" Left margin    : ").append(this.abstractBarcode.getLeftMargin()).append("\n")
                .append(" Right margin   : ").append(this.abstractBarcode.getRightMargin()).append("\n")
                .append(" Output format  : ").append(this.format.toUpperCase()).append("\n")
                .append(" Code label     : ").append(this.suffix).append(this.getFormatOfPrint()).append("\n")
                .append(" Suffix         : ").append(this.suffix).append("\n")
                .append(" Save path      : ").append(this.path).append("\n")
                .append(" Output name    : ").append(this.outputName).append(this.format).append("\n")
                .append(" Actual format  : ").append(this.getFormatOfPrint()).append("\n")
                .append(" Filling sign   : ").append(this.fillSign).append("\n")
                .append(" Length of sign : ").append(this.lengthCode).append("\n");
        return setting.toString();
    }

    private String getFormatOfPrint() {
        return String.format("%"+lengthCode + "d",this.id).replaceAll(" ",this.fillSign);
    }

    public void saveSettings(String path, String outputName) throws IOException {

        Object currentSettings =  new Settings(
                this.suffix,this.path, this.format, this.fillSign, this.outputName, this.id,
                this.lengthCode,this.abstractBarcode.getBarcodeHeight(), this.abstractBarcode.getBarcodeWidth(),
                this.abstractBarcode.getLeftMargin(), this.abstractBarcode.getRightMargin()
        );

        FileOutputStream fos = new FileOutputStream(path+outputName);
        ObjectOutputStream outStream = new ObjectOutputStream(fos);
        outStream.writeObject(currentSettings);
        outStream.close();
    }

    private boolean loadPreviousSettings(){

        Settings settings;

        try {
            FileInputStream fis = new FileInputStream("target/settings");
            ObjectInputStream ois = new ObjectInputStream(fis);
            settings = (Settings) ois.readObject();
            ois.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        this.suffix = settings.getSuffix();
        this.path = settings.getPath();
        this.format = settings.getFormat();
        this.fillSign = settings.getFillSign();
        this.outputName = settings.getOutputName();
        this.id = settings.getId();
        this.lengthCode = settings.getLengthCode();
        this.abstractBarcode.setBarcodeHeight(settings.getHeight());
        this.abstractBarcode.setBarcodeWidth(settings.getWidth());
        this.abstractBarcode.setLeftMargin(settings.getlMargin());
        this.abstractBarcode.setRightMargin(settings.getrMargin());
        return true;
    }
}
