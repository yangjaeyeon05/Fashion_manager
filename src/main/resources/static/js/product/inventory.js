console.log("inventory.js");

inventoryRead();

function inventoryRead() {
    let inventoryArea = document.querySelector(".inventoryArea");
    let html = `
                <h3> 재고 관리 페이지 </h3>
                <div class="inventoryRead">
                    <div>
                        제품명
                    </div>
                    <div>
                        성별
                    </div>
                    <div>
                        사이즈
                    </div>
                    <div>
                        재고 현황
                    </div>
                    <div>
                        비고
                    </div>
                </div>`;

    $.ajax({
        async: false,
        method: "get",
        url: "/inventory/read",
        success: function response(result) {
            console.log(result);
            result.forEach(r => {
                html += `<div class="inventoryRead">
                            <div>
                                ${r.prodName}
                            </div>
                            <div>
                                ${r.prodGender}
                            </div>
                            <div>
                                ${r.prodSize}
                            </div>
                            <div>
                                ${r.prodAmount}
                            </div>
                            <div>
                            <select id="inventoryLog">
                                <option value="1"> 재고 입고</option>
                                <option value="2"> 판매 </option>
                                <option value="3"> 취소 </option>
                                <option value="4"> 환불 </option>
                            </select>
                            <input type="text" id="inventoryChange">
                            <button type="button" onclick="inventoryLog(${r.prodDetailcode})"> 재고 현황 업데이트 </button>
                            </div>
                        </div>
                        <br>`;
            });
            inventoryArea.innerHTML = html;
        }

    })


}


// ===================================  2024-08-08 김민석 ========================================= //