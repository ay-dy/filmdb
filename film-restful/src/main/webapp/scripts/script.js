// Request initialisers
function jsonFilmTable(resultRegion) {
	let address = "filmapi";
	const data = "?format=json";
	address += data
	ajaxGet(address, resultRegion, function(request) {
		displayJsonFilmTable(request, resultRegion);
	});
}

// Request functions
function ajaxGet(address, resultRegion, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request, resultRegion);
	}
	request.open("GET", address, true);
	request.send(null);
}

function displayJsonFilmTable(request, resultRegion) {
	if (request.readyState == 4 && request.status == 200) {
		const films = JSON.parse(request.responseText);

		const headings = [];
		const preparedFilms = [];

		// Loop through the first film object and populate the "headings" array with its keys.
		for (let key in films[0]) {
			if (key != "id") {
				// Capitalise the first character and concatenate it to the remaining ones.
				headings.push(key.charAt(0).toUpperCase() + key.slice(1));
			}
		}

		// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
		films.forEach(film => {
			let noIdFilm = Object.values(film);
			// Remove the first value (id) from the array.
			noIdFilm.shift();
			preparedFilms.push(noIdFilm);
		});

		htmlInsert(resultRegion, generateTable(headings, preparedFilms));
	}
}

// This function is self-explanatory for anyone with decent HTML and JS understanding.
function generateTable(headings, films) {
	let thead = "<thead><tr>"
	headings.forEach(heading => {
		thead += "<th>" + heading + "</th>";
	})
	thead += "</tr></thead>";

	let tbody = "<tbody>";
	films.forEach(film => {
		tbody += "<tr>";
		film.forEach(detail => {
			tbody += "<td>" + detail + "</td>";
		});
		tbody += "</tr>";
	});
	tbody += "</tbody>";

	// For testing purposes, I have included in-line styling to visualise the results better.
	return ("<table style='text-align: center'>" + thead + tbody + "</table>");
}

function getSuitableHandler(format) {
	switch (format) {
		case "json":
			return displayJsonFilmTable;
		case "xml":
			return "";
		default:
			return "";
	}
}

function htmlInsert(resultRegion, html) {
	document.getElementById(resultRegion).innerHTML = html;
}