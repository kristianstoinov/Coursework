package kursova;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.PatternSyntaxException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DisplayQueryResults extends JFrame {

	static final String DRIVER = "com.mysql.jdbc.Driver";
	static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mydbKursova"; 
	static final String USERNAME = "root";
	static final String PASSWORD = "kristian4o";

	static final String DEFAULT_QUERY = "SELECT * FROM plants";
	// static final String Search_Query = "CALL genus_search";
	private ResultSetTableModel tableModel;
	private JTextArea queryArea;

	private BufferedImage image;

	public void ImagePanel(String path) {
		try {

			image = ImageIO.read(new File(path));
			ImageIcon icon = new ImageIcon(image);
			JLabel iconLabel = new JLabel(icon);
			JOptionPane.showMessageDialog(null, iconLabel);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// GUI
	public DisplayQueryResults() {
		super("Displaying Query Results");

		try {
			Connection con = DriverManager.getConnection(DATABASE_URL,
					USERNAME, PASSWORD);

			tableModel = new ResultSetTableModel(DRIVER, DATABASE_URL,
					USERNAME, PASSWORD, DEFAULT_QUERY);

			queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
			queryArea.setWrapStyleWord(true);
			queryArea.setLineWrap(true);

			JScrollPane scrollPane = new JScrollPane(queryArea,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

			Box boxNorth = Box.createHorizontalBox();

			JTable resultTable = new JTable(tableModel);

			JLabel filterLabel = new JLabel("Filter:");
			final JTextField filterText = new JTextField();

			JButton filterButton = new JButton("Search");
			Box boxSouth = boxNorth.createHorizontalBox();

			boxSouth.add(filterLabel);
			boxSouth.add(filterText);
			boxSouth.add(filterButton);

			add(boxNorth, BorderLayout.NORTH);
			add(new JScrollPane(resultTable), BorderLayout.CENTER);
			add(boxSouth, BorderLayout.SOUTH);
			final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
					tableModel);
			resultTable.setRowSorter(sorter);
			setSize(600, 700);
			setVisible(true);
			setTitle("Taxonomy");
			setLocationRelativeTo(null);

			filterButton.addActionListener(new ActionListener() {
				// pass filter text to listener
				public void actionPerformed(ActionEvent e) {
					String text = filterText.getText();

					if (text.length() == 0)
						sorter.setRowFilter(null);
					else {
						try {
							sorter.setRowFilter(RowFilter.regexFilter(text));
							if (text.contains("Salvia")) {
								String path = "C://Users/kasi/Desktop/Salvia.jpg";
								ImagePanel(path);
							}
							if (text.contains("Papaver")) {
								String path = "C://Users/kasi/Desktop/Poppy.jpg";
								ImagePanel(path);
							}
						} catch (PatternSyntaxException pse) {
							JOptionPane.showMessageDialog(null,
									"Bad regex pattern", "Bad regex pattern",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		} catch (ClassNotFoundException classNotFound) {
			JOptionPane.showMessageDialog(null, "Database Driver not found",
					"Driver not found", JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		} catch (SQLException sqlException) {
			JOptionPane.showMessageDialog(null, sqlException.getMessage(),
					"Database error", JOptionPane.ERROR_MESSAGE);

			tableModel.disconnectFromDatabase();

			System.exit(1);
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		addWindowListener(

		new WindowAdapter() {

			public void windowClosed(WindowEvent event) {
				tableModel.disconnectFromDatabase();
				System.exit(0);
			}
		});
	}

	public static void main(String args[]) {
		new DisplayQueryResults();
	}
}
