export const validator = {
    init: function () {
        const _this = this;
        document.getElementById('username')?.addEventListener("blur", function () {
            _this.checkUsernameDuplicate();
        })
        document.getElementById("email")?.addEventListener("blur", function () {
            _this.checkEmailDuplicate();
        })
        document.getElementById("password2")?.addEventListener("input", function () {
            _this.validatePasswordsMatch();
        })
        document.getElementById("editNickname").addEventListener("click", function () {
            _this.updateNickname();
        })
    },


// 중복 사용자명 체크
    checkUsernameDuplicate: function () {
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

    },

    // 중복 이메일 체크
    checkEmailDuplicate: function () {
        const email = document.getElementById("email").value;
        fetch(`/api/v1/user/email/check?email=${email}`)
            .then(response => response.json())
            .then(data => {
                if (data.isEmailDuplicate) {
                    const errorElement = document.getElementById("email-error");
                    errorElement.classList.add('input-error'); // 테두리 색 변경
                    errorElement.textContent = '이미 사용중인 이메일입니다.';
                    errorElement.style.display = 'block';
                } else {
                    document.getElementById('email-error').style.display = 'none';
                }
            }).catch(error => console.error('Error:', error));
    },

    //비밀번호 일치 확인
    validatePasswordsMatch: function () {
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
    },

    // 닉네임 변경 중복 체크
    updateNickname: function () {
        const nicknameField = document.getElementById('nickname');
        const nicknameError = document.getElementById('nickname-error');
        const updateButton = document.getElementById('editNickname');

        if (nicknameField.readOnly) {
            nicknameField.readOnly = false;
            updateButton.textContent = '변경';
        } else {
            const updatedNickname = nicknameField.value;
            fetch('/api/v1/user/updateNickname', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    nickname: updatedNickname
                }).toString()
            }).then(response => {
                if(!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.errorMessage || '알 수 없는 오류가 발생했습니다.');
                    })
                }
                return response.json();
            })
                .then(data => {
                    if (data.redirect) {
                        console.log(data);
                        nicknameField.readOnly = true;
                        updateButton.textContent = "수정";
                        nicknameError.style.display = 'none';
                        window.location.href = data.redirect;
                    } else {
                        // 오류 메시지 표시
                        nicknameError.textContent = data.errorMessage || '닉네임 변경에 실패했습니다.';
                        nicknameError.style.display = 'block';
                    }
                })
                .catch(error => {
                    console.log("Error:", error);
                    nicknameError.textContent = error.message;  // 네트워크 오류 메시지 표시
                    nicknameError.style.display = 'block'; // 오류 메시지 div를 보이게 함
                })
        }
    }
}

document.addEventListener('DOMContentLoaded', function () {
    validator.init();
})