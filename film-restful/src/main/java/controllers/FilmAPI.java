package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;
import models.Film;

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

		if (request.getParameter("q") == null) {
			ArrayList<Film> allFilms = dao.getAllFilms();
			output = FilmUtils.getFormattedFilms(allFilms, format);

		} else {
			String q = request.getParameter("q");
			ArrayList<Film> foundFilms = dao.searchFilms(q);
			output = FilmUtils.getFormattedFilms(foundFilms, format);
		}

		System.out.println(output);
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
