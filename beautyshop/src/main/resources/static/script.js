$(document).ready(function(){
	var d;
	$.ajax({
				type: "GET",
        		url: "http://localhost:2345/servs",
        		datatype: 'json',
        	    success: (function( data ) {
					for(var x in data){
						$("#ddd").append('<div>' + data[x].name + '</div');
    				}
            	})
            });	
    
 });