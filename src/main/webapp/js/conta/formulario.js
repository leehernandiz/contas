$(document).ready(function() {
	
	$("#data").mask("99/99/9999", {placeholder: " "});
	$("#valor").maskMoney({thousands:'.', decimal:',', symbolStay: false});
	
});