console.log("member.js");

memberList();
// ===================================  2024-08-13 김민석 html 수정(테이블) ========================================= //
function memberList() {
    let memberArea = document.querySelector(".memberArea");
    let html = `
    <div class="memberPrint">
        <h3> 회원관리 페이지 </h3>
        <table class="table">
            <thead>
                <tr>
                    <th>
                        회원번호
                    </th>
                    <th>
                        이름
                    </th>
                    <th>
                        전화번호
                    </th>
                    <th>
                        이메일
                    </th>
                    <th>
                        성별
                    </th>
                    <th>
                        사이즈
                    </th>
                    <th>
                        회원 가입 날짜
                    </th>
                    <th>
                        최근 접속일
                    </th>
                    <th>
                        선호 색상
                    </th>
                    <th>
                        블랙리스트
                    </th>
                    <th>
                        비고
                    </th>
                </tr>
            </thead>
                `;
    $.ajax({
        method: "get",
        url: "/member/list",
        success: function response(result) {
            result.forEach(r => {
                html += `<tbody class="memberPrint">
                            <tr>
                                <td class = "memcode">
                                    ${r.memcode}
                                </td>
                                <td>
                                    ${r.memname}
                                </td>
                                <td>
                                    ${r.memcontact}
                                </td>
                                <td>
                                    ${r.mememail}
                                </td>
                                <td>
                                    ${r.memgender}
                                </td>
                                <td>
                                    ${r.memsize}
                                </td>
                                <td>
                                    ${r.memjoindate}
                                </td>
                                <td>
                                    ${r.memlastdate}
                                </td>
                                <td>
                                    ${r.colorname}
                                </td>
                                <td class = "blacklist">
                                    ${r.blacklist == 0 ? "X" : "O"}
                                </td>
                                <td>
                                    <button type="button" onclick = "memberEdit(${r.memcode}, ${r.blacklist})"> 수정 </button><br>
                                    <button type="button" onclick = "memberRecommend(${r.memcode})"> 추천 </button><br>
                                    <button type="button"> 삭제 </button>
                                </td>
                            </tr>
                        </tbody>
                        
                    `;
            });
            html += '</table>';
            memberArea.innerHTML = html;
        }
    })

}
// ===================================  2024-08-13 김민석 html 수정(테이블) ========================================= //
function memberEdit(memcode, blacklist) {

    console.log(memcode);
    console.log(blacklist);
    $.ajax({
        async: false,
        method: "put",
        url: "/member/edit",
        data: JSON.stringify({ memcode: memcode, blacklist: blacklist }),
        contentType: "application/json",
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

// ===================================  2024-08-05 ========================================= //

function memberRecommend(memcode) {
    console.log(memcode);
    $.ajax({
        async: false,
        method: "get",
        url: "/member/recommend",
        data: { memcode: memcode },
        success: function response(result) {
            console.log(result);
        }
    })
}

// ===================================  2024-08-07 ========================================= //