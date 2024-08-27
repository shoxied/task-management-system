FROM openjdk:18.0.2

ARG VERSION=$VERSION
ENV jarFile=task-management-system-$VERSION.jar

RUN mkdir -p /opt/task-management-system
COPY target/${jarFile} /opt/task-management-system
EXPOSE 8180
CMD  cd /opt/task-management-system; java -XshowSettings:vm $JAVA_OPTS -Dspring.profiles.active=$PROFILE -jar ${jarFile} $ARGS