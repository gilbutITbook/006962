echo "Pushing service docker images to docker hub ...."
#docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
docker push dockerever/tmx-authentication-service:$BUILD_NAME
docker push dockerever/tmx-licensing-service:$BUILD_NAME
docker push dockerever/tmx-organization-service:$BUILD_NAME
docker push dockerever/tmx-confsvr:$BUILD_NAME
docker push dockerever/tmx-eurekasvr:$BUILD_NAME
docker push dockerever/tmx-zuulsvr:$BUILD_NAME
