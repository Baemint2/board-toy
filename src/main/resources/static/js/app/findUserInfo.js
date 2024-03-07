const findUserInfo = {
    init: function() {
        const _this = this;
        document.getElementById('submitId')?.addEventListener('click', () => {
            _this.verifyUserIdAndRedirect();
        });

        document.getElementById('changePasswordButton')?.addEventListener('click', () => {
            _this.changePasswordHandler();
        });

    },

    verifyUserIdAndRedirect: function() {
        const userId = document.getElementById('userId').value;
        fetch('/api/v1/user/verifyUserId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ userId: userId })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 응답이 실패했습니다.');
                }
                return response.json();
            })
            .then(data => {
                if (data.siteUser) {
                    window.location.href = "/user/find/username";
                } else {
                    alert('존재하지 않는 사용자입니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버 통신 중 오류가 발생했습니다.');
            });
    },

    changePasswordHandler: function () {
        const data = {
            username: document.getElementById('username').value,
            currentPassword: document.getElementById('currentPassword').value,
            newPassword: document.getElementById('newPassword').value,
            confirmPassword: document.getElementById('confirmPassword').value,
        }
        fetch('/api/v1/user/password/reset', {
            method: 'POST',
            headers: {
                'Content-Type':'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                return response.json().then(data => {
                    alert(data.message);
                    window.location.href = "/user/info";
                });
            } else {
                return response.json().then(data => {
                    alert(data.message);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('비밀번호 변경 중 오류가 발생했습니다.');
        });
    }
};

document.addEventListener('DOMContentLoaded', function () {
    findUserInfo.init();
});
