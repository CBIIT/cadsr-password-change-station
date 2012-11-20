		//this function should be replaced by HTML 5's autofocus="autofocus" in the future
		function setFocus(elementId, type) {
//		    var input = document.getElementById(elementId);
//		    var input2 = input.cloneNode(false);
//		    input2.type = type;
//		    input.parentNode.replaceChild(input2, input);
//		    setTimeout(function () {
//		        input2.focus();
//		    }, 10);
		    setTimeout(function() { document.getElementById(elementId).focus(); }, 1000);		    
		}