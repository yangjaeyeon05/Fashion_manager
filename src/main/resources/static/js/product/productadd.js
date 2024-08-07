//08.07
console.log('productadd.js');
//08.07 색깔 호출
getColor();
function getColor(){
    let color=document.querySelector('#productColor');
    let html='';
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

//08.07 카테고리 호출
getCategory();
function getCategory(){
    let category=document.querySelector('#productCategory');
    let html='';
    $.ajax({
        async:false, method:'get',
        url:"/product/category",
        success:result =>{
            result.forEach(r =>{
                html+=`<option value=${r.prodCatecod}>${r.prodCatename}</option>`
            })
            category.innerHTML=html;
        }
    })
};

//08.07 섬머노트 실행
$(document).ready(function() {

    let option={
        height:500,
        width:1200,
        lang:'ko-KR'
    }

    $('#summernote').summernote(option);
  });

//08.07 상품등록
function productAdd(){
    let productAddForm=document.querySelector('#productAdd');
    console.log(productAdd);

    let productAddFormData=new FormData(productAddForm);
    console.log(productAddFormData);
    
    
}
