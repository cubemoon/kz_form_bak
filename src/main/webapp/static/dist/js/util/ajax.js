define([],function(){function e(e,t,i,s,n,u){u=u||{},$.ajax("/pa/survey/surveys"+t,$.extend({type:e,data:i,dataType:"json",success:s,error:n},u))}var t={creatSurveys:function(t,i,s,n){e("put","?site_id="+t,i,s,n)},uploadPic:function(t,i,s,n,u){e("post","/images?site_id="+t,i,s,n,u)},delSurvey:function(t,i,s,n){e("delete","/"+t+"?site_id="+i,s,n)},publish:function(t,i,s,n){e("put","/"+t+"/publish?site_id="+i,s,n)},setStatus:function(t,i,s,n,u,d){e("put","/"+t+"/answers/"+s+"/"+u+"?site_id="+i,n,d)},deleteAnswer:function(t,i,s,n,u){e("delete","/"+t+"/answers/"+s+"?site_id="+i,n,u)}};return t});