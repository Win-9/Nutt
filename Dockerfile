FROM openjdk:17-alpine

# JAR 파일 메인 디렉토리에 복사
COPY ./build/libs/Nutt-0.0.1-SNAPSHOT.jar Nutt.jar

# 시스템 진입점 정의
ENV PROFILE="dev"
ENTRYPOINT ["java","-jar","/Nutt.jar", "--spring.profile.active=$PROFILE"]