export const searchResult = {
    init: function () {
        this.sortEvent();
        // this.initializeSearch();
    },

    sortEvent: function () {
        document.getElementById('search-btn')?.addEventListener('click', () => {
            this.searchPost();
        });
    },

    // initializeSearch: function() {
    //     const storedType = localStorage.getItem('type');
    //     const storedKeyword = localStorage.getItem('keyword');
    //     if (storedType && storedKeyword) {
    //         document.getElementById('search-type').value = storedType;
    //         document.getElementById('search-keyword').value = storedKeyword;
    //         this.searchPost();
    //     }
    // },

    searchPost: function (pageNumber=0) {
        const type = document.getElementById('search-type').value;
        const keyword = document.getElementById('search-keyword').value;

        localStorage.setItem('type', type);
        localStorage.setItem('keyword', keyword);

        const queryString = new URLSearchParams({
            type: type,
            keyword: keyword,
            page: pageNumber
        });

        fetch(`/api/v1/posts/search?${queryString}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                this.updateTableContent(data.content);
                this.updatePagination(data, type, keyword);
                window.history.pushState({}, "", `/?type=${type}&keyword=${keyword}&page=${pageNumber}`);
            })
            .catch(error => console.error('Error:', error));
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

    updatePagination: function(pagingData, type, keyword) {
        const existingPaginationContainer = document.querySelector('.pagination');
        if (existingPaginationContainer) {
            existingPaginationContainer.remove();
        }

        //이전 버튼 생성
        const paginationContainer = document.createElement('ul');
        paginationContainer.className = 'pagination justify-content-center'

        this.createPaginationButton(pagingData.first, pagingData.number - 1, '이전', type, keyword, paginationContainer);

        for(let i = 0; i < pagingData.totalPages; i++) {
            const isActive = i === pagingData.number;
            this.createPaginationButton(false, i, i + 1, type, keyword, paginationContainer, isActive);
        }

        this.createPaginationButton(pagingData.last, pagingData.number + 1, '다음', type, keyword, paginationContainer);

        const paginationParent = document.querySelector('#pagination-parent')
        paginationParent.appendChild(paginationContainer)
    },

    createPaginationButton: function (isDisabled, pageNumber, text, type, keyword, container, isActive) {
        const li = document.createElement("li");
        li.className = `page-item ${isDisabled ? 'disabled' : ''} ${isActive ? 'active' : ''}`;

        const link =document.createElement('a');
        link.className = 'page-link';
        link.href= `#${pageNumber}`;
        link.innerText = text;
        link.addEventListener('click', (e) => {
            e.preventDefault()
            this.searchPost(pageNumber);
        });

        li.appendChild(link);
        container.appendChild(li);
    },
};
