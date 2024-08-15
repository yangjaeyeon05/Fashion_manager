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
                alert('로그인 후 이용가능합니다.')
                location.href="/login"
            } else {
                console.log("로그인 상태");
            }
        }
    })
}