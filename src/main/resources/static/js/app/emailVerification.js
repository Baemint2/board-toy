import {stateManager} from "./stateManager.js";

export const emailVerify = {
    init: function () {
        const _this = this;
        document.querySelectorAll('.emailValidateBtn').forEach(btn => {
            btn.addEventListener('click', () => {
                const email = document.getElementById('email').value;
                const action = btn.getAttribute('data-action');
                _this.handleVerificationCode(email, action);
            });
        });
        document.getElementById('checkVerificationCode')?.addEventListener('click', () => {
            _this.checkVerifyCode();
        });
        document.getElementById('findUsernameBtn')?.addEventListener('click', () => {
            _this.findUsernameByEmail();
        });
        document.getElementById('findPasswordBtn')?.addEventListener('click', () => {
            _this.redirectToPasswordReset();
        })

    },
    handleVerificationCode: function(email, action) {
    // 공통 로직
    fetch('/api/email/sendVerificationCode', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({email: email})
    })
        .then(response => {
            if(!response.ok) {
                throw response;
            }
            return response.json();
        })
        .then(data => {
            console.log("Success:", data);
            alert(data.message || "인증번호가 전송되었습니다.");
            if(action === 'signup') {
                document.getElementById('verificationCodeDiv').style.display = 'block';
            } else if(action === 'findId') {
                document.getElementById('verificationCode').readOnly = false;
                document.getElementById('verificationCode').focus();
            }
        })
        .catch((error) => {

            error.json().then(errorData => {
                Object.keys(errorData).forEach(function(field) {
                    const errorElement = document.getElementById(`${field}-error`);
                    if (errorElement) {
                        errorElement.textContent = errorData[field];
                        errorElement.style.display = 'block';
                    }
            })
            alert("인증번호 전송에 실패했습니다.");

            });
        });
},


checkVerifyCode: function () {
        const data = {
            email: document.getElementById('email').value,
            code: document.getElementById('verificationCode').value,
        }
        fetch('/api/email/verifyCode', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if(data.message === "인증 성공") {
                    stateManager.setIsVerified(true); // 인증 성공 상태 업데이트
                    console.log(stateManager.isVerified)
                } else {
                    alert(data.error || "올바르지 못한 인증번호입니다.");
                    stateManager.setIsVerified(false); // 인증 실패 상태 업데이트
                    console.log(stateManager.isVerified)
                }
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("인증번호 검증에 실패했습니다.");
            });
    },
    findUsernameByEmail: function () {
        const data = {
            email: document.getElementById('email').value,
            nickname: document.getElementById('nickname').value
        }
        fetch(`/api/v1/user/findUsername`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
            if(data.username) {
                document.getElementById('usernameResult').textContent = `사용자 아이디: ${data.username}`
            } else {
                alert("해당 아이디로 등록된 아이디가 없습니다.");
                // alert가 아니라 검증 메시지 보여주기.
            }
        })
        .catch(error => {
            console.error("Error:", error)
            alert("사용자 아이디 찾기에 실패했습니다.");
        })
    },
    redirectToPasswordReset: function () {
        if (stateManager.isVerified) {
            // 사용자 인증이 성공했으면 비밀번호 변경 페이지로 리다이렉트
            window.location.href = "/user/password/reset";
        } else {
            alert("먼저 인증을 완료해주세요.");
        }
    }
};

document.addEventListener('DOMContentLoaded', function () {
    emailVerify.init();
});
