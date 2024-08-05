console.log("member.js");

memberList();

function memberList() {
    let memberArea = document.querySelector(".memberArea");
    let html = `<div>
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
        </div>;
        <div>
            블랙리스트
        </div>
        <div>
            비고
        </div>
        <br>`;
    $.ajax({
        method: "get",
        url: "/member/list",
        success: function response(result) {
            result.forEach(r => {
                html += `<div>
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
                        <div>
                            ${r.blacklist = 0 ? "X" : "O"}
                        </div>
                        <div>
                            <button type="button"> 수정 </button>
                            <button type="button"> 삭제 </button>
                        </div>
                        <br>`;
            });
            memberArea.innerHTML = html;
        }
    })
}