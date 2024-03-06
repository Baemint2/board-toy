import {searchResult} from "./searchResult.js";

export const sortPosts = {
    init: function () {
        this.sortEvent();
    },
    sortEvent: function () {
        document.getElementById('sort-latest').addEventListener('click', () => {
            this.fetchAndDisplayPosts(0, 'latest')
        });
        document.getElementById('sort-views').addEventListener('click', () => {
            this.fetchAndDisplayPosts(0, 'viewCount');
        });
        document.getElementById('sort-answers').addEventListener('click', () => {
            this.fetchAndDisplayPosts(0, 'answer');
        });
        document.getElementById('sort-likes').addEventListener('click', () => {
            this.fetchAndDisplayPosts(0, 'like');
        });

    },
    fetchAndDisplayPosts: function(pageNumber, sort) {
        fetch(`/api/v1/posts/${sort}/desc?page=${pageNumber}`)
            .then(response => response.json())
            .then(data => {
                this.updateTableContent(data.content);
                this.updatePagination(data, sort);
                window.history.pushState({}, "", `/?page=${pageNumber + 1}&sort=${sort}`)
            })
            .catch(error => console.error('Error:', error))
    },


    createTableRow: function (post) {
        const tr = document.createElement('tr');
        const formattedDate = post.modifiedDate ? this.formatDate(post.modifiedDate) : this.formatDate(post.createdDate);
        const categoryName = post.category || '기본';

        tr.innerHTML = `
              <td>${post.id}</td>
              <td>
                  <span>[${categoryName}]</span>
                  <a class="post-title" href="/posts/detail/${post.id}">${post.title}</a>
                  <span class="text-danger small ms-2">[${post.answerCount}]</span>
              </td>
              <td>${post.author}</td>
              <td>${formattedDate}</td>
              <td>${post.viewCount}</td>
              <td>${post.likeCount}</td>
        `;

        return tr;
    },

    formatDate: function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR', {
            year: "numeric",
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
        });
    },

    updateTableContent: function (posts) {
        const tbody = document.querySelector('table tbody');
        tbody.innerHTML = '';
        posts.forEach(post => {
            tbody.appendChild(this.createTableRow(post));
        });
    },

    // 페이지 버튼 생성
    updatePagination: function(pagingData, sort) {
        const existingPaginationContainer = document.querySelector('.pagination');
        if (existingPaginationContainer) {
            existingPaginationContainer.remove();
        }

        //이전 버튼 생성
        const paginationContainer = document.createElement('ul');
        paginationContainer.className = 'pagination justify-content-center'

        this.createPaginationButton(pagingData.first, pagingData.number - 1, '이전', sort, paginationContainer);

        for(let i = 0; i < pagingData.totalPages; i++) {
            const isActive = i === pagingData.number;
            this.createPaginationButton(false, i, i + 1, sort, paginationContainer, isActive);
        }

        this.createPaginationButton(pagingData.last, pagingData.number + 1, '다음', sort, paginationContainer);

        const paginationParent = document.querySelector('#pagination-parent')
        paginationParent.appendChild(paginationContainer)
    },

    createPaginationButton: function (isDisabled, pageNumber, text, sort, container, isActive) {
        const li = document.createElement("li");
        li.className = `page-item ${isDisabled ? 'disabled' : ''} ${isActive ? 'active' : ''}`;

        const link =document.createElement('a');
        link.className = 'page-link';
        link.href= `#${pageNumber}`;
        link.innerText = text;
        link.addEventListener('click', (e) => {
            e.preventDefault()
            this.fetchAndDisplayPosts(pageNumber, sort);
        });

        li.appendChild(link);
        container.appendChild(li);
    },
};

document.addEventListener('DOMContentLoaded', function () {
    searchResult.init();
})
