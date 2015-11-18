require({
    paths: {
        'jquery': 'empty:',
        'text': 'util/text'
        //'tinyscrollbar': 'plugins/jquery.tinyscrollbar'
    },
    config: {
        text: {
            useXhr: function (url, protocol, hostname, port) {
                return true;
            }
        }
    }
});
;(function(){
    require(
        ['module/form'],
        function(form){
             form.preload().show();
        }
    );
})();