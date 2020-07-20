temporary disable projects

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

### Путь до кастомного ключа ssh

```
SSH_KEY=/home/stCarolas/.ssh/id_rsa java -jar enki-generator-0.0.1.jar
```
