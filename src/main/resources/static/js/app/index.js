import {getClock} from './clock.js'
import {sortPosts} from "./indexSort.js";

const main = {
    init: function () {
        const _this = this;
        console.log(document.getElementById('btn-save'))
        document.getElementById('btn-save')?.addEventListener('click', function () {
            _this.save();
        });
        console.log(document.getElementById("btn-update"))
        document.getElementById('btn-update')?.addEventListener('click', function () {
            _this.update();
        });
        console.log(document.getElementById("btn-delete"))
        document.getElementById('btn-delete')?.addEventListener('click', function () {
            _this.deleteEvent();
        });

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
        const postDto = {
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            content: document.getElementById('content').value,
            category: document.getElementById('category').value,
        }

        const formData = new FormData();
        formData.append('post', new Blob([JSON.stringify(postDto)], {type: "application/json"}));

        const files = document.getElementById('files').files;
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }

        fetch('/api/v1/posts', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (response.ok) {
                alert("글이 등록되었습니다.")
                window.location.href = "/"
            } else {
                return response.json()
            }
        }).then(errorResponse => {
            document.querySelectorAll('.error-message').forEach(errorContainer => {
                errorContainer.style.display = 'none';
                errorContainer.textContent = '';
            });

            Object.keys(errorResponse).forEach(field => {
                const errorContainer = document.getElementById(`${field}-error`);
                if (errorContainer) {
                    errorContainer.textContent = errorResponse[field];
                    errorContainer.style.display = 'block';
                }
            })
        }).catch(error => console.log("Fetch error: ", error));
    },
    // 글 수정
    update: function () {
        const updatePostDto = {
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            content: document.getElementById('content').value,
            category: document.getElementById('category').value,
        }

        const formData = new FormData();
        formData.append('post', new Blob([JSON.stringify(updatePostDto)], {type: "application/json"}));

        const files = document.getElementById('files').files;
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
        }
        const id = document.getElementById('id').value;
        fetch(`/api/v1/posts/${id}`, {
            method: 'PUT',
            body: formData
        }).then(response => {
            if (!response.ok) {
                // 서버로부터의 응답이 오류를 나타내는 경우 (예: 상태 코드가 400 이상)
                throw new Error(`서버 오류: ${response.status}`);
            }
            return response.json();
        }).then(() => {
            alert("글이 수정되었습니다.");
            window.location.href = `/posts/detail/${id}`;
        }).catch(error => {
            // 네트워크 오류 또는 response.ok가 false 인 경우 여기서 처리
            alert(`오류가 발생했습니다: ${error.message}`);
        });
    },
}
document.addEventListener('DOMContentLoaded', function () {
    main.init();
    getClock();
    sortPosts.init();
})
