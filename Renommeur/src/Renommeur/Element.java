package Renommeur;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/**
 * 
 * @Description 元件
 * @Author zhangzuoqiang
 * @Date 2012-3-5
 */
public class Element extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int Numero;
	public static final int SIZE = Photo.SIZE + 25;
	private static final ResourceBundle locale = ResourceBundle
			.getBundle("Renommeur/resources/locale");
	private TitledBorder b;
	private MatteBorder mbdefaut;
	private Photo p;

	public Element(Photo p, AppFrame feor) {
		super();
		this.p = p;
		setPreferredSize(new Dimension(SIZE, SIZE));
		b = new TitledBorder(p.getName(feor.getVecPhotos().dontDel(),
				feor.isNumPhotos()));
		b.setTitlePosition(TitledBorder.CENTER);
		if (p.isDel()) {
			b.setTitleColor(Color.RED);
		} else if (p.hasChanged()) {
			b.setTitleColor(Color.blue);
		} else {
			b.setTitleColor(Color.gray);
		}
		if (feor.getVecPhotos().getCurrent() == p) {
			mbdefaut = new MatteBorder(2, 2, 2, 2, Color.orange);
		} else {
			mbdefaut = null;
		}
		b.setBorder(mbdefaut);
		setBorder(b);

		setHorizontalAlignment(JLabel.CENTER);
		setVerticalAlignment(JLabel.CENTER);
		if (p.hasMini()) {
			this.setIcon(p.getThumbnail());
		} else {
			this.setText(locale.getString("CHARGEMEMENT."));
		}
		Numero = p.getNb();
	}

	public void changeBorderSrc() {
		b.setBorder(new MatteBorder(2, 2, 2, 2, Color.magenta));
		repaint();
	}

	public void changeBorderDest() {
		b.setBorder(new MatteBorder(2, 2, 2, 2, Color.yellow));
		repaint();
	}

	public void resetBorder() {
		b.setBorder(mbdefaut);
		repaint();
	}

	public int getNum() {
		return Numero;
	}

	public void setNum(int i) {
		Numero = i;
	}

	public boolean hasImage() {
		return p.hasMini();
	}

	public void readImage() {
		setText("");
		setIcon(p.getThumbnail());
	}

	public Photo getPhoto() {
		return p;
	}

}
