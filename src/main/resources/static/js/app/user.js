const user = {
    init: function() {
        const _this = this;

        document.getElementById('btn-signup').addEventListener('click', function () {
            _this.signup();
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

    }
}

user.init();