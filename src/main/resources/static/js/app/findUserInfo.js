const findUserInfo = {
    init: function() {
        const _this = this;
        document.getElementById('submitId')?.addEventListener('click', () => {
            _this.submitIdHandler();
        });

        document.getElementById('changePasswordButton')?.addEventListener('click', () => {
            _this.changePasswordHandler();
        });

    },

    submitIdHandler: function() {
        const userId = document.getElementById('userId').value.trim();
        this.verifyUserIdAndRedirect(userId);
    },

    verifyUserIdAndRedirect: function(userId) {
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
                return response.text(); // JSON 대신 텍스트로 응답을 받음
            })
            .then(data => {
                // 서버 응답을 확인하고 적절한 조치를 취함
                if (data.includes('user')) { // 예시: 반환된 HTML에 'siteUser' 문자열이 포함되어 있으면
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
        .then(response => response.json())
        .then(data => {
            if(data.ok) {
                alert("비밀번호가 변경되었습니다.");
                window.location.href = "/user/login";
            } else {
                alert(data.message);
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
