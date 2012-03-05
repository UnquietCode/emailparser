package Renommeur;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.CanonMakernoteDirectory;
import com.drew.metadata.exif.ExifDirectory;

/**
 * 
 * @Description 数据对象，对应照片对象
 * @Author zhangzuoqiang
 * @Date 2012-3-5
 */
public class Photo implements Comparable<Object>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Photo(File F) {
		this.F = F;
		IsDel = false;
		Name = F.getName().substring(0, F.getName().lastIndexOf('.'));
		LastMod = F.lastModified();
		Length = F.length();
		ReadExif();
		IsRestaured = false;
		DoNumFin(-1, 0);
	}

	public static boolean isReadableImage(File F) {
		String[] extensions = { "jpg", "jpeg", "png", "gif" };
		String nom = F.getName().toLowerCase();

		for (String s : extensions) {
			if (nom.endsWith("." + s)) {
				return true;
			}
		}
		return false;
	}

	public boolean Identify(File file) {
		if (F.getName().equalsIgnoreCase(file.getName())) {
			if (LastMod == file.lastModified() && Length == file.length()) {
				F = file;
			} else {
				GrandeImage = null;
				Thumbnail = null;
				F = file;
				ReadExif();
			}
			IsRestaured = true;
			return true;
		}
		return false;
	}

	public String getName(int NbreDePhotos, boolean hasNum) {
		String rep = "";
		if (hasChanged() && !IsDel) {
			if (hasNum) {
				rep = NumeroAvecXXXChiffres(Nb, NbreDePhotos) + " " + Name;
			} else if (hasNumFin()) {
				rep = Name + " " + NumeroAvecXXXChiffres(NumFin, NbreMemeNom);
			} else {
				rep = Name;
			}
		} else {
			rep = Name;
		}
		return rep;
	}

	public ImageIcon getBigImage(int x, int y) {
		ReadBigImage(x, y);
		return new ImageIcon(GrandeImage);
	}

	public boolean hasSameName(String name) {
		return name.equals(Name);
	}

	public boolean ReadBigImage(int x, int y) {
		llabel = x;
		hlabel = y;
		BufferedImage ImageDOrigine;
		if (GrandeImage != null) {
			return true;
		}
		try {
			ImageDOrigine = ImageIO.read(F);
		} catch (Exception e) {
			return false;
		}
		GrandeImage = Recadre(ImageDOrigine, x - 30, y - 30);
		return true;
	}

	public void RemoveGde() {
		GrandeImage = null;
	}

	/**
	 * 获取缩略图
	 */
	public ImageIcon getThumbnail() {
		if (Thumbnail != null) {
			return Thumbnail;
		}

		BufferedImage ImageDépart = null;
		if (GrandeImage == null) {
			try {
				ImageDépart = ImageIO.read(F);
			} catch (Exception e) {
				return null;
			}
		} else {
			ImageDépart = GrandeImage;
		}
		Thumbnail = new ImageIcon(Recadre(ImageDépart, SIZE, SIZE));
		return Thumbnail;
	}

	public boolean hasMini() {
		return (Thumbnail != null);
	}

	public boolean IsEnoughGood(JLabel p) {
		return (p.getWidth() == llabel && p.getHeight() == hlabel);
	}

	public void setName(String n) {
		Name = n;
	}

	public void ReadExif() {
		try {
			String Date = "";
			Metadata metadata = JpegMetadataReader.readMetadata(F);
			Directory exifDirectory = metadata
					.getDirectory(ExifDirectory.class);
			String[] d = exifDirectory.getString(ExifDirectory.TAG_DATETIME)
					.split("\\D");
			String expo = "";
			if (d != null) {
				Calendar c = Calendar.getInstance();
				c.set(Integer.parseInt(d[0]), Integer.parseInt(d[1]),
						Integer.parseInt(d[2]), Integer.parseInt(d[3]),
						Integer.parseInt(d[4]), Integer.parseInt(d[5]));
				DateFormat sdf = DateFormat.getDateTimeInstance(
						DateFormat.FULL, DateFormat.LONG, Locale.getDefault());
				Date = sdf.format(c.getTime());
			}

			String fnumber = exifDirectory.getString(ExifDirectory.TAG_FNUMBER);
			if (fnumber != null) {
				expo += "f" + fnumber + " ";
			}
			String time = exifDirectory
					.getString(ExifDirectory.TAG_EXPOSURE_TIME);
			if (time != null) {
				expo += time + "s ";
			}
			String iso = exifDirectory
					.getString(ExifDirectory.TAG_ISO_EQUIVALENT);
			if (iso != null) {
				expo += "@ " + iso + " ISO ";
			}
			String flash = exifDirectory.getString(ExifDirectory.TAG_FLASH);
			if (flash != null) {
				int f = Integer.parseInt(flash);

				if ((f & 1) == 1) {
					flash = locale.getString("FLASH ENVOYÉ");
					if ((f & 64) == 0) {
						flash += locale.getString(" AVEC ANTI-YEUX ROUGES");
					} else {
						flash += locale.getString(" SANS ANTI-YEUX ROUGES");
					}
					if ((f & 6) == 6) {
						flash += locale.getString("[ RETOUR NON REÇU]");
					} else if ((f & 6) == 6) {
						flash += locale.getString("[ RETOUR REÇU]");
					}
				} else {
					flash = locale.getString("FLASH NON ENVOYÉ");
				}
				if ((f & 24) == 24) {
					flash += locale.getString(" (MODE AUTO)");
				} else if ((f & 24) == 8) {
					flash += locale.getString(" (MODE FORCÉ)");
				} else if ((f & 24) == 16) {
					flash += locale.getString(" (MODE FORCÉ)");
				}
			}
			if (iso == null) {
				exifDirectory = metadata
						.getDirectory(CanonMakernoteDirectory.class);
				iso = exifDirectory
						.getString(CanonMakernoteDirectory.TAG_CANON_STATE1_ISO);
				if (iso != null) {
					expo += "@ "
							+ (int) (50 * Math.pow(2,
									Integer.parseInt(iso) - 16)) + " ISO";
				}
			}
			Exif = Date + " | ";
			if (expo != null) {
				Exif += expo + " | ";
			}
			if (flash != null) {
				Exif += flash;
			}
			while (Exif.endsWith(" | ")) {
				Exif = Exif.substring(0, Exif.lastIndexOf(" | "));
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public int compareTo(Object o) {
		return F.getAbsolutePath().compareToIgnoreCase(
				((Photo) o).getFile().getAbsolutePath());
	}

	public File getFile() {
		return F;
	}

	public int getNb() {
		return Nb;
	}

	public void setNb(int i) {
		Nb = i;
	}

	public boolean isDel() {
		return IsDel;
	}

	public void Del(boolean b) {
		IsDel = b;
	}

	boolean hasChanged() {
		return (!Name.equals(F.getName().substring(0,
				F.getName().lastIndexOf('.'))) || IsDel);
	}

	boolean IsRestaured() {
		return IsRestaured;
	}

	String getExif() {
		return Exif;
	}

	void setUnidentified() {
		IsRestaured = false;
	}

	private BufferedImage Recadre(BufferedImage ImageOriginie, int LargeurFin,
			int HauteurFin) {
		int LargeurOrigine, HauteurOrigine;
		BufferedImage ImageRedimensionnée;

		// Choisi la bonne largeur et la bonne hauteur pour respecter le ratio.
		LargeurOrigine = ImageOriginie.getWidth();
		HauteurOrigine = ImageOriginie.getHeight();
		if (HauteurFin * LargeurOrigine < LargeurFin * HauteurOrigine) {
			LargeurFin = LargeurOrigine * HauteurFin / HauteurOrigine;
		} else {
			HauteurFin = HauteurOrigine * LargeurFin / LargeurOrigine;
		}

		// L'image redimensionnée
		ImageRedimensionnée = new BufferedImage(LargeurFin, HauteurFin,
				BufferedImage.TYPE_INT_RGB);

		// Redimensionnement de l'image
		Graphics2D g = (Graphics2D) ImageRedimensionnée.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(ImageOriginie, 0, 0, LargeurFin, HauteurFin, null);
		g.dispose();
		return ImageRedimensionnée;
	}

	public String getExtension() {
		return F.getName().substring(F.getName().lastIndexOf('.') + 1,
				F.getName().length());
	}

	public void DoNumFin(int num, int NbreMmNm) {
		NumFin = num;
		NbreMemeNom = NbreMmNm;
	}

	private boolean hasNumFin() {
		return (NumFin != -1);
	}

	private String NumeroAvecXXXChiffres(int num, int NbMaxi) {
		String rep = "";
		for (int i = 0; i < Integer.toString(NbMaxi).length()
				- Integer.toString(num).length(); i++) {
			rep += '0';
		}

		return rep + Integer.toString(num);
	}

	public String getName() {
		return Name;
	}

	private int Nb, NumFin, NbreMemeNom;
	private int llabel, hlabel;
	private String Name;
	private File F;
	private ImageIcon Thumbnail;
	private BufferedImage GrandeImage;
	public static final int SIZE = 150;
	private static final ResourceBundle locale = ResourceBundle
			.getBundle("Renommeur/resources/locale");
	private String Exif;
	private boolean IsDel, IsRestaured;
	private final long LastMod;
	private final long Length;

}
