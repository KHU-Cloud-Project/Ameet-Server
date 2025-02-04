
# A-meet

**AI 기반 통합 비대면 회의 솔루션**

![image](https://github.com/user-attachments/assets/788e5a81-34a0-46cc-bfbc-4f5a7e4a6683)

## 프로젝트 설명

---

A-meet는 AWS의 다양한 클라우드 서비스를 활용해 설계된 AI 기반 통합 비대면 회의 솔루션입니다.

분산된 협업 도구의 불편함을 해결하고, 하나의 플랫폼에서 비대면 회의의 모든 과정을 간편하고 효율적으로 관리할 수 있도록 설계되었습니다. 코로나19 이후 비대면 회의의 수요 증가에 대응하며, AI를 활용한 차별화된 기능을 통해 더 스마트한 회의 경험을 제공할 수 있습니다.

**주요 특징 및 목표**

- **AI 기반 회의 지원**:
    - AI 봇이 회의에 참석하여 자동 회의록 생성, 요약, 그리고 의사결정 지원 등 다양한 역할을 수행
- **음성 회의와 기록 관리**:
    - 팀별로 생성된 스페이스에서 음성 회의를 진행하며, 실시간 또는 녹음된 회의의 음성을 분석해 자동으로 요약본 및 기록물을 생성
- **통합 관리 대시보드**:
    - 나의 팀 스페이스와 회의 기록물을 한 곳에서 편리하게 관리 가능
- **효율적인 협업 환경 조성**:
    - 회의 진행을 돕는 다양한 AI 봇(정리봇, 리액션봇 등)을 통해 효율적이고 원활한 비대면 협업을 지원


## 기술 스택

### Frontend

<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white"> <img src="https://img.shields.io/badge/recoil-3578E5?style=for-the-badge&logo=recoil&logoColor=white"> <img src="https://img.shields.io/badge/emotion-C865B9?style=for-the-badge&logo=styledcomponents&logoColor=white"> <img src="https://img.shields.io/badge/vite-646CFF?style=for-the-badge&logo=vite&logoColor=white"> <img src="https://img.shields.io/badge/awscloudfront-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> 

### Backend

<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/lombok-CA4245?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/amazonbedrock-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/amazontranscribe-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"> 

### Infra

<img src="https://img.shields.io/badge/awsrds-527FFF?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/awss3-569A31?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/awscloudfront-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/awsroute53-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/awsec2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/amazoncloudwatch-FF4F8B?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/amazonsns-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/awschatbot-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/awseks-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white"> <img src="https://img.shields.io/badge/grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white"> 

## 프로젝트 아키텍처

![image](https://github.com/user-attachments/assets/d6085160-9a35-4a9c-96be-f48dd07be226)

## ERD
![image](https://github.com/user-attachments/assets/49eba8d5-3119-42e6-9932-1a0e2d5c05ad)



## 기능 


**1. 회원가입 및 로그인**

회원가입 페이지에서 사용자는 계정을 생성하고, 생성된 계정으로 로그인하여 비대면 회의 서비스를 이용할 수 있습니다. 로그인 후 사용자는 대시보드로 이동하며, 자신의 계정 정보를 확인하거나 수정할 수 있는 기능도 제공합니다.
![image](https://github.com/user-attachments/assets/07bea6ca-9eaa-49e8-b725-5b4b71e8192c)


**2. 팀 스페이스 관리**

팀 스페이스는 사용자가 속한 팀을 기반으로 회의와 협업을 진행하는 공간입니다.

- 팀 생성 시, 사용자는 팀 이름, 설명, 비밀번호를 입력하여 새로운 팀을 쉽게 만들 수 있습니다.
- 이미 존재하는 팀에는 팀 이름과 비밀번호를 입력해 참여할 수 있으며, 참여 후에는 대시보드나 사이드바를 통해 팀 목록을 확인하고 이동할 수 있습니다.
- 팀 소유자는 팀 이름, 설명 등의 정보를 수정하거나 팀을 삭제할 권한을 가집니다.

![image](https://github.com/user-attachments/assets/87b97445-f3da-46ea-abde-23127b33839a)

**3. 대시보드**

대시보드는 사용자의 메인 관리 화면으로, 자신이 속한 팀과 회의록 목록을 직관적으로 볼 수 있는 UI를 제공합니다.

- 사용자는 대시보드에서 팀 목록을 확인하고, 각 팀의 회의 활동 내역과 회의록 상태를 확인할 수 있습니다.
- 사이드바를 통해 다른 팀 스페이스로 이동하거나 빠르게 팀 간 전환이 가능합니다.
- 회의록 수동 업로드 기능을 사용할 수 있습니다.

![image](https://github.com/user-attachments/assets/2474e44d-43bc-448b-9db1-a8fc7c1c479a)

![image](https://github.com/user-attachments/assets/e47465a3-5e2a-43cd-b6b7-aba69b6b3a5e)

**4. 회의 생성 및 봇 활용**

특정 팀 스페이스에 참여한 사용자는 회의 설정 페이지에서 봇 기능과 회의 시간을 선택해 회의를 생성할 수 있습니다.

- 회의 화면의 좌측에는 참여자의 프로필이 표시되어 참석자를 쉽게 파악할 수 있습니다.
- 사용자는 봇과 상호작용하여 회의 요약, 긍정적인 피드백, 객관적인 피드백 등의 기능을 활용할 수 있습니다. 봇의 응답은 채팅 형식으로 표시됩니다.
- 타이머 기능을 통해 회의 시간을 관리하고, 회의 종료 시 자동으로 회의록이 생성됩니다.

![image](https://github.com/user-attachments/assets/535e31ab-d4a7-40c7-9564-ee090ce5cb30)

**5. 회의 기록 및 회의록 관리**

팀 스페이스 내에는 회의 기록 및 회의록을 관리하는 기능이 포함되어 있습니다.

- 사용자는 회의 로그 목록에서 회의 제목, 날짜, 길이, 참여자를 한눈에 확인할 수 있습니다.
- 음성 파일 업로드 기능을 통해 녹음된 회의 내용을 텍스트 기반 회의록으로 변환하고 저장할 수 있습니다.
- 자동 생성된 회의록은 원본과 AI 요약본으로 나뉘어 제공되며, 검색 및 조회 기능을 통해 필요한 정보를 쉽게 찾을 수 있습니다.

![image](https://github.com/user-attachments/assets/147c191e-9af9-4e93-acad-86b053a2de62)

![image](https://github.com/user-attachments/assets/d2a3bbfa-783f-416b-b11e-6b70d212df12)

## 작업 및 역할 분담

| [김채리](https://github.com/cherrie-k) | [고경택](https://github.com/tagtaek) | [김수진](https://github.com/cowboysj) | [손수민](https://github.com/SuminSSon) | [윤성아](https://github.com/Sungah-Yoon) |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://avatars.githubusercontent.com/cherrie-k" width="80"> | <img src="https://avatars.githubusercontent.com/tagtaek" width="80"> | <img src="https://avatars.githubusercontent.com/cowboysj" width="80"> | <img src="https://avatars.githubusercontent.com/SuminSSon" width="80"> | <img src="https://avatars.githubusercontent.com/Sungah-Yoon" width="80"> |
| **Design, FE** | **FE** | **BE** | **BE, Infra** | **BE** |
| 대시보드, 그룹 스페이스, 회의록 및 유저 관리, 실시간 회의 및 봇 연동 | 로그인, 회원가입, 회의실 UI 구현 로그인 및 팀 참여 API 연결 | 회의록 생성, AWS AI 서비스를 이용한 봇 기능 구현 | 로그인/유저 관리, 회의 관리, 화상회의, 회의록 API | 그룹 스페이스 생성/참여 및 관리, 회의로그 관리, 회의록 반환 |
