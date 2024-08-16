console.log('vendor.js'); 


vendorallread();
function vendorallread(){
    console.log('vendorallread()')
    let vendortList = [];
    let searchKey = document.querySelector(".searchKey").value;
    let searchKeyword = document.querySelector(".searchKeyword").value;
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/vendor/allread" ,
        data : {searchKey : searchKey , searchKeyword : searchKeyword} , 
        success : (r) => {
            console.log(r);
            vendortList = r;
        } , 
        error : (e) =>{
            console.log(e); 
        }
    })  // ajax end
    // 어디에
    let vendorPrinbox = document.querySelector(".vendorPrinbox");
    // 무엇을
    let html = ``;
    vendortList.forEach( v => {
            html += `<tr>
                        <td> <input type="checkbox" value="${v.vendorcode}" id="vendorcodeCheck"/> </td>
                        <td> ${v.vendorcode} </td>
                        <td>
                            <a href="#" class="wpDetailLink" data-bs-toggle="modal" data-bs-target="#wpModal" data-id="${v.vendorcode}" onclick="wpRead(${v.vendorcode})"> ${v.vname} </a> 
                        </td>
                        <td> ${v.vcontact} </td>
                        <td> ${v.vaddress} </td>
                        <td> ${v.vdate} </td>
                        </tr>`
                        ; 
        }
    )   // forEach end
    // 출력
    // console.log(html);
    vendorPrinbox.innerHTML = html;
    // 이름클릭 시 모달열기
    // document.querySelector(".wpDetailLink").forEach(link=>{
    //     link.addEvnetListener('click' , function(e){
    //         e.preventDefault();
    //         let vendorcode = this.getAttribute('data-id');
    //         wpRead(vendorcode);
    //     });
    // });
}   // vendorallread() end

// 거래처등록
function vendorAdd(){
    console.log('vendorAdd()');
    // 입력 값 가져오기
    let vname = document.querySelector(".vname").value;
    let vcontact = document.querySelector(".vcontact").value;
    let vaddress = document.querySelector(".vaddress").value;
    // 객체 만들기
    let info = {
        vname : vname , vcontact : vcontact , vaddress : vaddress
    }
    // ajax통신
    $.ajax({
        async : false , 
        method : 'post' , 
        url : "/vendor/add" , 
        data : JSON.stringify(info) , 
        contentType : "application/json" , 
        success : (r) =>{
            console.log(r);
            if(r){
                alert('등록성공');
                vendorallread();    // 새로고침
                // 모달 닫기
                $('#vendorAddModal').modal('hide');
                // 모달안 입력창 값 초기화
                $('#vendorAddModal').on('hidden.bs.modal', function (e) {
                    $(this).find('form')[0].reset();
                });
            }else{
                alert('등록실패 입력값을 확인해주세요.');
            }
        } , 
        error : (e) => {
            console.log(e)
        } 
    })  // ajax end
}   // vendorAdd() end

// 거래처 삭제
function vendorDelete(){
    console.log('vendorDelete()');
    let vendorcode = 0;
    // 체크박스에 설정된 값 가져오기 , ajax와 통신해야하기 때문에 변수 만들어서 저장.
    $('input:checkbox[id="vendorcodeCheck"]:checked').each(function() {
        vendorcode = $(this).val();
    });
    console.log(vendorcode);
    $.ajax({
        async : false , 
        method : 'delete' , 
        url : "/vendor/delete" , 
        data : {vendorcode : vendorcode} , 
        success : (r) => {
            console.log(r);
            if(r){
                alert('삭제성공');
                vendorallread();
            }else{
                alert('삭제실패');
            }
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // vendorDelete() end

// 수정 전 정보 가져오기
function infoRead(){
    console.log('infoRead()');
    let vendorcode = 0;
    // 체크박스에 설정된 값 가져오기 , ajax와 통신해야하기 때문에 변수 만들어서 저장.
    $('input:checkbox[id="vendorcodeCheck"]:checked').each(function() {
        vendorcode = $(this).val();
    });
    console.log(vendorcode);
    if(vendorcode == 0){
        alert('수정할 거래처를 선택해주세요.');
        $('#vendorUpdateModal').modal('hide');
        return;
    }
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/vendor/read" , 
        data : {vendorcode : vendorcode} , 
        success : (r) =>{
            console.log(r);
            document.querySelector("#vname").innerHTML = `${r.vname}`;  // div 
            document.querySelector("#vcontact").value = `${r.vcontact}`;    // input
            document.querySelector("#vaddress").value = `${r.vaddress}`;    // input
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // infoRead() end

// 거래처정보수정
function vendorUpdate(){
    console.log('vendorUpdate()');
    let vendorcode = 0;
    // 체크박스에 설정된 값 가져오기 , ajax와 통신해야하기 때문에 변수 만들어서 저장.
    $('input:checkbox[id="vendorcodeCheck"]:checked').each(function() {
        vendorcode = $(this).val();
    });
    console.log(vendorcode);
    // 입력값 가져오기
    let vcontact = document.querySelector('#vcontact').value;
    let vaddress = document.querySelector('#vaddress').value;
    // 객체만들기
    let info = {
        vendorcode : vendorcode , vcontact : vcontact , vaddress : vaddress
    }
    $.ajax({
        async : false , 
        method : 'put' , 
        url : "/vendor/update" , 
        data : JSON.stringify(info) , 
        contentType : "application/json" ,
        success : (r) =>{
            console.log(r);
            if(r){
                alert('수정성공');
                vendorallread();
                // 모달 닫기
                $('#vendorAddModal').modal('hide');
                // 모달안 입력창 값 초기화
                $('#vendorAddModal').on('hidden.bs.modal', function (e) {
                    $(this).find('form')[0].reset();
                });
            }else{
                alert('수정실패');
            }
        } , 
        error : (e) => {
            console.log(e);
        }
    })  // ajax end
}   // vendorUpdate() end

// 거래처별 도매상품출력
function wpRead(vendorcode){
    console.log('wpRead()');
    console.log(vendorcode);
    let wpList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/wholesaleproduct/read" ,
        data : {vendorcode : vendorcode} , 
        success : (r) => {
            console.log(r);
            wpList = r;
        } , 
        error : (e) =>{
            console.log(e); 
        }
    })  // ajax end
    // 어디에
    let wpPrintbox = document.querySelector(".wpPrintbox");
    // 무엇을
    let html = ``;
    wpList.forEach( wp => {
            html += `<tr>
                        <td> ${wp.wpcode} </td>
                        <td> ${wp.wpname} </td>
                        <td> ${wp.wpcost} </td>
                        <td> ${wp.prodname} </td>
                        <td> ${wp.prodsize} </td>
                        <td> ${wp.colorname} </td>
                        <td> ${wp.vname} </td>
                        <td> 
                        ${wp.inv} 
                        </td>
                        <td>
                            <input type="number" class="quantity" onKeyPress="if( event.keyCode==13 ){addFunc(${wp.wpcost});}"/>
                            <button type="button" class="btn btn-success" onclick="doPo(${wp.wpcode} , ${wp.wpcost} )" > + </button>
                            <span class="totalamount"> </span>
                        </td>
                        </tr>`
                        ; 
        }
    )   // forEach end
    // 출력
    // console.log(html);
    wpPrintbox.innerHTML = html;
}   // wpRead() end

// 주문총금액 출력
function addFunc(wpcost){
    console.log('addFunc()');
    console.log(wpcost);
    let quantity = document.querySelector(".quantity").value;
    console.log(quantity);
    // 어디에
    let totalamount = document.querySelector(".totalamount");
    console.log(totalamount);
    // 무엇을
    let mul = quantity * wpcost;
    console.log(mul);
    // 출력
    totalamount.innerHTML= mul + `원`;
}

// 발주
function doPo(wpcode , wpcost){
    console.log('wpcode()');
    console.log(wpcode);
    console.log(wpcost);
    // 주문 수량
    let quantity = document.querySelector(".quantity").value;
    // 주문 총금액
    let totalamount = quantity * wpcost;
    let info = {
        wpcode  : wpcode , quantity : quantity , totalamount : totalamount
    }
    $.ajax({
        async : false , 
        method : 'post' , 
        url : "/wholesaleproduct/dopo" , 
        data : JSON.stringify(info) , 
        contentType : "application/json" , 
        success : (r) =>{
            if(r){
                alert('발주성공');
                // 모달안 입력창 값 초기화
                $('#wpModal').on('hidden.bs.modal', function (e) {
                    $(this).find('form')[0].reset();
                });
                // 모달 닫기
                $('#wpModal').modal('hide');

            }else{
                alert('발주실패');
            }
        }
    })  // ajax end
}   // doPo() end

// 발주현황 출력
function pologRead(){
    console.log('pologRead()');
    let poList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/wholesaleproduct/poread" ,
        success : (r) => {
            console.log(r);
            poList = r;
        } , 
        error : (e) =>{
            console.log(e); 
        }
    })  // ajax end
    // 어디에
    let poPrintbox = document.querySelector(".poPrintbox");
    // 무엇을
    let html = ``;
    poList.forEach( po => {
            html += `<tr>
                        <td> ${po.pocode} </td>
                        <td> ${po.wpname} </td>
                        <td> ${po.quantity} </td>
                        <td> ${po.prodsize} </td>
                        <td> ${po.colorname} </td>
                        <td> ${po.totalamount} </td>
                        <td> ${po.vname} </td>
                        <td> ${po.quantitydate} </td>`;
            if(po.arrivaldate == null){
                html += `<td> </td>`;
            }else{
                html += `<td> ${po.arrivaldate} </td>`;
            }        
            html += `<td> 
                        ${po.quantitystatename} 
                        <button type="button" class="btn btn-success" onclick="addinvlog(${po.pocode} , ${po.proddetailcode} , ${po.quantity})" > 완료 </button>
                    </td>
                    </tr>`; 
        }
    )   // forEach end
    // 출력
    // console.log(html);
    poPrintbox.innerHTML = html;
}

// 완료버튼 누르면 처리상태 도착완료 , 재고로그 +
function addinvlog(pocode , proddetailcode , quantity){
    console.log('addinvlog()');
    console.log(pocode);
    console.log(proddetailcode);
    console.log(quantity);
    $.ajax({
        async : false ,
        method : 'put' , 
        url : "/wholesaleproduct/update" , 
        data : {pocode : pocode} , 
        success : (r)=>{
            console.log(r);
            if(r){
                alert("재고입고완료");
                pologRead();
            }
        } , 
        error : (e)=>{
            console.log(e);
        }
    })  // ajax
    // 재고로그 추가 ajax
    $.ajax({
        async : false ,
        method : 'post' , 
        url : "/wholesaleproduct/invlogadd" , 
        data : {proddetailcode : proddetailcode , quantity : quantity} , 
        success : (r)=>{
            console.log(r);
        } , 
        error : (e)=>{
            console.log(e);
        }
    })  // ajax end
}