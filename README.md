# Produtor RabbitMQ com Java — Broadcast

Aplicação Java que atua como **produtora de mensagens** utilizando o **RabbitMQ** e o serviço **CloudAMQP**.

A aplicação utiliza uma **Exchange do tipo `fanout`** para publicar mensagens. Nesse modelo, a mensagem enviada pelo produtor é encaminhada pelo RabbitMQ para **todas as filas vinculadas à Exchange**.

Neste aplicação temos somente N filas, 1 produtor e N consumidores.

## Tecnologias utilizadas

* **Java**
* **RabbitMQ**
* **CloudAMQP**
* **RabbitMQ Java Client**
* **Maven**
* **AMQPS**
* **UTF-8**

## Funcionamento

A aplicação:

1. Estabelece uma conexão segura com o RabbitMQ utilizando **AMQPS**.
2. Cria um canal de comunicação.
3. Declara a Exchange `demo.fanout`.
4. Configura a Exchange com o tipo **`fanout`**.
5. Define a Exchange como durável.
6. Publica uma mensagem na Exchange.
7. A Exchange encaminha a mensagem para todas as filas vinculadas a ela.

### Fluxo da comunicação

```text
                    ┌──→ Fila A → Consumidor A
                    │
Produtor → Exchange ├──→ Fila B → Consumidor B
          fanout    │
                    └──→ Fila C → Consumidor C
```

## Execução

Configure a URL de conexão do RabbitMQ no código:

```java
private static final String URL_RABBITMQ = "...";
```

Depois, compile o projeto:

```bash
mvn clean package
```

Execute pelo menos 2 instâncias do consumidor broadcast antes de executar o produtor broadcast.

Execute a aplicação:

```bash
mvn exec:java
```

## Observação

A URL do RabbitMQ contém credenciais de acesso. **Não publique credenciais reais no código-fonte ou em repositórios públicos.** Prefira utilizar variáveis de ambiente ou arquivos de configuração seguros.


## Aplicação produtora

https://github.com/osmarbraz/alomundo_broadcast_rabbitmq_produtor
