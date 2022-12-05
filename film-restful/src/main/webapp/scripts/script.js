// ==================== Request initialisers ====================
function getFilmTable(searchString, formatOption, resultRegion) {
	filmTitle = getEncodedValue(searchString);
	format = getEncodedValue(formatOption);

	const address = "filmapi?format=" + format + "&q=" + filmTitle;
	const responseHandler = getSuitableHandler(format);

	ajaxGet(address, resultRegion, function(request) {
		responseHandler(request, resultRegion);
	});
}

function deleteFilm(filmId, resultRegion) {
	console.log("delete button: " + filmId);
	const address = "filmapi?id=" + filmId;

	ajaxDelete(address, resultRegion, function(request) {
		displayCrudMessage(request, resultRegion);
	});

}

function updateFilm(filmId) {
	console.log("update button: " + filmId);
}

// ==================== Request functions ====================
function ajaxGet(address, resultRegion, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request, resultRegion);
	}
	request.open("GET", address, true);
	request.send(null);
}

function ajaxDelete(address, resultRegion, responseHandler) {
	const request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		responseHandler(request, resultRegion)
	}
	request.open("DELETE", address, true);
	request.send(null);
}

// ==================== Response handlers ====================
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
		preparedHeadings.push("Actions");
		console.log("JSON headings: ", preparedHeadings);

		// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
		films.forEach(film => {
			const preparedFilm = Object.values(film);
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
		preparedHeadings.push("Actions");
		console.log("XML headings: ", preparedHeadings);

		// Loop through all film objects and populate the "preparedFilms" array with its non-id versions.
		for (let i = 0; i < films.length; i++) {
			let rawFilmData = films[i].children;
			let preparedFilmData = [];
			for (let j = 0; j < rawFilmData.length; j++) {
				preparedFilmData.push(rawFilmData[j].innerHTML);
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

		const preparedHeadings = films[0].split("|");
		// Remove the Id heading as we don't need it.
		preparedHeadings.shift();
		preparedHeadings.push("Actions");
		// Now that we got the headings in a separate array, we can drop them from the films array'
		films.shift();
		console.log("String headings: ", preparedHeadings);

		const preparedFilms = [];
		for (let i = 0; i < films.length; i++) {
			let preparedFilmData = films[i].split("|");
			preparedFilms.push(preparedFilmData);
		}
		console.log("String films: ", preparedFilms);

		htmlInsert(resultRegion, generateTable(preparedHeadings, preparedFilms));
	}
}

// After a film has been updated or deleted
function displayCrudMessage(request, resultRegion) {
	if (request.readyState == 4 && request.status == 200) {
		const message = request.responseText.split("|");
		console.log(message[0], message[1]);
		
		document.getElementById(resultRegion).style = "display: block";
		
		htmlInsert(resultRegion, message[0]);
		document.getElementById(message[1]).style = "display: none";
	}
}

// ==================== Utility functions ====================
function generateTable(headings, films) {
	let thead = "<thead><tr>"
	headings.forEach((heading, index) => {
		if (index === 5) {
			// The "actions" heading must span across 2 columns, as there are 2 buttons.
			thead += "<th colspan='2'>" + heading + "</th>";
		} else {
			thead += "<th>" + heading + "</th>";
		}
	})
	thead += "</tr></thead>";

	let tbody = "<tbody>";
	films.forEach(film => {
		let filmId = null;

		film.forEach((detail, index) => {
			if (index === 0) {
				filmId = detail;
				tbody += "<tr id=\x22" + filmId +"\x22>";
			} else {
				// Inline styling for testing purposes to improve visualization.
				tbody += "<td style='padding: 0.5rem 1rem'>" + detail + "</td>";
			}
		});
		// Add "delete" and "edit" buttons at the end of each row.
		generateActionButtons(filmId).forEach(button => {
			tbody += button;
		})
		tbody += "</tr>";
	});
	tbody += "</tbody>";

	// Inline styling for testing purposes to improve visualization.
	return ("<table style='text-align: center'>" + thead + tbody + "</table>");
}

function generateActionButtons(filmId) {
	let buttons = [];

	buttons.push(
		/*
		   * The double quotes that embody onclick content had to be 
		   * encoded (\x22) to prevent formatting errors.
		*/
		"<td><button onclick=\x22deleteFilm('" + filmId + "', 'message')\x22>"
		+ "<i class='fa-regular fa-trash-can'></i>"
		+ "</button></td>"
	);

	buttons.push(
		"<td><button onclick=\x22updateFilm('" + filmId + "', 'message')\x22>"
		+ "<i class='fa-regular fa-pen-to-square'></i>"
		+ "</button></td>"
	);

	return buttons;
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

function getEncodedValue(inputField) {
	return encodeURIComponent(document.getElementById(inputField).value);
}
