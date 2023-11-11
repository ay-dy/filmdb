package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Film;

import java.sql.*;

public class FilmDAO
{

	private static FilmDAO instance = null;
	Film oneFilm = null;
	Connection conn = null;
	Statement stmt = null;
	String user = "suchonad";
	String password = "kothbaiL4";
	// Note that the non-default port 6306 was used instead of 3306.
	String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

	private FilmDAO() {
	}

	// Make sure that only one instance of FilmDAO exists.
	// "synchronized" avoids potential threading deadlock.
	public static synchronized FilmDAO getInstance()
	{
		if (instance == null) {
			return new FilmDAO();
		}
		return instance;
	}

	private void openConnection()
	{
		// Loading the JDBC driver for MySQL.
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println(e);
		}

		// Connecting to the database.
		try {
			// Connection string for demos database, username demos, and password demos.
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
		} catch (SQLException se) {
			System.out.println(se);
		}
	}

	private void closeConnection()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Film getNextFilm(ResultSet rs)
	{
		Film thisFilm = null;
		try {
			thisFilm = new Film(rs.getInt("id"), rs.getString("title"), rs.getInt("year"), rs.getString("director"),
					rs.getString("stars"), rs.getString("review"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return thisFilm;
	}

	public ArrayList<Film> getAllFilms()
	{
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();

		// Create a SELECT statement and execute it.
		try {
			String selectSQL = "SELECT * FROM films";
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results.
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
				allFilms.add(oneFilm);
			}

			stmt.close();
			closeConnection();
		} catch (SQLException e) {
			System.out.println(e);
		}

		return allFilms;
	}

	public Film getFilmByID(int id)
	{
		openConnection();
		oneFilm = null;

		// Create a SELECT statement and execute it.
		try {
			String selectSQL = "SELECT * FROM films WHERE id=" + id;
			ResultSet rs1 = stmt.executeQuery(selectSQL);
			// Retrieve the results.
			while (rs1.next()) {
				oneFilm = getNextFilm(rs1);
			}

			stmt.close();
			closeConnection();
		} catch (SQLException e) {
			System.out.println(e);
		}

		return oneFilm;
	}

	public boolean insertFilm(Film f)
	{
		boolean inserted;
		openConnection();

		// Create an INSERT statement and execute it.
		try {
			String insertSQL = "INSERT INTO films (title, year, director, stars, review) VALUES(?,?,?,?,?)";
			PreparedStatement insertPs = conn.prepareStatement(insertSQL);

			// Set the query parameters (replace "?" in the statement with actual data).
			insertPs.setString(1, f.getTitle());
			insertPs.setInt(2, f.getYear());
			insertPs.setString(3, f.getDirector());
			insertPs.setString(4, f.getStars());
			insertPs.setString(5, f.getReview());

			insertPs.executeUpdate();

			closeConnection();
			inserted = true;
		} catch (SQLException e) {
			System.out.println(e);
			inserted = false;
		}
		return inserted;
	}

	public boolean updateFilm(Film f)
	{
		boolean updated;
		openConnection();

		// Create an UPDATE statement and execute it.
		try {
			String updateSQL = "UPDATE films SET title=?, year=?, director=?, stars=?, review=? WHERE id=?";
			PreparedStatement updatePs = conn.prepareStatement(updateSQL);

			// Set the query parameters (replace "?" in the statement with actual data).
			updatePs.setString(1, f.getTitle());
			updatePs.setInt(2, f.getYear());
			updatePs.setString(3, f.getDirector());
			updatePs.setString(4, f.getStars());
			updatePs.setString(5, f.getReview());
			updatePs.setInt(6, f.getId());

			updatePs.executeUpdate();

			closeConnection();
			updated = true;
		} catch (SQLException e) {
			System.out.println(e);
			updated = false;
		}
		return updated;
	}

	public boolean deleteFilm(int id)
	{
		boolean deleted;
		openConnection();

		// Create a DELETE statement and execute it.
		try {
			String deleteSQL = "DELETE FROM films WHERE id=?";
			PreparedStatement deletePs = conn.prepareStatement(deleteSQL);

			deletePs.setInt(1, id);
			deletePs.executeUpdate();

			closeConnection();
			deleted = true;
		} catch (SQLException e) {
			System.out.println(e);
			deleted = false;
		}
		return deleted;
	}

	public ArrayList<Film> searchFilms(String searchStr)
	{
		ArrayList<Film> searchResults = new ArrayList<Film>();
		openConnection();

		// Create a SELECT statement and execute it.
		try {
			String searchSQL = "SELECT * FROM films WHERE title LIKE '%" + searchStr + "%';";
			ResultSet srs = stmt.executeQuery(searchSQL);
			// Retrieve the results.
			while (srs.next()) {
				oneFilm = getNextFilm(srs);
				searchResults.add(oneFilm);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}

		return searchResults;
	}

	public int getNextAutoIncrement()
	{
		int id = 0;
		openConnection();

		try {
			String selectSQL = "SELECT auto_increment FROM information_schema.tables WHERE table_name = 'films'";
			ResultSet rs = stmt.executeQuery(selectSQL);

			while (rs.next()) {
				id = rs.getInt("Auto_increment");
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return id;
	}

}
