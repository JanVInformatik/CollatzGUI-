import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// ACHTUNG: Die @Override (s) stehen da nicht ohne Grund
public class CollatzGUI {
  public static void main(String[] args) {

    JFrame fenster = new JFrame("Collatz Plotter");
    fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fenster.setSize(1920, 1080);

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());

    JLabel label = new JLabel("Eingabe: ");
    JTextField textfeld = new JTextField(10);
    JButton button = new JButton("Zeichnen");

    panel.add(label);
    panel.add(textfeld);
    panel.add(button);

    GraphPanel graphenPanel = new GraphPanel();

    JTextArea ausgabenFenster = new JTextArea(10, 50);
    ausgabenFenster.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(ausgabenFenster);

    fenster.setLayout(new BorderLayout());
    fenster.add(panel, BorderLayout.NORTH);
    fenster.add(graphenPanel, BorderLayout.CENTER);
    fenster.add(scrollPane, BorderLayout.SOUTH);

    // Bereits implementierte Logik: (+ ArrayList ;) )
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          long n = Long.parseLong(textfeld.getText());
          long originalInput = n;
          ArrayList<Long> sequenz = new ArrayList<>();
          sequenz.add(n);

          // Vorherige Ausgabe "loeschen"
          ausgabenFenster.setText("");

          int schritte = 0;

          while (n > 1) {
            if (n % 2 == 0) {
              n /= 2;
            } else {
              n = (n * 3) + 1;
            }
            sequenz.add(n);
            schritte++;

            if ((Math.log(n) / Math.log(2)) % 1 == 0) {
              ausgabenFenster.append("N = " + n + " ist eine Zweierpotenz." + "\n");
            }
          }
          ausgabenFenster.append("Man braucht " + schritte + " Schritte, um 1 zu erreichen. Input: " + originalInput);

          graphenPanel.setSequenz(sequenz);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(fenster, "Bitte geben Sie eine passende Nummer ein.", "Fehler",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    fenster.setVisible(true);
  }
}

class GraphPanel extends JPanel {
  private ArrayList<Long> sequenz;

  public GraphPanel() {
    sequenz = new ArrayList<>();
  }

  public void setSequenz(ArrayList<Long> sequenz) {
    this.sequenz = sequenz;
    repaint();
  }

  // NICHT LOESCHEN: Implementiert aus einer Schnittstelle
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (sequenz.isEmpty()) {
      return;
    }

    Graphics2D graphik2D = (Graphics2D) g;
    graphik2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int breite = getWidth();
    int hoehe = getHeight();
    int abstand = 50;
    int punktWeite = 5;

    // Maximalwert berechnen
    long maxWert = sequenz.stream().max(Long::compare).orElse(1L);

    // Graphikbereich berechnen
    int graphenBreite = breite - 2 * abstand;
    int graphenHoehe = hoehe - 2 * abstand;

    // Schrittweite berechnen
    int xSchritt = graphenBreite / (sequenz.size() - 1);
    int ySchritt = graphenHoehe / (int) maxWert;
    double ySkalierung = (double) graphenHoehe / maxWert;

    // Achsen
    graphik2D.drawLine(abstand, hoehe - abstand, abstand, abstand);
    graphik2D.drawLine(abstand, hoehe - abstand, breite - abstand, hoehe - abstand);

    // Beschriftung hierfuer
    graphik2D.drawString("Schritt", breite / 2, hoehe - abstand / 2);
    graphik2D.drawString("Wert", abstand / 4, hoehe / 2);

    // Plotte Punkte
    for (int i = 0; i < sequenz.size(); i++) {
      int x = abstand + i * xSchritt;
      int y = hoehe - abstand - (int) (sequenz.get(i) * ySchritt);

      graphik2D.fillOval(x - punktWeite / 2, y - punktWeite / 2, punktWeite, punktWeite);

      if (i > 0) {
        int prevX = abstand + (i - 1) * xSchritt;
        int prevY = hoehe - abstand - (int) (sequenz.get(i - 1) * ySchritt);
        graphik2D.drawLine(prevX, prevY, x, y);
      }
    }

    for (int i = 0; i < sequenz.size(); i++) {
      int x = abstand + i * xSchritt;
      graphik2D.drawLine(x, hoehe - abstand, x, hoehe - abstand + 5);
      if (i % 5 == 0) {
        graphik2D.drawString(String.valueOf(i), x - 5, hoehe - abstand + 20);
      }
    }

    int tickCount = 10;
    for (int i = 0; i <= tickCount; i++) {
      int y = hoehe - abstand - i * graphenHoehe / tickCount;
      graphik2D.drawLine(abstand - 5, y, abstand, y);
      String label = String.valueOf(maxWert * i / tickCount);
      graphik2D.drawString(label, abstand - 40, y + 5);
    }

    for (int i = 0; i < sequenz.size() - 1; i++) {
      int x1 = abstand + i * xSchritt;
      int y1 = hoehe - abstand - (int) (sequenz.get(i) * ySkalierung);
      int x2 = abstand + (i + 1) * xSchritt;
      int y2 = hoehe - abstand - (int) (sequenz.get(i + 1) * ySkalierung);

      // Draw line
      graphik2D.setColor(Color.BLUE);
      graphik2D.drawLine(x1, y1, x2, y2);

      // Draw points
      graphik2D.setColor(Color.RED);
      graphik2D.fillOval(x1 - punktWeite / 2, y1 - punktWeite / 2, punktWeite, punktWeite);
      graphik2D.fillOval(x2 - punktWeite / 2, y2 - punktWeite / 2, punktWeite, punktWeite);
    }
  }
}
