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