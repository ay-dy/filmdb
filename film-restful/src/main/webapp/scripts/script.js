// Request initialisers
function jsonFilmTable(resultRegion) {
	let address = "filmapi";
	const data = "?format=json";
	address += data
	ajaxGet(address, resultRegion, function(request) {
		displayJsonFilmTable(request, resultRegion);
	});
}

function jsonSearch(searchString, resultRegion) {
	let address = "filmapi";
	const data = "?format=json&q=" + getValue(searchString);
	address += data;
	ajaxGet(address, resultRegion, function(request) {
		displayJsonFilmTable(request, resultRegion);
	});
}

function xmlFilmTable(resultRegion) {
	let address = "filmapi";
	const data = "?format=xml";
	address += data;
	ajaxGet(address, resultRegion, function(request) {
		displayXmlFilmTable(request, resultRegion);
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

		const preparedHeadings = [];
		const preparedFilms = [];

		// Loop through the first film object and populate the "preparedHeadings" array with its keys.
		for (let key in films[0]) {
			if (key != "id") {
				// Capitalise the first character and concatenate it to the remaining ones.
				preparedHeadings.push(key.charAt(0).toUpperCase() + key.slice(1));
			}
		}
		console.log(preparedHeadings)

		// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
		films.forEach(film => {
			let noIdFilm = Object.values(film);
			// Remove the first value (id) from the array.
			noIdFilm.shift();
			preparedFilms.push(noIdFilm);
		});
		console.log(preparedFilms);

		htmlInsert(resultRegion, generateTable(preparedHeadings, preparedFilms));
	}
}

function displayXmlFilmTable(request, resultRegion) {
	if (request.readyState == 4 && request.status == 200) {

		const films = request.responseXML.getElementsByTagName("film");
		const headings = films[0].children;

		let preparedHeadings = [];
		let preparedFilms = [];

		// Loop through the first film object and populate the "preparedHeadings" array with its children's nodeNames.
		for (let i = 0; i < headings.length; i++) {
			let heading = headings[i].nodeName;
			if (heading != "id") {
				// Capitalise the first character and concatenate it to the remaining ones.
				preparedHeadings.push(heading.charAt(0).toUpperCase() + heading.slice(1));
			}
		}
		console.log(preparedHeadings);

		for (let i = 0; i < films.length; i++) {
			let filmElement = films[i].children;
			let filmData = [];
			for (let j = 0; j < filmElement.length; j++) {
				if (filmElement[j].nodeName != "id") {
					filmData.push(filmElement[j].innerHTML);
				}
			}
			preparedFilms.push(filmData);
		}
		console.log(preparedFilms);

		htmlInsert(resultRegion, generateTable(preparedHeadings, preparedFilms));
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

function getValue(inputField) {
	return encodeURIComponent(document.getElementById(inputField).value);
}