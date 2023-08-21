# 멀티 게시판 (23.06.25 ~ 23.08.04, 1인)

## 서비스 소개 & 주요 기능

회원가입, 로그인, 4가지 게시판(공지,자유,갤러리,문의) 서비스를 제공하는 프로젝트입니다.

각 서비스 특징은 아래와 같습니다.
- 회원가입  & 로그인
  - JWT 인증방식 사용
- 공지사항 
  - 알림글 설정 
- 자유게시판 
  - 첨부파일 업로드/다운로드
  - 댓글 작성/삭제
- 갤러리게시판
  - 썸네일 사용 
  - 이미지 네비게이션
- 문의사항 게시판
  - 문의사항 비밀글 처리
  - 관리자 답변 여부에따른 미답변/답변완료 표시

## 개발일지
- [멀티 게시판 개발일지 블로그](https://jeonje.github.io/multi_board_erd.html) 
    
## 아키텍쳐


![image](https://github.com/JeonJe/Multi_Board/assets/43032391/02fee8d6-0d47-40c4-920b-18e30e748487)
- Front-End
   - @vue/cli 5.0.8(사용자)
- Back-End
  - SpringBoot v3.1.0, JDK17, Mybatis3
- Database
  - MariaDB 10.11.3

--- 
### ERD
- [ERD Cloud](https://www.erdcloud.com/d/Em46o5hy4evaZy4oN)

![image](https://github.com/spring-projects/spring-boot/assets/43032391/bfa13e7c-82ed-4586-aa2c-acefcc9f4ba0)



--- 
### API명세서(Gitbook)
<details>
<summary>API명세서(Gitbook) 접기/펼치기</summary>
<div markdown="1">

- [Gitbook API 명세서 링크](https://premsie.gitbook.io/multi-board/)

![image](https://github.com/spring-projects/spring-boot/assets/43032391/2ab3dc86-d5f2-44a4-97b2-05c09c2e4151)

</div>
</details>

---


### Directory Tree
```
📦 
├─ BACK_SpringBoot_API
│  └─ board
│     ├─ .gitignore
│     ├─ build.gradle
│     ├─ gradlew
│     ├─ gradlew.bat
│     ├─ settings.gradle
│     └─ src
│        ├─ main
│        │  ├─ java
│        │  │  └─ ebrain
│        │  │     └─ board
│        │  │        ├─ BoardApplication.java
│        │  │        ├─ ServletInitializer.java
│        │  │        ├─ config
│        │  │        │  └─ WebMvcConfig.java
│        │  │        ├─ controller
│        │  │        │  ├─ BoardController.java
│        │  │        │  └─ UserController.java
│        │  │        ├─ dto
│        │  │        │  ├─ AttachmentDTO.java
│        │  │        │  ├─ BoardFreeDTO.java
│        │  │        │  ├─ BoardGalleryDTO.java
│        │  │        │  ├─ BoardInquiryDTO.java
│        │  │        │  ├─ BoardNoticeDTO.java
│        │  │        │  ├─ CategoryDTO.java
│        │  │        │  ├─ CommentDTO.java
│        │  │        │  ├─ ImageDTO.java
│        │  │        │  ├─ ReplyDTO.java
│        │  │        │  ├─ SearchConditionDTO.java
│        │  │        │  ├─ UserLoginDTO.java
│        │  │        │  └─ UserSignupDTO.java
│        │  │        ├─ exception
│        │  │        │  ├─ AppException.java
│        │  │        │  ├─ ErrorCode.java
│        │  │        │  └─ ExceptionManager.java
│        │  │        ├─ mapper
│        │  │        │  ├─ AttachmentRepository.java
│        │  │        │  ├─ BoardRepository.java
│        │  │        │  ├─ CommentRepository.java
│        │  │        │  ├─ ImageRepository.java
│        │  │        │  ├─ ReplyRepository.java
│        │  │        │  └─ UserRepository.java
│        │  │        ├─ response
│        │  │        │  ├─ APIResponse.java
│        │  │        │  ├─ BoardSearchResponse.java
│        │  │        │  └─ UserLoginResponse.java
│        │  │        ├─ security
│        │  │        │  ├─ AuthorizationExtractor.java
│        │  │        │  ├─ BearerAuthInterceptor.java
│        │  │        │  └─ JwtTokenProvider.java
│        │  │        ├─ service
│        │  │        │  ├─ AttachmentService.java
│        │  │        │  ├─ BoardCategory.java
│        │  │        │  ├─ BoardService.java
│        │  │        │  ├─ CommentService.java
│        │  │        │  └─ UserService.java
│        │  │        ├─ utils
│        │  │        │  ├─ AuthUtil.java
│        │  │        │  ├─ FileUtil.java
│        │  │        │  └─ ResponseBuilder.java
│        │  │        └─ vo
│        │  │           └─ User.java
│        │  └─ resources
│        │     ├─ log4jdbc.log4j2.properties
│        │     ├─ mapper
│        │     │  ├─ AttachmentMapper.xml
│        │     │  ├─ BoardMapper.xml
│        │     │  ├─ CommentMapper.xml
│        │     │  ├─ ImageMapper.xml
│        │     │  ├─ ReplyMapper.xml
│        │     │  └─ UserMapper.xml
│        │     └─ mybatis-config.xml
│        └─ test
│           └─ java
│              └─ ebrain
│                 └─ board
│                    └─ BoardApplicationTests.java
├─ FRONT_Vue_User_Page
│  └─ multi-board
│     ├─ .gitignore
│     ├─ README.md
│     ├─ babel.config.js
│     ├─ jsconfig.json
│     ├─ package-lock.json
│     ├─ public
│     │  ├─ favicon.ico
│     │  └─ index.html
│     ├─ src
│     │  ├─ App.vue
│     │  ├─ assets
│     │  │  └─ logo.png
│     │  ├─ components
│     │  │  ├─ BoardEditBtnGroup.vue
│     │  │  ├─ BoardPagination.vue
│     │  │  └─ SearchForm.vue
│     │  ├─ main.js
│     │  ├─ router
│     │  │  └─ index.js
│     │  ├─ scss
│     │  │  └─ custom.scss
│     │  ├─ services
│     │  │  ├─ axiosInstance.js
│     │  │  ├─ board-service.js
│     │  │  └─ user-service.js
│     │  ├─ store
│     │  │  └─ user.js
│     │  ├─ utils
│     │  │  └─ util.js
│     │  └─ views
│     │     ├─ DashBoard.vue
│     │     ├─ auth
│     │     │  ├─ UserLogin.vue
│     │     │  └─ UserSignup.vue
│     │     └─ boards
│     │        ├─ free
│     │        │  ├─ BoardFreeList.vue
│     │        │  ├─ BoardFreeView.vue
│     │        │  └─ BoardFreeWrite.vue
│     │        ├─ gallery
│     │        │  ├─ BoardGalleryList.vue
│     │        │  ├─ BoardGalleryView.vue
│     │        │  └─ BoardGalleryWrite.vue
│     │        ├─ inquiry
│     │        │  ├─ BoardInquiryList.vue
│     │        │  ├─ BoardInquiryView.vue
│     │        │  └─ BoardInquiryWrite.vue
│     │        └─ notice
│     │           ├─ BoardNoticeList.vue
│     │           └─ BoardNoticeView.vue
│     └─ vue.config.js
└─ README.md
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)


## 느낀 점
 앞선 [자바 스터디](https://github.com/JeonJe/Free_Board/tree/main)의 피드백을 떠올리며 비록 개인 프로젝트이지만 개발 코드는 협업을 한다는 생각을 갖고 `설계부터 폴더 구조, 메소드명, 주석까지` 최대한 신경써서 작성하려고 하였습니다. 앞으로도 많은 프로젝트를 하게 될텐데 기본적으로 지켜야하는 부분들을 놓치지 않고 코드의 품질을 유지할 수 있도록 신경써야 겠습니다.
  
  개인 복습 겸, 게시판을 구현하는 다른 예비 개발자분들에게 조금이 되고자[블로그](https://jeonje.github.io/multi_board_erd.html) 에 구현 과정, 어려웠던 점, 코드 리팩토링 등을 최대한 자세히 작성하려고 하다보니 원래 예정했던 개발 일정을 조금 넘어버렸습니다. 
이제 1차 목표였던 게시판 기능 구현은 완료하였으니 미비점을 보완해 나가면서 서비스 배포와 동시성, 부하 테스트 등을 새로운 내용을 공부하면서 적용해 나갈 예정입니다.
    





