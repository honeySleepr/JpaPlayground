# JDK11 이미지 사용
FROM openjdk:11-jdk

# `COPY [파일 경로] [새이름]` : 경로의 파일을 복사해서 도커 이미지 안으로 가져오고, 새 이름을 지정한다.
COPY ./build/libs/*.jar app.jar

# 빌드된 이미지가 run 될 때 실행할 명령어
ENTRYPOINT ["java","-jar","/app.jar"]
