package org.owlmail.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2011-12-29
 */
public class ImageUtil {

	/**
	 * ClassLoader获得BufferedImage,注意和getClass().getResource()的路径不同 最前面不带/
	 * 
	 * @param imgPath
	 * @return Image 返回类型
	 */
	public static Image getImage(String imgPath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(ClassLoader.getSystemResource(imgPath));
		} catch (IOException e) {
			System.out.println("Image not found: " + imgPath);
		}
		return img;
	}

	/**
	 * 
	 * @param imgPath
	 * @return
	 */
	public static ImageIcon getImageIcon(String imgPath) {
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ClassLoader.getSystemResource(imgPath));
			return icon;
		} catch (Exception e) {
			System.out.println("Image not found： " + imgPath);
		}
		return icon;
	}

	/**
	 * 
	 * @param icon
	 * @return
	 */
	public static Image iconToImage(Icon icon) {
		if (icon instanceof ImageIcon) {
			return ((ImageIcon) icon).getImage();
		} else {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			BufferedImage image = new BufferedImage(w, h, 2);
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}

	/**
	 * 
	 * @param icon
	 * @param color
	 * @return
	 */
	public static ImageIcon createDyedIcon(ImageIcon icon, Color color) {
		if (color == null) {
			return icon;
		} else {
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			BufferedImage bi = new BufferedImage(iconWidth, iconHeight, 2);
			Graphics2D g2d = bi.createGraphics();
			icon.paintIcon(null, g2d, 0, 0);
			g2d.dispose();
			Image dyedImage = createDyedImage(bi, color);
			return new ImageIcon(dyedImage);
		}
	}

	/**
	 * 
	 * @param image
	 * @param color
	 * @return
	 */
	public static Image createDyedImage(Image image, Color color) {
		if (color == null) {
			return image;
		}
		if (image != null) {
			int w = image.getWidth(null);
			int h = image.getHeight(null);
			int pixels[] = new int[w * h];
			PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
			try {
				pg.grabPixels();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return null;
			}
			BufferedImage bi = new BufferedImage(w <= 1 ? 1 : w,
					h <= 1 ? 1 : h, 2);
			for (int i = 0; i < pixels.length; i++) {
				int pixel = pixels[i];
				int row = i / w;
				int col = i % w;
				if (color != null && pixel != 0) {
					pixel = color.getRGB();
				}
				bi.setRGB(col, row, pixel);
			}
			return bi;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param icon
	 * @return
	 */
	public static Icon createMovedIcon(Icon icon) {
		return createMovedIcon(icon, 1, 1);
	}

	/**
	 * 
	 * @param icon
	 * @param offsetX
	 * @param offsetY
	 * @return
	 */
	public static Icon createMovedIcon(final Icon icon, final int offsetX,
			final int offsetY) {
		return new Icon() {
			public void paintIcon(Component c, Graphics g, int x, int y) {
				icon.paintIcon(c, g, x + offsetX, y + offsetY);
			}

			public int getIconWidth() {
				return icon.getIconWidth();
			}

			public int getIconHeight() {
				return icon.getIconHeight();
			}
		};
	}

	/**
	 * 
	 * @param imgPath
	 * @return
	 */
	public static TexturePaint createTexturePaint(String imgPath) {
		ImageIcon icon = getImageIcon(imgPath);
		int imageWidth = icon.getIconWidth();
		int imageHeight = icon.getIconHeight();
		BufferedImage bi = new BufferedImage(imageWidth, imageHeight, 2);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(icon.getImage(), 0, 0, null);
		g2d.dispose();
		return new TexturePaint(bi,
				new Rectangle(0, 0, imageWidth, imageHeight));
	}
}
