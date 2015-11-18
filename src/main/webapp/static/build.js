({
    appDir: "appsrc",
    baseUrl: "js/",
    dir: "dist",

    optimize: "uglify2",
    optimizeCss: "standard",
    paths: {
        // "jquery": "empty:",
        // 'tinyscrollbar': 'plugins/jquery/jquery.tinyscrollbar',
        // 'placeholder': 'plugins/jquery/placeholder',
        // 'mousewheel': 'plugins/jquery/jquery.mousewheel',
        // 'history': 'plugins/jquery/jquery.history',
        'text': 'util/text'
    },

    uglify2: {
        ascii_only: true
    },

    fileExclusionRegExp: /(as|test|old|\.psd|\.scss|\.DS_Store)$/,
    preserveLicenseComments: false,

    modules: [{
        name: 'formEdit'
    },{
        name: 'form'
    }]
})
