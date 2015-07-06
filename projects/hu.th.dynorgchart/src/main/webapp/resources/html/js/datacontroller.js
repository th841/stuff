$(document).ready(function(){
	window.baseURL = getAbsolutePath();
	$.ajax({
		type: "GET",
		url: baseURL + "data/getRoots",
		success: function(data){
			if(data){
				$.each(data, function(index, obj){
					$("#container").append(renderPerson(data[index]));
				});
			}
		},
		dataType: "json"
	});
});

function renderPerson(person){
	var div = $("<div>");
	div.addClass("person");
	if(person.hasPeople === true){
		var plus = $("<div>");
		plus.addClass("plus");
		plus.html("+");
		plus.prop("open", false);
		plus.click(function(e){
			console.log("clicked", this);
			var personDiv = $(this).parent();
			var firstName = personDiv.attr("firstName");
			var lastName = personDiv.attr("lastName");
			if($(this).prop("open") == false){
				$.ajax({
					type: "POST",
					url: baseURL + "/data/getPeopleList",
					data: {
						firstName: firstName,
						lastName: lastName
					},
					success: function(data){
						if(data){
							var peopleDiv = $("<div>");
							peopleDiv.addClass("people");
							$.each(data, function(index, obj){
								peopleDiv.append(renderPerson(data[index]));
							});
							personDiv.append(peopleDiv);
						}
					},
					dataType: "json"
				});
				$(this).html("-");
				$(this).prop("open", true);
			}else{
				personDiv.find(".people").remove();
				$(this).html("+");
				$(this).prop("open", false);
			}
		});
		div.append(plus);
	}
	div.attr("firstName", person.firstName);
	div.attr("lastName", person.lastName);
	var tb = $("<table>");
	div.append(tb);
	$.each(Object.keys(person), function(index, obj){
		var key = Object.keys(person)[index];
		var value = person[key];
		if(value){
			var tr = $("<tr>");
			var td = $("<td>");
			td.html(key + ": ");
			var td2 = $("<td>");
			td2.html(value);
			tr.append(td);
			tr.append(td2);
			tb.append(tr);
		}
	});
	return div;
}

function getAbsolutePath(){
	var loc = window.location;
	var pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
	return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
}