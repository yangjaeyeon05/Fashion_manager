console.log('support.js');

supAllread();
function supAllread(){
    console.log('supAllread()');
    let supportList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/support/allread" , 
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
                        <td> ${s.supstate} </td>
                        <td> ${s.supcategoryname} </td>
                        <td> ${s.suptitle} </td>
                        <td> ${s.memname} </td>
                    </tr> `;
        }
    )   // forEach end
    supportPrintBox.innerHTML = html;
    // 출력
}