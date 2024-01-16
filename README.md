# 전시로그 - 전시를 기록하다: 전시회 정보 플랫폼

<p align="center">
  <br>
  <img src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/JeonsiLog_Asset_1080.jpg">
  <br>
</p>

<br>

## 프로젝트 소개
<p>전시로그를 통해 각 전시회의 세부 정보를 확인할 수 있고 별점과 감상평을 통해 자신의 생각과 경험을 나눌 수 있어요</p>
<p>전시로그를 통해 전시회 경험을 쌓아보세요!</p>

<br>

## 팀원
| [서윤지](https://github.com/syjeuion) | [문장훈](https://github.com/moondev03) | [임준형](https://github.com/Liminghing)
| --- | --- | --- |
| `홈` `전시회 상세` `감상평 및 댓글` `관리자 페이지` | `소셜로그인` `유저 프로필` `알림` `통신 작업` | `검색` `FCM` `포토캘린더`  |

<br>

## 폴더 구조
```markdown
📂 com.example.jensilog
 ┣ 📂base
 ┣ 📂data
 ┃ ┗ 📂remote
 ┃   ┣ 📂api
 ┃   ┗ 📂dto
 ┃     ┣ 📂alarm
 ┃     ┣ 📂auth
 ┃     ┣ 📂calendar
 ┃     ┣ 📂exhibition
 ┃     ┣ 📂follow
 ┃     ┣ 📂interest
 ┃     ┣ 📂place
 ┃     ┣ 📂rating
 ┃     ┣ 📂reply
 ┃     ┣ 📂report
 ┃     ┣ 📂review
 ┃     ┗ 📂user
 ┣ 📂repository
 ┃ ┣ 📂alarm
 ┃ ┣ 📂auth
 ┃ ┣ 📂calendar
 ┃ ┣ 📂exhibition
 ┃ ┣ 📂follow
 ┃ ┣ 📂interest
 ┃ ┣ 📂place
 ┃ ┣ 📂rating
 ┃ ┣ 📂reply
 ┃ ┣ 📂report
 ┃ ┣ 📂review
 ┃ ┗ 📂user
 ┣ 📂fcm
 ┃ ┗ 📂services
 ┣ 📂view
 ┃ ┣ 📂admin
 ┃ ┣ 📂exhibition
 ┃ ┣ 📂home
 ┃ ┣ 📂login
 ┃ ┣ 📂mypage
 ┃ ┣ 📂notification
 ┃ ┣ 📂otheruser
 ┃ ┣ 📂photocalendar
 ┃ ┣ 📂search
 ┃ ┗ 📂splashPage
 ┣ 📂viewmodel
 ┗ 📂widget
   ┣ 📂extension
   ┗ 📂utils
```

<br>

## 기술 스택

| 항목             | 내용                               |
|-----------------|-------------------------------------|
| 개발 환경        | Android Studio: Giraffe             |
| Android 버전     | API 29(Q) ~ API 34(Tiramisu)         |
| 언어             | Kotlin                              |
| 아키텍처         | MVVM                                |
| JetPack          | DataBinding, LiveData, ViewModel, Navigation, Lifecycles |
| 로컬 데이터      | SharedPreferences, EncryptedSharedPreferences |
| 네트워킹         | Retrofit2, OkHttp3, Gson               |
| 비동기 처리      | Coroutines                          |
| 소셜 로그인      | Kakao Login                         |
| 이미지 처리      | Glide                               |
| 알림            | FCM                               |
| 협업 도구        | Jira, Confluence, Notion             |
| 디자인 도구      | Figma                               |

<br>

## ScreenShot
| 스플래시 | 로그인 | 약관동의 | 회원가입 |
| --- | --- | --- | --- |
| <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/splash.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/login.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/tos.jpg" > |<img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/signup.jpg"> |

| 홈 | 상세정보 | 포스터 다운로드 | 리뷰 작성 |
| --- | --- | --- | --- |
| <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/home.jpg" > | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/exhibition_info.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/poster_download.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/review_page.jpg"> |

| 검색 | 검색 결과 | 포토캘린더 | 포스터 불러오기 |
| --- | --- | --- | --- |
| <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/search.jpg" > | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/search_result.jpg" > | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/calendar.jpg" > | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/calendar_poster.jpg" > |

| 내 별점 | 내 감상평 | 내 즐겨찾기 | 설정 |
| --- | --- | --- | --- |
| <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/mypage_rating.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/mypage_review.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/mypage_interest.jpg"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/setting.jpg"> |

| 타 유저 포토캘린더 | 팔로잉 & 팔로워 | 활동 알림 | 전시 리마인더 알림 |
| --- | --- | --- | --- |
| <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/otheruser_calendar.png"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/following.png"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/noti_activity.png"> | <img width="200" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/noti_exhibition.png"> |

