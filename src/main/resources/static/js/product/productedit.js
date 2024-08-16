console.log('productedit.js');

let urlParams = new URL(location.href).searchParams;
let prodDetailcode = urlParams.get("proddetailcode") //상품번호

//08.16 섬머노트
$(document).ready(function() {

    let option={
        height:500,
        width:1200,
        lang:'ko-KR'
    }

    $('#summernote').summernote(option);
  });
// 색상 호출
  function getColor(){
    console.log('getColor()');    
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

// 카테고리 호출
function getCategory(){
    console.log('getCategory()');
    let category=document.querySelector('#productCategory');
    let html='';
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
  

 


  //08.16 개별출력
  getOne()
  function getOne(){
    console.log("getOne()");
    let product=[];
    getColor();
    getCategory();
    $.ajax({
        async:false, method:'get',
        url:"/product/getone",
        data:{prodDetailcode:prodDetailcode},
        success: r=>{console.log(r);
                    product=r;
                    document.querySelector('#productName').value=product.prodName;
                    document.querySelector('#productPrice').value=product.prodPrice;
                    document.querySelector('#gender').value=product.prodGender;
                    document.querySelector('#summernote').value=product.prodDesc;
                    document.querySelector('#productSize').value=product.prodSize;
                    document.querySelector('#registrationDate').value=product.prodDate;
                    document.querySelector('#productColor').value=product.colorCode;
                    document.querySelector('#productCategory').value=product.prodCatecode;
        },
        error: e=>{console.log(e);
        }
    })
  }
//08.16 상품 수정
function productEdit(){console.log('productEdit()');
    let productEditForm=document.querySelector('#productEdit');
    let productEditFormData=new FormData(productEditForm);

    productEditFormData.set( 'prodDetailcode' , prodDetailcode )

    $.ajax({
        method:'put', url:"/product/edit",
        data:productEditFormData,
        contentType:false, processData:false,
        success: r =>{console.log(r);
            if(r){
                alert("상품수정 완료")
                location.href="/product"
            }else{
                alert("상품수정 실패")
            }
        }, error:e=>{console.log(e);
        }
    })

}
