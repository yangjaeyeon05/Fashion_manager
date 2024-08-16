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
                            <a href="#" class="supDetailLink" data-bs-toggle="modal" data-bs-target="#staticBackdrop" data-id="${v.vendorcode}"> ${v.vname} </a> 
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