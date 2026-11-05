# CineDiary — atendimento aos requisitos de listas e MVVM (T1)

## Prototipação: duas telas com listas

As telas de **Home** e **Perfil** representam dois protótipos com listas em contextos diferentes. Abra os XMLs no Android Studio em **Design** ou **Split** para visualizar os itens de exemplo:

- Home: `app/src/main/res/layout/fragment_home.xml` — tendências de filmes.
- Perfil: `app/src/main/res/layout/fragment_profile.xml` — lista de filmes favoritos.

Os atributos `tools:listitem` deixam os itens visíveis na prévia do editor.

## Desenvolvimento: dois RecyclerViews e dois Adapters

| Tela | Componente | Adapter | Contexto |
|---|---|---|---|
| Home | `rowTrends` | `TrendingMoviesAdapter` | Filmes em tendência, lista horizontal |
| Perfil | `favoritesContainer` | `FavoriteMoviesAdapter` | Filmes favoritos, lista vertical |

Cada RecyclerView possui layout de item próprio.

## MVVM da Home

```text
HomeFragment (View)
      ↓ observa LiveData
HomeViewModel (ViewModel)
      ↓
HomeRepository (Model / acesso a dados)
      ├── TmdbCache (SharedPreferences)
      └── TmdbUpdateScheduler (WorkManager)
                 ↓
          TmdbUpdateWorker
                 ↓
          TmdbApi (Retrofit / TMDB)
```

O Fragment cuida da interface e configura o Adapter. O ViewModel disponibiliza `LiveData<HomeData>`, carrega os dados e reage à conclusão da atualização inicial. O Repository concentra leitura do cache e agendamento do Worker.
