package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req(HttpRequestType.POST.value(), PoohMode.TOPIC.value(), "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is("client6565 subscribed"));
    }

    @Test
    public void whenTopicPostWithoutSubscriber() {
        String paramForPublisher = "temperature=18";
        TopicService topicService = new TopicService();
        Resp result = topicService.process(
                new Req(HttpRequestType.POST.value(), PoohMode.TOPIC.value(), "weather", paramForPublisher)
        );
        assertThat(result.text(), is("No Subscribers to post"));
    }

    @Test
    public void whenTopicAndManySubscribers() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        Resp result1 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber2)
        );
        Resp result3 = topicService.process(
                new Req(HttpRequestType.POST.value(), PoohMode.TOPIC.value(), "weather", paramForPublisher)
        );
        Resp result4 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber1)
        );
        Resp result5 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("client407 subscribed"));
        assertThat(result2.text(), is("client6565 subscribed"));
        assertThat(result3.text(), is("OK"));
        assertThat(result4.text(), is("temperature=18"));
        assertThat(result5.text(), is("temperature=18"));
    }

    @Test
    public void whenEmptiedQueueThanNoText() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber = "client407";
        Resp result1 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber)
        );
        topicService.process(
                new Req(HttpRequestType.POST.value(), PoohMode.TOPIC.value(), "weather", paramForPublisher)
        );
        Resp result2 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber)
        );
        Resp result3 = topicService.process(
                new Req(HttpRequestType.GET.value(), PoohMode.TOPIC.value(), "weather", paramForSubscriber)
        );
        assertThat(result1.text(), is("client407 subscribed"));
        assertThat(result2.text(), is("temperature=18"));
        assertThat(result3.text(), is(""));
    }
}