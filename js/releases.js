$.get("/Modular-FluxFields/releases/releases.json", function(data) {
	var releases = data.releases;
	$.get("/Modular-FluxFields/releases/template.html", function(data) {

		var template = data;
		releases.forEach(function (obj) {

			var releaseHTML = template;
			$.each(obj, function (i, obj) {

				var re = new RegExp('{' + i + '}','g');

				if (i == "RELEASE-DESCRIPTION") {

					jQuery.ajax({
        		url: '/Modular-FluxFields/releases/' + obj,
        		success: function (data) {
            		releaseHTML = releaseHTML.replace(re, data);
        		},
        		async: false
    			});

				} else {
					releaseHTML = releaseHTML.replace(re, obj);
				}

			});

			$(".releases").prepend(releaseHTML);

		});
	});
});
