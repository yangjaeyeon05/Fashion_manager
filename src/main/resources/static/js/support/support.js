console.log('support.js');

document.querySelector('.endDate').value = new Date().toISOString().substring(0, 10);
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
