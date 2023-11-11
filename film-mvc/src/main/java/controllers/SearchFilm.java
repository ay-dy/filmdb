package controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;
import models.Film;

@WebServlet("/search")
public class SearchFilm extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		FilmDAO dao = FilmDAO.getInstance();
		String searchStr = request.getParameter("q");

		if (searchStr != "") {
			ArrayList<Film> foundFilms = dao.searchFilms(searchStr);
			// Keep user input in the search bar when the home page is loaded again.
			request.setAttribute("searchString", searchStr);
			// Store searchFilms() results in the same table as getAllFilms() results.
			request.setAttribute("films", foundFilms);
			request.setAttribute("resultCount", foundFilms.size());

			RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
			dispatcher.forward(request, response);
		} else {
			// Don't process the request any further. Redirect back to home page.
			response.sendRedirect("http://localhost:8081/film-mvc/home");
		}

	}

}
