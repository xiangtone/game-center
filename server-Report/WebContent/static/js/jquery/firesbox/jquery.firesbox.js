(function($){
 
	$.fn.fixPNG = function() {
		return this.each(function () {
			var image = $(this).css('backgroundImage');

			if (image.match(/^url\(["']?(.*\.png)["']?\)$/i)) {
				image = RegExp.$1;
				$(this).css({
					'backgroundImage': 'none',
					'filter': "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=" + ($(this).css('backgroundRepeat') == 'no-repeat' ? 'crop' : 'scale') + ", src='" + image + "')"
				}).each(function () {
					var position = $(this).css('position');
					if (position != 'absolute' && position != 'relative')
						$(this).css('position', 'relative');
				});
			}
		});
	};
	
	var elem, opts, busy = false;
	var isIE = ($.browser.msie && parseInt($.browser.version.substr(0,1)) < 8);

	$.fn.firesbox = function(settings){
		settings = $.extend({}, $.fn.firesbox.defaults, settings);

		function _initialize() {
			elem = this;
			opts = settings;

			_start();

			return false;
		};
		
		function _start() {
			if (busy) return;
			
			//$.fn.firesbox.build();
			if ($.isFunction(opts.callbackOnStart)) {
				opts.callbackOnStart();
			}
			if (opts.overlayShow) {
				if (isIE) {
					$('embed, object, select').css('visibility', 'hidden');
				}

				$("#firesbox_overlay").css('opacity', opts.overlayOpacity).show();
			}
		};
		
		_initialize();

	};
	
	$.fn.firesbox.start = function(content,settings)
	{
		
		$.fn.firesbox(settings);
		$.fn.firesbox.setcontent(content,opts.frameWidth, opts.frameHeight);
		
		return $("#firesbox_content");
	};
	
	$.fn.firesbox.setcontent = function(value,width,height)
	{
		busy = true;

			var pad = opts.padding;

			if (isIE) {
				$("#firesbox_content")[0].style.removeExpression("height");
				$("#firesbox_content")[0].style.removeExpression("width");
			}

			if (pad > 0) {
				width	+= pad * 2;
				height	+= pad * 2;

				$("#firesbox_content").css({
					'top'		: pad + 'px',
					'right'		: pad + 'px',
					'bottom'	: pad + 'px',
					'left'		: pad + 'px',
		 
					'width'		: (width-pad * 2) + 'px',
	 
					'height'	: (height-pad * 2) + 'px'
				});

				if (isIE) {
					$("#firesbox_content")[0].style.setExpression('height',	'(this.parentNode.clientHeight - 20)');
					$("#firesbox_content")[0].style.setExpression('width',		'(this.parentNode.clientWidth - 20)');
				}

			} else {
				$("#firesbox_content").css({
					'top'		: 0,
					'right'		: 0,
					'bottom'	: 0,
					'left'		: 0,
					'width'		: '100%',
					'height'	: '100%'
				});
			}

			if ($("#firesbox_outer").is(":visible") && width == $("#firesbox_outer").width() && height == $("#firesbox_outer").height()) {
		
						$("#firesbox_content").empty().append($(value))
						$.fn.firesbox.finish();
				
			}
			var w = $.fn.firesbox.getViewport();

			var itemLeft	= (width + 36)	> w[0] ? w[2] : (w[2] + Math.round((w[0] - width - 36) / 2));
			var itemTop		= (height + 50)	> w[1] ? w[3] : (w[3] + Math.round((w[1] - height - 50) / 2));

			var itemOpts = {
				'left':		itemLeft,
				'top':		itemTop,
				'width':	width + 'px',
				'height':	height + 'px'
			};

			if ($("#firesbox_outer").is(":visible")) {
			 
					$("#firesbox_content").empty();
		 
							$("#firesbox_content").append($(value))
							$.fn.firesbox.finish();
				 

			} else {

				if (opts.zoomSpeedIn > 0) {
					$("#firesbox_content").empty().append($(value));


					if (opts.zoomOpacity) {
						itemOpts.opacity = 'show';
					}

					$("#firesbox_outer").animate(itemOpts, opts.zoomSpeedIn, opts.easingIn, function() {
						$.fn.firesbox.finish();
					});

				} else {

					$("#firesbox_content").hide().empty().append($(value)).fadeIn(100);
					$("#firesbox_outer").css(itemOpts).fadeIn(100, function() {
						$.fn.firesbox.finish();
					});
				}
			}
	};
	
	$.fn.firesbox.finish = function()
	{
		if (opts.centerOnScroll) {
			$(window).bind("resize scroll", $.fn.firesbox.scrollBox);
		} else {
			$("div#firesbox_outer").css("position", "absolute");
		}

		if (opts.hideOnContentClick) {
			$("#firesbox_wrap").click($.fn.firesbox.close);
		}

		$("#firesbox_close").bind("click", $.fn.firesbox.close);

		$("#firesbox_close").show();

		if (opts.overlayShow && isIE) {
			$('embed, object, select', $('#firesbox_content')).css('visibility', 'visible');
		}

		if ($.isFunction(opts.callbackOnShow)) {
			opts.callbackOnShow();
		}

		busy = false;
	};
	
	$.fn.firesbox.getNumeric = function(el, prop) {
		return parseInt($.curCSS(el.jquery?el[0]:el,prop,true))||0;
	};

	$.fn.firesbox.getPosition = function(el) {
		var pos = el.offset();

		pos.top	+= $.fn.firesbox.getNumeric(el, 'paddingTop');
		pos.top	+= $.fn.firesbox.getNumeric(el, 'borderTopWidth');

		pos.left += $.fn.firesbox.getNumeric(el, 'paddingLeft');
		pos.left += $.fn.firesbox.getNumeric(el, 'borderLeftWidth');

		return pos;
	};
	
	$.fn.firesbox.scrollBox = function() {
		var pos = $.fn.firesbox.getViewport();

		$("#firesbox_outer").css('left', (($("#firesbox_outer").width()	+ 36) > pos[0] ? pos[2] : pos[2] + Math.round((pos[0] - $("#firesbox_outer").width()	- 36)	/ 2)));
		$("#firesbox_outer").css('top',  (($("#firesbox_outer").height() + 50) > pos[1] ? pos[3] : pos[3] + Math.round((pos[1] - $("#firesbox_outer").height()	- 50)	/ 2)));
	};
	
	$.fn.firesbox.resetWH = function(setting) {
		var default_settting = {width:100, height:100, method:'+', type:'height'};
		setting = $.extend({}, default_settting, setting);
		var $firesbox_outer = $("#firesbox_outer");
		if(setting.type == 'width'){
			var width = $firesbox_outer.width();
			if(setting.method == '+'){
				$firesbox_outer.width(width + setting.width);
			}else{
				$firesbox_outer.width(width - setting.width);		
			}
		}else if(setting.type == 'height'){
			var height = $firesbox_outer.height();
			if(setting.method == '+'){
				$firesbox_outer.height(height + setting.height);
			}else{
				$firesbox_outer.height(height - setting.height);		
			}
		}
		$.fn.firesbox.scrollBox();
	};
	
	$.fn.firesbox.getViewport = function() {
		return [$(window).width(), $(window).height(), $(document).scrollLeft(), $(document).scrollTop() ];
	};
	
	$.fn.firesbox.build = function() {
		var html = '';

		html += '<div id="firesbox_overlay"></div>';

		html += '<div id="firesbox_wrap">';

		html += '<div class="firesbox_loading" id="firesbox_loading"><div></div></div>';

		html += '<div id="firesbox_outer">';

		html += '<div id="firesbox_inner">';
 
		html +=  '<a href="javascript:;" id="firesbox_left"><span class="firesbox_ico" id="firesbox_left_ico"></span></a><a href="javascript:;" id="firesbox_right"><span class="firesbox_ico" id="firesbox_right_ico"></span></a>';

		html += '<div id="firesbox_content"></div>';

		html +=  '<div id="firesbox_title"></div>';

		html += '</div>';

		html += '</div>';

		html += '</div>';
		
		$(html).appendTo("body");

		$('<table cellspacing="0" cellpadding="0" border="0"><tr><td class="firesbox_title" id="firesbox_title_left"></td><td class="firesbox_title" id="firesbox_title_main"><div></div></td><td class="firesbox_title" id="firesbox_title_right"></td></tr></table>').appendTo('#firesbox_title');

		if (isIE) {
			$("#firesbox_inner").prepend('<iframe class="firesbox_bigIframe" scrolling="no" frameborder="0"></iframe>');
			$("#firesbox_close, .firesbox_bg, .firesbox_title, .firesbox_ico").fixPNG();
		}
		
	};
	
	$.fn.firesbox.defaults = {
		padding				:	0,
		zoomOpacity			:	true,
		zoomSpeedIn			:	0,
		zoomSpeedOut		:	0,
		zoomSpeedChange		:	300,
		easingIn			:	'swing',
		easingOut			:	'swing',
		easingChange		:	'swing',
		frameWidth			:	600,
		frameHeight			:	355,
		overlayShow			:	true,
		overlayOpacity		:	0.3,
		hideOnContentClick	:	false,
		centerOnScroll		:	true,
		callbackOnStart		:	null,
		callbackOnShow		:	null,
		callbackOnClose		:	null
	};
	
	$.fn.firesbox.close = function(){
		busy = true;


		$("#firesbox_close").unbind();

		if (opts.hideOnContentClick) {
			$("#firesbox_wrap").unbind();
		}

		$("#firesbox_close, .firesbox_loading, #firesbox_left, #firesbox_right, #firesbox_title").hide();

		if (opts.centerOnScroll) {
			$(window).unbind("resize scroll");
		}

		__cleanup = function() {
			$("#firesbox_overlay, #firesbox_outer").hide();

			if (opts.centerOnScroll) {
				$(window).unbind("resize scroll");
			}
			if (isIE) {
				$('embed, object, select').css('visibility', 'visible');
			}

			if ($.isFunction(opts.callbackOnClose)) {
				opts.callbackOnClose();
			}

			busy = false;
		};

		if ($("#firesbox_outer").is(":visible") !== false) {
			if (opts.zoomSpeedOut > 0 ) {
				
				if (opts.zoomOpacity) {
					itemOpts.opacity = 'hide';
				}

				$("#firesbox_outer").stop(false, true).animate(itemOpts, opts.zoomSpeedOut, opts.easingOut, __cleanup);

			} else {
				$("#firesbox_outer").stop(false, true).fadeOut(300, __cleanup);
			}

		} else {
			__cleanup();
		}

		return false;
	};

	$(document).ready(function() {
		$.fn.firesbox.build();
	});
	
})(jQuery);
