package fiap.tech.challenge.hospital_manager.config;

import fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "consulta_exchange";
    public static final String CONSULTA_QUEUE = "consulta_queue";
    public static final String ROUTING_KEY_CONSULTA = "consulta.new";

    public static final String EXCHANGE_NOTIFICATION_NAME = "notification_exchange";
    public static final String NOTIFICATION_QUEUE = "notification_queue";
    public static final String ROUTING_KEY_NOTIFICATION = "notification.new";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(EXCHANGE_NOTIFICATION_NAME);
    }

    @Bean
    public Queue consultaQueue() {
        return new Queue(CONSULTA_QUEUE, true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(consultaQueue()).to(exchange()).with(ROUTING_KEY_CONSULTA);
    }

    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue()).to(notificationExchange()).with(ROUTING_KEY_NOTIFICATION);
    }

    @Bean
    public Jackson2JsonMessageConverter consumerMessageConverter() {
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("fiap.tech.challenge.marcador_consultas.consulta_producer.dto.in.ConsultaIn",
                ConsultaIn.class);
        typeMapper.setIdClassMapping(idClassMapping);
        typeMapper.setTrustedPackages("*");
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setJavaTypeMapper(typeMapper);
        return converter;

    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter consumerMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(consumerMessageConverter);

        // 👇 força execução single-thread
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);

        // 👇 útil para debug
        factory.setDefaultRequeueRejected(false);
        factory.setMissingQueuesFatal(false);
        return factory;
    }
}
