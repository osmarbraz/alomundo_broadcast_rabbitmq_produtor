import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Exemplo de envio de mensagem para uma Exchange do tipo fanout(Broadcast)
 * do RabbitMQ utilizando o serviço CloudAMQP.
 *
 * A Exchange fanout encaminha a mensagem para todas as filas
 * que estiverem vinculadas a ela.
 */
public class Principal {

    // Nome da Exchange que receberá a mensagem.
    private static final String EXCHANGE = "demo.fanout";

    // URL de conexão com o servidor RabbitMQ.
    private static final String URL_RABBITMQ = "amqps://usuario:senha@host/virtualhost";

    // Mensagem que será publicada na Exchange.
    private static final String MENSAGEM = "Ola Mundo Broadcast! RabbitMQ CloudAMQP";

    public static void main(String[] args) {
        try {
            // Cria a fábrica responsável por estabelecer conexões
            // com o servidor RabbitMQ.
            ConnectionFactory factory = new ConnectionFactory();

            try {
                // Configura a conexão utilizando a URL do RabbitMQ.
                factory.setUri(URL_RABBITMQ);

            } catch (URISyntaxException ex) {
                // Trata erros relacionados ao formato da URL.
                System.err.println("Erro: " + ex.getMessage());
            } catch (NoSuchAlgorithmException ex) {
                // Trata erros relacionados ao algoritmo de segurança utilizado na conexão.
                System.err.println("Erro: " + ex.getMessage());
            } catch (KeyManagementException ex) {
                // Trata erros relacionados ao gerenciamento das chaves de segurança da conexão.
                System.err.println("Erro: " + ex.getMessage());
            }

            /*
             * Abre a conexão com o servidor RabbitMQ e cria um canal
             * para realizar as operações de mensageria.
             *
             * O try-with-resources garante que a conexão e o canal
             * sejam fechados automaticamente ao final da execução.
             */
            try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
            ) {

                /*
                 * Declara a Exchange no RabbitMQ.
                 *
                 * EXCHANGE:
                 * Nome da Exchange que será utilizada.
                 *
                 * "fanout":
                 * Define o tipo da Exchange. Nesse modelo, a mensagem
                 * é encaminhada para todas as filas vinculadas à Exchange.
                 *
                 * true:
                 * Define a Exchange como durável, mantendo-a após
                 * uma reinicialização do servidor RabbitMQ.
                 */
                channel.exchangeDeclare(EXCHANGE, "fanout", true);
                
                // Converte a mensagem para um array de bytes utilizando UTF-8.
                byte[] corpoMensagem = MENSAGEM.getBytes(StandardCharsets.UTF_8);

                /*
                 * Publica a mensagem na Exchange.
                 *
                 * EXCHANGE:
                 * Nome da Exchange que receberá a mensagem.
                 *
                 * "":
                 * Routing key vazia. Em uma Exchange do tipo fanout,
                 * a routing key é ignorada.
                 *
                 * MessageProperties.PERSISTENT_TEXT_PLAIN:
                 * Define a mensagem como persistente e indica que
                 * seu conteúdo é textual.
                 *
                 * A mensagem será encaminhada pela Exchange para
                 * todas as filas que estiverem vinculadas a ela.
                 */
                channel.basicPublish(EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, corpoMensagem);

                // Exibe uma confirmação no console.
                System.out.println("Mensagem enviada: '" + MENSAGEM + "'");
            }

        } catch (IOException | TimeoutException | RuntimeException e) {

            // Exibe uma mensagem de erro caso ocorra algum problema
            // durante a conexão ou o envio da mensagem.
            System.err.println("Erro ao enviar a mensagem: " + e.getMessage());
        }
    }
}