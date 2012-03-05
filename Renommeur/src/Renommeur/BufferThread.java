package Renommeur;

import java.awt.Color;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JProgressBar;

public class BufferThread extends Thread {

	private static final ResourceBundle locale = ResourceBundle
			.getBundle("Renommeur/resources/locale");
	private Photo current;
	// 窗体大小改变
	private boolean changementTaille;
	//
	private boolean modifierCacheGde;
	// 缩略图优先级
	private boolean priorityThumbnails;
	private JProgressBar pr;
	private final AppFrame frame;
	private int deb, fin;

	BufferThread(AppFrame frame) {
		this.frame = frame;
		pr = frame.getProgressCache();
		pr.setStringPainted(true);
		current = null;
		this.setPriority(MIN_PRIORITY);
		priorityThumbnails = false;
		deb = 0;
		fin = 0;
	}

	public void SetCurrentItem(Photo c) {
		if (current == c) {
			modifierCacheGde = false;
		} else {
			current = c;
			modifierCacheGde = true;
		}
	}

	public void isChangementTaille() {
		changementTaille = true;
	}

	@Override
	public void run() {
		Photo p = null;
		int i;
		Vector<Photo> vListeCache = new Vector<Photo>();
		// 缓存
		while (true) {
			try {
				if (changementTaille) {
					changementTaille = false;
					for (Photo tmp : frame.getVecPhotos()) {
						if (!tmp.IsEnoughGood(frame.getPhotoPanel())) {
							tmp.RemoveGde();
						}
					}
					modifierCacheGde = true;
				}

				if (modifierCacheGde) {
					modifierCacheGde = false;
					int liste[] = { 1, 2, 3, -1, -2, -3 };
					vListeCache.removeAllElements();
					// 生成新的对象数据
					for (i = 0; i < liste.length; i++) {
						doZone();
						pr.setMaximum(liste.length);
						pr.setForeground(Color.red);
						pr.setString(locale.getString("PRÉCHARG. ") + " "
								+ (int) (pr.getPercentComplete() * 100) + "%");
						vListeCache.add(frame.getVecPhotos()
								.photoGoto(liste[i]));
						vListeCache.elementAt(i).ReadBigImage(
								frame.getPhotoPanel().getWidth(),
								frame.getPhotoPanel().getHeight());
						pr.setValue(i);
					}
					vListeCache.add(frame.getVecPhotos().getCurrent());
					// 删除旧的对象数据
					for (Photo tmp : frame.getVecPhotos()) {
						if (!vListeCache.contains(tmp)) {
							tmp.RemoveGde();
						}
					}
				}

				// 小缩略图
				if (!(changementTaille || modifierCacheGde)) {
					for (i = 0; i < frame.getVecPhotos().size(); i++) {
						doZone();
						p = frame.getVecPhotos().photoAt(i);
						if (!p.hasMini()) {
							pr.setForeground(Color.orange);
							pr.setMaximum(frame.getVecPhotos().size());
							pr.setValue(i + 1);
							pr.setString(locale.getString("VIGNETTES ") + " "
									+ (int) (pr.getPercentComplete() * 100)
									+ "%");
							p.getThumbnail();
							frame.refreshPhoto(p);
						}
						if ((changementTaille || modifierCacheGde)) {
							i = frame.getVecPhotos().size();
						}
					}
				}

				if (!(changementTaille || modifierCacheGde)) {
					pr.setMaximum(1);
					pr.setValue(1);
					pr.setString(locale.getString("CACHE OK"));
					pr.setForeground(Color.green);
					sleep(200);
				}
			} catch (NullPointerException np) {
				// 缓冲区溢出
				// 清空 JProgressBar 属性
				pr.setValue(0);
				pr.setString("");
				pr.setToolTipText("");
				return;
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * 设置优先区缩略图
	 * 
	 * @param deb
	 * @param fin
	 */
	public void setZoneThumbnailsPriority(int deb, int fin) {
		if (this.deb != deb && this.fin != fin && deb != fin) {
			priorityThumbnails = true;
			this.deb = deb;
			this.fin = fin;
		}
	}

	private void doZone() {
		int d, f;
		Photo p;
		while (priorityThumbnails) {
			d = deb;
			f = fin;
			for (int i = deb; i <= fin; i++) {
				pr.setForeground(Color.cyan);
				pr.setMaximum(fin - deb);
				pr.setValue(i - deb);
				pr.setString(locale.getString("AFFICHAGE ") + " "
						+ (int) (pr.getPercentComplete() * 100) + "%");
				p = frame.getVecPhotos().photoAt(i);
				if (!(frame.getHide() && p.isDel())) {
					p.getThumbnail();
					frame.refreshPhoto(p);
				}
				if (d != deb || f != fin) {
					break;
				}
			}
			if (d == deb || f == fin) {
				priorityThumbnails = false;
			}
		}
	}

}
