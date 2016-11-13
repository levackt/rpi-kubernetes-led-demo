package example;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GpioRoute extends RouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Value("${gpioId:GPIO_01}")
    private String gpioId;

    @Override
    public void configure() throws Exception {

        //toggle led every second
        from("timer:default?period=1000")
                .to("pi4j-gpio://" + gpioId + "?action=TOGGLE");
    }

}
