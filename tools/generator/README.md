# Enki-generator

Генератор из mustache шаблонов. Берет git ssh url, клонирует репу,
перегоняет все файлы из нее в текущую директорию, создавая всю структуру папок, если надо.
При этом подставляет в каждый файл значения в плейсхолдеры mustache, если они есть.

### Параметры
```
java -jar enki-generator-0.0.1.jar [-d key=value] [-m source=target] gitSshUrl
```
где
- `-d key=value` - значение value для параметра key в шаблонах mustache
- `-m source=target` - по умолчанию при копировании файлы кладутся по тому же относительному пути, что и в изначальной репе.
Этот параметр позволяет для файла с путем source задать другое конечное месторасположение ( в том числе и с другим именем )

примеры:
```
#!/bin/sh
java -jar enki-generator-0.0.1.jar \
    -d groupId=rocks.mango.policy \
    -d artifactId=policy-component \
    -d javaComponentName=PolicyEvent \
    -d package=rocks.mango.policy.policyevent \
    -d receivingObjectClass=rocks.mango.api.policy.PPolicyEvent \
    -d notifyingObjectClass=rocks.mango.api.policy.PPolicyEvent \
    -d configKey=policy.policyevent \
    -d parentLibPackage=rocks.mango.api\
    -d parentLibArtifactId=policy-api \
    -d parentLibVersion=42.0 \
    -m "src/main/java/rocks/mango/createrefund/kafka/KafkaComponent.java"="src/main/java/rocks/mango/policy/policyevent/PolicyEventKafkaComponent.java" \
    -m "src/main/java/rocks/mango/createrefund/kafka/DefaultKafkaComponent.java"="src/main/java/rocks/mango/policy/policyevent/DefaultPolicyEventKafkaComponent.java" \
    -m "src/main/java/rocks/mango/createrefund/kafka/NotifierKafkaModule.java"="src/main/java/rocks/mango/policy/policyevent/PolicyEventNotifierKafkaModule.java" \
    -m "src/main/java/rocks/mango/createrefund/kafka/ReceiverKafkaModule.java"="src/main/java/rocks/mango/policy/policyevent/PolicyEventReceiverKafkaModule.java" \
    ssh://git@git.service.consul:2222/Mango/template-kafka-component.git
```
```
java -jar enki-generator-0.0.1.jar \
    -d pipelineName="KafkaComponent_Policy" \
    -d pipelineGroup="KafkaComponents" \
    ssh://git@git.service.consul:2222/Mango/template-library-pipeline.git
```

### Путь до кастомного ключа ssh
```
SSH_KEY=/home/stCarolas/.ssh/id_rsa java -jar enki-generator-0.0.1.jar
```
