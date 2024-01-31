const main = {
    init: function () {
        const _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
        $('#btn-update').on('click', function () {
            _this.update();
        });
        $('#btn-delete').on('click', function () {
            _this.delete();
        })
        $('#btn-signup').on('click', function () {
            _this.signup();
        })
        $('#btn-answerDelete').on('click', function () {
            _this.answerDelete
        })
    },
    signup: function () {
        const data = {
            username: $('#username').val(),
            password1: $('#password1').val(),
            password2: $('#password2').val(),
            email: $('#email').val()
        };
        $.ajax({
            type: 'POST',
            url: '/api/v1/sign',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data:JSON.stringify(data)
        }).done(function () {
            alert("회원가입이 완료되었습니다.");
            window.location.href = '/user/login'
        }).fail(function(error) {
            $('.alert-danger').hide().text('');

            if (error.responseJSON && error.responseJSON.errors) {
                error.responseJSON.errors.forEach(function (err) {
                    // 에러 메시지를 해당 필드의 에러 컨테이너에 표시
                    $(`#${err.field}-error`).text(err.defaultMessage).show();
                });
            } else {
                // 일반적인 오류 메시지 처리
                if (error.responseJSON && error.responseJSON.message) {
                    alert(error.responseJSON.message);
                } else {
                    alert("알 수 없는 오류가 발생했습니다.");
                }
            }
        })
    },
    save: function () {
        const data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };
        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("글이 등록되었습니다.");
            window.location.href = '/';
        }).fail(function (error) {
            $('.alert-danger').hide().text('');

            if (error.responseJSON && error.responseJSON.errors) {
                error.responseJSON.errors.forEach(function (err) {
                    // 에러 메시지를 해당 필드의 에러 컨테이너에 표시
                    $(`#${err.field}-error`).text(err.defaultMessage).show();
                });
                    }
        })
    },
    update: function () {
        const data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        const id = $('#id').val();
        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("글이 수정되었습니다.");
            window.location.href = '/posts/detail/' + id;
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    },
    delete: function () {
        const id = $('#id').val();

        $.ajax({
            type: "DELETE",
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert("글이 삭제되었습니다.");
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    },

    // 댓글 삭제
    answerDelete: function (element) {
        const answerId = $(element).data('answer-id');
        const postId = $(element).data('post-id');

        $.ajax({
            type: "DELETE",
            url: '/api/v1/answer/' + answerId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function () {
            alert("댓글이 삭제 되었습니다.");
            window.location.href = '/posts/detail/' + postId;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    editComment: function () {
        const answerId = $('#editCommentId').val();
        const content = $('#editCommentContent').val();

        $.ajax({
            type: "PUT",
            url: '/api/v1/answer/' + answerId,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify({content: content})
        }).done(function () {
            alert("댓글이 수정되었습니다.");
            // 페이지 새로고침 또는 댓글 목록 갱신
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    // upload:
    //     $('.custom-file-input').on('change', function () {
    //         let fileName = $(this).val().split('\\').pop();
    //         $(this).siblings('.custom-file-label').addClass('selected').html(fileName);
    //     })
};

main.init();