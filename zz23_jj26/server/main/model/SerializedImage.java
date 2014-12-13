package zz23_jj26.server.main.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public class SerializedImage implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 133482392128842583L;
	private BufferedImage image;
    
    public SerializedImage(BufferedImage image){
    	this.image = image;
    }
    
    public BufferedImage getImage(){
    	return image;
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        ImageOutputStream imgOut = ImageIO.createImageOutputStream(out);
        ImageIO.write(image, "png", imgOut); // png is lossless
        imgOut.close();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	ImageInputStream imgIn = ImageIO.createImageInputStream(in);
    	image = ImageIO.read(imgIn);
    	
//        in.defaultReadObject();
//        final int imageCount = in.readInt();
//        ImageInputStream imgIn = ImageIO.createImageInputStream(in);
//        System.out.println("image count is " + imageCount);
//        images = new ArrayList<BufferedImage>(imageCount);
//        for (int i=0; i< imageCount; i++) {
//            images.add(ImageIO.read(imgIn));
//        }
    }
}
