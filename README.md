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

## 기술 스택

### BackEnd

- Java 11, Spring Boot 2.6.5
- MariaDB, Spring-Data-JPA

### Infra

- AWS (EC2, Lambda), Cloud Flare(https, domain)
- Nginx, Docker
- Whatap, Slack

## 기타

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




