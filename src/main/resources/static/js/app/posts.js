document.addEventListener('DOMContentLoaded', function () {
    const likeButton = document.getElementById('post-like');
    if(likeButton) {
    const postId = likeButton.getAttribute('data-post-id');
    const userId = likeButton.getAttribute('data-user-id');

    fetch(`/api/v1/posts/${postId}/like/status`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    }).then(response => response.json())
        .then(data => {

            likeButton.setAttribute('data-liked', data.isLiked);
            likeButton.textContent = data.isLiked? 'Unlike' : 'Like';
        })
        .catch(error => console.error('Error:', error));


    likeButton.addEventListener('click', function () {
        const isLiked = this.getAttribute('data-liked') === 'true';
        const options = {
            method: isLiked ? "DELETE" : 'Post',
            headers: {
                'Content-Type': 'application/json',
            },
        };

        if(!isLiked) {
            options.body = JSON.stringify({userId: userId})
        }

        fetch(`/api/v1/posts/${postId}/like`, options).then(response => {
            if(response.ok) {
                console.log("좋아요 상태 변경됨")
                this.setAttribute('data-liked', !isLiked);
                this.textContent = isLiked ? 'Like' : 'Unlike';
            } else {
                console.log('좋아요 상태 변경 실패')
            }
        }).catch(error => console.error('Error:', error))
    });
    } else {
        console.error('Like button not found on the page.');
    }
})