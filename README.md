# Api Twitter usando Clojure

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Api Twitter prover um serviço simplificado, escalável e de grande perfomance para os recusros mais comuns do Twitter

### Tecnologias

Api Twitter utiliza alguns outras bibliotecas open source, são elas:

* [Leininger] - Conhece o Mavem para Java, Gems para Ruby? Bom é isso que o Leininger é para o Clojure
* [Ring] - Uma biblioteca para aplicações Web
* [Cheshire] - Codificador Json
* [Midje] - Excelente framework de testes para o Clojure
* [Monger] - MongoDB cliente para Clojure com vasta documentação

Api Twitter também é um projeto open source,, pode baixar, usar aprender bastante com ele.
 

### Instalação

Api Intranet requer o Leininger, pode ser baixado aqui  [Leininger](https://leiningen.org/). Você irar precisar dele para executar o projeto, sua instalação é simples e para usuários do Windows existe um instalador aqui [Leininger Instalador Windows ](https://djpowell.github.io/leiningen-win-installer/) 

Após o Leininger ser instalado, basta entrar na raiz do projeto e executar o comando abaixo para instalar as dependências.

```sh
$ lein deps
```

Estamos quase prontos para executar o projeto, tenha certeza antes de ter o MongoDB instalado localmente na sua máquina.  Bom se tudo tiver certo execute o comando abaixo e o nosso serviço estará no ar!

```sh
$ lein ring server-headless
```

Caso o seu MongoDB esteja instalado em outra porta ou máquina, vai ser necessário editar o arquivo db.clj e ajustar as configurações.


### Consumindo o serviço

Você pode utilizar uma ferramenta externa para consumir o serviço, aconselho muito o [Insomnia](https://insomnia.rest/) ele é um cliente Rest que você vai amar. Api Twitter possui por enquanto os seguintes serviços:

| Função | Rota |
| ------ | ------ |
| Cadastrar Usuário |[POST] localhost:3000\register |
| Autenticar Usuário |[POST] localhost:3000\sign |
| Criar Twitter |[POST] localhost:3000\twitter|
| Listar Meus Twitters | [GET] localhost:3000\twitter\\:user |
| Ver Meu Twitter |[GET] localhost:3000\twitter\\:user\status\\:id |

Vamos ver agora alguns exemplos de como realizar o acesso de cada um desses serviços.

```sh
[POST] localhost:3000\register
{
	"username" : "admin", 
	"email": "admin@upnid.com", 
	"pass_hash": "@gd67grdfbf87fhb87b872dh27"
}
```

```sh
[POST] localhost:3000\sign 
{
	"username" : "admin", 
	"pass_hash": "@gd67grdfbf87fhb87b872dh27"
}
```

```sh
[POST] localhost:3000\twitter
{
	"username" :"admin", 
	"post": "Esse meu twitter é da hora!"
}
```

```sh
[GET] localhost:3000\twitter\:user
```

```sh
[GET] localhost:3000\twitter\:user\status\:id
```
### Testes

Api Twitter foi construido utilizando a metodologia TDD(Test Driven Development), ou seja todo o desenvolvimento foi realizado orientado há testes, foram cerca de 30 testes entre unitários e de aceitação que podem ser facilmente executados usando o Leininger, é preciso estar na pasta do projeto.

Lembre-se que ao executar os testes todos os dados criados através dos serviços serão apagados, lembre-se disso se for utilizar a rotina em ambiente de produção, mantenha os testes separados sempre! 

Executando todos os testes:
```sh
$ lein midje
```
Executando apenas os testes de aceitação
```sh
$ lein midje :filter acceptance
```
Executando apenas os testes unitários
```sh
$ lein midje :filter -acceptance
```

### Estrutura do projeto

O projeto foi dividido em algumas camadas sendo assim temos:

* [src/api_twitter/core.clj] - Esse aqui é digamos nosso main do projeto onde todas as requisições empacotadas e nossas rotas criadas
* [src/api_twitter/user_service.clj] - Esse arquivo recebe todas as requisições relacionadas ao usuários fazendo o envio para o banco de dados
* [src/api_twitter/twitter_service.clj] - Esse arquivo recebe todas as requisições relacionadas aos twitters fazendo o envio para o banco de dados
* [src/api_twitter/db.clj] - Realiza a conexão com o MongoDB e realiza todos os acessos a base
* [test/acceptance/api_twitter/handller_acceptance_test.clj] - Nesse arquivo são realizados todos os testes de aceitação
* [test/units/api_twitter/handller_unit_test.clj] - Nesse arquivo são realizados todos os testes unitários
* [test/api_twitter/utils.clj] - Arquivos com funções uteis utilizados nos testes

#### Sobre o desafio técnico

Esse desafio técnico me surpreendeu, não poder utilizar um linguagem que você está acustumado me pegou de surpresa, mas foi uma surpresa boa, condizente com a vaga ofertada, foi o meu primeiro contato com uma liguagem funcional e adorei a experiência. Um novo aprendizado que pretendo evoluir e levar comigo para onde for.
Achei estranho no inicio, tipo como contruir um sistema só com funções? Depois fui me acustumando, não tive tanto tempo assim, mas deu para sentir que é possível criar sistemas de confiança com o Clojure.







