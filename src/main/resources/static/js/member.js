console.log("member.js");

memberList();
// ===================================  2024-08-13 김민석 html 수정(테이블) ========================================= //
function memberList(page = 1, size = 10) {
    let memberArea = document.querySelector(".memberArea");
    let searchKey = document.querySelector("#memberSearchValue").value;
    let searchKeyword = document.querySelector("#memberSearch").value;
    if(searchKey == 1){
        searchKey = "memname"
    }else if(searchKey == 2){
        searchKey = "memcontact"
    }else if(searchKey == 3){
        searchKey = "mememail"
    }else if(searchKey == 4){
        searchKey = "memgender"
    }else if(searchKey == 5){
        searchKey = "memsize"
    }else if(searchKey == 6){
        searchKey = "blacklist"
        searchKeyword = searchKeyword == "X" ? "0" : "1"
    }
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
        data : {page : page, size : size, searchKey : searchKey, searchKeyword : searchKeyword},
        success: function response(result) {
            //페이지 데이터 및 페이지네이션 정보 가져 오기
            let members = result.data; //조호된 게시물 정보
            let total = result.totalPage; //전체 페이지수
            let currentPage = result.page; // 현재 페이지 번호


            if (members && members.length > 0){
            members.forEach(r => {
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
                                </td>
                            </tr>
                        </tbody>
                        
                    `;
            });
        }
            let paginationBox = document.querySelector('.pagination');
                let pageHTML = '';

                if (total > 0){ //if start 총페이지수가 0 보다 많으면
                for(let i = 1; i<= total; i++){ //for start 반복문을 돌려서
                    pageHTML += `
                    <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="memberList(${i}, ${size})">${i}</a>
                    </li>
                    `;
                    //페이지의 번호와 데이터에 맞는 버튼 생성

                    } //for end
                } //if end

                //페이지네이션 버튼출력
                
            html += '</table>';
            memberArea.innerHTML = html;

            paginationBox.innerHTML = pageHTML;
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
        url: "/member/newrecommend",
        data: { memcode: memcode },
        success: function response(result) {
            console.log(result);
        }
    })
}

// ===================================  2024-08-07 ========================================= //

