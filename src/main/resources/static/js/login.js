
function doLogin() {
    let adminID = document.querySelector("#adminID").value;
    let adminPW = document.querySelector("#adminPW").value;
    $.ajax({
        method: "get",
        url: "/admin/login",
        data: { id: adminID, pw: adminPW },
        success: function response(result) {
            if (result) {
                alert("로그인 성공");
                location.href = "/"
            } else {
                alert("로그인 실패");
            }
        }
    })
}

