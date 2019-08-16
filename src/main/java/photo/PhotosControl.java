package photo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PhotosControl {

    private static final int A3_WIDTH = 842;
    private static final int A3_HEIGHT = 1191;
    private static final int A4_WIDTH = 842;
    private static final int A4_HEIGHT = 595;
    private static final int A5_WIDTH = 420;
    private static final int A5_HEIGHT = 595;





    private void mergePhotos() {
        File folder = new File("target/");
        List<File> listOfFiles = Arrays.asList(folder.listFiles());

        Collections.sort(listOfFiles);
        for (File file: listOfFiles) {
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

}
