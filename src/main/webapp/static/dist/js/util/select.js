define([],function(){var t={selectAction:function(t,i){var e;t.on("mouseenter",".f-select",function(){clearTimeout(e),t.find(".f-option-list").hide(),$(this).find(".f-option-list").show()}).on("mouseleave",".f-select",function(){var t=$(this).find(".f-option-list");e=setTimeout(function(){t.hide()},1e3)}).on("click","[data-action=option]",function(){var t=$(this).closest(".f-select").find("em"),e=$(this).closest(".f-option-list"),a=$(this).html(),n=$(this).attr("data-type"),o=$(this).attr("data-id"),s=$(this).attr("option-name"),r=$(this).attr("page-id"),d=$(this).attr("text-type"),c=$(this).attr("data-required"),h=$(this).attr("select-type");e.hide(),t.html(a),n&&t.attr("data-type",n),o&&t.attr("data-id",o),s&&t.attr("option-name",s),r&&t.attr("page-id",r),d&&t.attr("text-type",d),c&&t.attr("data-required",c),h&&t.attr("select-type",h),i&&i(this)})}};return t});