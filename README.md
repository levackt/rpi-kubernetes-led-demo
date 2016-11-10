# kubernetes-led-demo

Demonstrates using Kubernetes by deploying a container that toggles LEDs, in order to discuss key concepts such as deployment, services, labels, node selection, scaling, privileges etc.

For this we need to build a cluster, for this check out the following awesome projects;

[A Raspberry Pi 3 Kubernetes Cluster](https://ro14nd.de/kubernetes-on-raspberry-pi3)

[https://github.com/Project31](Project31 aims to deploy the Fabric8 applications on ARM64 architectures, in particular a cluster of RaspberryPi3 and Pine64.)

### Wire rPi to breadboard

    [Turning on an LED with your Raspberry Pi's GPIO Pins](https://thepihut.com/blogs/raspberry-pi-tutorials/27968772-turning-on-an-led-with-your-raspberry-pis-gpio-pins)

### Start a Camel route using Pi4J

  This route that toggles the state of the GPIO pin every second.

        from("timer:default?period=1000").to("pi4j-gpio://GPIO_01?mode=DIGITAL_OUTPUT&state=LOW&action=TOGGLE").to("log:default?showHeaders=true");

### Build a fat jar

  If using Spring then repackage as follows;


        <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring-boot.version}</version>
        <configuration>
            <mainClass>example.KubernetesLedDemo</mainClass>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>repackage</goal>
                </goals>
            </execution>
        </executions>
      </plugin>

 See if it works;

      mvn clean install

      sudo java -jar target/kubernetes-led-demo.jar



### Now build the docker image and Kubernetes manifest

 See [fabric8-maven-plugin](https://github.com/fabric8io/fabric8-maven-plugin)



        <plugin>
        <groupId>io.fabric8</groupId>
        <artifactId>fabric8-maven-plugin</artifactId>
        <version>3.1.92</version>
        <executions>
            <execution>
                <id>fmp</id>
                <goals>
                    <goal>resource</goal>
                    <goal>build</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <images>
                <image>
                    <name>${project.artifactId}:${project.version}</name>
                    <build>
                        <from>hypriot/rpi-java:1.8.0</from>
                        <assembly>
                            <basedir>/app</basedir>
                            <descriptorRef>artifact</descriptorRef>
                        </assembly>
                        <entryPoint>
                            <arg>java</arg>
                            <arg>-jar</arg>
                            <arg>/app/${project.artifactId}.jar</arg>
                        </entryPoint>
                    </build>
                </image>
            </images>

        </configuration>
      </plugin>

      NB Base image is Hypriot's Java 8 for rPi, to this we add the far-jar and configure the entry-point.


### test Docker
  docker run -it --privileged --rm kubernetes-led-demo:1.0-SNAPSHOT

  NB Run the container as privileged so wiringPi can access the device ( /dev/mem )

todo Lots left to do, fabric8 plugin commands, kubectl, label nodes etc
