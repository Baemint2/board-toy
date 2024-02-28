const posts = {
    init: function () {
        this.bindEvents();
        this.updateInitialLikeStatus();
    },

    bindEvents: function () {
        const likeButton = document.getElementById('post-like');
        const postId = likeButton ? likeButton.getAttribute('data-post-id') : null;

        if (likeButton) {
            likeButton.addEventListener('click', () => this.toggleLike(postId));
        }

        const answerBox = document.getElementById('noLoggedUser');
        if (answerBox) {
            answerBox.addEventListener('click', () => this.promptLogin(answerBox));
        }

        this.updateViewCountChange(postId);
    },

    toggleLike: function (postId) {
        const likeButton = document.getElementById('post-like');
        const isLiked = likeButton.getAttribute('data-liked') === 'true';
        const method = isLiked ? 'DELETE' : 'POST';
        const body = isLiked ? {} : {userId: likeButton.getAttribute('data-user-id')};

        fetch(`/api/v1/posts/${postId}/like`, {
            method: method,
            headers: {'Content-Type': 'application/json'},
            body: body ? JSON.stringify(body) : null
        }).then(response => {
            if (response.ok) {
                console.log("좋아요 상태 변경됨")
                this.updateLikeStatus(postId);
            } else {
                console.log('좋아요 상태 변경 실패')
            }
        }).catch(error => console.error('Error:', error))
    },

    // 페이지 로드 시 좋아요 상태를 초기화하는 함수
    updateInitialLikeStatus: function () {
        const postId = document.getElementById('post-like')?.getAttribute('data-post-id');
        if (postId) {
            this.updateLikeStatus(postId);
        }
    },

    updateLikeStatus: function (postId) {
        const likeButton = document.getElementById('post-like')
        fetch(`/api/v1/posts/${postId}/like/status`)
            .then(response => response.json())
            .then(data => {
                likeButton.setAttribute('data-liked', data.isLiked.toString());
                likeButton.innerHTML = data.isLiked ? '<i class="fa-solid fa-heart"></i>' : '<i class="fa-regular fa-heart"></i>';
                this.updateLikeCount(postId);
            }).catch(error => console.error("Error:", error));
    },
    updateLikeCount: function (postId) {
        fetch(`/api/v1/posts/${postId}/likes/count`)
            .then(response => response.json())
            .then(likesCount => {
                document.getElementById('like-count').textContent = likesCount;
            })
            .catch(error => console.error('Error:', error));
    },

    updateViewCountChange: function (postId) {
        if (!this.checkPostViewed(postId)) {
            this.updateViewCount(postId);
        }
    },
    updateViewCount: function (postId) {
        fetch(`/api/v1/posts/${postId}/increaseViewCount`)
            .then(response => response.json())
            .then(data => {
                document.getElementById('viewCount').textContent = data;
            }).catch(error => console.error("Error:", error));
    },

    checkPostViewed: function (postId) {
        const cookies = document.cookie.split(";");
        const postViewCookie = cookies.find(cookie => cookie.trim().startsWith("postView="));
        if (postViewCookie) {
            const viewedPosts = postViewCookie.split('=')[1];
            const viewedPostsArray = viewedPosts.split('[').join('').split(']').join('').split(',');
            return viewedPostsArray.includes(postId.toString());
        }
        return false;
    }, promptLogin: function (postId) {
        const userResponse = confirm("로그인을 하신 후 이용해 주시기 바랍니다.");
        if (userResponse) {
            const redirectUrl = encodeURIComponent(`/posts/detail/${postId}`);
            window.location.href = `/user/login?redirect=${redirectUrl}`;
        }

    },

}

document.addEventListener("DOMContentLoaded", function () {
    posts.init();
})