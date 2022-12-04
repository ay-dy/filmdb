package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;

@WebServlet("/filmapi")
public class FilmAPI extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	FilmDAO dao = FilmDAO.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		PrintWriter writer = response.getWriter();
		String format = request.getParameter("format");
		String searchString = request.getParameter("q");
		String output = "";

		switch (format) {
		case "json":
			response.setContentType("application/json");
			break;
		case "xml":
			response.setContentType("text/xml");
			break;
		default:
			response.setContentType("text/plain");
			break;
		}

		if (searchString == null) {
			writer.print(FilmUtils.getFormattedFilms(dao.getAllFilms(), format));
		} else {
			writer.print(FilmUtils.getFormattedFilms(dao.searchFilms(searchString), format));
		}

		writer.print(output);
		writer.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

	}

}
