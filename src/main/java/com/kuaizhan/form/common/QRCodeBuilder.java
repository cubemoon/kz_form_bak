package com.kuaizhan.form.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * modified code from <a href="https://github.com/skrymer/qrbuilder">here</a>,
 * reference <a href="http://blog.csdn.net/lcx46/article/details/13626841">here</a> and 
 * <a href="http://www.cnblogs.com/tuyile006/p/3416008.html">here</a>
 * @author Ryan Chen
 *
 */
public class QRCodeBuilder {
	private String data;
	private Boolean verify;
	private Integer width, height;
	private List<QRCodeDecorator> decorators;

	public QRCodeBuilder() {
		verify = true;
	}
	
	public QRCodeBuilder(boolean verify) {
		this.verify = verify;
	}

	public QRCodeBuilder newQRCode() {
		return this;
	}

	public QRCodeBuilder and() {
		return this;
	}

	public QRCodeBuilder doVerify(Boolean doVerify) {
		this.verify = doVerify;

		return this;
	}

	public QRCodeBuilder withData(String data) {
		this.data = data;

		return this;
	}

	public QRCodeBuilder withSize(Integer width, Integer height) {
		validateSize(width, height);

		this.width = width;
		this.height = height;

		return this;
	}

	public QRCodeBuilder decorate(QRCodeDecorator decorator) {
		if (decorators == null) {
			decorators = new LinkedList<QRCodeDecorator>();
		}

		decorators.add(decorator);

		return this;
	}

	public BufferedImage toBufferedImage() {
		BufferedImage qrcode = encode();
		qrcode = decorate(qrcode);
		verifyQRCode(qrcode);

		return qrcode;
	}

	public File toFile(String fileName, String fileFormat) throws IOException {
		if (fileName == null || fileName.isEmpty())
			throw new IllegalArgumentException("File name should to be specified");

		if (fileFormat == null || fileFormat.isEmpty())
			throw new IllegalArgumentException("File format should be specified");

		File imageFile = new File(fileName);

		ImageIO.write(toBufferedImage(), fileFormat, imageFile);

		return imageFile;
	}


	private void validateSize(Integer width, Integer height) {
		if (width <= 0)
			throw new IllegalArgumentException("Width is to small should be > 0 is " + width);

		if (height <= 0)
		    throw new IllegalArgumentException("Height is to small should be > 0 is " + height);
	}

	private void verifyQRCode(BufferedImage qrcode) {
		if (verify) {
			try {
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(
						qrcode)));

				Result readData = new QRCodeReader().decode(binaryBitmap, getDecodeHints());

				if (!readData.getText().equals(this.data)) {
					throw new IllegalStateException("The data contained in the qrcode is as expected: " + this.data
							+ " actual: " + readData);
				}
			} catch (NotFoundException|ChecksumException|FormatException nfe) {
				throw new IllegalStateException("The data contained in the qrcode is not readable", nfe);
			}  
		}
	}

	private Map<EncodeHintType, Object> getEncodeHints() {
		Map<EncodeHintType, Object> encodeHints = new HashMap<EncodeHintType, Object>();
		encodeHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		encodeHints.put(EncodeHintType.CHARACTER_SET, "utf-8"); 
		encodeHints.put(EncodeHintType.MARGIN, 0);
		return encodeHints;
	}

	private Map<DecodeHintType, Object> getDecodeHints() {
		Map<DecodeHintType, Object> decodeHints = new HashMap<DecodeHintType, Object>();
		decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

		return decodeHints;
	}

	private BufferedImage encode() {
		BufferedImage qrcode;

		try {
			BitMatrix matrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, this.width, this.height,
					getEncodeHints());
			qrcode = MatrixToImageWriter.toBufferedImage(matrix);
		} catch (Exception e) {
			throw new RuntimeException("QRCode could not be generated", e.getCause());
		}

		return qrcode;
	}

	private BufferedImage decorate(BufferedImage qrcode) {
		if (decorators != null) {
			for (QRCodeDecorator decorator : decorators) {
				qrcode = decorator.decorate(qrcode);
			}
		}

		return qrcode;
	}

	interface QRCodeDecorator {

		public BufferedImage decorate(BufferedImage qrcode);
	}

	public static class ColoredQRCode implements QRCodeDecorator {
		private Color color;

		/**
		 * @param color
		 */
		private ColoredQRCode(Color color) {
			this.color = color;
		}

		public static QRCodeDecorator colorizeQRCode(Color color) {
			return new ColoredQRCode(color);
		}

		public BufferedImage decorate(BufferedImage qrcode) {
			FilteredImageSource prod = new FilteredImageSource(qrcode.getSource(), new QRCodeRGBImageFilter());

			Image image = Toolkit.getDefaultToolkit().createImage(prod);
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2 = bufferedImage.createGraphics();
		    g2.drawImage(image, 0, 0, null);
		    g2.dispose();

		    return bufferedImage;
			
		}

		private class QRCodeRGBImageFilter extends RGBImageFilter {

			public int filterRGB(int x, int y, int rgb) {
				if (rgb == Color.black.getRGB())
					return color.getRGB();

				return rgb;
			}
		}
	}

	public static class ImageOverlay implements QRCodeDecorator {

		private BufferedImage overlay;
		private Float overlayToQRCodeRatio, overlayTransparency;

		public ImageOverlay(BufferedImage overlay, Float overlayTransparency, Float overlayToQRCodeRatio) {
			if (overlay == null)
				throw new IllegalArgumentException("Overlay is required");

			this.overlay = overlay;

			if (overlayTransparency == null) {
				this.overlayTransparency = 1f;
			} else {
				this.overlayTransparency = overlayTransparency;
			}

			if (overlayToQRCodeRatio == null) {
				this.overlayToQRCodeRatio = 0.2f;
			} else {
				this.overlayToQRCodeRatio = overlayToQRCodeRatio;
			}
		}


		public BufferedImage decorate(BufferedImage qrcode) {
			BufferedImage scaledOverlay = scaleOverlay(qrcode);

			Integer deltaHeight = qrcode.getHeight() - scaledOverlay.getHeight();
			Integer deltaWidth = qrcode.getWidth() - scaledOverlay.getWidth();

			BufferedImage combined = new BufferedImage(qrcode.getWidth(), qrcode.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = (Graphics2D) combined.getGraphics();
			g2.drawImage(qrcode, 0, 0, null);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayTransparency));
			g2.drawImage(scaledOverlay, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), null);

			return combined;
		}


		private BufferedImage scaleOverlay(BufferedImage qrcode) {
			Integer scaledWidth = Math.round(qrcode.getWidth() * overlayToQRCodeRatio);
			Integer scaledHeight = Math.round(qrcode.getHeight() * overlayToQRCodeRatio);

			BufferedImage imageBuff = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics g = imageBuff.createGraphics();
			g.drawImage(overlay.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH), 0, 0,
					null, null);
			g.dispose();

			return imageBuff;
		}
	}
}