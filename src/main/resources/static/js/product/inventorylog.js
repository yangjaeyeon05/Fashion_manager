console.log('inventorylog.js');

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
    invlogdetail : 0 , 
    searchKey : '' , 
    searchKeyword : '' , 
    startDate : '' , 
    endDate : '' , 
}

invlogAllRead();
// 검색버튼 눌렀을 때
function onSearch(){
    console.log('onSearch()');
    let invlogdetail = document.querySelector('.invlogdetail').value;
    let searchKey = document.querySelector('.searchKey').value;
    let searchKeyword = document.querySelector('.searchKeyword').value;
    let startDate = document.querySelector('.startDate').value;
    let endDate = document.querySelector('.endDate').value;
    searchInfo.invlogdetail = invlogdetail;
    searchInfo.searchKey = searchKey;
    searchInfo.searchKeyword = searchKeyword;
    searchInfo.startDate = startDate;
    searchInfo.endDate = endDate;
    console.log(searchInfo);
    // 새로고침
    invlogAllRead();
}

// 로그불러오기
function invlogAllRead(){
    console.log('invlogAllRead()');
    let invlogList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/inventory/invlogallread" ,
        data : searchInfo , // 전역변수 보내기
        success : (r) => {
            console.log(r);
            invlogList = r;
        } , 
        error : (e) =>{
            console.log(e); 
        }
    })  // ajax end
    // 어디에
    let invlogPrintBox = document.querySelector(".invlogPrintBox");
    // 무엇을
    let html = ``;
    invlogList.forEach( i => {
            html += `<tr>
                        <td> ${i.invlogcode} </td>
                        <td> ${i.invdate} </td>
                        <td> ${i.invlogdetailname} </td>
                        <td> ${i.prodname} </td>
                        <td> ${i.colorname} </td>
                        <td> ${i.prodsize} </td>
                        <td> ${i.invlogchange} </td>
                        </tr> `;
        }
    )   // forEach end
    // 출력
    invlogPrintBox.innerHTML = html;
}