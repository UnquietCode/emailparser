package Renommeur;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * @Description Manage Photo 标签页，照片浏览面板
 * @Author zhangzuoqiang
 * @Date 2012-3-5
 */
public class BrowsePane extends JPanel implements MouseListener,
		MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle locale = ResourceBundle
			.getBundle("Renommeur/resources/locale");

	private JScrollPane scrollPane;
	// 当前选中的缩略图
	private Element SelectedThumbnail;
	//
	private Element interm;
	//
	private Element depart;

	private boolean IsRefresh, drag;
	private AppFrame frame;
	private Element[] tab;
	private JPanel centre;
	private long old;

	public BrowsePane(AppFrame frame) {
		super();
		IsRefresh = false;
		this.frame = frame;
		SelectedThumbnail = null;
		old = System.currentTimeMillis();

		centre = new JPanel();
		scrollPane = new JScrollPane(centre);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(Photo.SIZE / 2);

		setLayout(new BorderLayout());
		add(scrollPane, "Center");

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (isShowing()) {
					RefreshAllImages();
				}
			}

			@Override
			public void componentShown(ComponentEvent e) {
				RefreshAllImages();
			}
		});

		scrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					@Override
					public void adjustmentValueChanged(AdjustmentEvent e) {
						ChargePrioritairementImages();
					}
				});
	}

	public void RefreshAllImages() {
		if (frame.getVecPhotos() == null) {
			return;
		}
		if (IsRefresh) {
			return;
		}
		IsRefresh = true;
		frame.setStatusMessageOngletVisionneuse(locale
				.getString("VOUS POUVEZ SUPPRIMER (BOUTON DROIT) OU TRIER LES PHOTOS PAR GLISSER/DÉPOSER. BOUTON CENTRAL POUR SÉLECTIONNER ET FERMER."));

		tab = new Element[frame.getVecPhotos().size()];

		// 行列数
		int nbl = (int) Math.floor((double) getSize().width / (Element.SIZE));
		int nbcol = (int) Math.ceil((double) (frame.getHide() ? frame
				.getVecPhotos().dontDel() : frame.getVecPhotos().size()) / nbl);

		Photo photo;
		centre.removeAll();

		centre.setLayout(new GridLayout(nbcol, nbl));
		int i, j;
		drag = false;
		Photo cur = frame.getVecPhotos().getCurrent();
		for (i = 0, j = 0; i < frame.getVecPhotos().size(); i++) {
			photo = frame.getVecPhotos().photoAt(i);
			if (!photo.isDel() || !frame.getHide()) {
				tab[j] = new Element(photo, frame);
				tab[j].setNum(i);
				if (cur == photo) {
					SelectedThumbnail = tab[j];
				}
				centre.add(tab[j]);
				centre.validate();
				j++;
			}
		}
		centre.addMouseListener(this);
		centre.addMouseMotionListener(this);
		centre.scrollRectToVisible(SelectedThumbnail.getBounds());
		this.repaint();
		this.validate();
		ChargePrioritairementImages();
		IsRefresh = false;
	}

	public void refreshThisPhoto(Photo p) {
		for (Component c : centre.getComponents())
			if (((Element) c).getPhoto() == p) {
				((Element) c).readImage();
			}
		centre.validate();
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (drag) {
			centre.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			depart.resetBorder();
			interm.resetBorder();

			try {
				int fin = ((Element) centre.getComponentAt(evt.getX(),
						evt.getY())).getNum();
				int departNb = depart.getNum();
				frame.getVecPhotos().changeOrder(departNb, fin);
				frame.getVecPhotos().setCurrent(
						frame.getVecPhotos().photoAt(
								((Element) centre.getComponentAt(evt.getX(),
										evt.getY())).getNum()));
				RefreshAllImages();
			} catch (Exception e) {
			}
		}
		drag = false;
	}

	@Override
	public void mouseEntered(MouseEvent evt) {
	}

	@Override
	public void mousePressed(MouseEvent evt) {
	}

	@Override
	public void mouseExited(MouseEvent evt) {
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
		if (!drag)
			try {
				frame.setStatusMessageOngletVisionneuse((frame.getVecPhotos()
						.photoAt(((Element) centre.getComponentAt(evt.getX(),
								evt.getY())).getNum())).getExif());
			} catch (Exception e) {
			}
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		// 验证点击时间大于 200ms 的操作
		long now = System.currentTimeMillis();
		if (now - old < 200) {
			return;
		}
		old = now;

		try {
			if (evt.getButton() == MouseEvent.BUTTON3) {
				frame.getVecPhotos().clear(
						frame.getVecPhotos().photoAt(
								((Element) (centre.getComponentAt(evt.getX(),
										evt.getY()))).getNum()));
				RefreshAllImages();
			} else if (evt.getButton() == MouseEvent.BUTTON1) {
				frame.getVecPhotos().setCurrent(
						frame.getVecPhotos().photoAt(
								((Element) (centre.getComponentAt(evt.getX(),
										evt.getY()))).getNum()));
				RefreshAllImages();
			} else {
				frame.getVecPhotos().setCurrent(
						frame.getVecPhotos().photoAt(
								((Element) (centre.getComponentAt(evt.getX(),
										evt.getY()))).getNum()));
				frame.setActiveTab(frame.RENOMMEUR);
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if (!drag) {
			try {
				drag = true;
				depart = (Element) centre
						.getComponentAt(evt.getX(), evt.getY());
				depart.changeBorderSrc();
				interm = depart;
				centre.setCursor(new Cursor(Cursor.HAND_CURSOR));
				frame.setStatusMessageOngletVisionneuse(locale
						.getString("DÉPLACE ")
						+ (frame.getVecPhotos().photoAt(((Element) centre
								.getComponentAt(evt.getX(), evt.getY()))
								.getNum())).getName(0, false) + "...");
			} catch (Exception e) {
			}
		} else {
			Element tmp = (Element) centre.getComponentAt(evt.getX(),
					evt.getY());
			if (tmp == depart || tmp == interm) {
				return;
			} else {
				if (tmp != null) {
					tmp.changeBorderDest();
					if (interm != depart) {
						interm.resetBorder();
					}
					interm = tmp;
				}
			}
		}
	}

	/**
	 * 优先加载图像
	 */
	private void ChargePrioritairementImages() {
		if (frame.getVecPhotos() == null) {
			return;
		}
		int deb = -1, fin = -1;

		centre.validate();
		Rectangle fen = centre.getVisibleRect();
		for (Component c : centre.getComponents()) {
			if (fen.contains(c.getLocation())) // Visible
				if (deb != -1) {
					fin = ((Element) c).getNum();
				} else {
					deb = ((Element) c).getNum();
				}
		}
		frame.setBufferZoneThumbnailsPriority(deb, fin);
	}
}
