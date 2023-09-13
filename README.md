## 개요
[NUMBLE](https://www.numble.it/)에서 2023.03.13 ~ 2023.03.26 동안 진행된 [**네이버 MYBOX 서버 만들기**](https://thoughtful-arch-8c2.notion.site/MYBOX-b00ce9f0e73a4f368a6c23075816b863) 챌린지에 참여해 수행한 개인 프로젝트입니다.

챌린지 종료 후 개인적으로 3주 정도 더 프로젝트를 보완하였습니다.

<br>

## 챌린지 목적 및 달성 상태
- Spring MVC, RDBMS, Storage 솔루션 활용 ✅
- 서로 다른 솔루션(DB, Storage)간의 연동 ✅
- transaction 처리 
- 아키텍쳐와 ERD, DB schema 설계를 진행하고 구현 ✅
- CI/CD 파이프라인 구축 ✅
- 테스트 코드 작성 ✅
- Spring Security + JWT를 활용한 회원가입/로그인 구현 ✅
- nGrinder를 통한 성능 테스트
- 정적 분석 등 추가적인 코드 품질 관리 요소 추가
- 캐시, 배치, message queue 등을 통한 성능 향상

<br>

## mini-MYBOX 와이어프레임
<img width="844" alt="image" src="https://github.com/0112leesy/mini-mybox-0112leesy/assets/78717113/b9cd2876-0030-419d-b392-8a45b8633a88">

<br>

## 요구사항
1. **유저 API**
    1. 유저 생성(login)
        1. 30GB 이상의 Root Folder 생성 기능
    2. 유저 정보 조회 — userId, 사용량 등
        1. 사용 가능한 용량 및 사용중 용량 조회 기능
2. **파일 API**
    1. 파일 조회
        1. (옵션) 이미지 파일인지 텍스트 파일인지 구분
    2. 파일 업로드
        1. 파일명 중복 확인 기능
        2. 파일 크기 확인 기능
        3. 사용 가능한 공간 확인 기능
        4. 사용 가능한 용량 차감 및 사용중 용량 증가 기능
        5. 업로드 성공 확인 기능
        6. 업로드 실패 시 사용 가능한 공간 롤백 기능
    3. 파일 다운로드
        1. 다운로드 용량 표시 기능
    4. 파일 삭제
        1. 사용 가능한 공간 확보
3. **폴더 API**
    1. 폴더 조회
    2. 폴더 생성
        1. 폴더명 중복 확인 기능
    3. 폴더 삭제
    4. 자식 파일/폴더 리스트 조회
    5. zip 파일 다운로드(옵션)

<br>

## 사용된 기술

### Framework
SpringBoot 2.6

### Database
MySQL

### Test
JUnit5 <br>
Mockito

### CI/CD
Github Actions

### Cloud
AWS EC2, RDS <br>
NCP Object Storage

<br>

## ERD

<img width="860" alt="image" src="https://github.com/0112leesy/mini-mybox-0112leesy/assets/78717113/991764c5-b819-40a6-bf93-94aabc24c846">


## 챌린지 기록 상세

[mini-MYBOX 챌린지 기록](https://nebula-erigeron-61c.notion.site/mini-MYBOX-7504f31806f142d5bb7a57399325efae?pvs=4)


