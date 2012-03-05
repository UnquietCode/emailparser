package Renommeur;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * 
 * @Description Photo对象数组
 * @Author zhangzuoqiang
 * @Date 2012-3-5
 */
public class VecPhotos extends Vector<Photo> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VecPhotos(AppFrame frame) {
		super();
		nameWarning = false;
		this.frame = frame;
	}

	public Photo photoGoto(int valeur) {
		int coef = (valeur < 0) ? -1 : 1;
		valeur = (valeur < 0) ? -valeur : valeur;
		int position = current;

		for (int i = 0; i < valeur; i++)
			do {
				position += coef;
				if (position == size()) {
					position = 0;
				}
				if (position == -1) {
					position = size() - 1;
				}
				// 不可能发生
				if (current == position) {
					return photoAt(position);
				}
			} while (frame.getHide() && photoAt(position).isDel());
		return photoAt(position);
	}

	private void CleanBeforeSave() {
		for (Photo p : this) {
			p.RemoveGde();
			p.setUnidentified();
		}
		frame = null;
	}

	public String Restaure(AppFrame frame, File[] F) {
		this.frame = frame;
		int ident = 0, nouv = 0, delet = 0;
		boolean Nouveau;
		for (File f : F) {
			if (Photo.isReadableImage(f)) {
				Nouveau = true;
				// 验证文件
				for (Photo p : this) {
					Nouveau &= !p.Identify(f);
				}

				// 有新的文件
				if (Nouveau) {
					add(new Photo(f));
					nouv++;
				} else {
					ident++;
				}
			}
		}
		// 删除引用中不再存在的文件
		for (Photo p : this)
			if (!p.IsRestaured()) {
				remove(p);
				delet++;
			}
		Renum();
		frame.setButtons(buttons);
		try {
			getCurrent();
		} catch (Exception e) {
			FirstIsCurrent();
		}

		return locale.getString("RESTAURATION TERMINÉE : ") + ident
				+ locale.getString(" FICHIER(S) RECONNU(S), ") + nouv
				+ locale.getString(" NOUVEAU(X) ET ") + delet
				+ locale.getString(" DISPARU(S).");
	}

	public void Save(String file, Component[] comp) {
		AppFrame feorTMP = frame;

		buttons = new JButton[comp.length];
		int i = 0;
		for (Component c : comp) {
			try {
				buttons[i] = (JButton) c;
				i++;
			} catch (Exception e) {
			}
		}
		CleanBeforeSave();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(photoAt(0)
					.getFile().getParent() + File.separator + file));
			oos.writeObject(this);
			frame = feorTMP;
			JOptionPane.showMessageDialog(frame.getFrame(),
					locale.getString("ENREGISTREMENT EFFECTUÉ AVEC SUCCÈS."),
					locale.getString("AU REVOIR."),
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			frame = feorTMP;
			JOptionPane.showMessageDialog(frame.getFrame(),
					file + " :\n" + ex.getLocalizedMessage(),
					locale.getString("ERREUR LORS DE LA SAUVEGARDE."),
					JOptionPane.ERROR_MESSAGE);
			frame.updateStatusMessage(locale
					.getString("ÉCHEC DE LA SAUVEGARDE."));
		}
	}

	public void changeOrder(int initial, int fin) {
		Photo p = photoAt(initial);
		remove(initial);
		add(fin, p);
		current = fin;
		Renum();
		if (!frame.isNumPhotos()) {
			ResoudreMemeNom();
		}
	}

	public void Renum() {
		int i = 1;
		for (Photo p : this) {
			if (!p.isDel()) {
				p.setNb(i++);
			}
		}
	}

	public void setCurrent(Photo p) {
		if (p != getCurrent()) {
			current = indexOf(p);
			frame.doChangeTextAndImage(true, true);
		}
	}

	public Photo getCurrent() {
		return photoAt(current);
	}

	public Photo photoAt(int a) {
		return elementAt(a);
	}

	public int NbDone() {
		int nb = 0;
		for (Photo p : this) {
			nb += (p.hasChanged()) ? 1 : 0;
		}
		if (nb == size()) {
			JOptionPane
					.showMessageDialog(
							frame.getFrame(),
							locale.getString("TOUTES LES PHOTOS ONT ÉTÉ MODIFIÉES. VOUS POUVEZ TERMINER, OU CONTINUER À LES RENOMMER"),
							locale.getString("INFORMATIONS."),
							JOptionPane.INFORMATION_MESSAGE);
		}
		return nb;
	}

	public void delete() {
		clear(photoAt(current));
		setCurrent(photoGoto(1));
	}

	public void clear(Photo p) {
		if (p.isDel()) {
			p.Del(false);
			Renum();
		} else {
			p.Del(true);
			Renum();
			dontDel();
		}
	}

	/**
	 * 不删除
	 * 
	 * @return
	 */
	public int dontDel() {
		int nb = 0, i = 0;
		while (i < size()) {
			nb += (!photoAt(i++).isDel()) ? 1 : 0;
		}
		if (nb == 0 && frame.getHide()) {
			JOptionPane
					.showMessageDialog(
							frame.getFrame(),
							locale.getString("TOUTES LES PHOTOS SONT SUPPRIMÉES. IMPOSSIBLE DE CACHER LES SUPPRIMÉES."),
							locale.getString("ERREUR"),
							JOptionPane.ERROR_MESSAGE);
			frame.doDontHide();
		}
		return nb;
	}

	public void ResoudreMemeNom() {
		for (Photo p : this) {
			dealSameName(p);
		}
	}

	/**
	 * 解决重名问题
	 * 
	 * @param p
	 */
	private void dealSameName(Photo p) {
		int nb = 0, nbreMemeNom = 0;
		// 检查是否存在重名的文件
		for (Photo autre : this) {
			if (!autre.equals(p) && autre.hasSameName(p.getName())) {
				nbreMemeNom++;
			}
		}

		if (nbreMemeNom++ == 0) {
			p.DoNumFin(-1, nbreMemeNom);
			return;
		}
		if (!nameWarning) {
			JOptionPane.showMessageDialog(frame.getFrame(),
					locale.getString("MESSAGE_MEME_NOM"),
					locale.getString("INFORMATION"),
					JOptionPane.INFORMATION_MESSAGE);
			nameWarning = true;
		}
		// 然后修复重命名问题
		for (Photo autre : this) {
			if (autre.hasSameName(p.getName())) {
				autre.DoNumFin(++nb, nbreMemeNom);
			}
		}

	}

	public void sort() {
		Collections.sort(this);
		Renum();
	}

	public void FirstIsCurrent() {
		current = 0;
		dontDel();
		if (frame.getHide() && getCurrent().isDel()) {
			photoGoto(1);
		}
	}

	private int current;
	private AppFrame frame;
	private JButton[] buttons;
	private boolean nameWarning;

	private static final ResourceBundle locale = ResourceBundle
			.getBundle("Renommeur/resources/locale");
}
