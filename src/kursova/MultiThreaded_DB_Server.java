package kursova;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

public class MultiThreaded_DB_Server {
	boolean ServerOn = true;
	ServerSocket s;
	{
		int i = 1;
		try {

			s = new ServerSocket(12345);
		} catch (Exception e) {
			System.out
					.println("Could not create server socket on the selected port. Quitting.");
			System.exit(-1);
		}
		while (ServerOn) {
			try {

				Socket incoming = s.accept();
				System.out.println("Spawning " + i);

				new DBClientSessionThread(incoming, i).start();
				i++;
			} catch (Exception e) {
				System.out
						.println("Exception encountered on accept. Ignoring. Stack Trace :");
				e.printStackTrace();

			}

		}
		try {
			s.close();
			System.out.println("Server Stopped");
		} catch (Exception e) {
			System.out.println("Problem stopping server socket");
			System.exit(-1);
		}

	}

	class DBClientSessionThread extends Thread {
		ObjectInputStream input;
		ObjectOutputStream output;

		public DBClientSessionThread(Socket i, int c) {
			incoming = i;
			counter = c;
		}

		public void run() {
			try {

				input = new ObjectInputStream(incoming.getInputStream());
				output = new ObjectOutputStream(incoming.getOutputStream());

				boolean done = false;
				while (!done) {
					String query = (String) input.readObject();
					if (query.isEmpty() || query.equals("BYE"))
						done = true;
					else {
						if (query.startsWith("SELECT")
								|| query.startsWith("select"))
							output.writeObject(getResultSetQuery(query));
						else {
							executeSQLCommand(query); // update, insert, delete
							output.writeObject(null);
						}

					}
				}
				incoming.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}

		ResultSet getResultSetQuery(String query) {

			return null;
		}

		void executeSQLCommand(String sqlCommand) {

			return;
		}

		private Socket incoming;
		private int counter;
	}

}
