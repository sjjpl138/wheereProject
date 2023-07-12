# wheereProject
최근 장애인, 고령자 등 일상생활에서 대중교통 사용에 불편을 느끼는 교통약자를 위한 저상버스의 보급이 확대되고 있다. 그러나 승강 설비 고장, 긴 배차간격, 무정차 통과 등의 문제로 교통약자는 여전히 대중버스 사용에 어려움을 겪고 있다. 이러한 문제를 해결하기 위해 본 논문에서는 모바일 버스 예약 시스템을 제안한다. 이를 통해 교통약자에게 편의성과 접근성을 갖춘 대중교통 서비스를 제공할 수 있다.

## 🌈 전체 시스템 구조
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/ea88e4da-6d5f-44ee-bc4c-720760f826a0.png" width=50% /></p>

## 📃 데이터베이스 구조
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/e64c904c-7624-41b9-b94f-4b9c64caf8a7.png" width=60% /></p>

## ☑️ 핵심기능
### ✔️ 회원가입&로그인(일반 사용자)
일반 사용자는 서비스 사용을 위해 회원 인증이 필요하고 본인 확인에 사용되는 정보를 입력하여 회원가입을 진행할 수 있습니다.
로그인이 완료되면 예약 현황을 확인할 수 있습니다.
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/d31777a7-1167-4773-9bb2-61916905bfad.png" width=80% /></p>

### ✔️ 기사 로그인(버스기사)
버스 기사는 로그인시 당일 운전할 버스 번호와 버스가 차고에서 출발하는 시간을 선택하여 당일 운행하는 버스를 선택할 수 있습니다.
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/6262e0d3-6546-4e72-80bf-3654509d8800.png" width=80% /></p>

### ✔️ 예약하기(일반 사용자)
일반 사용자가 예약에 필요한 정보를 작성하여 예약을 요청하면 서버는 버스 기사에게 이를 전달하고 예약 승인을 요청합니다.
거절할 특별한 사유가 없는 버스기사가 예약 승인 메시지를 전송하면 서버는 이를 데이터베이스에 저장하고 일반 사용자에게 예약 완료 메시지를 전달합니다.
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/454df6ba-1a54-4755-914f-fc86d2c38adf.png" width=80% /></p>

### ✔️ 예약 승인(버스기사)
일반사용자 예약 요청이 발생하면 즉시 버스사용자의 화면에는 예약 승인 요청 팝업이 나타납니다.
특별한 거절 사유가 없는 버스기사가 해당 예약을 승인하면 버스기사의 화면에 예약 신청자의 정보를 포함한 예약 정보가 출력됩니다.
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/af8ac581-0d0e-4372-a7c9-b76f5e7557e3.png" width=80% /></p>

### ✔️ 예약 취소(일반 사용자)
사용자 필요에 의해 예약 취소 기능을 제공합니다. 이를 통해 교통 약자의 버스 사용 편의성을 높일 수 있습니다.
일반 사용자는 개인적 사정에 따라 승인 대기 상태의 예약을 취소할 수 있습니다. 취소된 예약은 예약 조회에서 사라지게 됩니다.
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/22ce0b78-78f8-4171-8277-92141c0c5733.png" width=30% /></p>

### ✔️ 평점(일반 사용자)
탑승을 완료한 일반 사용자는 별점 요청 알림을 받습니다. 해당 서비스에 대한 평점을 입력하면 이는 DB에 누적 저장됩니다.
<p align="center"><img src="https://github.com/sjjpl138/wheereProject/assets/97449471/ae068e04-cf86-4d67-8dc8-a3643a4ccd03.png" width=30% /></p>

## 👥 역할 분담
#### Back-end
- 정연준 : 설계, 도메인 생성, 서비스 코드 구현
- 손지민 : 설계, 컨트롤러 코드 구현, SSE 알림 기능 구현
- 정영한 : 데이터베이스 SQL문 작성 및 관리, 문서화 작업
  
#### Mobile
- 박준식 : UI 설계, 예약 기능, 버스 기사 평점 기능 구현
- 이지현 : 로그인 기능, 서버 연동, sse 알림 기능 구현

## ⚒️ 기술 스택

#### 👩‍💻 Tools
<span><img src="https://img.shields.io/badge/Flutter-02569B?style=flat-square&logo=Flutter&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Spring-5FB832?style=flat-square&logo=Spring&logoColor=white"/></span>

#### 📂 Databases
<span><img src="https://img.shields.io/badge/MySql-00758F?style=flat-square&logo=MySql&logoColor=white"/></span>

#### 💭 협업 및 버전관리
<span><img src="https://img.shields.io/badge/GitHub-000000?style=flat-square&logo=GitHub&logoColor=white"/></span>
<span><img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/></span>


