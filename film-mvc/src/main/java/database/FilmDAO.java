package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Film;

import java.sql.*;

public class FilmDAO {

	
	Film oneFilm = null;
	Connection conn = null;
	Statement stmt = null;
	String user = "suchonad";
	String password = "kothbaiL4";
	// Note that the non-default port 6306 was used instead of 3306.
	String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

	private FilmDAO() {
	}

	private static FilmDAO instance = new FilmDAO();
	// Make sure that only one instance of FilmDAO exists.
	// "synchronized" avoids potential threading deadlock.
	public static synchronized FilmDAO getInstance()
	{
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

	public void insertFilm(Film f)
	{
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
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void updateFilm(Film f)
	{
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
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void deleteFilm(int id)
	{
		openConnection();

		// Create a DELETE statement and execute it.
		try {
			String deleteSQL = "DELETE FROM films WHERE id=?";
			PreparedStatement deletePs = conn.prepareStatement(deleteSQL);

			deletePs.setInt(1, id);

			deletePs.executeUpdate();

			closeConnection();
		} catch (SQLException e) {
			System.out.println(e);
		}
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

}
