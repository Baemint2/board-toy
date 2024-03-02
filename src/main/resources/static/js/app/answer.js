import {answerModified} from "./answerModified.js";

const answer = {
    init: function () {
        const _this = this;

        document.querySelectorAll('.btn-answerDelete').forEach(button => {
            button.addEventListener('click', function () {
                _this.answerDelete(this);
            });
        })
        document.getElementById('btn-delete')?.addEventListener('click', function () {
            _this.deleteEvent();
        });
        document.getElementById('btn-answer-save').addEventListener('click', function () {
            _this.answerSave();
        })

        _this.bindSubmitOnEnter();

    },

    //댓글 등록
    answerSave: function () {
        const id = document.getElementById('answer-box');
        const postId = id.getAttribute('data-post-id');
        const content = document.getElementById('answer-box').value;

        const data = {
            content: content
        }

        fetch(`/api/v1/answer/${postId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
        })
            .then(response => {
                if(!response.ok)
                    return response.json().then(data => {
                        // 서버로부터 받은 오류 메시지를 표시
                        Object.keys(data).forEach(function (field) {
                            const errorElement = document.getElementById(`${field}-error`);
                            if (errorElement) {
                                errorElement.textContent = data[field];
                                errorElement.style.display = 'block';
                            }
                        });
                        return Promise.reject(new Error("댓글 작성 중 문제가 발생했습니다."));
                    });
                return response.json();
            })
            .then(() => {
                window.location.href = `/posts/detail/${postId}`// 댓글 등록 후 페이지 새로고침 또는 댓글 목록 업데이트 로직
            })
            .catch(error => {
                console.error('Error : ', error.message)
            });
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
,


bindSubmitOnEnter: function () {
    const _this = this;
    const answerBox = document.querySelector('.answer-box');
    if (answerBox) {
        answerBox.addEventListener('keydown', function (evt) {
            console.log(evt.key);
            if (evt.key === 'Enter' && !evt.shiftKey) {
                _this.answerSave();
            }
        })
    }
}
,
}
document.addEventListener('DOMContentLoaded', function () {
    answer.init();
    answerModified.init();
});
