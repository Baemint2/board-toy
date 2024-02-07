const answer = {
    init: function () {
        const _this = this;

        document.querySelectorAll('.btn-answerDelete').forEach(button => {
            button.addEventListener('click', function () {
                _this.answerDelete(this);
            });
        })
    },

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

answer.init();