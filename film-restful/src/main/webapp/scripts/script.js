// Request initialisers
function getFilmTable(searchString, formatOption, resultRegion) {
	searchString = getValue(searchString);
	formatOption = getValue(formatOption);

	const address = "filmapi?format=" + formatOption + "&q=" + searchString;
	const responseHandler = getSuitableHandler(formatOption);

	ajaxGet(address, resultRegion, function(request) {
		responseHandler(request, resultRegion);
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
		console.log("JSON headings: ", preparedHeadings);

		// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
		films.forEach(film => {
			const preparedFilm = Object.values(film);
			// Remove the first value (id) from the array.
			preparedFilm.shift();
			preparedFilms.push(preparedFilm);
		});
		console.log("JSON films: ", preparedFilms);

		htmlInsert(resultRegion, generateTable(preparedHeadings, preparedFilms));
	}
}

function displayXmlFilmTable(request, resultRegion) {
	if (request.readyState == 4 && request.status == 200) {
		const films = request.responseXML.getElementsByTagName("film");
		const headings = films[0].children;

		const preparedHeadings = [];
		const preparedFilms = [];

		// Loop through the first film object and populate the "preparedHeadings" array with its children's nodeNames.
		for (let i = 0; i < headings.length; i++) {
			let heading = headings[i].nodeName;
			if (heading != "id") {
				// Capitalise the first character and concatenate it to the remaining ones.
				preparedHeadings.push(heading.charAt(0).toUpperCase() + heading.slice(1));
			}
		}
		console.log("XML headings: ", preparedHeadings);

		// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
		for (let i = 0; i < films.length; i++) {
			let rawFilmData = films[i].children;
			let preparedFilmData = [];
			for (let j = 0; j < rawFilmData.length; j++) {
				if (rawFilmData[j].nodeName != "id") {
					preparedFilmData.push(rawFilmData[j].innerHTML);
				}
			}
			preparedFilms.push(preparedFilmData);
		}
		console.log("XML films: ", preparedFilms);

		htmlInsert(resultRegion, generateTable(preparedHeadings, preparedFilms));
	}
}

function displayStringFilmTable(request, resultRegion) {
	if (request.readyState == 4 && request.status == 200) {
		// Split the string with each new line.
		const films = request.responseText.split(/\n+/);
		/*
			Remove the empty value from the end of the array.
			(It gets there because the last rerord also has a \n which gets split
			but there is nothing after it.)
		*/
		films.splice(-1);
		console.log("String films (RAW)", films);

		const preparedHeadings = films[0].split("|");
		// Remove the Id heading as we don't need it.
		preparedHeadings.shift();
		// Now that we got the headings in a separate array, we can drop them from the films array'
		films.shift();
		console.log("String headings: ", preparedHeadings);

		const preparedFilms = [];
		for (let i = 0; i < films.length; i++) {
			let preparedFilmData = films[i].split("|");
			preparedFilmData.shift();
			preparedFilms.push(preparedFilmData);
		}
		console.log("String films: ", preparedFilms);

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
			return displayXmlFilmTable;
		default:
			return displayStringFilmTable;
	}
}

function htmlInsert(resultRegion, html) {
	document.getElementById(resultRegion).innerHTML = html;
}

function getValue(inputField) {
	return encodeURIComponent(document.getElementById(inputField).value);
}
