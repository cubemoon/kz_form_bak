package com.kuaizhan.form.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {
	
	private final static Logger log = LoggerFactory.getLogger(ImageUtil.class);

	
	public static BufferedImage makeRoundedCorner(BufferedImage image, float cornerRadius) {
		int w = image.getWidth();
		int h = image.getHeight();
		if (log.isDebugEnabled()) {
			log.debug("makeRoundedCorner"+w+h);
		}
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		// This is what we want, but it only does hard-clipping, i.e. aliasing
		// g2.setClip(new RoundRectangle2D ...)

		// so instead fake soft-clipping by first drawing the desired clip shape
		// in fully opaque white with antialiasing enabled...
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, w * cornerRadius, h * cornerRadius));

		// ... then compositing the image on top,
		// using the white shape from above as alpha source
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}
	
	public static BufferedImage addWhiteContour(BufferedImage image, float rate, float cornerRadius) {
		int size = (int) (image.getWidth()*rate);
		int w = image.getWidth()+size*2;
		int h = image.getHeight()+size*2;
		if (log.isDebugEnabled()) {
			log.debug("addWhiteContour"+w+h+size);
		}
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		// This is what we want, but it only does hard-clipping, i.e. aliasing
		// g2.setClip(new RoundRectangle2D ...)

		// so instead fake soft-clipping by first drawing the desired clip shape
		// in fully opaque white with antialiasing enabled...
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, w * cornerRadius, h * cornerRadius));

		// ... then compositing the image on top,
		// using the white shape from above as alpha source
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, size, size, null);

		g2.dispose();

		return output;
	}

	public static byte[] buildQrcode(BufferedImage icon, String qrcodeData) throws IOException{
//		icon = makeRoundedCorner(icon, 0.35f);
//		icon = addWhiteContour(icon, 0.08f, 0.35f);
		BufferedImage qrcode = new QRCodeBuilder(true).newQRCode().withSize(250, 250).withData(qrcodeData)
//				.decorate(new ImageOverlay(icon, null, 0.36f))
				.toBufferedImage();
		
		ByteArrayOutputStream image = new ByteArrayOutputStream();
		ImageIO.write(qrcode, "PNG", image);
		return image.toByteArray();
	}
}
