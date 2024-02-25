document.addEventListener('DOMContentLoaded', function () {
    const likeButton = document.getElementById('post-like');
    const likeCountElement = document.getElementById('like-count');
    const postId = likeButton.getAttribute('data-post-id');
    const userId = likeButton.getAttribute('data-user-id');

    if(likeButton && likeCountElement) {
    updateLikeStatus(postId);

    likeButton.addEventListener('click', function () {
        const isLiked = this.getAttribute('data-liked') === 'true';
        const method = isLiked ? "DELETE" : 'POST';
        const headers= {
            'Content-Type': 'application/json'
        }
        let body;

        if(!isLiked) {
            body = JSON.stringify({userId: userId})
        }

        fetch(`/api/v1/posts/${postId}/like`, {method, headers, body}).then(response => {
            if(response.ok) {
                console.log("좋아요 상태 변경됨")
                updateLikeStatus(postId);
            } else {
                console.log('좋아요 상태 변경 실패')
            }
        }).catch(error => console.error('Error:', error))
    });
    } else {
        console.error('Like button not found on the page.');
    }

    function updateLikeStatus(postId) {
        fetch(`/api/v1/posts/${postId}/like/status`)
            .then(response => response.json())
            .then(data => {
                likeButton.setAttribute('data-liked', data.isLiked);
                likeButton.textContent = data.isLiked ? 'Unlike' : 'Like';
                fetch(`/api/v1/posts/${postId}/likes/count`)
                    .then(response => response.json())
                    .then(likesCount => {
                        likeCountElement.textContent = likesCount;
                    })
            })
            .catch(error => console.error('Error:', error));
    }
    function updateViewCount(postId) {
        fetch(`/api/v1/posts/${postId}/increaseViewCount`)
            .then(response => response.json())
            .then(data => {
                const viewCountElement = document.getElementById('viewCount');
                viewCountElement.textContent = data;
                console.log(viewCountElement)
            }).catch(error => console.error("Error:", error));
    }

    function checkPostViewed(postId) {
        const cookies = document.cookie.split(";");
        const postViewCookie = cookies.find(cookie => cookie.trim().startsWith("postView="));
        if(postViewCookie) {
            const viewedPosts = postViewCookie.split('=')[1];
            const viewedPostsArray = viewedPosts.split('[').join('').split(']').join('').split(',');
            return viewedPostsArray.includes(postId.toString());
        }
        return false;
    }

    if(!checkPostViewed(postId)) {
        updateViewCount(postId);
        }
})