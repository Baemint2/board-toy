const main = {
    init: function () {
        const _this = this;
        console.log(document.getElementById('btn-save'))
        document.getElementById('btn-save').addEventListener('click', function () {
            _this.save();
        });
        document.getElementById('btn-update').addEventListener('click', function () {
            _this.update();
        });
        document.getElementById('btn-delete').addEventListener('click', function () {
            _this.deleteEvent();
        });


        document.querySelectorAll('.answerList #answer').forEach(button => {
            button.addEventListener('click', function () {
                const answerId = this.parentElement.getAttribute('data-answer-id');
                const content = this.previousElementSibling.textContent;
                main.showEditForm(answerId, content);
            })
        })
    },

    // 댓글 수정 창
    showEditForm: function (answerId, content) {
        document.getElementById('editAnswerContent').value = content;
        document.getElementById('editAnswerForm').style.display = 'block';
        window.currentEditingId = answerId;
    },


    // 댓글 수정 완료
    submitEdit: function () {
        const content = document.getElementById('editAnswerContent').value;
        const answerId = window.currentEditingId;
        fetch(`/api/v1/answer/${answerId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({content: content})
        }).then(response => {
            if (!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            return response.json();
        }).then(data => {
            document.querySelector(`.answerList[data-answer-id="${answerId}"] .answer-content`).textContent = content;
            document.getElementById('editAnswerForm').style.display = 'none';
        }).catch(error => console.error('Error : ', error));

    },


    // 댓글 수정 취소
    cancelEdit: function () {
        document.getElementById('editAnswerForm').style.display = 'none';
    },

    // 게시글 삭제

    deleteEvent: function () {
        const id = document.getElementById("id").value;
        fetch(`/api/v1/posts/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            alert("글이 삭제되었습니다.")
            window.location.href = '/';
        }).catch(error => {
            alert(`오류가 발생했습니다. ${error.message}`);
        })
    },

    // 글 등록
    save: function () {
        const data = {
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            content: document.getElementById('content').value
        };
        fetch('/api/v1/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                alert("글이 등록되었습니다.")
                window.location.href = "/"
            } else {
                return response.json()
            }
        }).then(errorResponse => {
            document.querySelectorAll('.alert-danger').forEach(errorContainer => {
                errorContainer.style.display = 'none';
                errorContainer.textContent = '';
            });

            if (errorResponse && errorResponse.errors) {
                errorResponse.errors.forEach(err => {
                    const errorContainer = document.getElementById(`${err.field}-error`);
                    if (errorContainer) {
                        errorContainer.textContent = err.defaultMessage;
                        errorContainer.style.display = 'block';
                    }
                })
            }
        }).catch(error => console.log("Fetch error: ", error));
    },
    // 글 수정
    update: function () {
        const data = {
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        };
        const id = document.getElementById('id').value;
        fetch(`/api/v1/posts/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (!response.ok) {
                throw new Error("네트워크 에러가 발생했습니다.")
            }
            return response.json()
        }).then(() => {
            alert("글이 수정되었습니다.")
            window.location.href = `/posts/detail/${id}`
        }).catch(error => {
            alert(`오류가 발생했습니다. ${error.message}`);
        })
    },

    // 댓글 삭제

}

document.addEventListener('DOMContentLoaded', function () {
    main.init();
})

const clock = document.querySelector("#clock")


function getClock() {
    const date = new Date();
    const hours = String(date.getHours()).padStart(2,"0");
    const minutes = String(date.getMinutes()).padStart(2,"0");
    const seconds = String(date.getSeconds()).padStart(2,"0");
    clock.innerText =`${hours}:${minutes}:${seconds}`
}


getClock()
setInterval(getClock, 1000);