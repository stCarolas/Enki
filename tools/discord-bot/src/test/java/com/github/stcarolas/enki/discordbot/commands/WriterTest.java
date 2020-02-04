package com.github.stcarolas.enki.discordbot.commands;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;
import lombok.val;

public class WriterTest {

    public String message = "**Используемые параметры** __repo__ ssh://git@git.service.consul:2222/Mango/test-repo.git __template__ ssh://git@git.service.consul:2222/Mango/template-topic-adapter.git __data__ sourceMessageLibVersion = <версия maven артефакта, в котором лежат receivingObjectClass> sourceMessageLibPackage = <группа maven артефакта, в котором лежат receivingObjectClass> package = <java package, в котором будут лежать сгенеренные исходники> targetMessageLibPackage = <группа maven артефакта, в котором лежат notifyingObjectClass> targetMessageJavaComponentName = <название компонента/сущности ( к примеру - PolicyEvent)> groupId = <вставьте группу для мавен артефакта вида rocks.mango.policy> targetMessageComponentPackage = <группа maven артефакта с кафка-компонентом для notifyingObjectClass> targetMessageLibVersion = <версия maven артефакта, в котором лежат notifyingObjectClass> protoTypeClass = <java package, в котором будут лежать сгенеренные исходники> receivingObjectClass = <java class для обьекта, который приходит из кафки> sourceMessageComponentVersion = <версия maven артефакта с кафка-компонентом для receivingObjectClass> sourceMessageComponentPackage = <группа maven артефакта с кафка-компонентом для receivingObjectClass> sourceMessageJavaComponentName = <название компонента/сущности ( к примеру - PolicyEvent)> targetMessageComponentVersion = <версия maven артефакта с кафка-компонентом для notifyingObjectClass>> sourceMessageLibArtifactId = <имя maven артефакта, в котором лежат receivingObjectClass> targetMessageComponentArtifactId = <имя maven артефакта с кафка-компонентом для notifyingObjectClass>> artifactId = <название maven артефакта> notifyingObjectClass = <java class для обьекта, который посылается в кафку> targetMessageLibArtifactId = <имя maven артефакта, в котором лежат notifyingObjectClass> sourceMessageComponentArtifactId = <имя maven артефакта с кафка-компонентом для receivingObjectClass> __mapping__ src/main/java/rocks/mango/receiptadapter/TopicAdapter.java = <новый путь с пакетом> src/main/java/rocks/mango/receiptadapter/Application.java = <новый путь с пакетом> --- **Введите `yes` для выполнения**";

    @Test
    public void test(){
        List.ofAll(message.getBytes()).sliding(1000,1000).forEach( partialMessage -> {
            val bytes = partialMessage.asJava();
            val castedBytes = new byte[bytes.size()];
            for (int i=0; i<bytes.size(); i++){
                castedBytes[i] = bytes.get(i).byteValue();
            }
            System.out.println(new String(castedBytes));
        });
    }
}
