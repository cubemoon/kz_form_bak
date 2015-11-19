define([], function() {
    var module = {
        creatSurveys:function(siteId, body, callback, onError){
            ajax('put', '?site_id=' + siteId, body, callback, onError);
        },
        uploadPic: function(siteId, data, callback, onError, opts){
            ajax('post', '/images?site_id=' + siteId, data, callback, onError, opts);
        },
        uploadPicture: function(siteId, data, callback, onError, opts){
            _ajax('post', '/images?site_id=' + siteId, data, callback, onError, opts);
        },
        delSurvey: function(surveyId, siteId, data, callback){
            ajax('delete', '/' +surveyId +'?site_id=' + siteId, data, callback);
        },
        publish: function(surveyId, siteId, data, callback){
            ajax('put', '/' +surveyId +'/publish?site_id=' + siteId, data, callback);
        },
        setStatus: function(surveyId, siteId, id, data, status, callback){
            ajax('put', '/' + surveyId + '/answers/' + id +'/' + status + '?site_id=' + siteId, data, callback);
        },
        deleteAnswer: function(surveyId, siteId, id, data, callback){
            ajax('delete', '/' + surveyId + '/answers/' + id + '?site_id=' + siteId, data, callback);
        }
        // exportExcel: function(surveyId, siteId, data, callback){
        //     ajax('get', '/' + surveyId + '/answers/0/export?site_id=' + siteId, data, callback);
        // }
    };
    function ajax(method, url, data, success, error, opts) {
        opts = opts || {}
        $.ajax('/pa/survey/surveys' + url,
            $.extend({
                type: method,
                data: data,
                dataType: 'json',
                success: success,
                error: error
            },opts));
    }
    function _ajax(method, url, data, success, error, opts) {
        opts = opts || {}
        $.ajax('/forms'+ url,
            $.extend({
                type: method,
                data: data,
                dataType: 'json',
                success: success,
                error: error
            },opts));
    }

    function serializeArray(key, arr) {
        return key + '=' + arr.join('&' + key + '=');
    }
    return module;
});