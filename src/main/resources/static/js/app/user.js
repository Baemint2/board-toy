const user = {
    init: function() {
        const _this = this;

        document.getElementById('btn-signup').addEventListener('click', function () {
            _this.signup();
        })
        document.getElementById('username').addEventListener("blur", function () {
            _this.checkUsernameDuplicate();
        })
        document.getElementById("email").addEventListener("blur", function () {
            _this.checkEmailDuplicate();
        })
        document.getElementById("password2").addEventListener("input", function () {
            _this.validatePasswordsMatch();
        })
    },

    //회원가입
    signup: function () {
        const data = {
            username: document.getElementById('username').value,
            password1: document.getElementById('password1').value,
            password2: document.getElementById('password2').value,
            email: document.getElementById('email').value
        };
        fetch('/api/v1/user/sign', {
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

                document.querySelectorAll('.alert-danger').forEach(errorContainer => {
                    errorContainer.style.display = 'none';
                    errorContainer.textContent = '';
                });
                Object.keys(data).forEach(function(field) {
                    const errorElement = document.getElementById(`${field}-error`);
                    if (errorElement) {
                        errorElement.textContent = data[field];
                        errorElement.style.display = 'block';
                    }
                });
        }).catch(error => {
            alert("알 수 없는 오류가 발생했습니다.")
            console.error(error);
        })

    },
    //내가 쓴 글
    loadUserPosts: (function () {
        fetch("/api/v1/user/posts")
            .then(response => response.json())
            .then(posts => {
                this.displayUserPosts(posts);
            }).catch(error => console.log("Error:", error));
    }),
    //페이지 번호 클릭 이벤트 연결
    displayUserPosts: (function (posts) {
        const postsContainer = document.getElementById('tabContent');
        postsContainer.innerHTML = '';
        if(posts.length === 0) {
            const noPostsMessage = document.createElement('div')
            noPostsMessage.textContent = '아직 작성한 게시글이 없습니다.'
            noPostsMessage.className = 'no-posts-message';
            postsContainer.appendChild(noPostsMessage);
        }
        posts.forEach((post, index) => {
            const postElement = document.createElement('div');
            postElement.className = 'user-post';

            // 게시글 번호
            const postNumber = document.createElement('span');
            postNumber.className = 'post-number'
            postNumber.textContent = `${index + 1}. `;

            //게시글 제목
            const postsTitle = document.createElement('a')
            postsTitle.href = `/posts/detail/${post.id}`
            postsTitle.className = 'post-title';
            postsTitle.textContent =`${post.title} `;

            //작성 시간
            const postDate = document.createElement('span');
            postDate.className = 'post-date';
            postDate.textContent = new Date(post.createdDate).toLocaleString();

            postElement.appendChild(postNumber);
            postElement.appendChild(postsTitle);
            postElement.appendChild(postDate);

            postsContainer.appendChild(postElement);
        })
    }),
    loadUserAnswerPosts: (function () {
        fetch("/api/v1/user/answer")
            .then(response => response.json())
            .then(answer => {
                this.displayUserAnswerPosts(answer);
            }).catch(error => console.log("Error:", error));
    }),
    displayUserAnswerPosts: (function (answerPosts) {
        const answerPostsContainer = document.getElementById('tabContent')
        answerPostsContainer.innerHTML = '';

        if(answerPosts.length === 0) {
            const noAnswerPostsMessage = document.createElement('div')
            noAnswerPostsMessage.textContent = '아직 작성한 댓글이 없습니다.'
            noAnswerPostsMessage.className = 'no-posts-message';
            answerPostsContainer.appendChild(noAnswerPostsMessage);
        }

        answerPosts.forEach((answer, index) => {
            const answerPosts  = document.createElement('div');
            answerPosts.className = 'user-post';

            // 게시글 번호
            const answerPostsNumber = document.createElement('span');
            answerPostsNumber.className = 'post-number'
            answerPostsNumber.textContent = `${index + 1}. `;

            //게시글 제목
            const answerPostsTitle = document.createElement('a')
            answerPostsTitle.href = `/posts/detail/${answer.id}`
            answerPostsTitle.className = 'post-title';
            answerPostsTitle.textContent =`${answer.title} `;

            //작성 시간
            const answerPostDate = document.createElement('span');
            answerPostDate.className = 'post-date';
            answerPostDate.textContent = new Date(answer.createdDate).toLocaleString();

            answerPosts.appendChild(answerPostsNumber);
            answerPosts.appendChild(answerPostsTitle);
            answerPosts.appendChild(answerPostDate);

            answerPostsContainer.appendChild(answerPosts);
        })
    }),

    // 중복 사용자명 체크
    checkUsernameDuplicate: (function () {
        const username = document.getElementById('username').value;
        fetch(`/api/v1/user/username/check?username=${username}`)
            .then(response => response.json())
            .then(data => {
                if (data.isUserNameDuplicate) {
                    const errorElement = document.getElementById('username-error');
                    errorElement.textContent = '이미 사용중인 사용자명입니다.';
                    errorElement.style.display = 'block';
                } else {
                    document.getElementById('username-error').style.display = 'none';
                }
            })
            .catch(error => console.error('Error:', error));

    }),

    // 중복 이메일 체크
    checkEmailDuplicate: (function () {
        const email = document.getElementById("email").value;
        fetch(`/api/v1/user/email/check?email=${email}`)
            .then(response => response.json())
            .then(data => {
                if(data.isEmailDuplicate) {
                    const errorElement = document.getElementById("email-error");
                    errorElement.classList.add('input-error'); // 테두리 색 변경
                    errorElement.textContent = '이미 사용중인 이메일입니다.';
                    errorElement.style.display = 'block';
                } else {
                    document.getElementById('email-error').style.display = 'none';
                }
            }).catch(error => console.error('Error:', error));
    }),

    //비밀번호 일치 확인
    validatePasswordsMatch: (function () {
        const password1 = document.getElementById('password1').value;
        const password2 = document.getElementById('password2').value;
        const password2Field = document.getElementById('password2');
        const errorMessage = document.getElementById('password2-error-message');

        if (password1 !== password2) {
            password2Field.classList.add('input-error'); // 테두리 색 변경
            errorMessage.style.display = 'block'; // 오류 메시지 표시
            errorMessage.textContent = '비밀번호가 일치하지 않습니다.';
        } else {
            password2Field.classList.remove('input-error'); // 테두리 색 제거
            errorMessage.style.display = 'none'; // 오류 메시지 숨김
        }
    }),

}

document.addEventListener('DOMContentLoaded', function() {
    user.init();
});

document.addEventListener('DOMContentLoaded', function () {

        const btnWithdrawal = document.getElementById('btn-withdrawal');
        const modal = document.getElementById('modal-withdrawal-confirm');
        const originalModalContent = modal.querySelector('.modal-content').innerHTML;

        btnWithdrawal.addEventListener('click', function () {
            resetModal();
            modal.style.display = 'block';
        });

        function resetModal() {
            modal.querySelector('.modal-content').innerHTML = originalModalContent;
            bindModalEventListeners();
        }

        function bindModalEventListeners() {
            const btnClose = document.querySelectorAll('.close');
            const btnCancel = document.getElementById('cancel-withdrawal');

            btnClose.forEach(function (element) {
                element.addEventListener('click', function () {
                    console.log("탈퇴 창 닫기")
                    modal.style.display = 'none';
                })
            });

            btnCancel.addEventListener('click', function () {
                console.log("탈퇴 취소");
                modal.style.display = 'none';
            });

            const btnConfirm = document.getElementById('confirm-withdrawal')
            btnConfirm.addEventListener('click', function () {
                // 모달의 기존 내용을 숨기고 탈퇴 폼을 보여주는 로직
                const modalContent = modal.querySelector('.modal-content');
                modalContent.innerHTML = '';

                // 비밀번호 확인 폼을 모달 내에서 보여줍니다.
                const withdrawalForm = document.getElementById('password-confirmation-form').cloneNode(true);
                withdrawalForm.style.display = 'block';
                modalContent.appendChild(withdrawalForm);

                const closeButton = withdrawalForm.querySelector('.close');
                closeButton.addEventListener('click', function () {
                    modal.style.display = 'none';
                })
                const submitWithdrawal = document.getElementById('submit-withdrawal');

                submitWithdrawal.addEventListener("click", function () {
                    const username = document.getElementById('username').value;
                    const password = document.getElementById('password').value;
                    console.log(password);

                    fetch(`/api/v1/user/${username}`, {
                        method: "DELETE",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({username: username, password: password})
                    }).then(response => {
                        if (!response.ok) {
                            throw new Error("비밀번호가 불일치합니다.");
                        }
                        alert("회원 탈퇴가 성공적으로 처리되었습니다.");
                        window.location.href = "/";
                    }).catch(error => {
                        document.getElementById("password-error").textContent = error.message;
                        document.getElementById("password-error").style.display = 'block';
                    });

                })
            })
        }
})

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('myPostsTab').addEventListener('click', function () {
        user.loadUserPosts();
    })
    document.getElementById('myAnswersTab').addEventListener('click', function () {
        user.loadUserAnswerPosts();
    })
    // document.querySelector('.pagination').addEventListener('click', (e) => {
    //     if(e.target.tagName === 'A') {
    //         const page = e.target.getAttribute('data-page')
    //         user.loadUserPosts(page);
    //     }
    // })
})


