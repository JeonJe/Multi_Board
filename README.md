# 멀티 게시판

## 서비스 소개 & 주요 기능

회원가입, 로그인과 4가지 게시판(공지,자유,갤러리,문의)서비스를 제공합니다.

1. 회원가입 & 로그인
1. 공지사항
2. 자유게시판
3. 갤러리게시판
4. 공지게시판
<!-- - **게시판 목록**
    - 등록일, 카테고리, 검색어 조건을 이용한 게시글 필터링
    - 페이지네이션
    - 페이지 이동 후 다시 돌아올 때 기존 검색 조건 유지
- 게시글 보기
    - 댓글 출력, 첨부파일 존재 시 다운로드 제공
    - 수정 또는 삭제 시 비밀번호 검증
- 게시글 등록
    - 입력조건에 따른 유효성 검증
    - 첨부파일 등록
- 게시판 수정
    - 첨부파일 추가 폼은 동적으로 생성 (최대 3개)
    - 첨부파일 변경 시 변경된 첨부파일만 적용
    - 비밀번호 검증 실패 시 수정 미 반영
- ERD
    ![SCR-20230622-jjqx](https://github.com/JeonJe/Board/assets/43032391/ad7e2584-9704-41e4-8eed-935dd51579a9) -->

### 개발일지
[Github.io 블로그](https://jeonje.github.io/multi_board_erd.html) 
    
## 개발 기간 & 아키텍쳐
### 개발기간
2023.06.25 ~ 2023.08.04, 1인

### ERD

### API명세서(gitbook)

### Project Tree(Directory 명세서)
<details>
<summary>Project Tree 접기/펼치기</summary>
<div markdown="1">
```
📦 
├─ BACK_SpringBoot_API
│  └─ boardmodules.xml
│     ├─ .gitignoremrds.iml
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
└─ FRONT_Vue_User_Page
   ├─ .vscode
   │  └─ settings.json
   └─ multi-board
      ├─ .gitignore
      ├─ .sass-cache
      │  └─ 940cc9edc09d8df385545a44deced24d7b7e83c6
      │     └─ styles.module.scssc
      ├─ README.md
      ├─ babel.config.js
      ├─ jsconfig.json
      ├─ package-lock.json
      ├─ public
      │  ├─ favicon.ico
      │  └─ index.html
      ├─ src
      │  ├─ App.vue
      │  ├─ assets
      │  │  └─ logo.png
      │  ├─ components
      │  │  ├─ BoardEditBtnGroup.vue
      │  │  ├─ BoardPagination.vue
      │  │  └─ SearchForm.vue
      │  ├─ main.js
      │  ├─ router
      │  │  └─ index.js
      │  ├─ scss
      │  │  └─ custom.scss
      │  ├─ services
      │  │  ├─ axiosInstance.js
      │  │  ├─ board-service.js
      │  │  └─ user-service.js
      │  ├─ store
      │  │  └─ user.js
      │  ├─ utils
      │  │  └─ util.js
      │  └─ views
      │     ├─ DashBoard.vue
      │     ├─ auth
      │     │  ├─ UserLogin.vue
      │     │  └─ UserSignup.vue
      │     └─ boards
      │        ├─ free
      │        │  ├─ BoardFreeList.vue
      │        │  ├─ BoardFreeView.vue
      │        │  └─ BoardFreeWrite.vue
      │        ├─ gallery
      │        │  ├─ BoardGalleryList.vue
      │        │  ├─ BoardGalleryView.vue
      │        │  └─ BoardGalleryWrite.vue
      │        ├─ inquiry
      │        │  ├─ BoardInquiryList.vue
      │        │  ├─ BoardInquiryView.vue
      │        │  └─ BoardInquiryWrite.vue
      │        └─ notice
      │           ├─ BoardNoticeList.vue
      │           └─ BoardNoticeView.vue
      └─ vue.config.js
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)

</div>
</details>

### javadoc

<details>
<summary>javadoc 접기/펼치기</summary>
<div markdown="1">



</div>
</details>


## 어려웠던 점
   
## 배운점


## 결과 화면

