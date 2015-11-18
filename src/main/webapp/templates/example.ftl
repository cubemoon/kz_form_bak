<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>icocoa456的相册列表-搜狐相册</title>
    
<script type="text/javascript"    src="http://code.jquery.com/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" language="javascript"  >

var siteId = ${siteId?c};
function post_data(){
	var data = {"hi":"ok", "hi2":{"ni":"hao"}};
	var d2 = JSON.stringify(data);
	$.post("/surveys?site_id=10000", d2);
}

function laod_data(){
	$.get("/pa/survey/surveys/1000?site_id=" + window.KZSite["site_id"], function(response){
		alert(JSON.stringify(response));
	});
}
</script>
</head>
<body>
高级表单首页
<input type="text">
<input type="button" onclick="post_data();" value="提交数据">
<input type="button" onclick="laod_data();" value="Get数据"> 

</body>
</html>
