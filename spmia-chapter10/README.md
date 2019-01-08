Travis CI를 통해 10장을 AWS ECS에 빌드/배포하려는 [스프링 마이클서비스 코딩 공작소](https://www.gilbut.co.kr/book/view?bookcode=BN002339) 독자분을 위한 가이드
==============================================================================================================================================================

1.	먼저 10장을 읽어보십시오. :)

	10.4에 소개된 도구(메이븐, 도커, 스포티파이 플러그인, 도커 허브, 파이썬, ECS CLI 등)를 설치하고 AWS 인프라스트럭처(RDS, ElastiCache, ECS 클러스터)도 구축했다고 가정하겠습니다.

2.	코드 저장소 포크

	10장의 코드(https://github.com/klimtever/spmia-chapter10)와 Spring Config용 config-repo(https://github.com/klimtever/config-repo) 코드를 여러분의 GitHub 저장소(repository)로 포크(fork)합니다.

3.	Github와 Travis CI 연동

	-	Travis CI 사이트에서 Sign in with GitHub으로 가입한다.
	-	Sign in with GitHub > Authorize travis-ci 한 후 포크(fork)한 10장(spmia-chapter10)이 보일 것이다. 활성화한 후 아래 단계에 따라 Settings를 변경한다.<br><br>

4.	도커 허브 계정 수정

	빌드한 도커 이미지를 본인의 도커 허브 저장소에 올릴 수 있도록 이미지 이름을 변경해야 합니다. 10장의 코드에서 역자 도커 허브 계정인 *dockerever* 를 검색해 모두 본인의 도커 허브 계정으로 변경합니다.

5.	.travis.yml 수정

	-	환경 변수 설정:
		-	P379처럼 travis 도구로 설정하거나 travis 사이트에서 환경 변수를 설정할 수 있습니다.
		-	AWS_ACCESS_KEY, AWS_SECRET_KEY, DOCKER_USERNAME(역자의 경우, dockerever), DOCKER_PASSWORD, GITHUB_TOKEN 을 설정합니다.
	-	ECS 클러스터의 public IP 주소를 설정합니다.

	```
	  - export CONTAINER_IP={Your ECS Cluster IP}
	```

6.	/travis_scripts/tag_build.sh 파일의 $TARGET_URL을 설정합니다.

	```
	TARGET_URL="https://api.github.com/repos/{your GitHub account}/spmia-chapter10/releases?access_token=$GITHUB_TOKEN"
	```

7.	AWS 설정

	-	config-repo의 XXX-aws-dev.yml 파일에서 여러분의 AWS 구성에 맞게 수정합니다.

	```
	  spring.datasource.url: "jdbc:postgresql://eagle-eye-aws-dev2.cawltda6eb1j.ap-northeast-2.rds.amazonaws.com/eagle_eye_aws_dev"
	  spring.datasource.username: "eagle_eye_aws_dev"
	  spring.datasource.password: "{cipher}73e1f24af65b0f722cf0a2b7b68779cacad3ec18bc3a4aa958fd02147de22037"
	  redis.server: "spmia-tmx-redix-dev.5xpurl.0001.apn2.cache.amazonaws.com"
	```

	-	travis_scripts/deploy_to_amazon_ecs.sh에서 본인 AWS 설정에 맞게 수정합니다.

	```
	ecs-cli configure profile --profile-name "default" --access-key $AWS_ACCESS_KEY --secret-key $AWS_SECRET_KEY
	ecs-cli configure --cluster {your ECS cluster name} --default-launch-type EC2 --region ap-northeast-2 --config-name default
	```

8.	변경된 코드를 커밋하고 Travis CI에서 마이크로서비스의 도커 컨테이너가 성공적으로 빌드/배포되고 AWS ECS에서 수행되는지 확인합니다.

Introduction
============

Welcome to Spring Microservices in Action, Chapter 10. Chapter 10 is the end of the book and demonstrates how to create a build and deployment pipeline. We walkthrough how to build this pipeline and then deploy all of the services to Amazon's Elastic Container Service (ECS).

By the time you are done reading this chapter you will have built and/or deployed:

1.	A Spring Cloud Config server that is deployed as Docker container and can manage a services configuration information using a file system or GitHub-based repository.
2.	A Eureka server running as a Spring-Cloud based service. This service will allow multiple service instances to register with it. Clients that need to call a service will use Eureka to lookup the physical location of the target service.
3.	A Zuul API Gateway. All of our microservices can be routed through the gateway and have pre, response and post policies enforced on the calls.
4.	A organization service that will manage organization data used within EagleEye.
5.	A new version of the organization service. This service is used to demonstrate how to use the Zuul API gateway to route to different versions of a service.
6.	A special routes services that the the API gateway will call to determine whether or not it should route a user's service call to a different service then the one they were originally calling. This service is used in conjunction with the orgservice-new service to determine whether a call to the organization service gets routed to an old version of the organization service vs. a new version of the service.
7.	A licensing service that will manage licensing data used within EagleEye.
8.	A Kafka message bus to transport messages between services.

Our PostgreSQL database and Redis service have now been moved to Amazon services.

Software needed
===============

1.	[Apache Maven](http://maven.apache.org). I used version 3.3.9 of the Maven. I chose Maven because, while other build tools like Gradle are extremely popular, Maven is still the pre-dominate build tool in use in the Java ecosystem. All of the code examples in this book have been compiled with Java version 1.8.
2.	[Docker](http://docker.com). I built the code examples in this book using Docker V1.12 and above. I am taking advantage of the embedded DNS server in Docker that came out in release V1.11. New Docker releases are constantly coming out so it's release version you are using may change on a regular basis.
3.	[Git Client](http://git-scm.com). All of the source code for this book is stored in a GitHub repository. For the book, I used version 2.8.4 of the git client.

Building the Docker Images for Chapter 10
=========================================

To build the code examples for Chapter 10 as a docker image, open a command-line window change to the directory where you have downloaded the Chapter 9 source code.

Run the following maven command. This command will execute the [Spotify docker plugin](https://github.com/spotify/docker-maven-plugin) defined in the pom.xml file.

**mvn clean package docker:build**

If everything builds successfully you should see a message indicating that the build was successful.

Running the services in Chapter 10
==================================

Now we are going to use docker-compose to start the actual image. To start the docker image, change to the directory containing your chapter 10 source code. Issue the following docker-compose command:

**docker-compose -f docker/common/docker-compose.yml up**

If everything starts correctly you should see a bunch of Spring Boot information fly by on standard out. At this point all of the services needed for the chapter code examples will be running.
