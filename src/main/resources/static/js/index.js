console.log("index.js");


doLoginCheck();


function goLogin() {
    location.href = "/login";
}

function doLoginCheck() {
    $.ajax({
        async: false,
        method: "get",
        url: "/admin/login/check",
        success: function response(result) {
            if (result == '') {
                console.log("비 로그인 상태");
            } else {
                console.log("로그인 상태");
            }
        }
    })
}