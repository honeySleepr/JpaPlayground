# 연근마켓 프로젝트

## 🎯목표

JPA와 Spring의 기능들을 학습하며, 간단한 버전의 **당근마켓** 같은 중고거래 API 서버를 만들어보는 것이 목표입니다.

<br>

## 🔨 사용한 기술 스택

<img src="https://img.shields.io/badge/java-ED8B00?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=spring boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">

<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">

[//]: # (<img src="https://img.shields.io/badge/docker-2496ed.svg?style=for-the-badge&logo=docker&logoColor=white">)

<br>

## ✨ 구현한 내용

- Product, Reservation CRUD
- Bookmark 기능 구현 중
- 로그인 (OAuth + JWT ) - Spring Security X
- GitHub Action CI

<br>

## API 문서

https://documenter.getpostman.com/view/20860983/2s8YmHykWU

## PR review

더 나은 코드를 위해 혼자 고민하는 것도 좋지만, 더 넓은 시야를 가지기 위해 다른 사람의 피드백을 받는 게 좋겠다고 생각하였습니다. 그래서 지인에게
부탁하여 [PR 리뷰](https://github.com/honeySleepr/JpaPlayground/pulls?q=) 를 받고 있습니다.

> 다른 분들의 피드백도 환영합니다🙏

<br>

## 🤔 고민 & 트러블슈팅

[@SpringBootTest 중 @ConfigurationProperties 클래스 초기화 문제](https://github.com/honeySleepr/JpaPlayground/wiki/@SpringBootTest-%EC%A4%91-@ConfigurationProperties-%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%B4%88%EA%B8%B0%ED%99%94-%EB%AC%B8%EC%A0%9C)

[JSON 데이터를 DTO와 MultiValueMap으로 보냈을 때의 차이(feat. Naver 로그인 401 에러)](https://github.com/honeySleepr/JpaPlayground/wiki/JSON-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC-DTO%EC%99%80-MultiValueMap%EC%9C%BC%EB%A1%9C-%EB%B3%B4%EB%83%88%EC%9D%84-%EB%95%8C%EC%9D%98-%EC%B0%A8%EC%9D%B4(feat.-Naver-%EB%A1%9C%EA%B7%B8%EC%9D%B8-401-%EC%97%90%EB%9F%AC))

[Nested JSON을 Map으로 변환하는 방법](https://github.com/honeySleepr/JpaPlayground/wiki/Nested-JSON%EC%9D%84-Map%EC%9C%BC%EB%A1%9C-%EB%B3%80%ED%99%98%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95)

[Custom Exception, 어디까지 만들어야 하는가?](https://github.com/honeySleepr/JpaPlayground/wiki/Custom-Exception,-%EC%96%B4%EB%94%94%EA%B9%8C%EC%A7%80-%EB%A7%8C%EB%93%A4%EC%96%B4%EC%95%BC-%ED%95%98%EB%8A%94%EA%B0%80%3F)

[Filter와 Interceptor 중 어떤 걸 사용하는 게 좋을까?](https://github.com/honeySleepr/JpaPlayground/wiki/Filter%EC%99%80-Interceptor-%EC%A4%91-%EC%96%B4%EB%96%A4-%EA%B1%B8-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%EA%B2%8C-%EC%A2%8B%EC%9D%84%EA%B9%8C%3F)

[Refresh Token 재발급은 언제 해야할까](https://github.com/honeySleepr/JpaPlayground/wiki/Refresh-Token-%EC%9E%AC%EB%B0%9C%EA%B8%89%EC%9D%80-%EC%96%B8%EC%A0%9C%ED%95%B4%EC%95%BC%ED%95%A0%EA%B9%8C)

[Login 에러 메시지는 자세한 게 좋을까?](https://github.com/honeySleepr/JpaPlayground/wiki/Login-%EC%97%90%EB%9F%AC-%EB%A9%94%EC%84%B8%EC%A7%80%EB%8A%94-%EC%9E%90%EC%84%B8%ED%95%9C-%EA%B2%8C-%EC%A2%8B%EC%9D%84%EA%B9%8C%3F)

[단일 DTO와 Slice를 모두 수용할 수 있는 CommonResponse를 만드려면?(실패)](https://github.com/honeySleepr/JpaPlayground/wiki/%EB%8B%A8%EC%9D%BC-DTO%EC%99%80-Slice%EB%A5%BC-%EB%AA%A8%EB%91%90-%EC%88%98%EC%9A%A9%ED%95%A0-%EC%88%98-%EC%9E%88%EB%8A%94-CommonResponse%EB%A5%BC-%EB%A7%8C%EB%93%9C%EB%A0%A4%EB%A9%B4%3F(%EC%8B%A4%ED%8C%A8))
