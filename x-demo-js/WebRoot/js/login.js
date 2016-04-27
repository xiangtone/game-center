


$("#login_span1").on("click",function(){
	$("#login_span1").addClass("log_active");
	$("#login_span2").removeClass("log_active");
	$("#login").fadeIn();
	$("#register").fadeOut();
})
$("#login_span2").on("click",function(){
	$("#login_span2").addClass("log_active");
	$("#login_span1").removeClass("log_active");
	$("#register").fadeIn();
	$("#login").fadeOut();
})