package dials;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.imageio.*;
import javax.swing.*;

public class RotateImage {
    private static final String userDir = System.getProperty("user.dir");
    private static final String separator = System
            .getProperty("file.separator");

    private static final String userPath = userDir + separator + "images"
            + separator;
    private static final String NEEDLE = userPath + "gauge_needle.png";

    private static final String GAUGE_BLANK = userPath + "gauge_blank.png";

    private static Boolean DISPLAY_RANGE_VALUES = true;
    private static final int Y_VAL_POS = 117; // Defines the vertical
                                              // position to display the value
                                              // on the gauge
    private BufferedImage combined;
//    private static JPanel cp = new JPanel(new GridLayout(2, 2));
//    private static JFrame f = new JFrame("RotateImage");
    


    public static void main(String[] argc) throws IOException {
        Calendar cal = new GregorianCalendar();
        long t0=cal.getTimeInMillis();
        System.out.println(t0);
        int value = 9000;
        int rangeBottom = 0;
        int rangeTop = 10000;
        Boolean displayValue = true;
        new RotateImage(value, displayValue, rangeBottom, rangeTop);
        System.out.println(new GregorianCalendar().getTimeInMillis() - t0);
    }
    
    public BufferedImage getRotatedImage(){
        return combined;
    }
    
    public RotateImage(double value, Boolean displayValue, int rangeBottom, int rangeTop) throws IOException {
        BufferedImage needle;
        needle = ImageIO.read(new File(NEEDLE));
        BufferedImage gaugeBlank = ImageIO.read(new File(GAUGE_BLANK));

        GraphicsConfiguration gc = getDefaultConfiguration();
        Double angle;
        // Determine angle to rotate the needle based on the range and value
        angle = (double) ((((value - rangeBottom) * 260.) / (rangeTop - rangeBottom)) + 230.);
        angle += 130.;
        angle %= 360;

        System.out.println("VALUE: " + value + " ANGLE: " + angle + "(deg)  "
                + Math.toRadians(angle) + "(rad)");

        BufferedImage rotatedimage = doRotateImage(needle, angle, gc);

        int w = Math.max(gaugeBlank.getWidth(), rotatedimage.getWidth());
        int h = Math.max(gaugeBlank.getHeight(), rotatedimage.getHeight());
        combined = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);

        Graphics g = combined.getGraphics();
        g.drawImage(gaugeBlank, 0, 0, null);
        g.drawImage(rotatedimage, 0, 0, null);

        int xPos = 0;
        if (displayValue) {
            if (Math.abs(value) < 10)
                xPos = 80; // 1 Digit
            else if (Math.abs(value) < 100)
                xPos = 76; // 2 Digit
            else if (Math.abs(value) < 1000)
                xPos = 74; // 3 Digits
            else if (Math.abs(value) < 10000)
                xPos = 70;
            else if (Math.abs(value) >= 10000)
                xPos = 66;
            if (value < 0)
                xPos = xPos - 3;

            if (DISPLAY_RANGE_VALUES == true) {
                // Write Min/Max Values onto Gauge
                Graphics g1 = combined.getGraphics();
                g1.setFont(g1.getFont().deriveFont(10f));
                g1.setColor(Color.BLACK);
                g1.drawString(Integer.toString(rangeBottom), 42, 130);
                g1.drawString(Integer.toString(rangeTop), 103, 130);
                g1.drawString(Double.toString(value), xPos, Y_VAL_POS);
                g1.dispose();
                ImageIO.write(combined, "png", new File("GaugeIMG.png"));
            }

            // Prevent Gauge from Looping
            if (value > rangeTop)
                value = rangeTop;

            if (value < rangeBottom)
                value = rangeBottom;

            ImageIO.write(combined, "PNG", new File(userPath, "combined.png"));
            JPanel cp = new JPanel(new GridLayout(2, 2));
            display(combined, cp);
            display(combined, cp);
        }
    }

    private BufferedImage doRotateImage(BufferedImage image, double angle,
            GraphicsConfiguration gc) {

        System.out.println(" ANGLE: " + angle + "(deg)  "
                + Math.toRadians(angle) + "(rad)");

        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math
                .floor(h * cos + w * sin);
        int transparency = image.getColorModel().getTransparency();
        BufferedImage renderableImage = gc.createCompatibleImage(neww, newh,
                transparency);
        Graphics2D g = renderableImage.createGraphics();
        // g.translate((neww-w)/2, (newh-h)/2);
        g.translate(-33, -33);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(image, null);
        return renderableImage;
    }

    private GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    private void display(BufferedImage image, JPanel cp) {
        addImage(cp, image, "dial");
        JFrame f = new JFrame("RotateImage");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(cp);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void addImage(Container cp, BufferedImage im, String title) {
        JLabel lbl = new JLabel(new ImageIcon(im));
        lbl.setBorder(BorderFactory.createTitledBorder(title));
        cp.add(lbl);
    }
}