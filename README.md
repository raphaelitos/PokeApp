# PokeApp

Aplicativo Android desenvolvido em **Kotlin** para consultar informações de Pokémon por meio da **PokéAPI**, explorar uma listagem paginada e salvar favoritos por usuário no Cloud Firestore.

Projeto acadêmico da disciplina **Programação para Dispositivos Móveis (INF15977)**, da **Universidade Federal do Espírito Santo (UFES)**, com foco na aplicação de MVVM, consumo de APIs REST, autenticação, persistência em nuvem e componentes de interface do Android.

## Funcionalidades

- **Busca:** consulta um Pokémon pelo nome ou número.
- **Detalhes:** exibe imagem, identificação, tipos, altura, peso e estatísticas base.
- **Listagem:** apresenta Pokémon em uma grade com carregamento progressivo de páginas.
- **Favoritos:** permite adicionar, consultar e remover Pokémon no Firebase, associados ao usuário autenticado.
- **Autenticação:** oferece cadastro, login, recuperação de senha e logout com Firebase Authentication.
- **Confirmações:** apresenta Snackbar ao favoritar/desfavoritar e solicita confirmação por AlertDialog antes de remover um favorito na lista.
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
| Firebase Authentication | Cadastro, login, recuperação de senha e sessão |
| Cloud Firestore | Persistência dos favoritos por UID e atualização em tempo real |
| Snackbar e AlertDialog | Feedback de operações e confirmação de remoção |
| Navigation Component e Safe Args | Navegação entre Fragments e passagem de argumentos |
| AndroidX SplashScreen | Tela de inicialização personalizada |

## Organização do projeto

O aplicativo utiliza **uma Activity principal**, responsável por hospedar os Fragments. As camadas de apresentação e acesso a dados são mantidas separadas.

```text
app/src/main/
├── java/com/example/pokeapp/
│   ├── repository/
│   │   ├── api/              # Cliente Retrofit, endpoints e modelos da API
│   │   ├── favoritos/        # Modelo e Repository do Cloud Firestore
│   │   └── auth/             # Repository de autenticação com Firebase
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

Nos favoritos, a ViewModel utiliza o **PokemonFavoritosRepository**, que acessa `usuarios/{uid}/favoritos` no Cloud Firestore. Um `addSnapshotListener` acompanha as alterações na coleção e atualiza os dados observados pela interface. O UID vem da sessão do Firebase Authentication.

A remoção a partir da lista exige confirmação por AlertDialog. A ViewModel sinaliza o resultado da operação para que o Fragment apresente um Snackbar após o sucesso.

## Como executar

**Requisitos:** Android Studio compatível com o projeto, Android SDK **API 37** instalado, dispositivo ou emulador com **Android 12 (API 31)** ou superior e um projeto Firebase configurado para o app.

1. Clone o repositório e abra a pasta do projeto no Android Studio.
2. Configure no Firebase Console o Authentication com e-mail/senha e o Cloud Firestore com regras que restrinjam os favoritos ao UID autenticado.
3. Registre `com.example.pokeapp` no Firebase e coloque o `google-services.json` correspondente em `app/` (caso não esteja configurado para o seu projeto).
4. Aguarde a sincronização do Gradle e a instalação das dependências.
5. Conecte um dispositivo com depuração USB habilitada ou selecione um emulador compatível.
6. Execute a configuração **app** pelo Android Studio.

Para gerar um APK de depuração pelo terminal, na raiz do projeto:

```bash
./gradlew :app:assembleDebug
```

O APK gerado fica em `app/build/outputs/apk/debug/`.

**Observação:** as buscas, os detalhes e as imagens dependem da rede. Os favoritos pertencem à conta autenticada e são sincronizados com o Firestore; o SDK pode utilizar cache local, mas a disponibilidade offline depende de dados previamente carregados e de suas condições de sincronização.

## Validação

Os fluxos de busca, navegação, listagem e persistência por usuário no Firestore foram verificados manualmente em dispositivo Android. Os novos feedbacks de Snackbar e AlertDialog precisam ser verificados após a aplicação deste patch.

O projeto mantém os testes de exemplo gerados pela configuração inicial do Android Studio; não inclui uma suíte automatizada específica para todas as funcionalidades.

## Dados e créditos

- **PokéAPI:** fonte pública dos dados dos Pokémon.
- **PokeAPI/sprites:** imagens utilizadas na listagem e nos detalhes.
- **Android Developers:** documentação de referência para os componentes AndroidX empregados.
- **IA Generativa:** Claude para geração da interface e ChatGPT para acesso direto à documentação e planejamento do app
