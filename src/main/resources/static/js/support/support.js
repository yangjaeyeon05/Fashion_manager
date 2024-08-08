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
    supcode : 0 , 
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
    let supcode = document.querySelector('.supcodeBox').value;
    let supstate = document.querySelector('.supstateBox').value;
    let searchKey = document.querySelector('.searchKey').value;
    let searchKeyword = document.querySelector('.searchKeyword').value;
    let startDate = document.querySelector('.startDate').value;
    let endDate = document.querySelector('.endDate').value;
    searchInfo.supcode = supcode;
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
                        <td onclick="supDetailRead(${s.supcode})"> ${s.suptitle} </td>
                        <td> ${s.memname} </td>
                    </tr> `;
        }
    )   // forEach end
    supportPrintBox.innerHTML = html;
    // 출력
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
                    <tr>
                        <th> 답변내용  </th>
                        <td colspan="5"> 배송언제쯤 되나요? </td>
                    </tr>
            `;
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
    // 출력
    supDtailPrintBox.innerHTML = html;
}


