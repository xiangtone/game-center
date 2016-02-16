var Drag = {
    "obj":null,
    "init":function(handle, dragBody, e) {
        if (e == null) {
            handle.onmousedown = Drag.start;
        }
        handle.root = dragBody;

        if (isNaN(parseInt(handle.root.style.left)))handle.root.style.left = "0px";
        if (isNaN(parseInt(handle.root.style.top)))handle.root.style.top = "0px";
        handle.root.onDragStart = new Function();
        handle.root.onDragEnd = new Function();
        handle.root.onDrag = new Function();
        if (e != null) {
            var handle = Drag.obj = handle;
            e = Drag.fixe(e);
            var top = parseInt(handle.root.style.top);
            var left = parseInt(handle.root.style.left);
            handle.root.onDragStart(left, top, e.pageX, e.pageY);
            handle.lastMouseX = e.pageX;
            handle.lastMouseY = e.pageY;
            document.onmousemove = Drag.drag;
            document.onmouseup = Drag.end;
        }
    },

    "start":function(e) {
        var handle = Drag.obj = this;
        e = Drag.fixEvent(e);
        var top = parseInt(handle.root.style.top);
        var left = parseInt(handle.root.style.left);

        handle.root.onDragStart(left, top, e.pageX, e.pageY);
        handle.lastMouseX = e.pageX;
        handle.lastMouseY = e.pageY;
        document.onmousemove = Drag.drag;
        document.onmouseup = Drag.end;
        return false;
    },
    "drag":function(e) {
        e = Drag.fixEvent(e);
        var handle = Drag.obj;
        var mouseY = e.pageY;
        var mouseX = e.pageX;
        var top = parseInt(handle.root.style.top);
        var left = parseInt(handle.root.style.left);

        var currentLeft,currentTop;
        currentLeft = left + mouseX - handle.lastMouseX;
        currentTop = top + (mouseY - handle.lastMouseY);

        handle.root.style.left = currentLeft + "px";
        handle.root.style.top = currentTop + "px";

        handle.lastMouseX = mouseX;
        handle.lastMouseY = mouseY;

        handle.root.onDrag(currentLeft, currentTop, e.pageX, e.pageY);
        return false;
    },
    "end":function() {
        document.onmousemove = null;
        document.onmouseup = null;
        Drag.obj.root.onDragEnd(parseInt(Drag.obj.root.style.left), parseInt(Drag.obj.root.style.top));
        Drag.obj = null;
    },
    "fixEvent":function(e) {
        if (typeof e == "undefined")e = window.event;
        if (typeof e.layerX == "undefined")e.layerX = e.offsetX;
        if (typeof e.layerY == "undefined")e.layerY = e.offsetY;
        if (typeof e.pageX == "undefined")e.pageX = e.clientX + document.body.scrollLeft - document.body.clientLeft;
        if (typeof e.pageY == "undefined")e.pageY = e.clientY + document.body.scrollTop - document.body.clientTop;
        return e;
    }
};

(function ($) {

    $.fn.JDialog = $.JDialog = function (dialog) {
        this.options = $.extend({
            autoOpen: false,
            animate:{},
            buttons: {},
            closeText: '[关闭]',
            hideText:"[最小化]",
            showHide:true,
            showClose:true,
            dialogClass: '',
            draggable: true,
            close: false,
            height: 'auto',
            maxHeight: false,
            maxWidth: false,
            minHeight: 250,
            minWidth: 150,
            title:"",
            closeEvent:false,
            hideEvent:false,
            openEvent:false,
            Drag:false
        }, dialog);

        this.element = this.hide();

        this.drawEle = function() {
            var self = this;

            if (self.uiDialog) return;

            var options = self.options,title = options.title || '提示';

            self.uiDialogTitlebar = $('<div style="border-bottom:solid 1px #707070;height:32px;background-image:url(assets/images/popup.jpg);"></div>');

            self.uiDialog = $('<div style="border:solid 1px #707070;position: absolute; overflow: hidden; z-index:999999;"></div>');

            var uiDialog = self.uiDialog.appendTo(document.body).hide(),
                    uiDialogContent = self.element.show().addClass("ui-dialog-content ui-widget-content").css({
                        //"margin":1,
                        "overflow-y":"hidden",
                        "overflow-x":"hidden",
						"background-color":"#FFFFFF"
                    }).removeAttr('title').appendTo(uiDialog),
            
                    uiDialogTitlebar = self.uiDialogTitlebar.prependTo(uiDialog),
                    uiDialogTitlebarClose = $('<a href="#" style="float:right;margin-right: 5px;text-decoration:none;text-align:center;line-height:30px;color: #FFFFFF;font-weight: bold;font-size: 13px;font-family: tahoma, arial, verdana, sans-serif;"></a>')
                            .addClass('ui-dialog-titlebar-close ui-corner-all')
                            .hover(
                                  function() {
                                      uiDialogTitlebarClose.addClass('ui-state-hover');
                                  },
                                  function() {
                                      uiDialogTitlebarClose.removeClass('ui-state-hover');
                                  }
                            ).focus(
                                   function() {
                                       uiDialogTitlebarClose.addClass('ui-state-focus');
                                   }
                            ).blur(
                                  function() {
                                      uiDialogTitlebarClose.removeClass('ui-state-focus');
                                  }
                            ).click(
                                   function(event) {
                                       self.close(event);
                                       return false;
                                   }).appendTo(uiDialogTitlebar),

                    uiDialogTitlebarHide = $('<a href="#" style="float:right;text-decoration:none;text-align:center;line-height:30px;color: #FFFFFF;font-weight: bold;font-size: 13px;font-family: tahoma, arial, verdana, sans-serif;"></a>')
                            .addClass('ui-dialog-titlebar-hide ui-corner-all')
                            .hover(
                                  function() {
                                      uiDialogTitlebarHide.addClass('ui-state-hover');
                                  },
                                  function() {
                                      uiDialogTitlebarHide.removeClass('ui-state-hover');
                                  }
                            ).focus(
                                   function() {
                                       uiDialogTitlebarHide.addClass('ui-state-focus');
                                   }
                            ).blur(
                                  function() {
                                      uiDialogTitlebarHide.removeClass('ui-state-focus');
                                  }
                            ).click(
                                   function(event) {
                                       self._hide(event);
                                       return false;
                                   }).appendTo(uiDialogTitlebar),

                    uiDialogTitlebarCloseText = self.options.showClose ? (self.uiDialogTitlebarCloseText = $('<span></span>'))
                            .addClass('ui-icon ui-icon-closethick')
                            .text(self.options.closeText)
                            .appendTo(uiDialogTitlebarClose) : null,

                    uiDialogTitlebarHideText = self.options.showHide ? (self.uiDialogTitlebarHideText = $('<span></span>'))
                            .addClass('ui-icon ui-icon-hidethick')
                            .html(self.options.hideText + "&nbsp;").appendTo(uiDialogTitlebarHide) : null,

                    uiDialogTitle = $('<span style="float:left;text-align:center;line-height:30px;color: #FFFFFF;font-weight: bold;font-size: 15px;font-family: tahoma, arial, verdana, sans-serif;"></span>')
                            .addClass('ui-dialog-title')
                            .attr('id', new Date())
                            .html("&nbsp;" + title)
                            .prependTo(uiDialogTitlebar);

            self.createButtons(options.buttons);
            this.initDrag();
        };

        this.position = function() {
            var cWidth = document.body.clientWidth,cHeight = document.body.clientHeight;
            this.uiDialog.css({
                top:(cHeight - this.uiDialog.height()) / 2,
                left:(cWidth - this.uiDialog.width()) / 2
            })
        };

        this.size = function() {
            var options = this.options,
                    nonContentHeight,
                    minContentHeight,
                    isVisible = this.uiDialog.is(":visible");

            this.element.show().css({
				opacity:"1",
                width: 'auto',
                minHeight: 0,
                height: 0
            });

            if (options.minWidth > options.width) {
                options.width = options.minWidth;
            }

            nonContentHeight = this.uiDialog.css({
                height: 'auto',
				opacity:"1",
                width: options.width
            }).height();

            minContentHeight = Math.max(0, options.minHeight - nonContentHeight);

            if (options.height === "auto") {
                if ($.support.minHeight) {
                    this.element.css({
                        minHeight: minContentHeight,
                        height: "auto"
                    });
                } else {
                    this.uiDialog.show();
                    var autoHeight = this.element.css("height", "auto").height();
                    if (!isVisible) {
                        this.uiDialog.hide();
                    }
                    this.element.height(Math.max(autoHeight, minContentHeight));
                }
            } else {
                this.element.height(options.height);
            }
        };

        this.initDrag = function() {
            Drag.init(this.uiDialogTitlebar[0], this.uiDialog[0]);
        };

        this.createButtons = function(buttons) {
            var self = this,
                    hasButtons = false,
                    uiDialogButtonPane = $('<div style="padding:4px;text-align:right;line-height:30px;background-color:#F3F3F3;border-top:solid 1px #E4E4E4;height:35px;"></div>')
                            .addClass(
                            'ui-dialog-buttonpane ' +
                                    'ui-widget-content ' +
                                    'ui-helper-clearfix'
                            ),
                    uiButtonSet = $("<div></div>")
                            .addClass("ui-dialog-buttonset")
                            .appendTo(uiDialogButtonPane);

            self.uiDialog.find('.ui-dialog-buttonpane').remove();

            if (typeof buttons === 'object' && buttons !== null) {
                $.each(buttons, function() {
                    return !(hasButtons = true);
                });
            }
            if (hasButtons) {
                $.each(buttons, function(name, props) {
                    props = $.isFunction(props) ? { click: props, text: name } : props;
                    //var button = $('<button type="button" style="height:28px;"></button>')
                    var button = $('<input style="margin-left:5px;margin-top:5px;" type="button" value="'+name+'"/>')
                            .click(
                                  function() {
                                      props.click.apply(self.element[0], arguments);
                                  }).appendTo(uiButtonSet);

                    $.each(props, function(key, value) {
                        if (key === "click")
                            return;
                        if (key in {
                            val: true,
                            css: true,
                            //html: true,
                            //text: true,
                            data: true,
                            width: true,
                            height: true,
                            offset: true,
                            click: true
                        }) {
                            button[ key ](value);
                        } else {
                            button.attr(key, value);
                        }
                    });
                });
                uiDialogButtonPane.appendTo(self.uiDialog);
            }
        };

        this.open = function() {
            if (this.isOpen)  return;
            this.isOpen = true;
            this.drawEle();
            this.size();
            this.position();
            if (this.options.openEvent) this.options.openEvent.call(this);
            //this.uiDialog.show(this.options.show);
            this.uiDialog.show();
          
        };

        this.isOpen = function() {
            return this.isOpen
        };

        this.close = function() {
            this.isOpen = false;
            if (this.options.close) {
                $(this.element).unbind('.JDialog')
                        .removeData('dialog')
                        .removeClass('ui-dialog-content ui-widget-content')
                        .hide().appendTo('body');

                this.uiDialog.remove();

                delete this.uiDialog;

                if (this.options.closeEvent) {
                    this.options.closeEvent.call(this);
                }
            } else {
				$("body").append(this.uiDialog);
                if (typeof this.options.animate == "object")
                    this.uiDialog.animate(this.options.animate, 250).hide(1);
                else {
                    this.uiDialog.hide();
                }
				
                //if (this.uiDialog.find("form")[0])
                //    this.uiDialog.find("form")[0].reset();
                if (this.options.hideEvent) this.options.hideEvent.call(this);
            }
        };

        this._hide = function() {
            this.isOpen = false;
            if (this.options.hideEvent) this.options.hideEvent.call(this);
			$("body").append(this.uiDialog);
            this.uiDialog.animate(this.options.animate, 250).hide(1);
        };

        this.onDestroy = function() {
            var self = this;
            //todo
        };

        if (this.options.autoOpen) this.open();
        else this.isOpen = false;

        return this;
    };
})(jQuery);

