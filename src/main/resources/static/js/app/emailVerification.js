import {stateManager} from "./stateManager.js";

export const emailVerify = {
    init: function () {
        const _this = this;
        document.getElementById('emailValidateBtn')?.addEventListener('click',  ()  => {
            _this.requestVerificationCode();
        });
        document.getElementById('checkVerificationCode')?.addEventListener('click', () => {
            _this.checkVerifyCode();
        });
    },
    requestVerificationCode: function () {
        const email = document.getElementById('email').value;
        fetch('/api/email/sendVerificationCode', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: email})
        })
            .then(response => response.json())
            .then(data => {
                console.log("Success:", data);
                alert(data.message || "인증번호가 전송되었습니다.");
                document.getElementById('verificationCodeDiv').style.display = 'block';
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("인증번호 전송에 실패했습니다.");
            });
    },

    checkVerifyCode: function () {
        const email = document.getElementById('email').value;
        const code = document.getElementById('verificationCode').value;
        fetch('/api/email/verifyCode', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email: email, code: code})
        })
            .then(response => response.json())
            .then(data => {
                if(data.message === "인증 성공") {
                    alert(data.message);
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
    }
};

document.addEventListener('DOMContentLoaded', function () {
    emailVerify.init();
});
