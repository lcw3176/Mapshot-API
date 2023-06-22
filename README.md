<p align="center">
  <img src="https://user-images.githubusercontent.com/59993347/166405369-0d610a83-68d5-4d31-8215-6eba806fba06.png" height="250">
</p>
<p align="center">
<img src="https://img.shields.io/badge/Made%20with-SpringBoot-blue">
<img src="https://img.shields.io/badge/Service%20begun%20in-2021.02-brigntgreen">
</p>
<p align="center">
  <a href="https://kmapshot.com">https://kmapshot.com</a>
</p>  

## 구현

### 완료

- 사용자는 지도 이미지를 요청, 발급 받을 수 있습니다.
- 사용자는 공지사항을 읽을 수 있습니다.
- 관리자는 공지사항을 등록, 수정, 삭제할 수 있습니다.

### 예정

- 사용자는 문의사항을 보낼 수 있습니다.
    - 답신받을 메일과 문의 내용이 포함되어야 합니다.
    - 이미지 파일 첨부가 가능해야 합니다.

## 기술적 과제

### 지도 이미지를 어떻게 효율적으로 생성할 것인가?

- 지도 이미지는 자바스크립트 코드를 통해 동적으로 생성됨
    - 직접 브라우저를 동작시켜서 해당 html view를 만들어야 함.
    - 동적 웹페이지를 크롤링하는 작업이 필요

- 효율적인 웹 크롤링 작업 고려
    - 초기 서비스
        - 단일 리눅스 서버에서 크롤링 작업을 도맡아서 함
        - 한정적인 자원 때문에 사용자 요청 Queue에 넣고 순차적으로 작업 처리
        - 갑자기 트래픽이 몰리면 요청이 점점 쌓이는 문제 발생
    - 현재 서비스
        - 크롤링 작업 aws lambda로 이동
        - 요청이 누적되지 않고 개별 처리됨

- selenium을 통해 웹 자동화
    - selenium과 playwright가 후보였음.
    - 지도 이미지 캡쳐를 해야하는데 playwright는 jpeg 확장자 스크린샷을 지원하지 않음, 용량 문제 발생
    - aws lambda 전용으로 제작된 selenium 라이브러리 존재, 최종 채택

### 지도 이미지를 어떻게 사용자에게 발급할 것인가?

- 지도 이미지는 별도의 DB에 저장되어서는 안됨 (API 사용 약관 위배)
    - 매번 유저가 요청할 때마다 API 키를 활용해서 html view를 만들어야 함
    - 이렇게 생성한 이미지는 DB에 저장해서 사용하는 것이 금지되어 있음.

- 이미지 파일이 aws lambda 단일 응답 용량을 넘어섬
    - 이미지 파일이 약 10MB, lambda 응답 용량 한계(6MB) 넘음

- 서버에 Key, Value 쌍의 Map을 이용한 휘발성 저장소 생성
    - lambda 서버는 지도 이미지를 분할 캡쳐 함
    - UUID를 이용한 키값과 이미지 바이트 값을 밸류로 EC2 서버에 임시 보관 요청
    - 저장 완료 시 사용자에게 키값 전달, 사용자는 해당 값을 이용해 이미지 요청
    - 이미지는 발급됨과 동시에 삭제 처리됨

## 개선 방향

### 다수의 서버에서 동작 시 이미지 전송을 어떻게 할 것인가?

- 현재 시스템은 서버의 메모리에 이미지가 저장되기 때문에 도중에 다른 서버로 요청이 분산되면 이미지 전송이 불가능함
- Redis 같은 속도가 빠른 메모리 DB에 중앙 집중적으로 저장하는 방법도 있겠으나, 만약 사용하게 된다면 API 사용 약관 위배일 가능성이 큼
- AWS 같은 경우는 Sticky Session 이라는 동일 세션은 같은 서버로 지속적으로 연결해주는 옵션도 있다고 함.

## 기타

### 배포 프로세스

![asdasd (1)](https://github.com/lcw3176/Mapshot-API/assets/59993347/3b448bb3-19d4-4397-bb65-64ec2d6805f1)

### 서버 아키텍쳐

![asdasd](https://github.com/lcw3176/Mapshot-API/assets/59993347/005720e8-f1c6-43e8-b138-5eaaf85f66a0)

### 기술 스택

#### BackEnd

- Java 11, Spring Boot 2.6.5
- H2 Database, Spring-Data-JPA

#### Infra

- AWS (EC2, Lambda), Cloud Flare(https, domain), Docker
- Whatap, Slack

### 서버 구조

```shell
├── ***.env
├── docker-compose.yml
└── mapshot-executor
    └── was-logs
        ├── info.2023-03-01.0.log.gz
        ├── warn.2023-03-01.0.log.gz
        └── error.2023-03-01.0.log.gz
    └── logs
        ├── info.log
        ├── warn.log
        └── error.log
    
```

### docker-compose

```shell
version: '3'

services:
  mapshot-executor:
    image: 'docker-hub image path'
    ports:
      - "port:number"
    logging:
      driver: none
    env_file:
      - ***.env
    volumes:
      - /mapshot-executor:/mapshot-executor
    deploy:
      resources:
        limits:
          memory: 400M
```




