<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>
	<title>Optimal Ghost</title>

	<link rel="stylesheet" href="./css/style.css">
  
	<script src="./js/jquery-2.0.3.min.js"></script>
   
	<script type="text/javascript">
	
	var generalWord = '';
	
	function doNewGame() {	
		
		window.location.reload();
		
	}

	function doAjaxPost() {		

        var letter = $('#letra_input').val();
        
        if (letter == '') {
        	alert("Please add a letter");
        	return;
        }
		
		generalWord = generalWord + letter;		
		
		
		$('#words').append("<div id='"+  generalWord +"' class='card_human' style='display: none;'>" + letter + "</div>");
		
		
		$( '#' + generalWord ).fadeIn( "slow", function() {
    
		});
		
		$('#letra_input').val('');
		
		$( "#letra_input" ).focus();
		
		var selValue = $('input[name=level]:checked').val();
		
		$.ajax({
			
			         type: "POST",
			 
			         url: "/GhostWeb-0.0.1-SNAPSHOT/rest/",
			 
			         data: "word=" + generalWord + "&letter=" + letter + "&result=0" + "&level=" + selValue,
			         
			         dataType: "json",
			 
			         success: function(response){ 
			        	 
			        	 generalWord = response.word;
			        	 
			        	 if (response.result === 0) {
			 
			         	// we have the response
			         	//var obj = jQuery.parseJSON( response );
						//alert( "word " + response.word + "letter " + response.letter + "word " + response.result);
			        	 $('#words').append("<div id='"+  generalWord +"' class='card_computer' style='display: none;'>" + response.letter + "</div>");
			        	 
			        	 $( '#' + generalWord ).fadeIn( "slow", function() {
			        		    
			     		});
			        	 
			        	 } else if (response.result === 1){
			        		 
			        		 $('#words').append("<div id='"+  generalWord +"' class='card_computer' style='display: none;'>" + response.letter + "</div>");
			        		 
			        		 $( '#' + generalWord ).fadeIn( "slow", function() {
			        			 
			        			 alert ("you win!!");
				        		 $('#word_form_id').html("<input type='button' class ='button_send' value='Play again' onclick='doNewGame()''/>");
				        		    
					     	});
			        		 
			        		 
			        	 } else {
			        		 alert ("you lose!!");
			        		 $('#word_form_id').html("<input type='button' class ='button_send' value='Play again' onclick='doNewGame()''/>");
			        	 }
						 	
			 
			         },
			
			         error: function(e){
			 
			         	alert('Error: ' + e);
			 
			         }
			 
			         });

    }
	
	</script>
  
</head>

<body>

<div class="content">

<div id="words" class="word">
	<div  class="card_aux">

	
	</div>

</div>

<br>

<div id="word_form_id" class="word_form">
	<input type="text" id="letra_input" maxlength="1"><br/></td></tr>
	<input type="button" class ="button_send" value="Add letter" onclick="doAjaxPost()"/>
	
	<div>
		<input type="radio" name="level" value="0" checked> Easy
		<input type="radio" name="level" value="1" > Hard
	</div>
	
</div>


</div>


</body>


</html>
