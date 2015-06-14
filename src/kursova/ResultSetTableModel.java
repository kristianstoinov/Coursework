package kursova;

import java.beans.Statement;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel {

	private Connection connection;
	private java.sql.Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	private boolean connectedToDatabase = false;

	public ResultSetTableModel(String driver, String url, String username,
			String password, String query) throws SQLException,
			ClassNotFoundException {

		Class.forName(driver);
		connection = DriverManager.getConnection(url, username, password);

		statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		connectedToDatabase = true;

		setQuery(query);
	}

	// Calls database
	public ResultSetTableModel(String query) throws SQLException,
			ClassNotFoundException {

		try {
			setRemoteQuery(query);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}

	}

	public Class getColumnClass(int column) throws IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {
			String className = metaData.getColumnClassName(column + 1);

			return Class.forName(className);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return Object.class;
	}

	public int getColumnCount() throws IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {
			return metaData.getColumnCount();
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return 0;
	}

	public String getColumnName(int column) throws IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {
			return metaData.getColumnName(column + 1);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return "";
	}

	public int getRowCount() throws IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		return numberOfRows;
	}

	public Object getValueAt(int row, int column) throws IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		try {
			resultSet.absolute(row + 1);
			return resultSet.getObject(column + 1);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return "";
	}

	public void setQuery(String query) throws SQLException,
			IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		resultSet = statement.executeQuery(query);

		metaData = resultSet.getMetaData();

		resultSet.last();
		numberOfRows = resultSet.getRow();

		fireTableStructureChanged();
	}

	private ObjectOutputStream output;
	private ObjectInputStream input;

	private void getStreams() throws IOException {

		output = new ObjectOutputStream(dbServer.getOutputStream());
		output.flush();

		input = new ObjectInputStream(dbServer.getInputStream());

	}

	private Socket dbServer;

	public void setRemoteQuery(String query) throws SQLException,
			IllegalStateException, Exception {

		dbServer = new Socket(InetAddress.getByName("localhost"), 12345);

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		output.writeObject(query);
		resultSet = (ResultSet) input.readObject();

		if (resultSet == null && query.startsWith("SELECT")
				&& query.startsWith("select"))
			connectedToDatabase = true;

		metaData = resultSet.getMetaData();

		resultSet.last();
		numberOfRows = resultSet.getRow();

		fireTableStructureChanged();
	}

	public void disconnectFromDatabase() {
		if (connectedToDatabase) {

			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException sqlException) {
				sqlException.printStackTrace();
			} finally {
				connectedToDatabase = false;
			}
		}
	}

}
