import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;


class Cell extends JPanel {

  public Cell(Board b, int r, int c) {
    state = new UnInit(this);
    board = b;
    row = r;
    col = c;
  }

  public LinkedList<Cell> getNeighbors() {
    LinkedList<Cell> n = new LinkedList<Cell>();

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i != 0 || j != 0) {
          try { n.add(board.get(row + i, col + j)); }
          catch(RuntimeException e) {}
        }
      }
    }

    return n;
  }

  public void init() {
    state = new UnChecked();
    state = new UnChecked(this);
  }
  public void check() {
    state = new Checked();
    state = new Checked(this);
  }
  public void flag() { state = new Flagged(this); }

  private abstract class State { State(Cell c) { cell = c;  if (cell != null) { c.removeAll(); } } protected Cell cell; }

  private class Flagged extends State implements ActionListener {
    public Flagged(Cell c) {
      super(c);
      JButton b = new JButton("F");
      b.addActionListener(this);
      c.add(b); c.revalidate(); c.repaint();
    }

    public void actionPerformed(ActionEvent e) {
      cell.state = new UnChecked(cell);
    }
  }

  private class Checked extends State {
    public Checked() { super(null); }
    public Checked(Cell c) {
      super(c);

      String label = "";

      if (!c.mine) {
        int bombs = 0;
        LinkedList<Cell> ns = c.getNeighbors();
        for (Cell n : ns) { bombs += n.mine ? 1 : 0; }
        if (bombs == 0) { for (Cell n : ns) { if (n.state instanceof UnChecked) { n.check(); } } }

        label = Integer.toString(bombs);
      } else { label = "B"; }

      cell.add(new JLabel(label));
      cell.revalidate(); cell.repaint();
    }
  }

  private class UnChecked extends State implements ActionListener {
    public UnChecked() { super(null); }
    public UnChecked(Cell c) {
      super(c);
      JButton b = new JButton(" ");
      b.addActionListener(this);
      c.add(b); c.revalidate(); c.repaint();

      LinkedList<Cell> ns = cell.getNeighbors();
      for (Cell n: ns) { if (n.state instanceof UnInit) { n.mine = Math.random() > .8;  n.init(); } }
    }

    public void actionPerformed(ActionEvent e) {
      String mod = (String)board.mods.getSelectedItem();
      if (mod.equals("Dig")) { cell.check(); }
      else if (mod.equals("Flag")) { cell.flag(); }
    }
  }

  private class UnInit extends State implements ActionListener {
    public UnInit(Cell c) {
      super(c);
      JButton b = new JButton(" ");
      b.addActionListener(this);
      c.add(b); c.revalidate(); c.repaint();
    }

    public void actionPerformed(ActionEvent e) {
      cell.mine = false;

      LinkedList<Cell> ns = cell.getNeighbors();
      cell.state = new UnChecked();
      for (Cell n: ns) { n.mine = false; n.init(); }
      cell.state = new UnChecked(cell);
      cell.check();
    }
  }
  private Board board;

  private State state;
  private boolean mine;

  private int row;
  private int col;
}
