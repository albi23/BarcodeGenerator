package photo;

import barcode.Colors;
import control.Control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class PhotosControl extends Control {

    private static final int A3_WIDTH = 842;
    private static final int A3_HEIGHT = 1191;
    private static final int A4_WIDTH = 842;
    private static final int A4_HEIGHT = 595;
    private static final int A5_WIDTH = 420;
    private static final int A5_HEIGHT = 595;
    private Scanner scanner;


    public PhotosControl(Scanner scanner) {
        this.scanner = scanner;
        managePhotos();
    }

    private void mergePhotos() {
        File folder = new File("target/");
        List<File> listOfFiles = Arrays.asList(folder.listFiles());

        Collections.sort(listOfFiles);
        for (File file : listOfFiles) {
            System.out.println(file.getName());
        }
        boolean nextTo = false;
        assert listOfFiles != null;
        for (int i = 1; i < listOfFiles.size(); i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                mergeImages(listOfFiles.get(0).getName(), listOfFiles.get(i).getName(), "target/", listOfFiles.get(0).getName(), nextTo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mergeImages(String firstPhotoName, String secondPhotoName, String pathToImages, String resultName) throws IOException {
        mergeImages(firstPhotoName, secondPhotoName, pathToImages, resultName, false);
    }

    private void mergeImages(String firstPhotoName, String secondPhotoName, String pathToImages) throws IOException {
        mergeImages(firstPhotoName, secondPhotoName, pathToImages, null, false);
    }

    private void mergeImages(String firstPhotoName, String secondPhotoName, String pathToImages, String resultName, boolean mergeNexTo) throws IOException {
        BufferedImage image = ImageIO.read(new File(pathToImages, firstPhotoName));
        BufferedImage overlay = ImageIO.read(new File(pathToImages, secondPhotoName));
        mergeImages(image, overlay, mergeNexTo, pathToImages, resultName);
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

    private void showAvailableFormatsToMerge() {
        System.out.println(makeColor("\n Choose merge format:  1 : .GIF \n 2 : .JPG \n 3 : .PNG \n", Colors.WHITE));

    }// /home/albert/IdeaProjects/Barcode/target

    private void managePhotos() {
        while (true) {
            System.out.println(makeColor("\n 1 : Choose path with images \n 2 : Back", Colors.WHITE));
            printOptionMessage();
            switch (scanner.nextInt()) {
                case 1: {
                    System.out.print(makeColor("Enter the path to the photos : ", Colors.PINK));
                    List<File> files = collectFile(validateInputPath(scanner.next()));
                    if (files != null) {
                        printImageChooseMessage();
                        validateChoosedImages(scanner.next());
                    }
                    break;
                }
                case 2:
                    return;
            }
        }
    }


    private File[] validateInputPath(String path) {
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

    //choosed
    private boolean validateChoosedImages(String input) {
        String[] inputData = input.trim().split(",");
        for (String s : inputData) {
            if (!s.contains("-")) {
                try {
                    System.out.println(Integer.parseInt(s));
                }catch (NumberFormatException ex){}
            } else {
                System.out.println(s);
            }
        }
        return true;
    }

    private void fillWithComma(String s){
        StringBuilder builder = new StringBuilder();
        int start;
        for (start = 0; start < s.length(); start++)
            if (s.charAt(start) > 47 && s.charAt(start) <= 57) break;

        builder.append(s.charAt(start));

        for (int i = start+1; i < s.length(); i++) {
            if ((s.charAt(i) > 47 && s.charAt(i) <= 57)){ // jeśli jest cyfrą
                builder.append(s.charAt(i)); // dodaj tą cyfre
                // ttuaj jeśli następny znak nie jest cyfrą to przecinek albo myślink
                for (int j = i+1; j < s.length(); j++) {
                    if ((s.charAt(j) > 47 && s.charAt(j) <= 57) || s.charAt(j)  == 4){

                    }
                    i++;
                }
            }else if (s.charAt(i) == 44 ){
                builder.append('-');
            }
        }
    }
}
