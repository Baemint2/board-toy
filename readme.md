## 스프링 공부를 위한 게시판 개인 프로젝트

---
제작 기간 : 2024.01.24 ~ ing      

--- 
기술 스택
--
* Spring Framework 6.1.4
* Spring boot 3.2.2      
* JDK 17   
* MySQL
* Spring-data-JPA   
* Spring-security   
* Thymeleaf   
* Querydsl   
* lombok

---
## 기능 구현
* 게시판 CRUD
  * 게시판 카테고리, 게시글 사진 업로드
  * 게시판 정렬 기능 ajax()
  * 현재 최신순 기준은 최종 수정 시간을 기준으로 함. (등록 시간 기준으로 변경 예정)
  * 쿠키를 활용한 조회수 기능 (유저당 1일 1 조회수 증가, 00시 기준 쿠키 삭제)
  * 좋아요 기능 
  ![인덱스 페이지 ajax.gif](..%2F..%2F%EC%9D%B8%EB%8D%B1%EC%8A%A4%20%ED%8E%98%EC%9D%B4%EC%A7%80%20ajax.gif)   
  * 비회원 댓글 등록 시 로그인 리다이렉트
  ![비회원 리다이렉트.gif](..%2F..%2F%EB%B9%84%ED%9A%8C%EC%9B%90%20%EB%A6%AC%EB%8B%A4%EC%9D%B4%EB%A0%89%ED%8A%B8.gif)
* 댓글 CRUD
* 회원가입, 회원탈퇴, 정보수정(닉네임 변경)
  * 이메일 인증 기능
* My page 에서 작성한 글, 댓글 단 글, 좋아요 누른 글 ajax 통신으로 버튼 클릭 시 확인 가능
  * 제목 클릭시 해당 게시글로 이동
    ![마이페이지 ajax.gif](..%2F..%2F%EB%A7%88%EC%9D%B4%ED%8E%98%EC%9D%B4%EC%A7%80%20ajax.gif)

---
### 기능 추가 예정
  * 검색 기능 
  * 아이디 찾기, 비밀번호 찾기(비밀번호 변경)
  * 소셜 로그인 추가
  * springdoc-openapi를 이용해 api 문서화
  * 시간적 여유가 되면 프론트 꾸미기