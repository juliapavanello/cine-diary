# Prototipação das telas com listas — CineDiary

As duas telas abaixo são as telas de protótipo que apresentam listas em contextos diferentes. Os layouts XML também mantêm a pré-visualização de itens no Android Studio (`tools:listitem`), mesmo antes de executar o aplicativo.

## 1. Home — tendências da semana

- Arquivo visual: `app/src/main/res/layout/fragment_home.xml`
- Lista prevista no protótipo: pôsteres de filmes em tendência, na horizontal.
- Layout de cada item: `app/src/main/res/layout/item_tmdb_trending.xml`
- Implementação: `RecyclerView` + `TrendingMoviesAdapter`.

## 2. Perfil — lista de favoritos

- Arquivo visual: `app/src/main/res/layout/fragment_profile.xml`
- Lista prevista no protótipo: filmes favoritos do usuário, na vertical.
- Layout de cada item: `app/src/main/res/layout/item_profile_favorite.xml`
- Implementação: `RecyclerView` + `FavoriteMoviesAdapter`.

## Como conferir os protótipos

No Android Studio, abra `fragment_home.xml` e `fragment_profile.xml` e selecione **Design** ou **Split**. Os previews dos RecyclerViews usam os layouts dos itens indicados por `tools:listitem`.

> O enunciado permite `ListView` ou `RecyclerView` no desenvolvimento. O CineDiary usa `RecyclerView` nas duas telas, cada uma com seu Adapter específico.
