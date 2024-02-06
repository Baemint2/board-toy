const main = {
    init: function () {
        const _this = this;
        // document.getElementById('btn-signup').addEventListener('click', function () {
        //     _this.signup();
        // });
        // document.getElementById('btn-save').addEventListener('click', function () {
        //     _this.save();
        // });
        // document.getElementById('btn-update').addEventListener('click', function () {
        //     _this.update();
        // });
        // document.getElementById('btn-delete').addEventListener('click', function () {
        //     _this.deleteEvent();
        // });

        document.querySelectorAll('.btn-answerDelete').forEach(button => {
            button.addEventListener('click', function () {
                _this.answerDelete(this);
            });
        })
        document.querySelectorAll('.answerList #answer').forEach(button => {
            button.addEventListener('click', function () {
                const answerId = this.parentElement.getAttribute('data-answer-id');
                const content = this.previousElementSibling.textContent;
                main.showEditForm(answerId, content);
            })
        })
    },

    showEditForm: function (answerId, content) {
        document.getElementById('editAnswerContent').value = content;
        document.getElementById('editAnswerForm').style.display = 'block';
        window.currentEditingId = answerId;
    },

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

    cancelEdit: function () {
        document.getElementById('editAnswerForm').style.display = 'none';
    },

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

    signup: function () {
        const data = {
            username: document.getElementById('username').value,
            password1: document.getElementById('password1').value,
            password2: document.getElementById('password2').value,
            email: document.getElementById('email').value
        };
        fetch('/api/v1/sign', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                alert("회원가입이 완료되었습니다.");
                window.location.href = '/user/login';
            } else {
                return response.json();
            }
        }).then(data => {
            if (data && data.errors) {
                data.errors.forEach(function (err) {
                    const errorElement = document.getElementById(`${err.field}-error`);
                    if (errorElement) {
                        errorElement.textContent = err.defaultMessage;
                        errorElement.style.display = 'block';
                    }
                });
            } else if (data && data.message) {
                alert(data.message);
            }
        }).catch(error => {
            alert("알 수 없는 오류가 발생했습니다.")
            console.error(error);
        })

    },

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

document.addEventListener('DOMContentLoaded', function () {
    main.init();
})
