/**
 * 自动验证字段是否已填入
 * 使用方法：
 * 1) 在需要验证的html控件上加上一个class，名为 required 或者 valid（required 则不能为空，valid可为空，但需要验证类型有效性）
 * 2) 在需要触发验证的控件上加上一个class，名为 check_form。在该控件click事件时触发验证，验证失败时返回false
 * 3) 增加一个id 为 err_msg的控件，用以显示错误信息
 * 4）1中需要验证的控件需要加上属性 txt_name，用以显示错误时，标识具体的控件；
 * 5）可用属性：
 *        valid_type：有效值（num,list）。num：数字，list：下拉列表
 **/
function check_form() {
    // check_form ------------------------------
    function check_ctl(ctl, required) {
        $("#err_msg").html("");
        //字段值
        var val = ctl.val();
        if (ctl.is("img"))
            val = ctl.attr("src");
        //验证类型
        var valid_type = ctl.attr("valid_type");
        //字段名称
        var txt_name = "";
        if (ctl.attr("txt_name")) {
            txt_name = ctl.attr("txt_name").toString();
        } else {
            txt_name = ctl.attr("id");
        }

        if (required && val == '') {
            ctl.attr("class", ctl.attr("class") + " warn_required");
            $("#err_msg").html(txt_name + "不能为空");
            return false;
        }
        else {
            if (valid_type == "num") {
                if (!parseInt(val)) {
                    ctl.attr("class", ctl.attr("class") + " warn_required");
                    $("#err_msg").html(txt_name + "&nbsp;必须为数字");
                    return false;
                }
            }
            else if (valid_type == "list") {
                var selectedVal = parseInt(val);
                if (selectedVal && selectedVal >= 0) {
                    //
                }
                else {
                    ctl.attr("class", ctl.attr("class") + " warn_required");
                    $("#err_msg").html("请选择一个有效的&nbsp;" + txt_name + "&nbsp;值");
                    return false;
                }
            }
            ctl.attr("class", ctl.attr("class").replace(" warn_required", ""));
            return true;
        }
    }
    $(".check_form").click(function () {
        for (var i = 0; i < $(".required").length; i++) {
            var ctl = $($(".required")[i])
            if (!check_ctl(ctl, true)) {
                return false;
            }
        }
        for (var i = 0; i < $(".valid").length; i++) {
            var ctl = $($(".valid")[i])
            if (!check_ctl(ctl, false)) {
                return false;
            }
        }
        return true;
    });
    $(".required").blur(function () {
        check_ctl($(this),true);
    });
    $(".valid").blur(function () {
        check_ctl($(this),false);
    });
    // check_form END ------------------------------
}