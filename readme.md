## 스프링 공부를 위한 게시판 개인 프로젝트

---
제작 기간 : 2024.01.24 ~ 2024.03.10

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
  * 현재 최신순 기준은 최종 수정 시간을 기준으로 함. (등록 시간 기준으로 변경 예정)
  * 쿠키를 활용한 조회수 기능 (유저당 1일 1 조회수 증가, 00시 기준 쿠키 삭제)
  * 좋아요 기능
  * 게시판 정렬 기능 ajax() 
  ![인덱스 페이지 ajax](https://github.com/Baemint2/SpringBoot-learning/assets/54212480/ff23f272-aff0-4de9-ada1-e6223a06a6d9)
  * 비회원 댓글 등록 시 로그인 리다이렉트
  ![비회원 리다이렉트](https://github.com/Baemint2/SpringBoot-learning/assets/54212480/13a7085f-626f-4c43-982d-ddc111408e3f)
  * 댓글 CRUD
* 회원가입, 회원탈퇴, 정보수정(닉네임 변경)
  * 이메일 인증 기능
* My page 에서 작성한 글, 댓글 단 글, 좋아요 누른 글 ajax 통신으로 버튼 클릭 시 확인 가능
  * 제목 클릭시 해당 게시글로 이동
![마이페이지 ajax](https://github.com/Baemint2/SpringBoot-learning/assets/54212480/e1dc90cb-42e3-44d5-9b71-2a4a048fc760)
* 검색 기능
* 아이디 찾기, 로그인 시 비밀번호 변경
---
### 기능 추가 예정
  * 비밀번호 찾기(필터를 이용해서 비로그인 상태일 때 로직 처리)