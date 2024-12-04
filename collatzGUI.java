import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// ACHTUNG: Die @Override (s) stehen da nicht ohne Grund
public class CollatzGUI {
  public static void main(String[] args) {

    JFrame fenster = new JFrame("Collatz Conjecture Plotter");
    fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fenster.setSize(1920, 1080);

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());

    JLabel label = new JLabel("Input: ");
    JTextField textfeld = new JTextField(10);
    JButton button = new JButton("Plot");

    panel.add(label);
    panel.add(textfeld);
    panel.add(button);

    GraphPanel graphenPanel = new GraphPanel();

    fenster.setLayout(new BorderLayout());
    fenster.add(panel, BorderLayout.NORTH);
    fenster.add(graphenPanel, BorderLayout.CENTER);

    // Bereits implementierte Logik: (ArrayList ;) )
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          long n = Long.parseLong(textfeld.getText());
          ArrayList<Long> sequenz = new ArrayList<>();
          sequenz.add(n);

          while (n > 1) {
            if (n % 2 == 0) {
              n /= 2;
            } else {
              n = (n * 3) + 1;
            }
            sequenz.add(n);
          }

          graphenPanel.setSequenz(sequenz);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(fenster, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
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
    int padding = 50;
    int punktWeite = 5;

    // Maximalwert berechnen
    long maxWert = sequenz.stream().max(Long::compare).orElse(1L);

    // Graphikbereich berechnen
    int graphenBreite = breite - 2 * padding;
    int graphenHoehe = hoehe - 2 * padding;

    // Schrittweite berechnen
    int xStep = graphenBreite / (sequenz.size() - 1);
    int yStep = graphenHoehe / (int) maxWert;

    // Achsen
    graphik2D.drawLine(padding, hoehe - padding, padding, padding);
    graphik2D.drawLine(padding, hoehe - padding, breite - padding, hoehe - padding);

    // Punkte und Verbindungen zeichnen
    for (int i = 0; i < sequenz.size(); i++) {
      int x = padding + i * xStep;
      int y = hoehe - padding - (int) (sequenz.get(i) * yStep);

      graphik2D.fillOval(x - punktWeite / 2, y - punktWeite / 2, punktWeite, punktWeite);

      if (i > 0) {
        int prevX = padding + (i - 1) * xStep;
        int prevY = hoehe - padding - (int) (sequenz.get(i - 1) * yStep);
        graphik2D.drawLine(prevX, prevY, x, y);
      }
    }
  }
}
