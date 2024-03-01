export const myPagePosts = {
    init: function () {
        this.bindButtonEvents();
    },

    bindButtonEvents: function () {
        const _this = this;
      document.getElementById("myPostsTab").addEventListener('click', function () {
          _this.loadUserPosts();
      });
      document.getElementById("myAnswersTab").addEventListener('click', function () {
          _this.loadUserAnswerPosts();
      });
      document.getElementById("myLikesTab").addEventListener('click', function () {
          _this.loadUserLike();
      })
    },


//내가 쓴 글
loadUserPosts: function () {
    fetch("/api/v1/user/posts")
        .then(response => response.json())
        .then(posts => this.disPlayUserLikedPosts('내가 쓴 글', posts, '아직 작성한 게시글이 없습니다.'))
        .catch(error => console.log("Error:", error));
},
    //댓글 단 글
    loadUserAnswerPosts:  function () {
        fetch("/api/v1/user/answer")
            .then(response => response.json())
            .then(answer => this.disPlayUserLikedPosts('댓글 단 글', answer, '아직 작성한 댓글이 없습니다.'))
            .catch(error => console.log("Error:", error));
    },
    //좋아요 누른 글
    loadUserLike: function () {
    fetch(`/api/v1/posts/likes`)
        .then(response => response.json())
        .then(likes => this.disPlayUserLikedPosts('좋아요 누른 글', likes, '아직 좋아요 누른 게시글이 없습니다.'))
        .catch(error => console.log("Error:", error));
    },
    disPlayUserLikedPosts: function (tab, posts, emptyMessage) {
        const containerId = this.getContainerIdByTitle(tab);
        const postsContainer = document.getElementById('tabContent');
        postsContainer.innerHTML = '';
    if(posts.length === 0) {
        const messageElement = document.createElement('div')
        messageElement.textContent = emptyMessage;
        messageElement.className = 'no-posts-message'
        postsContainer.appendChild(messageElement);
    }
    posts.forEach((post, index) => {
        const postElement = document.createElement('div')
        postElement.className = 'user-post';

        postElement.innerHTML = `
          <span class="post-number">${index + 1}</span>
          <a href="/posts/detail/${post.id}" class="post-title">${post.title}</a>
          <span class="post-date">${new Date(post.createdDate).toLocaleString()}</span>
            `;
        postsContainer.appendChild(postElement);

    });
},

    getContainerIdByTitle: function (tab) {
        return 'tabContent'
    }
}
