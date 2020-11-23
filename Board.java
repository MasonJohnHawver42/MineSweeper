import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

class Board extends JPanel {

  public Board(int rs, int cs) {


    JPanel cells = new JPanel();
    cells.setLayout(new GridLayout(rs, cs));

    board = new Cell[rs][cs];


    for (int i = 0; i < rs; i++) {
      for (int j = 0; j < cs; j++) {
        Cell c = new Cell(this, i, j);
        board[i][j] = c; cells.add(c).setLocation(i, j);;
      }
    }

    String[] data = {"Dig", "Flag"};
    mods = new JComboBox(data);
    JPanel options = new JPanel();
    options.add(mods);

    BoxLayout boxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(boxlayout);

    add(cells);
    add(options);
  }

  public boolean exists(int i, int j) { return (i >= 0 && i < board.length ) && (j >= 0 && j < board[0].length ); }
  public Cell get(int i, int j) {
    if (exists(i, j)) { return board[i][j]; }
    throw new RuntimeException();
  }

  private Cell[][] board;
  public JComboBox<String> mods;

  public static void main(String[] args) {
    Board b = new Board(10, 10);

    JFrame frame = new JFrame("MineSweeper");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.add(b);
    frame.pack();

    frame.setVisible(true);
  }
}
