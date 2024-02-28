export const answerModified = {
    init: function () {
        const _this = this;
        document.querySelectorAll('.editAnswerBtn').forEach(button => {
            button.addEventListener('click', function () {
                // closest 메서드를 사용하여 .answerList 요소를 찾음
                const answerListElement = this.closest('.answerList');
                // .answerList 요소로부터 data-answer-id 속성 값을 가져옴
                const answerId = answerListElement.getAttribute('data-answer-id');
                _this.showEditForm(answerId);

                document.getElementById('submitEditBtn-' + answerId).addEventListener('click', function () {
                    _this.submitEdit(answerId);
                });

                // 취소 버튼에 이벤트 리스너 등록
                document.getElementById('cancelEditBtn-' + answerId).addEventListener('click', function () {
                    _this.cancelEdit(answerId);
                });
            })
        });
    },
    // 댓글 수정 창
    showEditForm: function (answerId) {
        // console.log(document.querySelector(`.answerList[data-answer-id="${answerId}"] .answer-content`).textContent);
        console.log(document.getElementById(`editForm-${answerId}`));
        const editForm = document.getElementById(`editForm-${answerId}`)
        const currentContent = document.querySelector(`.answerList[data-answer-id="${answerId}"] .answer-content`).textContent;
        console.log(document.getElementById(`editContent-${answerId}`).value = currentContent);
        editForm.style.display = 'block';
    },

// 댓글 수정 완료
    submitEdit: function (answerId) {
        const editedContent = document.getElementById(`editContent-${answerId}`).value;
        fetch(`/api/v1/answer/${answerId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({content: editedContent})
        }).then(response => {
            if (!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            return response.json();
        }).then(() => {
            document.querySelector(`.answerList[data-answer-id="${answerId}"] .answer-content`).textContent = editedContent;
            document.getElementById(`editForm-${answerId}`).style.display = 'none';
        }).catch(error => console.error('Error : ', error));

    },


// 댓글 수정 취소
    cancelEdit: function (answerId) {
        const editForm = document.getElementById(`editForm-${answerId}`);
        editForm.style.display = 'none';
    },
};

document.addEventListener("DOMContentLoaded", function () {
    answerModified.init();
});