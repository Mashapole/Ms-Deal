package com.enfint.deal.service.Kafka;

import com.enfint.deal.dto.EmailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, String> template;
    private final ObjectMapper objectMapper;


    @Value("${custom.message.topic.finish-registration}")
    protected  String finishRegistrationTopic;

    @Value("${custom.message.topic.create-document}")
    protected  String createDocumentTopic;

    @Value("${custom.message.topic.send-document}")
    protected  String sendDocumentTopic;

    @Value("${custom.message.topic.send-ses}")
    protected  String sendSesTopic;

    @Value("${custom.message.topic.credit-issued}")
    protected  String creditIssueTopic;

    @Value("${custom.message.topic.application-declined}")
    protected  String applicationDeclinedTopic;

    public void message(EmailMessage mess)
    {
        String theme=String.valueOf(mess.getTheme());
        String topic;


        switch (theme)
        {
            case "FINISH_REGISTRATION":
                topic=finishRegistrationTopic;
                break ;
            case "CREATE_DOCUMENTS":
                topic=createDocumentTopic;
                break;
            case "SEND_DOCUMENTS":
                topic=sendDocumentTopic;
                break;
            case "SEND_SES":
                topic=sendSesTopic;
                break;
            case "CREDIT_ISSUED":
                topic=creditIssueTopic;
                break;
            default:
                topic=applicationDeclinedTopic;
                break;
        }

        try
        {
            template.send(topic, objectMapper.writeValueAsString(mess));
        }
        catch (Exception exception)
        {
        log.error(exception.toString());
        }
    }
}
