Array.prototype.remove = function() {
    var what, a = arguments, L = a.length, ax;
    while (L && this.length) {
        what = a[--L];
        while ((ax = this.indexOf(what)) !== -1) {
            this.splice(ax, 1);
        }
    }
    return this;
};

var matchClasses = [];
var mcVersions = ["release-mc-1-8"];
var searchCriteria = {
	"release-mc-1-8": "Minecraft 1.8",
	"release-stable": "Stable",
	"release-beta": "Beta",
	"release-alpha": "Alpha"
};

$(".btn-mcVer-All").click(function() {
	// Search all MC versions

	for (var i = 0; i < mcVersions.length; i++) {
		matchClasses.remove(mcVersions[i]);
	}

	searchReleases();

});

$(".btn-mcVer-1-8").click(function() {
	// Search for MC version 1.8

	for (var i = 0; i < mcVersions.length; i++) {
		matchClasses.remove(mcVersions[i]);
	}

	matchClasses.push("release-mc-1-8");
	searchReleases();

});

$(".checkbox-stable").click(function() {
	// Search for stable versions

	if ($(this).is(":checked")) {
		matchClasses.push("release-stable");
	} else {
		matchClasses.remove("release-stable");
	}

	searchReleases();

});

$(".checkbox-beta").click(function() {
	// Search for stable versions

	if ($(this).is(":checked")) {
		matchClasses.push("release-beta");
	} else {
		matchClasses.remove("release-beta");
	}

	searchReleases();

});

$(".checkbox-alpha").click(function() {
	// Search for stable versions

	if ($(this).is(":checked")) {
		matchClasses.push("release-alpha");
	} else {
		matchClasses.remove("release-alpha");
	}

	searchReleases();

});

function searchReleases() {

	console.log("Searching for classes...");
	console.log(matchClasses);

	$(".searchCriteria").html("");

	for (var i = 0; i < matchClasses.length; i++) {
		$(".searchCriteria").append("<span class=\"badge\">" + searchCriteria[matchClasses[i]] + "</span> ");
	}

	if (matchClasses.length === 0) {
		$(".release-panel").css("display", "block");
	} else {
		$(".release-panel").css("display", "none");
		for (var j = 0; j < matchClasses.length; j++) {
			$("." + matchClasses[j]).css("display", "block");
		}
	}
}

//INIT
matchClasses.push("release-stable");
matchClasses.push("release-beta");
matchClasses.push("release-alpha");
searchReleases();
