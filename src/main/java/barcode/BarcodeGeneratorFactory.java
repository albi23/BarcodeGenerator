package barcode;

import com.onbarcode.barcode.*;

public class BarcodeGeneratorFactory {

    public static AbstractBarcode codeType(int chasedOption){
        switch (chasedOption){
            case 1: return new Code128();
            case 2: return new Code93();
            case 3: return new Code39();
            case 4: return new Code25();
            case 5: return new Code11();
            case 6: return new QRCode();
            case 7: return new PDF417();
            case 8: return new DataMatrix();
        }
        return null;
    }
}


/*
Jaki barcode
Jaki format
Jaki rozmiar
Ile barcodów
Od jakiego do jakiego numeru
Uidu ?
Randomowe ?
Jaki typ Barcodu ?
 */