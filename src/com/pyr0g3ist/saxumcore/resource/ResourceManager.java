package com.pyr0g3ist.saxumcore.resource;

import com.pyr0g3ist.saxumcore.exception.SaxumException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ResourceManager {

    private static ResourceManager sharedInstance;

    private ClassLoader loader = getClass().getClassLoader();

    private ResourceManager() {
    }

    private final GraphicsConfiguration graphicsConfiguration
            = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

    private final HashMap<String, BufferedImage> imageMap = new HashMap<>();
    private final HashMap<String, ArrayList<BufferedImage>> animationMap = new HashMap<>();

    public void addImage(String name, String path) throws SaxumException {
        try {
            BufferedImage image = ImageIO.read(loader.getResourceAsStream(path));
            imageMap.put(name, convertToCompatibleImage(image));
        } catch (IOException ex) {
            throw new SaxumException("Error loading image: " + ex.getMessage());
        }
    }

    public BufferedImage getImage(String name) {
        return imageMap.get(name);
    }

    public void addAnimation(String name, String path) throws SaxumException {
        URL url = loader.getResource(path);
        if (url == null) {
            throw new SaxumException("Error, missing animation folder.");
        } else {
            try {
                ArrayList<BufferedImage> images = new ArrayList<>();
                File animationFolder = new File(url.toURI());
                for (File frameFile : animationFolder.listFiles()) {
                    images.add(convertToCompatibleImage(ImageIO.read(frameFile)));
                }
                animationMap.put(name, images);
            } catch (URISyntaxException | IOException ex) {
                throw new SaxumException("Error loading animation: " + ex.getMessage());
            }
        }
    }

    public ArrayList<BufferedImage> getAnimationFrames(String name) {
        return animationMap.get(name);
    }

    private BufferedImage convertToCompatibleImage(BufferedImage source) {
        BufferedImage compatableImage = graphicsConfiguration.createCompatibleImage(
                source.getWidth(),
                source.getHeight(),
                Transparency.TRANSLUCENT);
        compatableImage.getGraphics().drawImage(source, 0, 0, null);
        return compatableImage;
    }

    public void setClassLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public static ResourceManager getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ResourceManager();
        }
        return sharedInstance;
    }

}
