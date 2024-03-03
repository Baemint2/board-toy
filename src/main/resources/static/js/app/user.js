import {myPagePosts} from "./myPagePosts.js";
import {validator} from "./validator.js";
import {stateManager} from "./stateManager.js";
import {emailVerify} from "./emailVerification.js";

const user = {
    initialized: false,
    init: function() {
        const _this = this;
        if (this.initialized) return;
        document.getElementById('btn-signup')?.addEventListener('click', function () {
            _this.signup();
        })

        this.initialized = true;

    },

    //회원가입
    signup: function () {
        if (!stateManager.getIsVerified()) {
            alert('인증번호가 확인되지 않았습니다.');
            return; // 인증 실패 상태면 함수 종료
        }
            const data = {
                username: document.getElementById('username').value,
                nickname: document.getElementById('nickname').value,
                password1: document.getElementById('password1').value,
                password2: document.getElementById('password2').value,
                email: document.getElementById('email').value,
            }

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
                throw new Error('회원가입 처리 중 문제가 발생했습니다.');
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
}

document.addEventListener('DOMContentLoaded', function() {
    if (!user.initialized) {
        user.init();
        validator.init();
        myPagePosts.init();
        emailVerify.init();
    }
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


