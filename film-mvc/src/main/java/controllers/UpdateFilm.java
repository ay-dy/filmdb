package controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;
import models.Film;

@WebServlet("/update")
public class UpdateFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		FilmDAO dao = FilmDAO.getInstance();

		int id = Integer.parseInt(request.getParameter("uid"));

		/*
		 * Pre-populate the "update" form data fields with the data from the selected
		 * film.
		 */
		request.setAttribute("film", dao.getFilmByID(id));

		RequestDispatcher dispatcher = request.getRequestDispatcher("update.jsp");
		dispatcher.include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		FilmDAO dao = FilmDAO.getInstance();

		int id = Integer.parseInt(request.getParameter("update-button"));

		// Get the film from database for comparison against user input.
		Film film = dao.getFilmByID(id);

		/*
		 * Store film data in the same order as it is in the database (title, year,
		 * director, stars, review).
		 */
		String[] data = request.getParameterValues("update-data");

		if (	// Check if the user made any changes.
				!(
						data[0].equals(film.getTitle()) 
						&& Integer.parseInt(data[1]) == film.getYear()
						&& data[2].equals(film.getDirector())
						&& data[3].equals(film.getStars())
						&& data[4].equals(film.getReview())
				)
			) 
		{
			/*
			 * Specify which film to update. Use the data array to populate the remaining
			 * fields.
			 */
			dao.updateFilm(new Film(id, data[0], Integer.parseInt(data[1]), data[2], data[3], data[4]));
		}

		/*
		 * Redirect the response to prevent the "update" form from being resubmitted
		 * upon page refresh.
		 */
		response.sendRedirect("http://localhost:8081/film-mvc/home");
	}

}
