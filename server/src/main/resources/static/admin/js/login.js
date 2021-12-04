let url = "http://localhost:8080/page/login"
let account = "";
let pwd = "";
let data = "";
document.getElementById('login-btn-box').onclick = function (){
    account = document.getElementById('login-account').value;
    pwd = document.getElementById('login-password').value;
    ajax({
        data: {
            account: account,
            password: pwd,
        },
        type: 'post',
        // 请求地址
        url: url,
        success: function (data) {
            console.log('这里是success函数');
            console.log(data)
        }
    });
}



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