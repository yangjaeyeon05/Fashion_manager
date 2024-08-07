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
// 오늘 버튼을 눌렀을 때
function todayBtn(){
    console.log("todayBtn()")
    let startDate = document.querySelector(".startDate");
    startDate.value = date;
    let endDate = document.querySelector(".endDate");
    endDate.value = date;
}
// 3일 버튼을 눌렀을 때
function threeDays(){
    console.log('threeDays()');
    let startDate = document.querySelector(".startDate");
    currentDay = parseInt(currentDay)-3 < 10 ? "0"+(parseInt(currentDay)-3) : parseInt(currentDay)-3;
    date = `${currentYear}-${currentMonth}-${currentDay}`;
    startDate.value = date;
    console.log(date);
}

// 1주일 버튼을 눌렀을 때
function aweek(){
    console.log('aweek()');
    console.log(currentYear)
    console.log(currentMonth)
    console.log(currentDay)
    let startDate = document.querySelector(".startDate");
    currentDay = parseInt(currentDay)-7 < 10 ? "0"+(parseInt(currentDay)-7) : parseInt(currentDay)-7;
    if(currentDay<1){
        currentDay=31;
        currentMonth-1
    }

    date = `${currentYear}-${currentMonth}-${currentDay}`;
    startDate.value = date;
    console.log(date);
}

// 1개월 버튼 눌렀을 때
function oneMonth(){
    console.log('oneMonth()')
}
// 3개월 버튼 눌렀을 때
function threeMonth(){
    console.log('threeMonth()')
}

// 검색 기능 객체
let searchInfo = {
    supcode : 0 , 
    supstate : 0 , 
    searchKey : '' , 
    searchKeyword : ''
}

supAllread();
// 검색버튼 눌렀을 때
function onSearch(){
    console.log('onSearch()');
    let supcode = document.querySelector('.supcodeBox').value;
    let supstate = document.querySelector('.supstateBox').value;
    let searchKey = document.querySelector('.searchKey').value;
    let searchKeyword = document.querySelector('.searchKeyword').value;
    searchInfo.supcode = supcode;
    searchInfo.supstate = supstate;
    searchInfo.searchKey = searchKey;
    searchInfo.searchKeyword = searchKeyword;
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
                        <td> ${s.suptitle} </td>
                        <td> ${s.memname} </td>
                    </tr> `;
        }
    )   // forEach end
    supportPrintBox.innerHTML = html;
    // 출력
}
