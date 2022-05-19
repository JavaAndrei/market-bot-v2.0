package pro.keenetic.marketbot.bot.market_bot.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pro.keenetic.marketbot.bot.market_bot.exceptions.WebsocketException;
import pro.keenetic.marketbot.bot.market_bot.json.ExmoOrderBook;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@ClientEndpoint
public class ExmoWebsocket {

    private Session session = null;
    private final String serverName = "Exmo";

    private String endpoint;

    private List<String> messages;

    private boolean alive;

    private ExmoOrderBook exmoOrderBook;

    public ExmoWebsocket(String endpoint, ExmoOrderBook exmoOrderBook) {
        this.endpoint = endpoint;
        this.exmoOrderBook = exmoOrderBook;
    }

    @PostConstruct
    private void init() {
        connect();
    }

    @PreDestroy
    private void destroy() {
        disconnect();
    }

    public void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(endpoint)); //"wss://ws-api.exmo.com:443/v1/public"
        } catch (DeploymentException | URISyntaxException | IOException e) {
            //log.logSEVERE(dao, login, String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
            throw new WebsocketException(String.format("Can't connect to server \"%s\". %s", serverName, e.getMessage()));
        }
    }

    public void disconnect() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                throw new WebsocketException(String.format("\"%s\" server's session closed with error. %s", serverName, e.getMessage()));
            }
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        //log.logINFO(dao, login, String.format("opening websocket \"%s\"", serverName));
        System.out.println(String.format("opening websocket \"%s\"", serverName));
        this.session = userSession;
        for (String message : messages)
            sendMessage(message);
        alive = true;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        //log.logINFO(dao, login, String.format("closing websocket \"%s\". Reason %s", serverName, reason.getReasonPhrase()));
        System.out.println(String.format("closing websocket \"%s\". Reason %s", serverName, reason.getReasonPhrase()));
        this.session = null;
        alive = false;
    }

    @OnMessage
    public void onMessage(String message) {
        //if (message.contains("error")) {
            //log.logSEVERE(String.format("Error communicating with server \"%s\". %s", serverName, message));
        //}
        parseMessage(message);
    }

    @OnError
    public void onError(Session userSession, Throwable error){
        //log.logSEVERE(dao, login, String.format("Error communicating with server \"%s\". %s", serverName, error.getMessage()));
        System.out.println(String.format("Error communicating with server \"%s\". %s", serverName, error.getMessage()));
        alive = false;
    }

    public void parseMessage(String message) {
        try {
            if (message.contains("update") && message.contains("spot/order_book_snapshots:BTC_USDT")) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(message);
                exmoOrderBook.setTime(node.get("ts").longValue());
                node = node.get("data");
                exmoOrderBook.setAsk(mapper.readValue(node.get("ask").toString(), new TypeReference<List<List<Double>>>() {}));
                exmoOrderBook.setBid(mapper.readValue(node.get("bid").toString(), new TypeReference<List<List<Double>>>() {}));
//                System.out.println(exmoOrderBook);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
