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
    })
}

document.addEventListener('DOMContentLoaded', function() {
    user.init();
});