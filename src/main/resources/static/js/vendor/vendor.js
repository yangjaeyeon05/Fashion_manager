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
                        <td> <input type="checkbox" value="${wp.wpcode}" id="wpcodeCheck"/> </td>
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
                            <input type="number" class="quantity" data-cost="${wp.wpcost}"/>
                            <button type="button" class="btn btn-success" onclick="doPo(${wp.wpcode} , ${wp.wpcost} )" > + </button>
                            <span class="totalamount"> </span>
                        </td>
                        </tr>`
                        ; 
        }
    )   // forEach end
    // 출력
    wpPrintbox.innerHTML = html;
    // 입력 필드에 대한 keypress 이벤트 핸들러 추가
    document.querySelectorAll('.quantity').forEach(input => {   // 모든 .quantity 클래스를 가진 입력필드 선태
        input.addEventListener('keypress', function (event) {   // 각 입력필드에 keypress 이벤트리스너 츠가
            if (event.code === 'Enter') {                       // 엔터키가 눌려지면
                const wpcost = this.getAttribute('data-cost');  // data-cost 속성에서 가격 값을 가져와 , 각 입력 필드라는 것을 알기 위해 this도 같이 전달
                console.log(this, wpcost);                      
                addFunc(this , wpcost);                         // 함수에 전달
                event.preventDefault();
            }
        });
    });
}   // wpRead() end

// 주문 총금액 출력
function addFunc(inputElement, wpcost) {
    console.log('addFunc()');
    console.log(wpcost);

    // 입력 필드의 값을 가져옵니다.
    let quantity = inputElement.value;
    console.log(quantity);

    // 총금액 계산
    let mul = quantity * wpcost;
    console.log(mul);

    // 그냥 출력했을 때 두개 이상의 input값이 있으면 첫번째 input에 값이 항상 들어감
    // 그래서 출력할 값의 자리를 정확히 찾기 위한 부모요소 찾기
    // 입력 필드의 상위 td 요소를 찾고 그 안에서 totalamount 요소를 찾습니다.
    let tdElement = inputElement.parentElement;
    let totalamountElement = tdElement.querySelector('.totalamount');
    console.log(totalamountElement);

    // 총금액 출력
    totalamountElement.innerHTML = mul + `원`;
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
    let quantitystate = document.querySelector(".supstateBox").value;
    let poList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/wholesaleproduct/poread" ,
        data : {quantitystate : quantitystate} , 
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
}   // addinvlog end


// 도매상품등록할때 필요한 거래처 목록 불러오기
function vencoderead(){
    console.log('vencoderead()');
    $.ajax({
        async:false,
        method:'get',
        url:"/wholesaleproduct/vencoderead",
        success:(r) =>{
            console.log(r);
            // 어디에
            let vendorlist = document.querySelector("#vendorlist");
            // 무엇을
            let html = ``;
                // - 서버로부터 응답받은 데이터를 타입 확인했더니 , List 타입이므로 반복문 사용하자.
                    // 언어별 화살표 함수 표현 방식 JAVA : () -> {}, JS : () => {}
            r.forEach(v =>{
                html+=`<option value="${v.vendorcode}">${v.vname}</option>`;
            })
            // 출력
            vendorlist.innerHTML=html;
        } ,
        error : (e) => {
            console.log(e)
        }
    })
}

// 도매 상품 등록하기
function wpadd(){
    console.log('wpadd()');
    // 입력값 가져오기
    let wpname = document.querySelector(".wpname").value;
    let wpcost = document.querySelector(".wpcost").value;
    let proddetailcode = document.querySelector(".proddetailcode").value;
    let vendorcode = document.querySelector("#vendorlist").value;

    // 객체만들기
    let info = {
        wpname : wpname , wpcost : wpcost , proddetailcode : proddetailcode , vendorcode : vendorcode
    }
    $.ajax({
        async : false , 
        method : 'post' , 
        url : "/wholesaleproduct/wpadd" , 
        data : JSON.stringify(info) , 
        contentType : "application/json" , 
        success : (r) =>{
            console.log(r);
            if(r){
                alert('등록성공');
                // 모달안 입력창 값 초기화
                $('#wpaddModal').on('hidden.bs.modal', function (e) {
                    $(this).find('form')[0].reset();
                });
                // 모달 닫기
                $('#wpaddModal').modal('hide');
            }else{
                alert('등록실패')
            }
        }
    })  // ajax end
}   // wpadd

// 도매상품 삭제
function wpdelete(){
    console.log('wpdelete()');
    // 체크박스에 설정된 값 가져오기 , ajax와 통신해야하기 때문에 변수 만들어서 저장.
    $('input:checkbox[id="wpcodeCheck"]:checked').each(function() {
        wpcode = $(this).val();
    });
    console.log(wpcode);
    $.ajax({
        async : false , 
        method : 'delete' , 
        url : "/wholesaleproduct/wpdelete" ,
        data : { wpcode : wpcode } , 
        success : (r)=>{
            console.log(r);
            if(r){
                alert('삭제성공');
                // 모달안 입력창 값 초기화
                $('#wpModal').on('hidden.bs.modal', function (e) {
                    $(this).find('form')[0].reset();
                });
                // 모달 닫기
                $('#wpModal').modal('hide');
            }else{
                alert('삭제실패');
            }
        } ,
        error : (e) =>{
            console.log(e);
        }
    })  // ajax end
}
// 보통 도매 상가에서 도매상품 가격이 수정되는 경우는 거의 없음 , 새로운 상품이름으로 출시되는 경우가 많음
// 그래서 수정 기능은 넣지 않음.