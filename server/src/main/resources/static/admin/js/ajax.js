function ajax (options) {
    var defaults = {
        type: 'get',
        url: '',
        data: {},
        header: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: function () {},
        error: function () {}
    };

    Object.assign(defaults, options);

    var xhr = new XMLHttpRequest();
    var params = '';
    for (var attr in defaults.data) {
        params += attr + '=' + defaults.data[attr] + '&';
    }

    params = params.substr(0, params.length - 1);

    if (defaults.type == 'get') {
        defaults.url = defaults.url + '?' + params;
    }


    xhr.open(defaults.type, defaults.url);
    if (defaults.type == 'post') {
        var contentType = defaults.header['Content-Type']
        xhr.setRequestHeader('Content-Type', contentType);
        if (contentType == 'application/json') {
            xhr.send(JSON.stringify(defaults.data))
        }else {
            xhr.send(params);
        }
    }else {
        xhr.send();
    }

    xhr.onload = function () {
        var contentType = xhr.getResponseHeader('Content-Type');
        var responseText = xhr.responseText;
        if (contentType.includes('application/json')) {
            responseText = JSON.parse(responseText)
        }
        if (xhr.status == 200) {
            defaults.success(responseText, xhr);
        }else {
            defaults.error(responseText, xhr);
        }
    }
}

var ques = {
    "questionnaireName":"大学生心理健康调察问卷",
    "description":"描述",
    "questionnaireHeaders":[
        "姓名",
        "公司名"
    ],
    "questionnaireBody":[
        [
            {
                "moduleName":"模块1",
                "questions":[
                    {
                        "question":"问题1",
                        "options":[
                            "选项1",
                            "选项2",
                            "选项3"
                        ]
                    },
                    {
                        "question":"问题2",
                        "options":[
                            "选项1",
                            "选项2",
                            "选项3"
                        ]
                    }
                ]
            },
            {
                "moduleName":"模块2",
                "questions":[
                    {
                        "question":"问题3",
                        "options":[
                            "选项1",
                            "选项2",
                            "选项3"
                        ]
                    },
                    {
                        "question":"问题4",
                        "options":[
                            "选项1",
                            "选项2",
                            "选项3"
                        ]
                    }
                ]
            }
        ],
        [
            {
                "moduleName":"模块3",
                "questions":[
                    {
                        "question":"问题7",
                        "options":[
                            "选项1",
                            "选项2",
                            "选项3"
                        ]
                    }
                ]
            }
        ]
    ]
}