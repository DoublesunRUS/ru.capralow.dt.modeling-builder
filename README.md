![Build](https://github.com/DoublesunRUS/ru.capralow.dt.modeling-builder/workflows/CI/badge.svg)


## Экспорт форм и модулей в СППР для [1C:Enterprise Development Tools](http://v8.1c.ru/overview/IDE/) 2022.1

Минимальная версия EDT: 2022.1

Текущий релиз в ветке [master: 0.1.0](https://github.com/DoublesunRUS/ru.capralow.dt.modeling-builder/tree/master).<br>
Разработка ведется в ветке [dev](https://github.com/DoublesunRUS/ru.capralow.dt.modeling-builder/tree/dev).<br>

В данном репозитории хранятся только исходники.<br>

Плагин можно установить в EDT через пункт "Установить новое ПО" указав сайт обновления http://capralow.ru/edt/modeling-builder/0.1.0/ . Для установки может потребоваться запуск EDT под правами администратора.<br>
Для самостоятельной сборки плагина необходимо иметь доступ к сайту https://releases.1c.ru и настроить соответствующим образом Maven. Подробности настройки написаны [здесь](https://github.com/1C-Company/dt-example-plugins/blob/master/simple-plugin/README.md).

### Возможности
Эскпорт метаданных и форм в формате yaml.<br>
Экспорт описания методов из всех модулей в формате yaml.<br>