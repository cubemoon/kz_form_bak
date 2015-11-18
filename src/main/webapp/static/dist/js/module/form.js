define(["util/doT","util/ajax","text!template/form.html!strip"],function(t,i,e){function n(){$.getJSON(location.href+"?_="+(new Date).getTime(),function(t){t.data.id?(w=t.data.site_id,g.html(x(t.data)),a(),s(),u(),h(t.data.id)):g.html("表单不存在或者被删除")})}function a(){var t=$('[data-name="id_dropdown"]');d(t)}function d(t,i){t.on("click",".f-select",function(){"none"==$(this).find(".f-option-list")[0].style.display?($(".f-option-list").hide(),$(".f-form-item").css({"z-index":1}),$(this).find(".f-option-list").show(),$(this).closest(".f-form-item").css({"z-index":10})):($(this).find(".f-option-list").hide(),$(this).closest(".f-form-item").css({"z-index":1}))}).on("click","[data-action=option]",function(){var t=$(this).closest(".f-select").find("em"),e=($(this).closest(".f-option-list"),$(this).html()),n=$(this).attr("data-lid");t.html(e),n&&t.attr("data-lid",n),i&&i()})}function r(t,i){return t.id=$(i).attr("data-id"),t.title=$(i).find("h2").find("em").text(),t.instruct="",$(i).find("h2").find("span")[0]?t.required=!0:t.required=!1,t}function f(t,i,e){var n=$(i).find("label");return $.each(n,function(t,i){var n={};n.lid=$(i).attr("data-lid"),n.name=$(i).find("span").text(),n.img=$(i).find("em img").attr("src"),$(i).find("input")[0].checked?n.selected=!0:n.selected=!1,e.push(n)}),e}function o(){var t={},i=[],e=[],n=$(".f-form-item",g);$.each(n,function(t,n){var a,d=$(n).attr("data-name"),o=!1;if($(n).find("img").length>0&&(o=!0),"id_checkbox"==d&&(e=[],a={},o?a.name="id_image_checkbox":a.name="id_checkbox",r(a,n),f(a,n,e),a.value=e),"id_radio"==d&&(e=[],a={},o?a.name="id_image_radio":a.name="id_radio",r(a,n),f(a,n,e),a.value=e),"id_section"==d&&(a={},a.name="id_section",a.id=t,a.title=$(n).find("h2").find("em").text(),a.subtitle=""),"id_text"==d&&(a={},a.name="id_text",a.value=$(n).find(".f-input").val(),r(a,n)),"id_dropdown"==d){e=[],a={},a.name="id_dropdown",r(a,n);var l=$(n).find("li"),s=[];$.each(l,function(t,i){var e={};e.lid=$(i).attr("data-lid"),e.name=$(i).text(),$(i).attr("data-lid")==$(i).closest(".f-select ").find("em").attr("data-lid")?e.selected=!0:e.selected=!1,s.push(e)}),a.value=s}"id_textarea"==d&&(a={},a.name="id_textarea",a.value=$(n).find("textarea").val(),r(a,n)),"id_image"==d&&(a={},a.name="id_image",r(a,n),a.description=$(n).find(".f-description").find("span").text(),a.img=$(n).find("img").attr("src"),a.value=$(n).find("img").attr("src")),"id_picture"==d&&(a={},a.name="id_picture",a.id=t,a.url=$(n).find("a").attr("href"),a.descriptions=$(n).find("span").text(),a.img=$(n).find("img").attr("src")),i.push(a)}),t.title=g.find("h1").text(),t.subtitle=g.find(".f-subtitle").text(),t.title_visible=!0,t.published=!0,t.fields=i,t.id=g.find("h1").attr("surveyId");var a=JSON.stringify(t);return a}function l(){var t=!0,i=$(".f-form-item",g);return $.each(i,function(i,e){var n=$(e).attr("data-required"),a=$(e).attr("data-name");if("true"==n){if("id_text"==a||"id_textarea"==a){var d=$(e).find("input").val(),r=$(e).find("textarea").val();if(""==d||""==r)return alert("您有没填写的必填项"),$(e)[0].scrollIntoView(),$(e).find("input").focus(),$(e).find("textarea").focus(),t=!1,!1;if("email"==$(e).find("input").attr("type")){var f=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;if(!f.test(d))return $(e).find("input")[0].scrollIntoView(),$(e).find("input").focus(),alert("请输入正确的Email！"),t=!1,!1}else if("tel"==$(e).find("input").attr("type")){var o=/^((\+?86)|(\(\+86\)))?1\d{10}$/;if(!o.test(d))return $(e).find("input")[0].scrollIntoView(),$(e).find("input").focus(),alert("请输入正确的手机号！"),t=!1,!1}}else if("id_checkbox"==a||"id_image_checkbox"==a||"id_radio"==a||"id_image_radio"==a){var l=$(e).find("input"),s=0;if($.each(l,function(t,i){i.checked&&(s+=1)}),!s)return alert("您有没选择的必选项"),$(e)[0].scrollIntoView(),t=!1,!1}else if("id_dropdown"==a){var c=$(e).find(".f-select > em").attr("data-lid");if(!c)return alert("您有没选择的必选项"),$(e)[0].scrollIntoView(),t=!1,!1}}else{var d=$(e).find("input").val();if("email"==$(e).find("input").attr("type")){var f=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;if(!f.test(d)&&d)return $(e).find("input")[0].scrollIntoView(),$(e).find("input").focus(),alert("请输入正确的Email！"),t=!1,!1}else if("tel"==$(e).find("input").attr("type")){var o=/^((\+?86)|(\(\+86\)))?1\d{10}$/;if(!o.test(d)&&d)return $(e).find("input")[0].scrollIntoView(),$(e).find("input").focus(),alert("请输入正确的手机号！"),t=!1,!1}}}),t}function s(){g.on("change",'[data-action="add-image"]',function(){c(this)})}function c(t){var e=new FormData;e.append("image",t.files[0]),i.uploadPic(w,e,function(t){var i={};i.img=t.data,g.find(".f-image").attr("src",i.img)},function(){alert("文件超过5M或者格式有误，请重新选择图片！")},{processData:!1,contentType:!1})}function u(){var t=$(".f-btn-wrap"),i=g.find("h1").attr("data-id");t.on("click",'[data-action="post-form"]',function(){if(l()){var t=o();$.ajax({url:i+"/answers",type:"post",data:t,dataType:"JSON",success:m,error:p})}})}function m(t){alert("您已经成功提交，谢谢！"),location.href=t.data.value}function p(){alert("提交失败！")}function h(t){$.ajax("/forms/"+t+"/visits",{type:"post"})}function v(){n()}function _(){return b}var w,x=t.template(e),g=$(".f-form-page"),b={preload:_,show:v};return b});