# AnimeAPI AndroidStudioProject

Android проект для получения списка популярных аниме и манги с помощью Jikan API.


## Содержание

#### [Jikan API описание и документация](#jikan-api)
#### [Стек технологий](#ts-desc)
#### [Структура проекта](#structure)


----------

### Jikan API описание и документация
<a name="jikan-api"></a>
[Jikan](https://jikan.moe/)
(時間) открытый исходный код PHP и REST API для «самого активного онлайн-сообщества и базы данных аниме + манги» - MyAnimeList.net. Он анализирует веб-сайт, чтобы предоставить API, которого нет у MyAnimeList.

Узнать больше [документация](https://jikan.docs.apiary.io/)

В данном проекте использовались GET запросы к API:
1) Получение аниме по индексу ```https://api.jikan.moe/v3/anime/{id}``` [Открыть пример запроса](https://api.jikan.moe/v3/anime/1)
2) Получение 20 самых популярных аниме ```https://api.jikan.moe/v3/search/anime?q=&order_by=members&sort=desc&limit=20``` [Открыть пример запроса](https://api.jikan.moe/v3/search/anime?q=&order_by=members&sort=desc&limit=20)

----------

### Стек технологий
<a name="ts-desc"></a>

 * Android/Kotlin
 * Jikan API
 * Room
 * Retrofit 2

----------

### Структура проекта
<a name="structure"></a>
```
AnimeAPI
├─ app
│  ├─ manifests
│  ├─ java
│  │  ├─ com.example.animeapi
│  │  │  └─ db
│  │  │  │  └─ AppDatabase
│  │  │  │  └─ UserDao
│  │  │  └─ models
│  │  │  │  └─ AnimeTitle
│  │  │  │  └─ AnimeTitleList
│  │  │  │  └─ User
│  │  │  └─ AnimeTitleAdapter
│  │  │  └─ ApiService
│  │  │  └─ FisrtFragment
│  │  │  └─ MainActivity
│  │  │  └─ SecondFragment
```

**AppDatabase** - подключение базы данных Room.

**UserDao** - интерфейс с описанием общих методов, которые будут использоваться при взаимодействии с БД.

**User** - объект БД


**AnimeTitle** - класс для получения одного аниме тайтла из запроса

**AnimeTitleList** - класс для получения списка аниме тайтлов из запроса

**AnimeTitleAdapter** - адаптер для подгрузки данных в RecyclerView

**ApiService** - сервис для api запросов


