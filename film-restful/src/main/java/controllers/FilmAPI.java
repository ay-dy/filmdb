package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import database.FilmDAO;
import jakarta.xml.bind.JAXBException;

@WebServlet("/filmapi")
public class FilmAPI extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	FilmDAO dao = FilmDAO.getInstance();
	Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		PrintWriter writer = response.getWriter();
		String option = request.getParameter("opt");
		String format = request.getParameter("format");

		response.setContentType(FilmUtils.getContentType(format));

		// Executed when page is loaded.
		if (option.equals("af")) {
			writer.print(FilmUtils.getFormattedFilms(dao.getAllFilms(), format));
		}

		// Executed when "search" form is submitted.
		if (option.equals("sr")) {
			String searchQuery = request.getParameter("q");
			writer.print(FilmUtils.getFormattedFilms(dao.searchFilms(searchQuery), format));
		}

		// Executed when page is loaded and when a new film is added to database.
		if (option.equals("nai")) {
			writer.print(dao.getNextAutoIncrement());
		}

		// Executed when "edit" button is clicked

		writer.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter writer = response.getWriter();

		boolean inserted = false;
		
		try {
			inserted = dao.insertFilm(FilmUtils.getDecodedFilm(request));
		} catch (JsonSyntaxException | JsonIOException | IOException | JAXBException e) {
			e.printStackTrace();
		}

		if (inserted) {
			writer.print("Film inserted successfully");
		} else {
			writer.print("Failed to insert film. Please try again.");
		}

		writer.close();
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Update existing film
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		PrintWriter writer = response.getWriter();
		
		boolean updated = false;
		try {
			updated = dao.updateFilm(FilmUtils.getDecodedFilm(request));
		} catch (JsonSyntaxException | JsonIOException | IOException | JAXBException e) {
			e.printStackTrace();
		}

		if (updated) {
			writer.print("Film updated successfully");
		} else {
			writer.print("Failed to update film. Please try again.");
		}
		writer.close();

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		PrintWriter writer = response.getWriter();
		int id = Integer.parseInt(request.getParameter("id"));

		boolean deleted = dao.deleteFilm(id);

		if (deleted) {
			writer.print("Film deleted successfully.");
		} else {
			writer.print("Failed to delete film. Please try again.");
		}

		writer.close();
	}
}
