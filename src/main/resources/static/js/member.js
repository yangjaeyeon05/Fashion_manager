console.log("member.js");

memberList();

function memberList() {
    let memberArea = document.querySelector(".memberArea");
    let html = `
    <div class="memberPrint">
        <div>
            회원번호
        </div>
        <div>
            이름
        </div>
        <div>
            전화번호
        </div>
        <div>
            이메일
        </div>
        <div>
            성별
        </div>
        <div>
            사이즈
        </div>
        <div>
            회원 가입 날짜
        </div>
        <div>
            최근 접속일
        </div>
        <div>
            선호 색상
        </div>
        <div>
            블랙리스트
        </div>
        <div>
            비고
        </div>
    </div>
        <br>`;
    $.ajax({
        method: "get",
        url: "/member/list",
        success: function response(result) {
            result.forEach(r => {
                html += `<div class="memberPrint">
                            <div class = "memcode">
                                ${r.memcode}
                            </div>
                            <div>
                                ${r.memname}
                            </div>
                            <div>
                                ${r.memcontact}
                            </div>
                            <div>
                                ${r.mememail}
                            </div>
                            <div>
                                ${r.memgender}
                            </div>
                            <div>
                                ${r.memsize}
                            </div>
                            <div>
                                ${r.memjoindate}
                            </div>
                            <div>
                                ${r.memlastdate}
                            </div>
                            <div>
                                ${r.colorname}
                            </div>
                            <div class = "blacklist">
                                ${r.blacklist == 0 ? "X" : "O"}
                            </div>
                            <div>
                                <button type="button" onclick = "memberEdit()"> 수정 </button><br>
                                <button type="button"> 삭제 </button>
                            </div>
                        </div>
                        <br>`;
            });
            memberArea.innerHTML = html;
        }
    })
}

function memberEdit() {
    let memcode = document.querySelector(".memcode").value;
    let blacklist = document.querySelector(".blacklist").value;
    if (blacklist == "X") {
        blacklist == 0;
    } else {
        blacklist == 1;
    }
    $.ajax({
        async: false,
        method: "put",
        url: "/member/edit",
        data: { memcode: memcode, blacklist: blacklist },
        success: function response(result) {
            console.log(result);
            if (result) {
                alert("블랙리스트 정보 변경 완료");
            } else {
                alert("블랙리스트 정보 변경 실패");
            }
        }
    })
    memberList();
}