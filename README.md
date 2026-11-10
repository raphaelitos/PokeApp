# PokeApp

Aplicativo Android desenvolvido em **Kotlin** para consultar informações de Pokémon por meio da **PokéAPI**, explorar uma listagem paginada e salvar favoritos no dispositivo.

Projeto acadêmico da disciplina **Programação para Dispositivos Móveis (INF15977)**, da **Universidade Federal do Espírito Santo (UFES)**, com foco na aplicação de MVVM, consumo de APIs REST, persistência local e componentes de interface do Android.

## Funcionalidades

- **Busca:** consulta um Pokémon pelo nome ou número.
- **Detalhes:** exibe imagem, identificação, tipos, altura, peso e estatísticas base.
- **Listagem:** apresenta Pokémon em uma grade com carregamento progressivo de páginas.
- **Favoritos:** permite adicionar, consultar e remover Pokémon salvos localmente.
- **Navegação:** utiliza Fragments, Navigation Component e uma barra de navegação inferior.
- **Interface:** inclui splash screen, ícone próprio e suporte a tema claro e escuro.

## Tecnologias e componentes

| Tecnologia | Utilização |
| --- | --- |
| Kotlin e Android SDK | Desenvolvimento do aplicativo |
| XML, ViewBinding e Material 3 | Construção da interface |
| ViewModel e LiveData | Gerenciamento e observação de estado com MVVM |
| Retrofit e Gson | Requisições HTTP e conversão de respostas JSON |
| RecyclerView, ListAdapter e DiffUtil | Listagem e atualização eficiente dos itens |
| Glide | Carregamento de imagens remotas |
| Room | Persistência local dos favoritos |
| Navigation Component e Safe Args | Navegação entre Fragments e passagem de argumentos |
| AndroidX SplashScreen | Tela de inicialização personalizada |

## Organização do projeto

O aplicativo utiliza **uma Activity principal**, responsável por hospedar os Fragments. As camadas de apresentação e acesso a dados são mantidas separadas.

```text
app/src/main/
├── java/com/example/pokeapp/
│   ├── repository/
│   │   ├── api/              # Cliente Retrofit, endpoints e modelos da API
│   │   └── local/            # Entity, DAO, RoomDatabase e repositório local
│   └── ui/
│       ├── adapter/          # Adapters e ViewHolders das listagens
│       ├── fragments/        # Busca, lista, detalhes e favoritos
│       ├── util/             # Utilitários de apresentação
│       ├── viewModel/        # ViewModels e estados da interface
│       └── MainActivity.kt
└── res/
    ├── layout/              # Layouts XML
    ├── navigation/          # Grafo de navegação
    ├── menu/                # Navegação inferior
    └── values/              # Strings, cores e temas
```

### Fluxo de dados

Na consulta remota, o **Fragment** solicita uma ação à **ViewModel**, que utiliza o **Repository** para acessar a **PokéAPI** pelo Retrofit. O resultado é disponibilizado à interface por meio de **LiveData**.

Nos favoritos, a ViewModel utiliza um repositório local e um **DAO do Room**. As alterações no banco são observadas pela interface, atualizando a lista de favoritos.

## Como executar

**Requisitos:** Android Studio compatível com o projeto, Android SDK **API 37** instalado e dispositivo ou emulador com **Android 12 (API 31)** ou superior.

1. Clone o repositório e abra a pasta do projeto no Android Studio.
2. Aguarde a sincronização do Gradle e a instalação das dependências.
3. Conecte um dispositivo com depuração USB habilitada ou selecione um emulador compatível.
4. Execute a configuração **app** pelo Android Studio.

Para gerar um APK de depuração pelo terminal, na raiz do projeto:

```bash
./gradlew :app:assembleDebug
```

O APK gerado fica em `app/build/outputs/apk/debug/`.

**Observação:** as buscas, os detalhes e as imagens dependem de conexão com a internet. Os registros de favoritos permanecem salvos localmente, mas suas imagens também podem depender da rede ou do cache.

## Validação

Os fluxos principais foram verificados manualmente em dispositivo Android: busca, navegação, listagem, paginação e gerenciamento dos favoritos.

O projeto mantém os testes de exemplo gerados pela configuração inicial do Android Studio; não inclui uma suíte automatizada específica para todas as funcionalidades.

## Dados e créditos

- **PokéAPI:** fonte pública dos dados dos Pokémon.
- **PokeAPI/sprites:** imagens utilizadas na listagem e nos detalhes.
- **Android Developers:** documentação de referência para os componentes AndroidX empregados.

Projeto desenvolvido com finalidade didática, sem vínculo oficial com Pokémon, Nintendo, Game Freak ou The Pokémon Company.
