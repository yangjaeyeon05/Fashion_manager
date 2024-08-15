console.log('productgetall.js');
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
    prodCatecode : 0 , 
    colorCode : 0 ,
    minPrice:0,
    maxPrice:0, 
    prodSize : '' , 
    searchKeyword : '' , 
    startDate : '' , 
    endDate : '' ,
    prodGender:'' 
}

productAllread();
// 검색버튼 눌렀을 때 08.14
function onSearch(){
    console.log('onSearch()');
    let prodCatecode = document.querySelector('#prodCatecode').value;
    let colorCode = document.querySelector('#colorCode').value;
    let prodSize = document.querySelector('#prodSize').value;
    let searchKeyword = document.querySelector('#searchKeyword').value;
    let startDate = document.querySelector('.startDate').value;
    let endDate = document.querySelector('.endDate').value;
    let minPrice=document.querySelector('.minPrice').value;
    let maxPrice=document.querySelector('.maxPrice').value;
    let prodGender=document.querySelector('#prodGender').value;
    if(minPrice==""){minPrice=0}
    if(maxPrice==""){maxPrice=0}
    searchInfo.prodCatecode = prodCatecode;
    searchInfo.colorCode = colorCode;
    searchInfo.prodSize = prodSize
    searchInfo.searchKeyword = searchKeyword;
    searchInfo.startDate = startDate;
    searchInfo.endDate = endDate;
    searchInfo.minPrice=minPrice;
    searchInfo.maxPrice=maxPrice;
    searchInfo.prodGender=prodGender;
    console.log(searchInfo);
    
    // 새로고침
    productAllread();
}
// 상품목록 전체 출력 08.14
function productAllread(){
    console.log('productAllread()');
    let productList = [];
    $.ajax({
        async : false , 
        method : 'get' , 
        url : "/product/getall" ,
        data : searchInfo , // 전역변수 보내기
        success : (r) => {
            console.log(r);
            productList = r;
        } , 
        error : (e) =>{
            console.log(e); 
        }
    })  // ajax end
    // 어디에
    let productPrint = document.querySelector(".productPrint");
    // 무엇을
    let html = ``;
    productList.forEach( p => {
            html += `<tr>
                        <th scope="row">${p.prodDetailcode}</th>
                        <td>${p.prodName}</td>
                        <td>${p.prodPrice}</td>
                        <td>${p.prodGender}</td>
                        <td>${p.prodCatename}</td>
                        <td>${p.colorName}</td>
                        <td>${p.prodSize}</td>
                        <td>${p.prodDate}</td>
                        <td><a href="/file/download?filename=${p.prodFilename}">${p.prodFilename==null?"":p.prodFilename.split('_')[1]}</a></td>
                    </tr>`;
            
        }
    )   // forEach end
    // 출력
    productPrint.innerHTML = html;
}
getColor();
function getColor(){//08.14
    let color=document.querySelector('#colorCode');
    let html='<option value=0>전체</option>';
    $.ajax({
        async:false, method:'get',
        url:"/product/color",
        success:result =>{
            result.forEach(r =>{
                html+=`<option value=${r.colorCode}>${r.colorName}</option>`
            })
            color.innerHTML=html;
        }

    })
};
getCategory();
function getCategory(){//08.14
    let category=document.querySelector('#prodCatecode');
    let html='<option value=0>전체</option>';
    $.ajax({
        async:false, method:'get',
        url:"/product/category",
        success:result =>{
            result.forEach(r =>{
                html+=`<option value=${r.prodCatecode}>${r.prodCatename}</option>`
            })
            category.innerHTML=html;
        }
    })
};