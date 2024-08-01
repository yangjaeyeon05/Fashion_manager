
// script.js
document.addEventListener('DOMContentLoaded', function() {
    var dropdownBtns = document.querySelectorAll('.dropdown-btn');

    dropdownBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            // 현재 버튼의 다음 형제 요소인 .dropdown-container를 토글합니다.
            var dropdownContent = this.nextElementSibling;

            if (dropdownContent.style.display === 'block') {
                dropdownContent.style.display = 'none';
            } else {
                // 모든 드롭다운 메뉴를 숨기기
                document.querySelectorAll('.dropdown-container').forEach(function(container) {
                    container.style.display = 'none';
                });
                dropdownContent.style.display = 'block';
            }
        });
    });

    // 클릭하면 사이드바를 숨기는 기능 추가 (옵션)
    document.addEventListener('click', function(event) {
        if (!event.target.matches('.dropdown-btn')) {
            document.querySelectorAll('.dropdown-container').forEach(function(container) {
                container.style.display = 'none';
            });
        }
    });
});

