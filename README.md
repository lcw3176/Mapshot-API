<p align="center">
  <img src="https://user-images.githubusercontent.com/59993347/166405369-0d610a83-68d5-4d31-8215-6eba806fba06.png" height="250">
</p>
<p align="center">
<img src="https://github.com/lcw3176/Mapshot-API/actions/workflows/deploy.yml/badge.svg">
<img src="https://github.com/lcw3176/Mapshot-API/actions/workflows/pr.yml/badge.svg"/>
<img src="https://github.com/lcw3176/Mapshot-API/actions/workflows/deploy-dev.yml/badge.svg"/>
</p>

<p align="center">
  <a href="https://kmapshot.com">https://kmapshot.com</a>
</p>  

## 소개

맵샷은 넓은 반경의 고화질 위성 이미지를 제공해주는 서비스입니다.

도시 계획 직종에 근무하는 친구의 효율적인 작업을 위해 고안되었으며,
지도를 한 구역씩 캡쳐해서 포토샵으로 합치던 기존의 프로세스를 자동화해서
효율적인 업무가 가능합니다.

### 이용 대상

<div style="text-align: center">
<img width="200" style="margin:10px;" src="https://user-images.githubusercontent.com/59993347/164415956-f8a6a057-8943-4656-bd94-e8a5ffdec329.jpg">
<img width="200" style="margin:10px;" src="https://user-images.githubusercontent.com/59993347/164415966-d33b7751-cdfe-4a65-8b72-03b1a6b4cae9.jpg">
</div>

- 위와 같은 도시 계획도의 배경 위성 이미지가 필요하신 분
- 연속 지적도, 토지이용 계획도 등 도시 계획 레이어 지도 이미지가 필요하신 분
- 고화질의 넓은 지도 이미지가 필요하신 분

### 작동 방식

![화면 캡처 2023-07-02 211656](https://github.com/lcw3176/mapshot-admin/assets/59993347/54d34f27-bfe8-4bc3-91a4-c051c2128c7d)

- 캡쳐할 범위가 정해지면, 반경에 맞게 범위를 적절히 나누어 각각의 이미지 좌표를 계산합니다.
- 분할한 타일들의 이미지를 불러온 후, 알맞게 조립합니다.
- 최소 121장, 최대 441장의 사진을 호출합니다.

### API 문서

https://docs.kmapshot.com

![localhost_63342_mapshot-api_src_main_resources_static_docs_mapshot-docs html__ijt=lkjgksifj5npj4b8olrn74bour _ij_reload=RELOAD_ON_SAVE](https://github.com/lcw3176/Mapshot-API/assets/59993347/e8e3bed9-9198-4ddf-8049-0f582249a8db)

### 모니터링

https://monitor.kmapshot.com

- 그라파나

#### 리눅스 (Node Exporter Full)

![스크린샷 2024-03-16 003531](https://github.com/lcw3176/Mapshot-API/assets/59993347/a07c4f90-e1bd-445c-97b1-700a3fe87965)

#### 어플리케이션 (Spring Boot Statistics & Endpoint Metrics)

![스크린샷 2024-03-16 003746](https://github.com/lcw3176/Mapshot-API/assets/59993347/a8b0ee5a-3a36-40a8-a66e-72a63225453b)

### 배포 프로세스

![asdasd (1)](https://github.com/lcw3176/Mapshot-API/assets/59993347/3b448bb3-19d4-4397-bb65-64ec2d6805f1)

### 서버 아키텍쳐

![asdasd](https://github.com/lcw3176/Mapshot-API/assets/59993347/005720e8-f1c6-43e8-b138-5eaaf85f66a0)

## 부가 정보

### 기술 스택

- Java 17, Spring Boot 3.0.3, Node.js
- Spring-Data-JPA, H2 Database
- AWS (EC2, Lambda), Cloud Flare(https, domain), Docker(compose, hub)
- Sentry, Grafana

### 서버 내부 구조

```shell
├── ***.env
├── docker-compose.yml
└── mapshot-executor
    └── logs
        ├── app.log
        └── app.2023-03-01.0.log.gz
    └── main.db
    
```

### docker-compose

```sh
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
    environment:
      - JVM_OPTS=-Xmx300m -Xms300m
      - TZ=Asia/Seoul
    deploy:
      resources:
        limits:
          memory: 512M
```

### 브랜치

- feature/*: 개발 기능, 티켓
- dev: 개발 브랜치, 테스트 거친 후 자동 배포
- master: 운영 브랜치

