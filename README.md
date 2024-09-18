Sistema de Gestão de Hóspedes

Visão Geral do Projeto
Este projeto é um sistema de gestão de hóspedes para um hotel, permitindo a gestão de reservas, check-ins e check-outs. Ele fornece uma API REST para a manipulação de dados de hóspedes e reservas e utiliza o PostgreSQL como banco de dados.

Funcionalidades Principais:

Armazenamento persistente de informações de hóspedes (Nome, Documento, Telefone);

Gerenciamento de reservas;

Capacidade de pesquisar hóspedes por nome, documento ou telefone;

Capacidade de identificar hóspedes atualmente hospedados no hotel;

Suporte para realizar check-ins e check-outs;

Regras de negócio para precificação com base em dias da semana e finais de semana;

Cobrança adicional por vagas de estacionamento e check-outs tardios;

Tecnologias Utilizadas:

Backend: Java 11+, Spring Boot, PostgreSQL;
Banco de Dados: PostgreSQL (via Docker Compose);
API REST: Spring Boot;
Containerização: Docker e Docker Compose;
Pré-requisitos;

Antes de rodar o projeto, certifique-se de que você tenha os seguintes softwares instalados:

Docker
Docker Compose
Java 11+
Maven (caso deseje rodar a aplicação fora do Docker)
Como Executar o Projeto
1. Clonar o Repositório
Clone o repositório para sua máquina local:

git clone https://github.com/andre1397/hotel.git

cd hotel

2. Configuração do Banco de Dados com Docker Compose
Este projeto utiliza o PostgreSQL em um contêiner Docker. O arquivo docker-compose.yml contido na pasta database-docker já está configurado para criar o banco de dados necessário.

Passos para executar o composer e criar o banco de dados:

Abra a pasta database-docker e então execute o seguinte comando para subir o contêiner do banco de dados:

docker-compose up

Isso irá iniciar um contêiner com PostgreSQL e criar o banco de dados conforme especificado no docker-compose.yml.

3. Configuração do Backend (Spring Boot):

Após configurar o banco de dados, você pode iniciar a aplicação Spring Boot.

Passos:
No diretório raiz, execute o seguinte comando para iniciar o backend:

./mvnw spring-boot:run

Ou, caso esteja utilizando Maven instalado no sistema:

mvn spring-boot:run

Isso iniciará a aplicação no endereço http://localhost:8080.

4. Testar os Endpoints:
   
Pode ser usada uma ferramenta como Postman ou curl para interagir com os endpoints da API REST.

Exemplos de endpoints disponíveis:

Cadastrar Hóspede: POST /api/guests

Pesquisar Hóspedes: GET /api/guests/search?name={name}&document={document}&phone={phone}

Criar Reserva: POST /api/reservations/guest/{guestId}

Realizar Check-In: PUT /api/reservations/check-in/{reservationId}

Realizar Check-Out: PUT /api/reservations/check-out/{reservationId}

Pesquisar Hóspedes que ainda não realizaram o check-in: GET /api/reservations/active

Pesquisar Hóspedes que estão hospedados no momento: GET /api/reservations/pending-checkin

5. Desligar o Banco de Dados:

Após terminar de utilizar a aplicação, você pode parar o contêiner do banco de dados abrindo novamente a pasta database-docker no terminal e executando:

docker-compose down

Testes de Backend:

Este projeto inclui testes unitários no backend utilizando JUnit e Mockito. Para rodar os testes, utilize o comando:

mvn test

Autor:
André Luiz dos Santos
