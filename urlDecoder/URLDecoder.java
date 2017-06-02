package urlDecoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

public class URLDecoder {
	private File inputFile = null;
	private Charset inputCharset = Charset.defaultCharset();

	public static void main(String[] args) throws IOException {
		URLDecoder x = new URLDecoder();
		x.help(args);
		x.validate(args);
		x.dekodiert(x.inputFile != null ? new FileInputStream(x.inputFile) : System.in, x.inputCharset);
	}

	void help(String[] args) {
		for (int j = 0; j < args.length; j++) {
			if (args[j].equals("-h") || args[j].equals("--help")) {

				System.out.println();
				System.out.println("BEZEICHNUNG");
				System.out.println("       urlDecoder - URL-Escapte Daten in Klartext wandeln");
				System.out.println();
				System.out.println("ÜBERSICHT");
				System.out.println("       URLDecoder [OPTION] [DATEI]");
				System.out.println("       URLDecoder [DATEI] [OPTION]");
				System.out.println();
				System.out.println("BESCHREIBUNG");
				System.out.println(
						"       Eingabe der Daten ohne DATEI aus der Standardeingabe. Als Standardzeichensatz wird der Systemstandard genutzt.");
				System.out.println();
				System.out.println("       Absolute Dateipfadangabe vor oder nach dem Zeichensatz möglich");
				System.out.println();
				System.out.println("       -h, --help");
				System.out.println("              Öffnen der Hilfe und Terminierung des Programms");
				System.out.println();
				System.out.println("       -c charset, --charset charset");
				System.out.println("              Eingabe des vom Standard abweichenden Charsets der Eingabe");
				System.out.println();
				System.exit(1);

			}
		}
	}

	void validate(String[] args) {
		if (args.length > 3) {
			System.err.println("Zu viele Parameter angegeben");
			System.exit(1);
		}
		int charsetEingeleitetIndex = -1;
		boolean charsetAngegeben = false;
		boolean charsetKorrekt = false;
		int dateiAngegebenIndex = -1;
		boolean dateiKorrekt = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c") || args[i].equals("--charset")) {
				charsetEingeleitetIndex = i;
				charsetAngegeben = i < args.length - 1;
				if (charsetAngegeben) {
					try {
						charsetKorrekt = Charset.isSupported(args[i + 1]);
						if (charsetKorrekt) {
							inputCharset = Charset.forName(args[i + 1]);
						}
					} catch (IllegalCharsetNameException e) {
					}
				}
			}
		}
		if (charsetAngegeben && args.length == 3) {
			if (charsetEingeleitetIndex == 0) {
				dateiAngegebenIndex = 2;
			} else {
				dateiAngegebenIndex = 0;
			}
			File f = new File(args[dateiAngegebenIndex]);
			if (f.isFile() && f.canRead()) {
				dateiKorrekt = true;
				inputFile = f;
			}
		} else if (charsetAngegeben == false && args.length == 1) {
			dateiAngegebenIndex = 0;
			File f = new File(args[dateiAngegebenIndex]);
			if (f.isFile() && f.canRead()) {
				dateiKorrekt = true;
				inputFile = f;
			}
		}
		if (charsetAngegeben == false && args.length > 1) {
			System.err.println("Bitte nur eine Datei");
			System.exit(1);
		}
		if (dateiAngegebenIndex != -1 && dateiKorrekt == false) {
			System.err.println("Datei konnte nicht gelesen werden");
			System.exit(1);
		}
		if (charsetEingeleitetIndex != -1 && charsetKorrekt == false) {
			System.err.println("Zeichensatz ungültig");
			System.exit(1);
		}
	}

	void dekodiert(InputStream is, Charset chars) throws IOException {
		BufferedReader leser = new BufferedReader(new InputStreamReader(is, chars));
		String zeile = null;
		while ((zeile = leser.readLine()) != null) {
			System.out.print(java.net.URLDecoder.decode(zeile, chars.toString()) + "\r\n");
			// System.out.print(zeile+"\r\n");
		}
	}
}
