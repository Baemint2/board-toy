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
        document.querySelectorAll('.editAnswerBtn').forEach(button => {
            button.addEventListener('click', function () {
                // closest 메서드를 사용하여 .answerList 요소를 찾음
                const answerListElement = this.closest('.answerList');
                // .answerList 요소로부터 data-answer-id 속성 값을 가져옴
                const answerId = answerListElement.getAttribute('data-answer-id');
                main.showEditForm(answerId);
            })
        })
    },

    // 댓글 수정 창
    showEditForm: function (answerId) {
        // console.log(document.querySelector(`.answerList[data-answer-id="${answerId}"] .answer-content`).textContent);
        console.log(document.getElementById(`editForm-${answerId}`));
        const editForm = document.getElementById(`editForm-${answerId}`)
        const currentContent = document.querySelector(`.answerList[data-answer-id="${answerId}"] .answer-content`).textContent;
        console.log(document.getElementById(`editContent-${answerId}`).value = currentContent);
        editForm.style.display= 'block';
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
        for(let i = 0; i < files.length; i++) {
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
            document.querySelectorAll('.alert-danger').forEach(errorContainer => {
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
        const data = {
            category: document.getElementById('category').value,
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
})

const clock = document.querySelector("#clock")


document.addEventListener('DOMContentLoaded', function () {

    const sortByLatest = document.getElementById('sort-latest');
    const sortByViews = document.getElementById('sort-views');
    const sortByAnswers = document.getElementById('sort-answers');
    const sortByLikes = document.getElementById('sort-likes');

    sortByLatest.addEventListener('click', () => {
        fetchAndDisplayPosts(0, 'latest');
    });

    sortByViews.addEventListener('click', () => {
        fetchAndDisplayPosts(0, 'viewCount');
    });

    sortByAnswers.addEventListener('click', () => {
        fetchAndDisplayPosts(0, 'answer');
    });

    sortByLikes.addEventListener('click', () => {
        fetchAndDisplayPosts(0, 'like');
    })


    function fetchAndDisplayPosts(pageNumber, sort) {
        fetch(`/api/v1/posts/${sort}/desc?page=${pageNumber}`)
            .then(response => response.json())
            .then(data => {
                updateTableContent(data.content);
                updatePagination({
                    first: data.first,
                    last: data.last,
                    number: data.number,
                    totalPages: data.totalPages,
                }, sort);
                window.history.pushState({}, "", `/?page=${pageNumber}&sort=${sort}`)
            })
            .catch(error =>console.error('Error:',error))
    }


    function createTableRow(post) {
        const tr = document.createElement('tr');
        const formattedDate = post.modifiedDate ? formatDate(post.modifiedDate) : formatDate(post.createdDate);
        const categoryName= post.category || '기본';

        tr.innerHTML = `
              <td>${post.id}</td>
              <td>
                  <span>[${categoryName}]</span>
                  <a class="post-title" href="/posts/detail/${post.id}">${post.title}</a>
                  <span class="text-danger small ms-2">[${post.answerCount}]</span>
              </td>
              <td>${post.author}</td>
              <td>${formattedDate}</td>
              <td>${post.viewCount}</td>
              <td>${post.likeCount}</td>
        `;
        console.log(post);

        return tr;
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR', {
            year: "numeric",
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
        });
    }
    function updateTableContent(posts) {
        const tbody = document.querySelector('table tbody');
        tbody.innerHTML = '';
        posts.forEach(post => {
            tbody.appendChild(createTableRow(post));
        });
    }
    function updatePagination(pagingData, endpoint) {
        const existingPaginationContainer = document.querySelector('.pagination');
        if(existingPaginationContainer) {
            existingPaginationContainer.remove();
        }

        //이전 버튼 생성
        const paginationContainer = document.createElement('ul');
        paginationContainer.className = 'pagination justify-content-center'

        const prevLi = document.createElement('li');
        prevLi.className = pagingData.first ? 'page-item disabled' : 'page-item'
        const prevLink = document.createElement('a');
        prevLink.className = 'page-link';
        prevLink.href = `#${pagingData.number - 1}`;
        prevLink.innerText = '이전';
        prevLink.addEventListener('click', (e) => {
            e.preventDefault();
            fetchAndDisplayPosts(pagingData.number - 1, endpoint);
        });
        prevLi.appendChild(prevLink);
        paginationContainer.appendChild(prevLi);

        // 페이지 번호 버튼 생성
        for(let i = 0; i < pagingData.totalPages; i++) {
            const pageLi = document.createElement('li');
            pageLi.className = i === pagingData.number ? 'page-item active' : 'page-item';
            const pageLink = document.createElement('a');
            pageLink.className = 'page-link';
            pageLink.href = `#${i}`;
            pageLink.innerText = i + 1;
            pageLink.addEventListener('click', (e) => {
                e.preventDefault();
                fetchAndDisplayPosts(i, endpoint);
            });
            pageLi.appendChild(pageLink);
            paginationContainer.appendChild(pageLi);
        }

        // 다음 페이지 버튼 생성
        const nextLi = document.createElement('li');
        nextLi.className = pagingData.last ? 'page-item disabled' : 'page-item';
        const nextLink = document.createElement('a');
        nextLink.className = 'page-link';
        nextLink.href = `#${pagingData.number + 1}`;
        nextLink.innerText = '다음';
        nextLink.addEventListener('click', (e) => {
            e.preventDefault();
            fetchAndDisplayPosts(pagingData.number + 1, endpoint);
        })
        nextLi.appendChild(nextLink);
        paginationContainer.appendChild(nextLi);


        const paginationParent = document.querySelector('#pagination-parent');
        paginationParent.appendChild(paginationContainer);
    }

})

function getClock() {
    const date = new Date();
    const hours = String(date.getHours()).padStart(2,"0");
    const minutes = String(date.getMinutes()).padStart(2,"0");
    const seconds = String(date.getSeconds()).padStart(2,"0");
    clock.innerText =`${hours}:${minutes}:${seconds}`
}


getClock()
setInterval(getClock, 1000);