package photo;

import barcode.Colors;
import barcode.PrintSize;
import control.Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class PhotosControl extends Control {

    private Scanner scanner;
    private String path;
    private HashSet<Integer> validated;


    public PhotosControl(Scanner scanner) {
        this.scanner = scanner;
        managePhotos();
    }

    private void mergePhotos(List<String> filesName, String outputFormat, PrintSize size) {

//        BufferedImage outImage = new BufferedImage(PrintSize.getWidth(size), PrintSize.getHeight(size), BufferedImage.TYPE_INT_ARGB);
        String firstImgName = filesName.get(0);
        int currentWidth, currentHeight;
        for (int i = 0; i < filesName.size(); i++) {
            try {
                BufferedImage overlay = null;
                BufferedImage image = (i == 0) ? ImageIO.read(new File(path, firstImgName)) : ImageIO.read(new File(path, "merged" + outputFormat));
                if (i + 1 < filesName.size()) overlay = ImageIO.read(new File(path, filesName.get(i+1)));
                if (overlay != null) {
                    currentHeight = image.getHeight() + overlay.getHeight();
                    if (currentHeight > PrintSize.getHeight(size))
                    mergeImages(image, overlay, false, path, "merged" + outputFormat);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectPhoto(List<String> filesName, String outputFormat, PrintSize size){

        int selectedWidth = PrintSize.getWidth(size);
        int selectedHeight = PrintSize.getHeight(size);
        int currentWidth = 0, currentHeight = 0, imgCounter = 0, maxWidth = 0;
        boolean printed = false;

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss.SSS");
        BufferedImage outputImg = new BufferedImage(selectedWidth, selectedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D outputImgGraphics = (Graphics2D) outputImg.getGraphics();
        try {
            while (imgCounter < filesName.size()) {

                BufferedImage image = ImageIO.read(new File(path, filesName.get(imgCounter)));
                if (image.getWidth() > selectedWidth || image.getHeight() > selectedHeight){
                    System.out.println(makeColor("Size of "+filesName.get(imgCounter)+" is to big for selected format "+size.toString(), Colors.RED));
                    imgCounter++;
                    continue;
                }
                if (currentHeight + image.getHeight() > selectedHeight) {
                    maxWidth = currentWidth + maxWidth;
                    currentHeight = 0;
                }
                if (currentHeight + image.getHeight() <= selectedHeight && maxWidth + image.getWidth() <= selectedWidth) {
                    outputImgGraphics.drawImage(image, maxWidth, currentHeight, null);
                }  else {
                    if (maxWidth + image.getWidth() > selectedWidth) {
                        outputImgGraphics.dispose();
                        ImageIO.write(outputImg, outputFormat.substring(1).toUpperCase(), new File(path, "Merged_" + size.toString() + "_" + formatter.format(new Date()) + outputFormat));
                        outputImg = new BufferedImage(selectedWidth, selectedHeight, BufferedImage.TYPE_INT_ARGB);
                        outputImgGraphics = (Graphics2D)outputImg.getGraphics();
                        currentHeight = currentWidth = maxWidth = 0;
                        printed = true;
                    }
                    outputImgGraphics.drawImage(image, maxWidth, currentHeight, null);
                }
                currentHeight += image.getHeight();
                currentWidth = Math.max(image.getWidth(), currentWidth);
                imgCounter++;
                if (imgCounter == filesName.size() && !printed) {
                    ImageIO.write(outputImg, outputFormat.substring(1).toUpperCase(), new File(path, "Merged_"  + size.toString() + "_"+ formatter.format(new Date()) + outputFormat));
                }
                printed = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void mergeImages(BufferedImage firstImage, BufferedImage secondImage, boolean mergeNexTo, String pathToImages, String resultName) throws IOException {
        int newWidth, newHeight;
        if (mergeNexTo) {
            newWidth = firstImage.getWidth() + secondImage.getWidth();
            newHeight = Math.max(firstImage.getHeight(), secondImage.getHeight());
        } else {
            newWidth = Math.max(firstImage.getWidth(), secondImage.getWidth());
            newHeight = firstImage.getHeight() + secondImage.getHeight();
        }

        BufferedImage combined = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(firstImage, 0, 0, null);

        if (mergeNexTo) {
            g.drawImage(secondImage, firstImage.getWidth(), 0, null);
        } else {
            g.drawImage(secondImage, 0, firstImage.getHeight(), null);
        }
        g.dispose();

        String nameFile = (resultName != null) ? resultName : "Merged_" + System.currentTimeMillis() + ".png";
        ImageIO.write(combined, "PNG", new File(pathToImages, nameFile));
    }


    private void managePhotos() {
        while (true) {
            System.out.println(makeColor("\n 1 : Choose path with images \n 2 : Back", Colors.WHITE));
            printOptionMessage();
            switch (scanner.nextInt()) {
                case 1: {
                    System.out.print(makeColor("Enter the path to the photos : ", Colors.PINK));
                    path = scanner.next();
                    List<File> files = collectFile(validateInputPath());
                    if (files != null) {
                        printImageChooseMessage();
                        validateChoosedImages(scanner.next(), files.size(), scanner.nextLine());

                        showOptions(files);
                    }
                    break;
                }
                case 2:
                    return;
            }
        }
    }


    private File[] validateInputPath() {
        File[] nameFiles = {};
        try {
            File newFile = new File(path);
            nameFiles = newFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.matches(".*(.gif|.jpg|.png)");
                }
            });

        } catch (Exception e) {
            System.out.println(makeColor("Entered path is incorrect.", Colors.RED));
        }
        return nameFiles;
    }

    private List<File> collectFile(File[] files) {
        if (files == null || files.length == 0) {
            System.out.println(makeColor("Entered path is not contains any photo.", Colors.RED));
            return null;
        }

        List<File> fileArray = Arrays.asList(files);
        printAllFiles(fileArray);
        return fileArray;
    }

    private void printAllFiles(List<File> files) {

        StringBuilder stringBuilder = new StringBuilder();
        int maxNumber = Integer.toString(files.size()).length();
        int maxName = findLongestName(files);

        for (int i = 0, j = 4; i < files.size(); i++) {
            stringBuilder.append(String.format("%" + maxNumber + "d. ", i + 1)).append(String.format("%-" + maxName + "s", files.get(i).getName())).append(" ");
            if (i == j) {
                stringBuilder.append("\n");
                j += 5;
            }
        }
        System.out.println(makeColor(stringBuilder.toString(), Colors.BLUE));
    }

    private int findLongestName(List<File> files) {
        int longestName = 0;
        for (File file : files)
            if (file.getName().length() > longestName) longestName = file.getName().length();

        return longestName;
    }

    private void printImageChooseMessage() {
        System.out.println(makeColor("\nChose which image will be merged.\n" +
                "You can choose only numbers e.g. 1, 15, 30 etc. or range  e.g. 1-5, 17-28 ", Colors.YELLOW));
    }


    private boolean validateChoosedImages(String input, int max, String... args) {
        StringBuilder builder = new StringBuilder();
        if (args != null)
            for (String s : args) builder.append(s);

        builder.insert(0, input);
        String[] inputData = fillWithComma(builder.toString()).split(",");
        validated = new LinkedHashSet<>();

        for (String s : inputData) {
            if (s.contains("-")) {
                String[] res = s.split("-");
                for (int i = Integer.parseInt(res[0]); i <= Integer.parseInt(res[1]); i++) {
                    if (i > max) return false;
                    else validated.add(i);
                }
            } else {
                validated.add(Integer.parseInt(s));
            }
        }

        return true;
    }

    private String fillWithComma(String s) {
        StringBuilder builder = new StringBuilder(" ");
        boolean markComma = false;
        char last = ' ';

        for (int i = 0; i < s.length(); i++) {
            if ((s.charAt(i) > 47 && s.charAt(i) <= 57)) { // is number
                last = builder.substring(builder.length() - 1).charAt(0);

                if (last > 47 && last <= 57 && markComma) { // space between number = ','
                    builder.append(",").append(s.charAt(i));
                } else {
                    builder.append(s.charAt(i));
                }
                markComma = false;
            } else if (s.charAt(i) == 45) {
                builder.append('-');
                markComma = false;
            } else {
                markComma = true;
            }
        }
        return builder.toString().substring(1);
    }

    private void showOptions(List<File> files) {
        List<String> filesNames = getFilesNames(files);
        String outputFormat = chooseOutputFormat();
        PrintSize outputSize = chooseOutputSize();
        connectPhoto(filesNames, outputFormat, outputSize);

    }

    private String chooseOutputFormat() {
        System.out.println(makeColor("\n Choose merge format: \n 1 : .GIF \n 2 : .JPG \n 3 : .PNG \n", Colors.WHITE));
        while (true) {
            switch (scanner.nextInt()) {
                case 1:
                    return ".gif";
                case 2:
                    return ".jpg";
                case 3:
                    return ".png";
                default: {
                    System.out.println(makeColor("Choose correct answer", Colors.RED));
                }
            }
        }
    }

    private PrintSize chooseOutputSize() {
        System.out.println(makeColor("\n Choose output size: \n 1 : A3 \n 2 : A4 \n 3 : A5 \n", Colors.WHITE));
        while (true) {
            switch (scanner.nextInt()) {
                case 1:
                    return PrintSize.A3;
                case 2:
                    return PrintSize.A4;
                case 3:
                    return PrintSize.A5;
                default: {
                    System.out.println(makeColor("Choose correct answer", Colors.RED));
                }
            }
        }
    }

    private List<String> getFilesNames(List<File> files) {
        List<String> fileNAme = new ArrayList<>();
        List<Integer> filesId = new ArrayList<>(validated);
        Collections.sort(filesId);
        filesId.forEach(i -> {
            fileNAme.add(files.get(i-1).getName());
        });
        return fileNAme;
    }
}
