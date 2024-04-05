# Задача

Спроектировать и реализовать набор программных компонентов, 
которые позволяют производить преобразование произвольного объекта в JSON строку.

## API

Предлагаемый API для реализации предполагает наличие метода, с сигнатурой аналогичной:

```scala
def asJson[A](a: A): String
```

Вы можете изменить или дополнить сигнатуру по своему усмотрению.

Реализовать метод можно посредством паттерна typeclass. Соотвественно, 
определение вашего класса типов может выглядеть следующим образом:
    
```scala
trait AsJson[A] {
    def asJson(a: A): String
}
```

## Критерии
- Конвертер в JSON должен быть реализован в виде тайпкласса
- Конвертер должен поддерживать преобразование базовых типов (Int, String, Boolean, List, Map), ровно как и преобразования кейс классов
- Конвертер должен уметь работать с вложенными структурами
- Необходимо написать набор тестов к конвертеру; для тестирования можно воспользоваться JSON библиотеками

## Оценка за основную часть
За безупречное выполнение вышеописанных условий вы можете получить четыре балла из пяти.

## Дополнительные задания
Будем плюсом, если вы усовершенствуете либо обогатите вашу реализацию как-либо полезным образом, например:
- Поддержка `Option` типа
- Поддержка `enum`
- Поддержка форматирования результирующей строки (pretty, compact)

## Рекомендации

Для написания тестов рекомендуется обратить внимение на библиотеки `scalatest` и `munit`.

Примеры конфигурации проекта и работы с зависимостями в sbt можно найти здесь: https://www.scala-sbt.org/1.x/docs/Library-Dependencies.html

Для работы с JSON в тестах можно рассмотреть библиотеки `circe` и `tethys`.

### N.B.
Для того, чтобы автоматически выводить инстансы `AsJson` для кейс классов, вам могут понадобится следующие методы:
- Tuple.fromProductTyped
- Product#productElementNames

```scala
case class User(name: String, email: String)
val u = User("Alex", "alex@mail.com")

//получаем Tuple, где каждый элемент соответствует полю кейс класса
Tuple.fromProductTyped(u)
//получаем список имен полей кейс класса
u.productElementNames.toList
```
Полученные сведения вы можете применить для поэлеметного поиска инстансов вашего тайпкласса
`AsJson` к полям кейкласса и их последующего преобразования в JSON строку. 
Для этого вам пригодятся рекурсивные `given` определения с `uaing` параметрами. 