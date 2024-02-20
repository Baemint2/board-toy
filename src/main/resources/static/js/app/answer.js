const answer = {
    init: function () {
        const _this = this;

        document.querySelectorAll('.btn-answerDelete').forEach(button => {
            button.addEventListener('click', function () {
                _this.answerDelete(this);
            });
        })
    },

        //댓글 삭제
        answerDelete: function (element) {
            const answerId = element.getAttribute('data-answer-id')
            const postId = element.getAttribute('data-post-id')

            fetch(`/api/v1/answer/${answerId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {
                    throw new Error("네트워크 에러가 발생했습니다.")
                }
                alert("댓글이 삭제 되었습니다.")
                window.location.href = `/posts/detail/${postId}`
            }).catch(error => {
                alert(`오류가 발생했습니다. ${error.message}`)
            })
        }
}
//     showReplyForm: function (element) {
//             const answerId = element.getAttribute('data-answer-id');
//             // 대댓글 입력을 위한 폼이 이미 존재하는지 확인
//             const existingForm = document.getElementById('reply-form-' + answerId);
//             if (existingForm) {
//                 // 이미 폼이 존재하면 해당 폼을 제거
//                 existingForm.remove();
//                 return;
//             }
//
//             // 대댓글 입력 폼 생성
//             const replyForm = document.createElement('div');
//             replyForm.id = 'reply-form-' + answerId;
//             replyForm.innerHTML = `
//         <textarea class="form-control mt-2" id="reply-content-${answerId}" placeholder="대댓글 내용을 입력하세요."></textarea>
//         <button class="btn btn-sm btn-primary mt-2" onclick="submitReply(${answerId})">대댓글 등록</button>
//         <button class="btn btn-sm btn-secondary mt-2" onclick="cancelReply(${answerId})">취소</button>
//     `;
//
//             // 대댓글 입력 폼을 대댓글 작성 버튼 바로 아래에 삽입
//             element.parentNode.insertBefore(replyForm, element.nextSibling);
//
//         const submitButton = replyForm.querySelector(`button[onclick="submitReply(${answerId})"]`);
//         submitButton.onclick = () => this.submitReply(answerId);
//
//         // 대댓글 취소 버튼에 이벤트 리스너 추가
//         const cancelButton = replyForm.querySelector(`button[onclick="cancelReply(${answerId})"]`);
//         cancelButton.onclick = () => this.cancelReply(answerId);
//
//     },
//
//         // 대댓글 등록 버튼 클릭 처리
//     submitReply: function(answerId) {
//             const replyContent = document.getElementById('reply-content-' + answerId).value;
//             console.log("대댓글 내용:", replyContent);
//
//             // 대댓글 내용을 서버로 전송하는 로직을 구현
//             // 예: AJAX 요청을 사용하여 서버에 대댓글 데이터 전송
//             // 서버에서 대댓글 처리 후 페이지를 새로고침하거나 대댓글 목록을 동적으로 업데이트
//         },
//
//         // 대댓글 입력 취소 처리
//     cancelReply: function(answerId) {
//         const replyForm = document.getElementById('reply-form-' + answerId);
//         if (replyForm) {
//             replyForm.remove();
//         }
//     }
// }
document.addEventListener('DOMContentLoaded', function() {
    answer.init();
});

document.addEventListener("DOMContentLoaded", function () {
    document.querySelector('.answer-box').addEventListener('keydown', function (event) {
        console.log(event.key);
        if(event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            document.querySelector('.comment-form').submit();
        }
    })
})