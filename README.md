<h1> 스프링 마이크로서비스 코딩 공작소(길벗, 2018)

## 예제 파일에 대하여 
- 이 책의 모든 코드는 Java 8, Maven 3.5.4, Docker 18.06, Spring boot 2.0.3, Spring Cloud Finchley.ReLEASE, Git Client 2.17.1 버전을 기준으로 합니다.(원서는 스프링 1 버전 기준이지만, 역자분이 스프링 2로 모두 수정하였습니다.)

- 실습 방법은 책의 부록 A를 참고합니다. 페이지 하단에 간략한 실습 과정을 표기합니다.

- 원서와 달리 번역서 깃허브는 하나의 프로젝트로 구성되어 있습니다. 전체 예제 파일을 내려받은 후 장별 폴더로 이동해 실습하면 됩니다.

- spmia-postman-testing 폴더는 실습용 POSTMAN 파일입니다.

## 역자 깃허브에서는 장별 깃허브를 독립적으로 구성해 제공합니다. 원서 깃허브와도 포크되어 있습니다.
- 원서 깃허브 주소(스프링 1 버전) : https://github.com/carnellj/native_cloud_apps <br/>
- 역자 깃허브 주소 : https://github.com/klimtever/   <br/>
(역자 깃허브는 장마다 개별 저장소로 구성되어 있습니다. 예를 들어 1장의 경우 https://github.com/klimtever/spmia-chapter1)

## 전체 실습 흐름

1. 자바 8, 메이븐, 도커를 설치한다. <br/>

2. 깃허브에서 예제 파일을 내려받는다. <br/>

3. 내려받은 예제 파일 폴더에서 다음 두 명령어를 순차적으로 실행한다(도커 실행 기준이며, 첫 명령어가 에러 없이 실행되면 다음 명령어를 실행한다).  <br/>
mvn clean package docker:build  <br/>
docker-compose -f docker/common/docker-compose.yml up  <br/>

4. 정상적으로 실행되었다면 POSTMAN에서 값을 입력해 테스트한다. 깃허브에서 제공하는 spmia-postman-testing/spmia-example-2.0.postman_collection.json을 사용해도 된다. <br/>

5. 각 장 실습이 끝난 이후에는 다음 명령어로 도커를 내린다.  <br/>
docker-compose -f docker/common/docker-compose.yml down  <br/>

6. 또 컨테이너 이미지를 모두 삭제한다(자세한 명령어는 부록 A 참고). <br/>
docker rmi -f $(docker images -q -a)  <br/>

## 10장 실습 가이드

Travis CI를 통해 10장을 AWS ECS에 빌드/배포하려는 [스프링 마이크로서비스 코딩 공작소](https://www.gilbut.co.kr/book/view?bookcode=BN002339) 독자분을 위한 가이드


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