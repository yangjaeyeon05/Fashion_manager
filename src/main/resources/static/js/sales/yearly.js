console.log('support.js');
let date = new Date();
console.log(date);
let currentYear = date.getFullYear();
let currentMonth = date.getMonth()+1 < 10 ? "0"+(date.getMonth()+1) : date.getMonth()+1;
let currentDay = date.getDate() < 10 ? "0"+(date.getDate()) : date.getDate();
date = `${currentYear}-${currentMonth}-${currentDay}`;
console.log(date)

// 날짜 검색 기능 8/7 검색 기능 구현 중
document.querySelector('.endDate').value = date;
// 날짜 버튼을 눌렀을 때
function changeDate(day){
    // startdate에 넣을 때 빼기로 설정을 해줘야하므로 설정에 따른 값 매개변수 day로 받아오기
    console.log("changeDate()")
    let startDateInput = document.querySelector(".startDate");
    let startDate = new Date();     // 왼쪽 input
    startDate.setDate(startDate.getDate()-day);     // 오늘 날짜에서 매개변수로 받아온 일수 만큼 빼주기 js라이브러리 함수
    console.log(startDate);
    let startYear = startDate.getFullYear();    console.log(startYear);
    // 한자리수일 경우에 0 앞에 붙이는 삼항연산자
    let startMonth = startDate.getMonth()+1 < 10 ? "0"+(startDate.getMonth()+1) : startDate.getMonth()+1;      
    console.log(startMonth);
    let startDay = startDate.getDate() < 10 ? "0"+(startDate.getDate()) : startDate.getDate();          
    console.log(startDay);
    let strStartDate = `${startYear}-${startMonth}-${startDay}`;    // input date 포맷이 문자열 "YY-MM-DD" 형식으로만 받기 떄문에 문자 따로 만들어주기
    console.log(strStartDate)
    startDateInput.value = strStartDate;
    let endDateInput = document.querySelector(".endDate");  // 오른쪽 input
    endDateInput.value = date;  // 컨셉 상 enddate는 오늘 날짜를 기준으로 함 전역변수로 설정해둔 오늘 날짜 대입.
}

// 검색 기능 객체 컨셉 상 검색 조건이 문의 유형별 , 처리상태별 , 검색별 , 기간별 이므로 해당하는 객체 만들어주기
let searchInfo = {
    supcategory : 0 , 
    supstate : 0 , 
    searchKey : '' , 
    searchKeyword : '' , 
    startDate : '' , 
    endDate : '' , 
}

supAllread();
// 검색버튼 눌렀을 때
function onSearch(){
    console.log('onSearch()');
    let supcategory = document.querySelector('.supcodeBox').value;
    let supstate = document.querySelector('.supstateBox').value;
    let searchKey = document.querySelector('.searchKey').value;
    let searchKeyword = document.querySelector('.searchKeyword').value;
    let startDate = document.querySelector('.startDate').value;
    let endDate = document.querySelector('.endDate').value;
    searchInfo.supcategory = supcategory;
    searchInfo.supstate = supstate;
    searchInfo.searchKey = searchKey;
    searchInfo.searchKeyword = searchKeyword;
    searchInfo.startDate = startDate;
    searchInfo.endDate = endDate;
    console.log(searchInfo);
    // 새로고침
    supAllread();
}
// 상담목록 전체 출력
function supAllread(){
    console.log('supAllread()');
    let supportList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/support/allread" ,
        data : searchInfo , // 전역변수 보내기
        success : (r) => {
            console.log(r);
            supportList = r;
        } , 
        error : (e) =>{
            console.log(e); 
        }
    })  // ajax end
    // 어디에
    let supportPrintBox = document.querySelector(".supportPrintBox");
    // 무엇을
    let html = ``;
    supportList.forEach( s => {
            html += `<tr>
                        <td> ${s.supcode} </td>
                        <td> ${s.supdate} </td>
                        <td> ${s.supstatename} </td>`;
            if(s.ordcode != 0){
                html += `<td> ${s.ordcode} </td>`;
            }else{
                html += `<td> </td>`;
            }                    
            html += `<td> ${s.supcategoryname} </td>
                        <td>
                            <a href="#" class="supDetailLink" data-bs-toggle="modal" data-bs-target="#staticBackdrop" data-id="${s.supcode}"> ${s.suptitle} </a> 
                        </td>
                        <td> ${s.memname} </td>
                    </tr> `;
        }
    )   // forEach end
    // 출력
    supportPrintBox.innerHTML = html;
    // 제목 클릭 시 모달 열기
    document.querySelectorAll('.supDetailLink').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            let supcode = this.getAttribute('data-id');
            supDetailRead(supcode);  // 모달에 상세 정보 로드
        });
    });
}   // supAllread() end

function supDetailRead(supcode){
    console.log('supDetailRead()');
    // 어디에
    let supDtailPrintBox = document.querySelector('.supDtailPrintBox');
    // 무엇을
    let html = ``;
    $.ajax({
        async : false , 
        method : "get" , 
        url : "/support/read" , 
        data : { "supcode" : supcode } , 
        success : (r) => {
            console.log(r);
            html += `
            <table class="table">
                <tbody>
                    <tr>
                        <th> 접수일 </th>
                        <td> ${r.supdate} </td>
                        <th> 문의 유형 </th> 
                        <td> ${r.supcategoryname} </td> 
                        <th> 처리 상태 </th>
                        <td> ${r.supstatename} </td>
                    </tr>
                    <tr>
                        <th> 작성자명 </th>
                        <td> ${r.memname} </td>
                        <th> 작성자이메일</th> 
                        <td> ${r.mememail} </td> 
                        <th> 작성자연락처 </th>
                        <td> ${r.memcontact} </td>
                    </tr>
                    <tr>
                        <th> 주문번호 </th>
                    `;
                    if(r.ordcode != 0){             // 만약에 주문코드가 있으면 주문코드를 출력하고 
                        html += `<td> ${r.ordcode} </td>`
                    }else{                          // 없으면 공백
                        html += `<td> </td>`;
                    }
            html += `   <th> 상품코드</th> `;
                    if(r.proddetailcode != 0){             // 만약에 상품코드가 있으면 상품코드를 출력하고 
                        html += `<td> ${r.proddetailcode} </td>`
                    }else{                          // 없으면 공백
                        html += `<td> </td>`;
                    }
            html += `   <th> 상품이름</th> `;
                    if(r.proddetailcode != 0){             // 만약에 상품코드가 있으면 상품코드를 출력하고 
                        html += `<td> ${r.prodname} </td>`
                    }else{                          // 없으면 공백
                        html += `<td> </td>`;
                    }
            html += `</tr>
                    <tr>
                        <th> 문의내용 </th>
                        <td colspan="5"> ${r.supcontent} </td>
                    </tr>
                    <tr class="replyPrintBox">
                        <th> 답변내용  </th>
                        <td colspan="5" class="replyPrint"> </td>
                    </tr>
                    <tr class="replyUpdatebox">
                        
                    </tr>
                    </tbody>
                    </table>
                    <div class="replyBtn">
                        <button type="button" class="btn btn-success btn-sm" onclick="replyAdd(${supcode})"> 답변등록 </button>
                        <button type="button" class="btn btn-success btn-sm" onclick="replyDelete(${supcode})"> 답변삭제 </button>
                        <button type="button" class="btn btn-success btn-sm" onclick="replyUpdateTocom(${supcode})" > 상담완료 </button>
                    </div>    
                        `;
            if (supDtailPrintBox) {
                supDtailPrintBox.innerHTML = html;
            }
            replyRead(supcode);
            // 모달을 열기
            // let modal = new bootstrap.Modal(document.getElementById('staticBackdrop'));
            // modal.show();
            // 부트스트랩 자체에서 실행되는데 한번 더 코드를 넣으니까 두개가 실행되서 하나 끄게 되면 하나 남아서 오류가 뜸
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}

// 답글 출력
function replyRead(supcode){
    console.log('replyRead()')
    // 어디에
    let replyPrint = document.querySelector('.replyPrint');
    let html = ``;
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/support/respread" , 
        data : {"supcode" : supcode } , 
        success : (r) => {
            console.log(r);
            if (replyPrint) {               // document.querySelector로 요소 가져올 경우 선택한 요소가 
                if(r.replycontent!=null){
                    html += `<div class="replyBox">
                            ${r.replycontent}
                            <button type="button" class="btn btn-success btn-sm" onclick="createBox(${r.replycode} , ${supcode})">답변수정</button>
                            </div>
                            `;
                }else{
                    html += `<input style="width:100%;" type="text" class="replyContent" />`;
                }
                replyPrint.innerHTML = html;   // null 일 수 있으므로 if 문으로 확인 출력 안그럼 오류가 뜸???
            }        
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // replyRead() end

// 딥변 등록
function replyAdd(supcode){
    console.log('replyAdd()');
    console.log(supcode);
    let replycontent = document.querySelector(".replyContent").value;
    let info = {
        "replycontent" : replycontent , "supcode" : supcode
    }
    $.ajax({
        async : false , 
        method : 'post' , 
        url : "/support/respadd" , 
        data : JSON.stringify(info) , // 객체를 JSON 문자열로 변환
        contentType : "application/json" , 
        success : (r) => {
            console.log(r);
            if(r){
                alert('답변등록성공');
                replyUpdateToing(supcode);  // 처리상태 변경
                replyRead(supcode);     // 새로고침

            }else{
                alert('답변등록실패')
            }
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // replyAdd() end

// 답변 등록 했을 때 처리상태 변경 상담 전 -> 진행 중
function replyUpdateToing(supcode){
    console.log('replyUpdateToing()');
    $.ajax({
        async : false , 
        method : 'put' , 
        url : "/support/edittoing" , 
        data : {supcode : supcode} , 
        success : (r) =>{
            console.log(r)
            replyRead(supcode);     // 답변 처리하면 답변 등록된 게 새로고침 되어야하고 전체출력이랑 상세 출력도 새로고침 되어야 한다.
            supDetailRead(supcode);
            supAllread();
        } , 
        error : (e) => {
            console.log(e)
        }
    })  // ajax end
}   // replyUpdateToing() end

// 처리 완료 눌렀을 때 처리상태 변경 진행 중 -> 처리 완료
function replyUpdateTocom(supcode){
    console.log('replyUpdateTocom()');
    $.ajax({
        async : false , 
        method : 'put' , 
        url : "/support/edittocom" , 
        data : {supcode : supcode} , 
        success : (r) =>{
            console.log(r)
            replyRead(supcode);     // 답변 처리하면 답변 등록된 게 새로고침 되어야하고 전체출력이랑 상세 출력도 새로고침 되어야 한다.
            supDetailRead(supcode);
            supAllread();
        } , 
        error : (e) => {
            console.log(e)
        }
    })  // ajax end
}   // replyUpdateTocom() end

// 답변삭제
function replyDelete(supcode){
    console.log('replyDelete()')
    $.ajax({
        async : false , 
        method : 'delete' , 
        url : "/support/respedelete" , 
        data : {supcode : supcode} , 
        success : (r) =>{
            console.log(r)
            replyRead(supcode);     // 답변 처리하면 답변 등록된 게 새로고침 되어야하고 전체출력이랑 상세 출력도 새로고침 되어야 한다.
            supDetailRead(supcode);
            supAllread();
        } , 
        error : (e) => {
            console.log(e)
        }
    })  // ajax end
}   // replyDelete() end

// 엑셀로 테이블 데이터 다운받기
function excelExport(){
    console.log('excelExport');
    var xhr = new XMLHttpRequest();
            xhr.open('GET', '/file/export/excel', true);
            xhr.responseType = 'blob'; // 서버에서 반환된 데이터를 Blob으로 처리
            xhr.onload = function() {
                if (xhr.status === 200) {
                    var disposition = xhr.getResponseHeader('Content-Disposition');
                    var filename = 'downloaded-file';
                    if (disposition && disposition.indexOf('filename=') !== -1) {
                        filename = disposition.split('filename=')[1].replace(/"/g, '');
                    }

                    var link = document.createElement('a');
                    link.href = window.URL.createObjectURL(xhr.response);
                    link.download = filename;
                    document.body.appendChild(link); // 링크를 문서에 추가
                    link.click(); // 링크 클릭하여 다운로드
                    document.body.removeChild(link); // 다운로드 후 링크 제거

                    // 메모리 정리를 위한 객체 URL 해제
                    window.URL.revokeObjectURL(link.href);
                } else {
                    console.error('Download failed: ' + xhr.statusText);
                }
            };
            xhr.onerror = function() {
                console.error('Download failed');
            };
            xhr.send();
}

// 답변 내용 수정
function createBox(replycode , supcode){
    console.log('replyUpdate()');
    // 수정을 위한 텍스트 입력 창 먼저 출력
    // 어디에 
    let replyUpdatebox = document.querySelector(".replyUpdatebox");
    // 무엇을
    let html = `
                <th> 답변수정내용  </th>
                <td colspan="5"> 
                <div class="updateBox">
                <input type="text" style="width:80%;"class="replycontent" placeholder="수정할 내용을 입력하세요" /> 
                <button type="button" class="btn btn-success btn-sm" onclick="submitUpdate(${replycode} , ${supcode})">수정등록</button>
                </div>
                </td>
                `;
    // 출력
    replyUpdatebox.innerHTML = html;
    replyUpdate(replycode , supcode);
}   // createBox() end

// 답변 수정 요청을 서버로 보내는 함수
function submitUpdate(replycode , supcode){
    // 출력 후 입력된 값 가져오기
    let replycontent = document.querySelector(".replycontent").value;
    console.log(replycontent);
    // 입력 내용이 비어 있는 경우 경고 메시지
    if (!replycontent) {
        alert('답변 내용을 입력해 주세요.');
        return;
    }
    // 객체 만들어서 전달
    let info = {
        replycontent : replycontent , replycode : replycode
    }
    console.log(info);
    // ajax 통신
    $.ajax({
        async : false , 
        method : "put" , 
        url : "/support/respedit" , 
        data : JSON.stringify(info) , // 객체를 JSON 문자열로 변환
        contentType : "application/json" , 
        success : (r) => {
            console.log(r);
            if(r){
                alert('답변수정성공');
                replyRead(supcode);     // 새로고침
                supDetailRead(supcode);
            }else{
                alert('답변수정실패')
            }
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // submitUpdate() end