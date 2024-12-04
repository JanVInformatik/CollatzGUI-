import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CollatzGUI {
  public static void main(String[] args) {
    // Create the main frame
    JFrame frame = new JFrame("Collatz Conjecture Plotter");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 400);

    // Create a panel for input and button
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());

    JLabel label = new JLabel("Enter a number:");
    JTextField textField = new JTextField(10);
    JButton plotButton = new JButton("Plot");

    panel.add(label);
    panel.add(textField);
    panel.add(plotButton);

    // Create a panel for the graph
    GraphPanel graphPanel = new GraphPanel();

    // Add panels to the frame
    frame.setLayout(new BorderLayout());
    frame.add(panel, BorderLayout.NORTH);
    frame.add(graphPanel, BorderLayout.CENTER);

    // Add action listener to the button
    plotButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          long n = Long.parseLong(textField.getText());
          ArrayList<Long> sequence = new ArrayList<>();
          sequence.add(n);

          while (n > 1) {
            if (n % 2 == 0) {
              n /= 2;
            } else {
              n = (n * 3) + 1;
            }
            sequence.add(n);
          }

          graphPanel.setSequence(sequence);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(frame, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Display the frame
    frame.setVisible(true);
  }
}

class GraphPanel extends JPanel {
  private ArrayList<Long> sequence;

  public GraphPanel() {
    sequence = new ArrayList<>();
  }

  public void setSequence(ArrayList<Long> sequence) {
    this.sequence = sequence;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (sequence.isEmpty()) {
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();
    int padding = 50;
    int pointWidth = 5;

    long maxValue = sequence.stream().max(Long::compare).orElse(1L);

    int graphWidth = width - 2 * padding;
    int graphHeight = height - 2 * padding;

    int xStep = graphWidth / (sequence.size() - 1);
    int yStep = graphHeight / (int) maxValue;

    // Draw axes
    g2d.drawLine(padding, height - padding, padding, padding);
    g2d.drawLine(padding, height - padding, width - padding, height - padding);

    // Plot points
    for (int i = 0; i < sequence.size(); i++) {
      int x = padding + i * xStep;
      int y = height - padding - (int) (sequence.get(i) * yStep);

      g2d.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth);

      if (i > 0) {
        int prevX = padding + (i - 1) * xStep;
        int prevY = height - padding - (int) (sequence.get(i - 1) * yStep);
        g2d.drawLine(prevX, prevY, x, y);
      }
    }
  }
}
