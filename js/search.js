var getQueryString = function ( field, url ) {
    var href = url ? url : window.location.href;
    var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i' );
    var string = reg.exec(href);
    return string ? string[1] : null;
};

searchString = decodeURIComponent(getQueryString("q"));
$(".searchQuery").html('<span class="glyphicon glyphicon-search" aria-hidden="true"></span> ' + searchString);
searchArray = searchString.toLowerCase().replace(/[^\w\s]|_/g, "").split(" "); //Split search string into words
searchResults = [];

$.get("/Modular-FluxFields/search/pages.json", function(data) {
  if (searchArray[0] !== "") {
	 $.each(data.pages, function(i, obj) {
		  $.each(obj.tags, function(j, tag) {
			 if ($.inArray(tag, searchArray) != -1) {
				  //Found a match
				  var foundMatch = false;
				  for (var k = 0; k < searchResults.length; k++) {
    			 if (searchResults[k][0] == i) {
						  searchResults[k][1] = searchResults[k][1] + 1;
						  foundMatch = true;
					 }
				  }
				  if (!foundMatch) {
					 searchResults.push([i, 1]);
				  }

			 }
		  });
	 });
 } else {
   $.each(data.pages, function(i, obj) {
     searchResults.push([i, 1]);
   });
  }

  if (searchArray[0] !== "") {
	 searchResults.sort(compareSecondColumn);
 }

	$.get("/Modular-FluxFields/search/searchResult.html", function(resultData) {
		$(".searchResults").html("<br>");

		for (var i = 0; i < searchResults.length; i++) {
			var resultHTML = resultData;
			resultHTML = resultHTML.replace("{PAGE-TITLE}", data.pages[searchResults[i][0]].title);
			resultHTML = resultHTML.replace("{PAGE-LINK}", data.pages[searchResults[i][0]].path);
			$(".searchResults").append(resultHTML);

			for (var l = 0; l < data.pages[searchResults[i][0]].tags.length; l++) {
				if ($.inArray(data.pages[searchResults[i][0]].tags[l], searchArray) != -1) {
					$(".search-tags").append("<span class='label label-success'>" + data.pages[searchResults[i][0]].tags[l] + "</span> ");
				} else {
					$(".search-tags").append("<span class='label label-default'>" + data.pages[searchResults[i][0]].tags[l] + "</span> ");
				}
			}
			$(".search-tags").removeClass("search-tags");

		}

	});

});

function compareSecondColumn(a, b) {
    if (a[1] === b[1]) {
        return 0;
    }
    else {
        return (a[1] > b[1]) ? -1 : 1;
    }
}
