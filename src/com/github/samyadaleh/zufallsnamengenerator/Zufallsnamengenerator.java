package com.github.samyadaleh.zufallsnamengenerator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.Random;
import java.lang.Math;
import javax.swing.event.*;
import java.net.*;
import java.util.regex.*;

public class Zufallsnamengenerator extends JFrame
    implements ActionListener, ChangeListener, ItemListener {

  private static String[] Konsa, Vok, Suff, Konse;
  private static Vector<String> alphabet = new Vector<>();
  private static String Meldung = "Hier erscheinen Meldungen.";
  private JTextArea meldungsfenster = new JTextArea(11, 2);
  private JSlider slid = new JSlider(SwingConstants.VERTICAL, 0, 10, 2);
  private static int silbenanzahl = 2;
  private JRadioButton end_immer;
  private JRadioButton end_manch;
  private static Choice anfang = new Choice();
  private static Choice alphawahl = new Choice();
  private JRadioButton kv;
  private JRadioButton kvk;
  private JCheckBox einzig = new JCheckBox("einzigartig (nur einzeln)");

  private int hilfecount = 0;

  private Zufallsnamengenerator() {
    try {
      BufferedReader in = new BufferedReader(new FileReader("buecher.txt"));
      String ein = in.readLine();
      while (ein != null) {
        alphawahl.add(ein.trim());
        ein = in.readLine();
      }
      in.close();
      alphaeinlesen();
    } catch (NullPointerException ignored) {
    } catch (FileNotFoundException ex) {
      Meldung = Meldung
          + "\nDatei buecher.txt wurde nicht gefunden, Alphabete konnten nicht geladen werden. Standartwerte werden verwendet.";
      sKonsa();
      sKonse();
      sVok();
      sSuff();
    } catch (IOException ex) {
      Meldung = Meldung
          + "\nFehler beim Lesen aus buecher.txt, Alphabete konnten nicht geladen werden. Standartwerte werden verwendet.";
      sKonsa();
      sKonse();
      sVok();
      sSuff();
    }

    setTitle("Zufallsnamengenerator");
    getContentPane().setLayout(new BorderLayout());

    slid.setSnapToTicks(true);
    slid.setMajorTickSpacing(1);
    slid.setSnapToTicks(true);
    slid.setPaintLabels(true);

    end_immer = new JRadioButton("immer", false);
    end_manch = new JRadioButton("manchmal", false);
    JRadioButton end_nie = new JRadioButton("nie", true);

    ButtonGroup rgroup = new ButtonGroup();
    rgroup.add(end_immer);
    rgroup.add(end_manch);
    rgroup.add(end_nie);

    JLabel endverw = new JLabel("Endung verwenden");
    Box suffix = new Box(BoxLayout.Y_AXIS);
    suffix.add(endverw);
    suffix.add(end_immer);
    suffix.add(end_manch);
    suffix.add(end_nie);

    meldungsfenster.setEditable(false);
    JButton name = new JButton("Namen generieren");
    name.addActionListener(this);
    JButton save = new JButton("Wort speichern");
    save.addActionListener(this);
    JButton listealpha = new JButton("Liste mit 1000 Einträgen generieren");
    listealpha.addActionListener(this);
    JButton hilfe = new JButton("Hilfe & Info");
    hilfe.addActionListener(this);
    slid.addChangeListener(this);
    meldungsfenster.setText(Meldung);
    alphawahl.addItemListener(this);

    name.setActionCommand("name");
    save.setActionCommand("save");
    listealpha.setActionCommand("liste");
    hilfe.setActionCommand("hilfe");

    JPanel buttons = new JPanel();
    buttons.add(name);
    buttons.add(save);
    buttons.add(listealpha);
    buttons.add(hilfe);

    Box laenge = new Box(BoxLayout.Y_AXIS);
    JLabel lang = new JLabel("Silbenanzahl pro Wort");
    laenge.add(lang);
    laenge.add(slid);

    JLabel alphabetverw = new JLabel("Alphabet:");
    Box alpha = new Box(BoxLayout.Y_AXIS);
    alpha.add(alphabetverw);
    alpha.add(alphawahl);
    JLabel anfangverw = new JLabel("Anfangsbuchstaben erzwingen:");
    Box anf = new Box(BoxLayout.Y_AXIS);
    anf.add(anfangverw);
    anf.add(anfang);

    Box einst = new Box(BoxLayout.Y_AXIS);
    einst.add(alpha);
    einst.add(anf);

    kv = new JRadioButton("KV", false);
    JRadioButton kv_kvk = new JRadioButton("KV oder KVK", true);
    kvk = new JRadioButton("KVK", false);

    ButtonGroup rgroup2 = new ButtonGroup();
    rgroup2.add(kv);
    rgroup2.add(kv_kvk);
    rgroup2.add(kvk);

    JLabel sillang = new JLabel("Silbenlänge");
    Box silben = new Box(BoxLayout.Y_AXIS);
    silben.add(sillang);
    silben.add(kv);
    silben.add(kv_kvk);
    silben.add(kvk);

    Box radios = new Box(BoxLayout.Y_AXIS);
    radios.add(suffix);
    radios.add(silben);

    Box einzigbox = new Box(BoxLayout.Y_AXIS);
    einzigbox.add(einzig);

    Box einstm = new Box(BoxLayout.X_AXIS);
    einstm.add(laenge);
    einstm.add(einzigbox);

    getContentPane().add(meldungsfenster, BorderLayout.NORTH);
    getContentPane().add(buttons, BorderLayout.SOUTH);
    getContentPane().add(einst, BorderLayout.WEST);
    getContentPane().add(radios, BorderLayout.EAST);
    getContentPane().add(einstm, BorderLayout.CENTER);
  }

  public void actionPerformed(ActionEvent evt) {
    if (evt.getActionCommand().equals("name")) {
      boolean einzigartig = true;
      if (einzig.isSelected())
        einzigartig = false;
      int counter = 0;
      do {
        Meldung = "";
        for (int index = 0; index < silbenanzahl; index++) {
          if (index == 0 && !("".equals(anfang.getSelectedItem())
              || anfang.getSelectedItem() == null)) {
            if (kv.isSelected()) {
              Meldung += Silbenbau(2, true);
            } else if (kvk.isSelected()) {
              Meldung += Silbenbau(3, true);
            } else
              Meldung +=
                  Silbenbau(Math.abs(new Random().nextInt()) % 2 + 2, true);
          } else {
            if (kv.isSelected()) {
              Meldung += Silbenbau(2, false);
            } else if (kvk.isSelected()) {
              Meldung += Silbenbau(3, false);
            } else
              Meldung +=
                  Silbenbau(Math.abs(new Random().nextInt()) % 2 + 2, false);
          }
        }
        if (end_immer.isSelected()) {
          Meldung += Suff[Math.abs(new Random().nextInt()) % Suff.length];
        }
        if (end_manch.isSelected()) {
          if (Math.abs(new Random().nextInt()) % 2 == 0)
            Meldung += Suff[Math.abs(new Random().nextInt()) % Suff.length];
        }
        if (einzig.isSelected())
          if (Einzigartigkeit(Meldung))
            einzigartig = true;
        counter++;
      } while (!einzigartig && counter <= 5);
      if (counter >= 1000)
        Meldung =
            "Nach 1000 Durchläufen wurde kein einzigartiger Name gefunden.";

      Meldung = Erstergross(Meldung);

      meldungsfenster.setText(Meldung);
      if (slid.getValue() == 0)
        silbenanzahl = Math.abs(new Random().nextInt()) % 3 + 1;
    }
    if (evt.getActionCommand().equals("save")) {
      try {
        PrintWriter print = new PrintWriter(
            new BufferedWriter(new FileWriter("Namen.txt", true)));
        print.println(Meldung);
        print.close();
        Meldung = "Aktuelles Wort wurde an Namen.txt angehängt.";
      } catch (IOException ex) {
        Meldung = "Fehler beim Schreiben nach Namen.txt";
      }
      meldungsfenster.setText(Meldung);
    }
    if (evt.getActionCommand().equals("liste")) {
      try {
        PrintWriter print =
            new PrintWriter(new BufferedWriter(new FileWriter("Liste.txt")));

        Vector<String> Namen = new Vector<>(0);
        boolean vorhanden = false;
        int anzahl = 1000;
        int counter = 0;
        StringBuilder newname;
        while (anzahl > 0 && counter < 10000) {
          newname = new StringBuilder();
          for (int index = 0; index < silbenanzahl; index++) {
            if (index == 0 && !("".equals(anfang.getSelectedItem()))) {
              if (kv.isSelected()) {
                newname.append(Silbenbau(2, true));
              } else if (kvk.isSelected()) {
                newname.append(Silbenbau(3, true));
              } else
                newname.append(
                    Silbenbau(Math.abs(new Random().nextInt()) % 2 + 2, true));
            } else {
              if (kv.isSelected()) {
                newname.append(Silbenbau(2, false));
              } else if (kvk.isSelected()) {
                newname.append(Silbenbau(3, false));
              } else
                newname.append(
                    Silbenbau(Math.abs(new Random().nextInt()) % 2 + 2, false));
            }
          }

          if (end_immer.isSelected()) {
            newname
                .append(Suff[Math.abs(new Random().nextInt()) % Suff.length]);
          }
          if (end_manch.isSelected()) {
            if (Math.abs(new Random().nextInt()) % 2 == 0)
              newname
                  .append(Suff[Math.abs(new Random().nextInt()) % Suff.length]);
          }
          newname = new StringBuilder(Erstergross(newname.toString()));
          for (int index = 0; index < Namen.size(); index++) {
            if (newname.toString().equals(Namen.elementAt(index)))
              vorhanden = true;
          }
          counter++;
          if (!vorhanden) {
            Namen.addElement(newname.toString());
            print.println(newname);
            anzahl--;
          }
          vorhanden = false;
        }

        print.close();
        if (anzahl <= 0)
          Meldung =
              "Eine Liste mit 1000 Einträgen wurde erstellt und ist unter Liste.txt einsehbar.";
        else
          Meldung = "Eine Liste mit " + (1000 - anzahl)
              + " Einträgen wurde erstellt und ist unter Liste.txt einsehbar.\nMehr war mit der Einstellung nicht möglich.";

      } catch (IOException ex) {
        Meldung = "Fehler beim Schreiben nach Namen.txt";
      }
      meldungsfenster.setText(Meldung);
    }
    if (evt.getActionCommand().equals("hilfe")) {
      hilfecount++;
      if (hilfecount == 1)
        Meldung =
            ("Button: Namen generieren -> generiert einen zufälligen Namen mit den aktuellen Einstellungen.\n"
                + "Button: Wort speichern -> Speichert den Inhalt des Meldungsfensters nach Namen.txt\n"
                + "Button: Liste mit 1000 Einträgen generieren -> mit den aktuellen Einstellungen, diese Liste überschreibt Liste.txt.\n"
                + "Sind nicht genug Werte vorhanden, ist die Liste kürzer.\n"
                + "Das Programm achtet darauf, dass kein Name in der Datei zweimal vorkommt.\n"
                + "Button: Hilfe und Info -> Bei jedem Klick wird ein weiterer Abschnitt der Hilfe angezeigt");
      if (hilfecount == 2)
        Meldung =
            ("Der Schieberegler links legt die Wortlänge fest, Endung nicht mitgerechnet. Mindestzahl ist 1.\n"
                + "Die Auswahl rechts erlaubt die Verwendung von Endungen. Bei 'manchmal' wird mit einer Wahrscheinlickeit von 50%\n eine Endung verwendet."
                + "Silbenlänge: Einstellung, ob die Silben aus Konsonant+Vokal oder Konsonant+Vokal+Konsonant oder beiden Möglichkeiten bestehen sollen"
                + "einzigartig !Achtung! Erfordert eine Internetverbindung und testet, ob der Name mit Yahoo gefunden wird."
                + "Verlangsamt das Generieren von Namen und du könntest gebannt werden, daher steht die Funktion nur für einzelne Namen zur Verfügung.");
      if (hilfecount == 3)
        Meldung =
            ("Beim Start lädt das Programm die zur Verfügung stehenden Alphabete aus\n"
                + "'buecher.txt', wo steht, welche weiteren Dateien geladen werden sollen. Ein paar Beispieldateien werden mitgeliefert.\n"
                + "(Bemerkung: 'Namen.txt' und 'Liste.txt' können jederzeit bedenkenlos gelöscht werden.)\n"
                + "Diese 'Alphabete' sind so aufgebaut: Links steht in einer Zeile ein Buchstabe, Buchstabenkombination oder Morphem.\n"
                + "Die Zahlen geben die Verwendung an in dieser Reihenfolge: Als Konsonant am Anfang, Vokal, Konsonant am Ende, Endung.");
      if (hilfecount == 4)
        Meldung =
            ("Diese Textdateien können editiert werden, falls jemand anderen Buchstaben verwendet haben möchte.\n"
                + "Das Programm baut Silben entweder aus: Anfangskonsonant+Vokal, oder Anfangskonsonant+Vokal+Endkonsonant auf.\n"
                + "Warum zwei Reihen für Konsonanten?\n"
                + "Nehmen wir z.B das Wort 'Produkt'. 'Pr' kann nur am Anfang einer Silbe stehen und müsste nach 'Konsonanten anfang.txt'\n geschrieben werden.\n"
                + "'Kt' hingegen kann nur am Ende gesprochen werden. Dies sollte beim Editieren beachtet werden, um aussprechbare\n Wörter zu erhalten.");
      if (hilfecount == 5)
        Meldung =
            ("Bei 0 wird eine Buchstabenverbindung nicht verwendet. Mit den Zahlen wird die Gewichtung beeinflusst.\n"
                + "Es spielt keine Rolle ob bei allen Buchstaben eine 1 oder bei allen eine 5 steht. Aber wenn bei einem 1 und beim anderen 5\n"
                + "auftaucht, erscheint der Buchstabe mit der 5 fünfmal häufiger. In den Standarddateien sind die Wahrscheinlichkeiten den entsprechenden Sprachen.\n"
                + "angenähert. Im Deutschen sind 2% der Buchstaben ein b, daher steht bei b eine 2. Man kann es auch ganz anders machen.\n"
                + "Sollen Silben mit einem Vokal beginnen können, ist es nötig, ein Zeichen für den Knacklaut davor, z.B. ' einzufügen.");
      if (hilfecount == 6) {
        Meldung =
            ("Version 1.5.1 vom 8.3.2011 \nFragen, Anregungen, Kritik etc. zum Zufallsnamengenerator bitte an: samya@behindthematrix.de\n"
                + "\n"
                + "Hier endet die Hilfe. Mit dem nächsten Klick beginnt sie von vorn.");
        hilfecount = 0;
      }
      meldungsfenster.setText(Meldung);
    }
  }

  public void stateChanged(ChangeEvent evt) {
    if (slid.getValue() != 0) {
      silbenanzahl = slid.getValue();
    } else
      silbenanzahl = Math.abs(new Random().nextInt()) % 3 + 1;
  }

  private static String Silbenbau(int lang, boolean erst) {
    int kons1 = Math.abs(new Random().nextInt()) % Konsa.length;
    int vok1 = Math.abs(new Random().nextInt()) % Vok.length;
    int kons2 = Math.abs(new Random().nextInt()) % Konse.length;
    String silbe = "";
    if (lang == 1) {
      if (erst)
        silbe = anfang.getSelectedItem();
      else
        silbe = Konsa[kons1];
    }
    if (lang == 2) {
      if (erst)
        silbe = anfang.getSelectedItem() + Vok[vok1];
      else
        silbe = Konsa[kons1] + Vok[vok1];
    }
    if (lang == 3) {
      if (erst)
        silbe = anfang.getSelectedItem() + Vok[vok1] + Konse[kons2];
      else
        silbe = Konsa[kons1] + Vok[vok1] + Konse[kons2];
    }
    return silbe;
  }

  /**
   * wandelt den ersten Buchstaben in einen Großbuchstaben um
   */
  private static String Erstergross(String name) {
    char[] nam = name.toCharArray();
    name = "" + name.substring(0, 1);
    StringBuilder nameBuilder = new StringBuilder(name.toUpperCase());
    for (int index = 1; index < nam.length; index++) {
      nameBuilder.append(nam[index]);
    }
    name = nameBuilder.toString();
    return name;
  }

  private static void alphaeinlesen() {
    try {
      alphabet.clear();
      BufferedReader in =
          new BufferedReader(new FileReader(alphawahl.getSelectedItem()));
      String ein = in.readLine();
      for (; ein != null; ein = in.readLine()) {
        StringTokenizer tok = new StringTokenizer(ein.trim(), "\t");
        while (tok.hasMoreElements()) {
          alphabet.addElement((String) tok.nextElement());
        }
      }
    } catch (NullPointerException npe) {
      //nix
    } catch (IOException ex) {
      Meldung = Meldung
          + "\nAusgewähltes Alphabet konnte nicht geladen werden. Datei nicht vorhanden oder falsch geschrieben. Standardeinstellung wird verwendet.";
      sKonsa();
      sKonse();
      sVok();
      sSuff();
    }
    anfangsauswahl();
    int indes;
    int count;
    try {
      count = 0;
      for (int i = 1; i < alphabet.size(); i += 5) {
        count += Integer.parseInt(alphabet.elementAt(i));
      }
      Konsa = new String[count];
      indes = 0;
      for (int i = 1; i < alphabet.size(); i += 5) {
        for (int j = 0;
             j < Integer.parseInt(alphabet.elementAt(i)); j++) {
          Konsa[indes] = alphabet.elementAt(i - 1);
          indes++;
        }
      }
    } catch (NumberFormatException nex) {
      Meldung +=
          "\nFehlerhafter Eintrag in der Alphabetdatei. Standartwerte werden für Konsonanten am Anfang verwendet.";
      sKonsa();
    }
    try {
      count = 0;
      for (int i = 3; i < alphabet.size(); i += 5) {
        count += Integer.parseInt(alphabet.elementAt(i));
      }
      Konse = new String[count];
      indes = 0;
      for (int i = 3; i < alphabet.size(); i += 5) {
        for (int j = 0;
             j < Integer.parseInt(alphabet.elementAt(i)); j++) {
          Konse[indes] = alphabet.elementAt(i - 3);
          indes++;
        }
      }
    } catch (NumberFormatException nex) {
      Meldung +=
          "\nFehlerhafter Eintrag in der Alphabetdatei. Standartwerte werden für Konsonanten am Ende verwendet.";
      sKonse();
    }
    try {
      count = 0;
      for (int i = 2; i < alphabet.size(); i += 5) {
        count += Integer.parseInt(alphabet.elementAt(i));
      }
      Vok = new String[count];
      indes = 0;
      for (int i = 2; i < alphabet.size(); i += 5) {
        for (int j = 0;
             j < Integer.parseInt(alphabet.elementAt(i)); j++) {
          Vok[indes] = alphabet.elementAt(i - 2);
          indes++;
        }
      }
    } catch (NumberFormatException nex) {
      Meldung +=
          "\nFehlerhafter Eintrag in der Alphabetdatei. Standartwerte werden für Vokale verwendet.";
      sVok();
    }
    try {
      count = 0;
      for (int i = 4; i < alphabet.size(); i += 5) {
        count += Integer.parseInt(alphabet.elementAt(i));
      }
      Suff = new String[count];
      indes = 0;
      for (int i = 4; i < alphabet.size(); i += 5) {
        for (int j = 0;
             j < Integer.parseInt(alphabet.elementAt(i)); j++) {
          Suff[indes] = alphabet.elementAt(i - 4);
          indes++;
        }
      }
    } catch (NumberFormatException nex) {
      Meldung +=
          "\nFehlerhafter Eintrag in der Alphabetdatei. Standartwerte werden für Endungen verwendet.";
      sSuff();
    }
  }

  private static void anfangsauswahl() {
    anfang.removeAll();
    anfang.add("");

    try {
      for (int index = 1; index < alphabet.size(); index += 5) {
        if (Integer.parseInt(alphabet.elementAt(index)) > 0) {
          anfang.add(alphabet.elementAt(index - 1));
        }
      }
    } catch (NullPointerException npe) {
      Meldung += "\nFehler beim Einlesen der Anfangsbuchstaben";
    }
  }

  public void itemStateChanged(ItemEvent e) {
    alphaeinlesen();
  }

  private static void sKonsa() {
    Konsa =
        new String[] {"b", "b", "ch", "ch", "c", "d", "d", "d", "d", "d", "f",
            "f", "g", "g", "h", "h", "h", "k", "l", "l", "l", "m", "m", "m",
            "n", "n", "n", "n", "n", "n", "n", "n", "n", "n", "g", "p", "r",
            "r", "r", "r", "r", "r", "r", "s", "s", "s", "s", "s", "s", "st",
            "t", "t", "t", "t", "t", "v", "w", "w", "z", "'", "'", "'", "'",
            "'", "'", "'", "'", "'", "'", "'"};
  }

  private static void sKonse() {
    Konse =
        new String[] {"b", "b", "ch", "ch", "ck", "d", "d", "d", "d", "d", "f",
            "f", "g", "g", "h", "h", "h", "l", "l", "l", "m", "m", "m", "n",
            "n", "n", "n", "n", "n", "n", "n", "nk", "ng", "p", "r", "r", "r",
            "r", "r", "r", "r", "s", "s", "s", "s", "s", "s", "s", "t", "t",
            "t", "t", "t", "t", "v", "w", "w", "z"};
  }

  private static void sVok() {
    Vok = new String[] {"a", "a", "a", "a", "aa", "au", "e", "e", "e", "e", "e",
        "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "ei", "eu", "i", "i",
        "i", "i", "i", "i", "i", "o", "oo", "u", "u"};
  }

  private static void sSuff() {
    Suff =
        new String[] {"ung", "nis", "keit", "en", "ig", "lich", "lein", "chen",
            "isch", "bar", "in"};
  }

  private static boolean Einzigartigkeit(String testname) {
    try {
      testname = URLEncoder.encode(testname, "UTF-8");
      String suchanfrage = "http://de.search.yahoo.com/search?p=%2B"
          + testname;  //Google mag das nicht
      URL url = new URL(suchanfrage);
      URLConnection con = url.openConnection();
      InputStream urlinstr = con.getInputStream();
      InputStreamReader isr = new InputStreamReader(urlinstr);
      BufferedReader br = new BufferedReader(isr);
      StringBuilder datei = new StringBuilder();
      String zeile = br.readLine();
      while (zeile != null) {
        datei.append(zeile);
        zeile = br.readLine();
      }
      br.close();

      Matcher matcher = Pattern.compile("Es wurden keine Ergebnisse gefunden")
          .matcher(datei.toString());
      if (matcher.find())
        return true;
    } catch (MalformedURLException e) {
      Meldung += " Die Einzigartigkeit konnte nicht geprüft werden.";
      return true;
    } catch (IOException e) {
      Meldung +=
          " Die Einzigartigkeit konnte nicht geprüft werden. Ev. steht keine Internetverbindung zur Verfügung?";
      return true;
    }
    return false;
  }

  public static void main(String[] args) {
    Zufallsnamengenerator frm = new Zufallsnamengenerator();
    WindowQuitter wquit = new WindowQuitter();
    frm.addWindowListener(wquit);
    frm.setSize(700, 433);
    frm.setVisible(true);
  }
}

class WindowQuitter extends WindowAdapter {
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }
}
